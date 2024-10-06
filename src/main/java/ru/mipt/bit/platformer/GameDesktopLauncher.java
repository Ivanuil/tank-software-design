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
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.view.TankGraphics;
import ru.mipt.bit.platformer.view.TreeGraphics;
import ru.mipt.bit.platformer.util.KeyListener;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private final KeyListener keyListener = new KeyListener();

    private TreeGraphics treeGraphics;
    private TankGraphics tankGraphics;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        treeGraphics = new TreeGraphics(groundLayer, "images/greenTree.png", new GridPoint2(2, 3));
        tankGraphics = new TankGraphics(0.4f, "images/tank_blue.png");

        keyListener.addKeyPressedCallback(List.of(UP, W), () ->
                tankGraphics.moveModel(MovementDirection.UP, treeGraphics.getCoordinates()));
        keyListener.addKeyPressedCallback(List.of(LEFT, A), () ->
                tankGraphics.moveModel(MovementDirection.LEFT, treeGraphics.getCoordinates()));
        keyListener.addKeyPressedCallback(List.of(DOWN, S), () ->
                tankGraphics.moveModel(MovementDirection.DOWN, treeGraphics.getCoordinates()));
        keyListener.addKeyPressedCallback(List.of(RIGHT, D), () ->
                tankGraphics.moveModel(MovementDirection.RIGHT, treeGraphics.getCoordinates()));
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
        treeGraphics.render(batch);

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
        treeGraphics.dispose();
        tankGraphics.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
