package ru.mipt.bit.platformer.model.commands;

import ru.mipt.bit.platformer.view.HealthBarsToggle;

public class SwitchHealthBarToggleCommand implements Command {

    @Override
    public void execute() {
        HealthBarsToggle.switchToggle();
    }

}
