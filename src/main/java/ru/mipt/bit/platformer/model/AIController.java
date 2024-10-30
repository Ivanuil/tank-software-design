package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.commands.MoveTankCommand;

import java.util.List;

public class AIController {

    private final List<List<MoveTankCommand>> commandsList;

    public AIController(List<List<MoveTankCommand>> commandsList) {
        this.commandsList = commandsList;
    }

    public void control() {
        for (var commandsForTank : commandsList) {
            int i = (int) (Math.random() * commandsForTank.size());
            var command = commandsForTank.get(i);
            command.execute();
        }
    }

}
