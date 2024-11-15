package ru.mipt.bit.platformer.model.commands;

import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.view.LevelGraphics;

public class MovePlayerTankCommand implements Command {

    private final LevelGraphics levelGraphics;
    private final MovementDirection direction;

    public MovePlayerTankCommand(LevelGraphics levelGraphics, MovementDirection direction) {
        this.levelGraphics = levelGraphics;
        this.direction = direction;
    }

    @Override
    public void execute() {
        levelGraphics.getPlayerTank().moveModel(direction);
    }

}
