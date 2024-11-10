package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.commands.Command;

import java.util.List;

public class AIController {

    private final List<List<Command>> commandsList;

    public AIController(List<List<Command>> commandsList) {
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
