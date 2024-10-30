package ru.mipt.bit.platformer.model.commands;

import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.view.TankGraphics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MoveTankCommandProducer {

    public static List<MoveTankCommand> produceAllCommands(TankGraphics tank) {
        return Arrays.stream(MovementDirection.values())
                .map(direction -> new MoveTankCommand(tank, direction))
                .collect(Collectors.toList());
    }

    public static List<List<MoveTankCommand>> produceAllCommands(List<TankGraphics> tanks) {
        return tanks.stream()
                .map(MoveTankCommandProducer::produceAllCommands)
                .collect(Collectors.toList());
    }

}
