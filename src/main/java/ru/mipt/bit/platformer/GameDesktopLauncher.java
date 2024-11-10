package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.levelloaders.FileLevelLoader;
import ru.mipt.bit.platformer.levelloaders.LevelLoader;
import ru.mipt.bit.platformer.levelloaders.RandomisedLevelLoader;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.model.commands.MoveTankCommand;
import ru.mipt.bit.platformer.model.commands.MoveAndShootTankCommandProducer;
import ru.mipt.bit.platformer.model.commands.ShootCommand;
import ru.mipt.bit.platformer.view.*;
import ru.mipt.bit.platformer.view.commands.SwitchHealthBarToggleCommand;
import ru.mipt.bit.platformer.util.KeyListener;
import ru.mipt.bit.platformer.util.TileMovement;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener, ShellUpdateSubscriber, TankUpdateSubscriber {

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private final KeyListener keyListener = new KeyListener();

    private Collection<TreeGraphics> treeGraphics;
    private MovingGraphicsObject playerTank;
    private List<MovingGraphicsObject> npcTanks;
    private List<ShellGraphics> shells = new LinkedList<>();

    private AIController aiController;
    private final LevelModel levelModel;

    public GameDesktopLauncher(LevelLoader levelLoader) {
        levelModel = levelLoader.loadLevel();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        treeGraphics = levelModel.getTrees().stream()
                .map(treeModel -> new TreeGraphics(groundLayer, "images/greenTree.png", treeModel))
                .collect(Collectors.toList());
        playerTank = new TankGraphics(0.4f, "images/tank_blue.png", levelModel.getPlayerTank());
        playerTank = new HealthBarDecorator(playerTank);
        playerTank.getModel().setNewShellSubscriber(this);
        playerTank.getModel().setTankUpdateSubscriber(this);
        npcTanks = levelModel.getNpcTanks().stream()
                        .map(tankModel -> new TankGraphics(0.4f, "images/tank_blue.png", tankModel))
                        .map(HealthBarDecorator::new)
                        .peek(healthBarDecorator -> healthBarDecorator.getModel().setNewShellSubscriber(GameDesktopLauncher.this))
                        .peek(healthBarDecorator -> healthBarDecorator.getModel().setTankUpdateSubscriber(GameDesktopLauncher.this))
                        .collect(Collectors.toList());

        keyListener.addKeyPressedCallback(List.of(UP, W), new MoveTankCommand(playerTank, MovementDirection.UP));
        keyListener.addKeyPressedCallback(List.of(LEFT, A), new MoveTankCommand(playerTank, MovementDirection.LEFT));
        keyListener.addKeyPressedCallback(List.of(DOWN, S), new MoveTankCommand(playerTank, MovementDirection.DOWN));
        keyListener.addKeyPressedCallback(List.of(RIGHT, D), new MoveTankCommand(playerTank, MovementDirection.RIGHT));
        keyListener.addKeyPressedCallback(List.of(L), new SwitchHealthBarToggleCommand(), false);
        keyListener.addKeyPressedCallback(List.of(SPACE), new ShootCommand(levelModel.getPlayerTank()), false);

        aiController = new AIController(MoveAndShootTankCommandProducer.produceAllCommands(npcTanks));
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        keyListener.checkPressedKeys(Gdx.input);
        aiController.control();

        playerTank.moveImage(tileMovement, deltaTime);
        npcTanks.forEach(tankGraphics -> tankGraphics.moveImage(tileMovement, deltaTime));
        List.copyOf(shells).forEach(shellGraphics -> shellGraphics.moveImage(tileMovement, deltaTime));

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        playerTank.render(batch);
        treeGraphics.forEach(treeGraphics -> treeGraphics.render(batch));
        npcTanks.forEach(tankGraphics -> tankGraphics.render(batch));
        shells.forEach(shellGraphics -> shellGraphics.render(batch));

        // submit all drawing requests
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        treeGraphics.forEach(TreeGraphics::dispose);
        npcTanks.forEach(MovingGraphicsObject::dispose);
        playerTank.dispose();
        level.dispose();
        batch.dispose();
        shells.forEach(ShellGraphics::dispose);
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);

        LevelLoader levelLoader = getLevelLoader(args);

        new Lwjgl3Application(new GameDesktopLauncher(levelLoader), config);
    }

    private static LevelLoader getLevelLoader(String[] args) {
        String levelLoaderName = args[0];
        if (FileLevelLoader.class.getName().endsWith(levelLoaderName)) {
            File intitializerFile = new File(args[1]);
            return new FileLevelLoader(intitializerFile);
        } else if (RandomisedLevelLoader.class.getName().endsWith(levelLoaderName)) {
            double obstacleDensity = Double.parseDouble(args[1]);
            int rowCount = Integer.parseInt(args[2]);
            int columnCount = Integer.parseInt(args[3]);
            int npcTanksCount = Integer.parseInt(args[4]);
            return new RandomisedLevelLoader(obstacleDensity, npcTanksCount, rowCount, columnCount);
        } else {
            throw new RuntimeException("Level loader not specified");
        }
    }

    @Override
    public void onNewShell(ShellModel shell) {
        shells.add(new ShellGraphics(0.4f, "images/shell.png", shell));
        shell.setShellUpdateSubscriber(this);
    }

    @Override
    public void onShellDestroyed(ShellModel shell) {
        ShellGraphics shellGraphics = shells.stream()
                .filter(shellGraphics1 -> shellGraphics1.getShellModel() == shell)
                .findFirst().get();
        shellGraphics.dispose();
        shells.remove(shellGraphics);
    }

    @Override
    public void onTankDestroyed(TankModel tankModel) {
        MovingGraphicsObject tankGraphics = npcTanks.stream()
                .filter(movingGraphicsObject -> movingGraphicsObject.getModel() == tankModel)
                .findFirst().orElse(null);
        if (tankGraphics != null) {
            tankGraphics.dispose();
            npcTanks.remove(tankGraphics);
            return;
        }

        if (playerTank.getModel() == tankModel) {
            throw new RuntimeException("Game over!");
        }
    }

}
