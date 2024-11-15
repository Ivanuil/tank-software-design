package ru.mipt.bit.platformer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.model.LevelModel;
import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.model.commands.MovePlayerTankCommand;
import ru.mipt.bit.platformer.model.commands.ShootCommand;
import ru.mipt.bit.platformer.util.KeyListener;
import ru.mipt.bit.platformer.view.LevelGraphics;
import ru.mipt.bit.platformer.view.commands.SwitchHealthBarToggleCommand;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

@Configuration
public class KeyListenerConfiguration {

    private final LevelModel levelModel;
    private final LevelGraphics levelGraphics;

    public KeyListenerConfiguration(LevelModel levelModel, LevelGraphics levelGraphics) {
        this.levelModel = levelModel;
        this.levelGraphics = levelGraphics;
    }

    @Bean
    public KeyListener keyListener() {
        var keyListener = new KeyListener();

        keyListener.addKeyPressedCallback(List.of(UP, W), new MovePlayerTankCommand(levelGraphics, MovementDirection.UP));
        keyListener.addKeyPressedCallback(List.of(LEFT, A), new MovePlayerTankCommand(levelGraphics, MovementDirection.LEFT));
        keyListener.addKeyPressedCallback(List.of(DOWN, S), new MovePlayerTankCommand(levelGraphics, MovementDirection.DOWN));
        keyListener.addKeyPressedCallback(List.of(RIGHT, D), new MovePlayerTankCommand(levelGraphics, MovementDirection.RIGHT));
        keyListener.addKeyPressedCallback(List.of(L), new SwitchHealthBarToggleCommand(), false);
        keyListener.addKeyPressedCallback(List.of(SPACE), new ShootCommand(levelModel.getPlayerTank()), false);

        return keyListener;
    }

}
