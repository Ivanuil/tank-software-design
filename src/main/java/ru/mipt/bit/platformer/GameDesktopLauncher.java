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
import ru.mipt.bit.platformer.model.LevelModel;
import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.model.TreeModel;
import ru.mipt.bit.platformer.view.Obstacle;
import ru.mipt.bit.platformer.view.TankGraphics;
import ru.mipt.bit.platformer.util.KeyListener;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.view.TreeGraphics;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private final KeyListener keyListener = new KeyListener();

    private Collection<Obstacle> obstacles;
    private Collection<TreeGraphics> treeGraphics;
    private TankGraphics tankGraphics;

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

        obstacles = (Collection<Obstacle>) levelModel.getObstacles();
        treeGraphics = levelModel.getTrees().stream()
                .map(treeModel -> new TreeGraphics(groundLayer, "images/greenTree.png", treeModel))
                .collect(Collectors.toList());;
        tankGraphics = new TankGraphics(0.4f, "images/tank_blue.png", levelModel.getTank());

        keyListener.addKeyPressedCallback(List.of(UP, W), () ->
                tankGraphics.moveModel(MovementDirection.UP, obstacles));
        keyListener.addKeyPressedCallback(List.of(LEFT, A), () ->
                tankGraphics.moveModel(MovementDirection.LEFT, obstacles));
        keyListener.addKeyPressedCallback(List.of(DOWN, S), () ->
                tankGraphics.moveModel(MovementDirection.DOWN, obstacles));
        keyListener.addKeyPressedCallback(List.of(RIGHT, D), () ->
                tankGraphics.moveModel(MovementDirection.RIGHT, obstacles));
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        keyListener.checkPressedKeys(Gdx.input);

        tankGraphics.moveImage(tileMovement, deltaTime);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        tankGraphics.render(batch);
        treeGraphics.forEach(treeGraphics -> treeGraphics.render(batch));

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
        tankGraphics.dispose();
        level.dispose();
        batch.dispose();
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
            System.out.println(new File(args[1]).getAbsolutePath());
            return new FileLevelLoader(intitializerFile);
        } else if (RandomisedLevelLoader.class.getName().endsWith(levelLoaderName)) {
            double obstacleDensity = Double.parseDouble(args[1]);
            int rowCount = Integer.parseInt(args[2]);
            int columnCount = Integer.parseInt(args[3]);
            return new RandomisedLevelLoader(obstacleDensity, rowCount, columnCount);
        } else {
            throw new RuntimeException("Level loader not specified");
        }
    }

}
