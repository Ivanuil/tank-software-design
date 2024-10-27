package ru.mipt.bit.platformer.model.commands;

import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.model.TankModel;

public interface MoveTankCommand extends Command {

    default MoveTankCommand getCommand(TankModel tank, MovementDirection direction) {
        switch (direction) {
            case UP: return new MoveTankUpCommand(tank);
            case DOWN: return new MoveTankDownCommand(tank);
            case LEFT: return new MoveTankLeftCommand(tank);
            case RIGHT: return new MoveTankRightCommand(tank);
        }
        return null;
    }

}
