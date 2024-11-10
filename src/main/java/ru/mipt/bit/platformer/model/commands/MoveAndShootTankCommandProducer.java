package ru.mipt.bit.platformer.model.commands;

import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.view.MovingGraphicsObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MoveAndShootTankCommandProducer {

    public static List<Command> produceAllCommands(MovingGraphicsObject tank) {
        List<Command> commands = Arrays.stream(MovementDirection.values())
                .map(direction -> new MoveTankCommand(tank, direction))
                .collect(Collectors.toList());
        commands.add(new ShootCommand(tank.getModel()));
        return commands;
    }

    public static List<List<Command>> produceAllCommands(List<MovingGraphicsObject> tanks) {
        return tanks.stream()
                .map(MoveAndShootTankCommandProducer::produceAllCommands)
                .collect(Collectors.toList());
    }

}
