package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

import java.io.File;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private final KeyListener keyListener = new KeyListener();

    private AIController aiController;
    private final LevelModel levelModel;
    private final LevelGraphics levelGraphics;

    public GameDesktopLauncher(LevelLoader levelLoader) {
        levelModel = levelLoader.loadLevel();
        levelGraphics = new LevelGraphics(levelModel);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelGraphics.create(batch);

        MovingGraphicsObject playerTank = levelGraphics.getPlayerTank();
        keyListener.addKeyPressedCallback(List.of(UP, W), new MoveTankCommand(playerTank, MovementDirection.UP));
        keyListener.addKeyPressedCallback(List.of(LEFT, A), new MoveTankCommand(playerTank, MovementDirection.LEFT));
        keyListener.addKeyPressedCallback(List.of(DOWN, S), new MoveTankCommand(playerTank, MovementDirection.DOWN));
        keyListener.addKeyPressedCallback(List.of(RIGHT, D), new MoveTankCommand(playerTank, MovementDirection.RIGHT));
        keyListener.addKeyPressedCallback(List.of(L), new SwitchHealthBarToggleCommand(), false);
        keyListener.addKeyPressedCallback(List.of(SPACE), new ShootCommand(levelModel.getPlayerTank()), false);

        List<MovingGraphicsObject> npcTanks = levelGraphics.getNpcTanks();
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

        levelGraphics.render(deltaTime);

        levelGraphics.renderLevel();

        // start recording all drawing commands
        batch.begin();
        levelGraphics.render(batch);

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
        batch.dispose();
        levelGraphics.dispose();
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

}
