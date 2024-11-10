package ru.mipt.bit.platformer.view;

import ru.mipt.bit.platformer.model.ShellModel;

public interface ShellUpdateSubscriber {

    void onNewShell(ShellModel shell);

    void onShellDestroyed(ShellModel shell);

}
