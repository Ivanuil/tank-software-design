package ru.mipt.bit.platformer.model.commands;

import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.view.MovingGraphicsObject;

public class MoveTankCommand implements Command {

    private final MovingGraphicsObject tank;
    private final MovementDirection direction;

    public MoveTankCommand(MovingGraphicsObject tank, MovementDirection direction) {
        this.tank = tank;
        this.direction = direction;
    }

    @Override
    public void execute() {
        tank.moveModel(direction);
    }

}
