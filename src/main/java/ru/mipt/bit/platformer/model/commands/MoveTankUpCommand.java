package ru.mipt.bit.platformer.model.commands;

import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.model.TankModel;

public class MoveTankUpCommand implements MoveTankCommand {

    private final TankModel tank;

    public MoveTankUpCommand(TankModel tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.moveModel(MovementDirection.UP);
    }

}
