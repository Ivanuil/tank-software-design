package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.model.commands.Command;

import java.util.*;

public class KeyListener {

    List<KeyBinding> keyBindings = new ArrayList<>();

    public void addKeyPressedCallback(Collection<Integer> keyCodes, Command command) {
        keyBindings.add(new KeyBinding(keyCodes, command, true));
    }

    public void addKeyPressedCallback(Collection<Integer> keyCodes, Command command, boolean toggleOnEveryRender) {
        keyBindings.add(new KeyBinding(keyCodes, command, toggleOnEveryRender));
    }

    public void checkPressedKeys(Input input) {
        for (var binding : keyBindings) {
            if (binding.isToggleOnEveryRender() && binding.getKeyCodes().stream().anyMatch(input::isKeyPressed))
                binding.getCallBack().execute();
            if (!binding.isToggleOnEveryRender() && binding.getKeyCodes().stream().anyMatch(input::isKeyJustPressed))
                binding.getCallBack().execute();
        }
    }

    private static class KeyBinding {

        public KeyBinding(Collection<Integer> keyCodes, Command command, boolean toggleOnEveryRender) {
            this.keyCodes = keyCodes;
            this.command = command;
            this.toggleOnEveryRender = toggleOnEveryRender;
        }

        private final Collection<Integer> keyCodes;
        private final Command command;
        private final boolean toggleOnEveryRender;

        public Collection<Integer> getKeyCodes() {
            return keyCodes;
        }

        public Command getCallBack() {
            return command;
        }

        public boolean isToggleOnEveryRender() {
            return toggleOnEveryRender;
        }

    }

}
