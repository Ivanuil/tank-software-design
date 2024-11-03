package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.model.commands.Command;

import java.util.*;

public class KeyListener {

    List<KeyBinding> keyBindings = new ArrayList<>();

    public void addKeyPressedCallback(Collection<Integer> keyCodes, Command command) {
        keyBindings.add(new KeyBinding(keyCodes, command));
    }

    public void checkPressedKeys(Input input) {
        for (var binding : keyBindings) {
            if (binding.getKeyCodes().stream().anyMatch(input::isKeyPressed))
                binding.getCallBack().execute();
        }
    }

    private static class KeyBinding {

        public KeyBinding(Collection<Integer> keyCodes, Command command) {
            this.keyCodes = keyCodes;
            this.command = command;
        }

        private final Collection<Integer> keyCodes;
        private final Command command;

        public Collection<Integer> getKeyCodes() {
            return keyCodes;
        }

        public Command getCallBack() {
            return command;
        }
    }

}
