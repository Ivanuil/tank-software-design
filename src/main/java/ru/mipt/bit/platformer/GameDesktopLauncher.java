package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.model.commands.MoveAndShootTankCommandProducer;
import ru.mipt.bit.platformer.view.*;
import ru.mipt.bit.platformer.util.KeyListener;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

@Component
public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private final KeyListener keyListener;

    private AIController aiController;
    private final LevelModel levelModel;
    private final LevelGraphics levelGraphics;

    public GameDesktopLauncher(KeyListener keyListener, LevelModel levelModel, LevelGraphics levelGraphics) {
        this.keyListener = keyListener;
        this.levelModel = levelModel;
        this.levelGraphics = levelGraphics;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelGraphics.create(batch);

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

    @PostConstruct
    public void startUI() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);

        new Lwjgl3Application(this, config);
    }

}
