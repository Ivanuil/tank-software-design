package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.LevelModel;
import ru.mipt.bit.platformer.model.ShellModel;
import ru.mipt.bit.platformer.model.TankModel;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelGraphics implements ShellUpdateSubscriber, TankUpdateSubscriber {

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Collection<TreeGraphics> treeGraphics;
    private MovingGraphicsObject playerTank;
    private List<MovingGraphicsObject> npcTanks;
    private List<ShellGraphics> shells = new LinkedList<>();

    private final LevelModel levelModel;

    public LevelGraphics(LevelModel levelModel) {
        this.levelModel = levelModel;
    }

    public void create(Batch batch) {
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
                .peek(healthBarDecorator -> healthBarDecorator.getModel().setNewShellSubscriber(this))
                .peek(healthBarDecorator -> healthBarDecorator.getModel().setTankUpdateSubscriber(this))
                .collect(Collectors.toList());
    }

    public void render(Batch batch) {
        playerTank.render(batch);
        treeGraphics.forEach(treeGraphics -> treeGraphics.render(batch));
        npcTanks.forEach(tankGraphics -> tankGraphics.render(batch));
        shells.forEach(shellGraphics -> shellGraphics.render(batch));
    }

    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        treeGraphics.forEach(TreeGraphics::dispose);
        npcTanks.forEach(MovingGraphicsObject::dispose);
        playerTank.dispose();
        level.dispose();
        shells.forEach(ShellGraphics::dispose);
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

    public void renderLevel() {
        // render each tile of the level
        levelRenderer.render();
    }

    public void render(float deltaTime) {
        playerTank.moveImage(tileMovement, deltaTime);
        npcTanks.forEach(tankGraphics -> tankGraphics.moveImage(tileMovement, deltaTime));
        List.copyOf(shells).forEach(shellGraphics -> shellGraphics.moveImage(tileMovement, deltaTime));
    }

    public List<MovingGraphicsObject> getNpcTanks() {
        return npcTanks;
    }

    public MovingGraphicsObject getPlayerTank() {
        return playerTank;
    }
}
