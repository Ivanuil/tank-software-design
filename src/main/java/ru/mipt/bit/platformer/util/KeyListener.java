package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Input;

import java.util.*;

public class KeyListener {

    List<KeyBinding> keyBindings = new ArrayList<>();

    public void addKeyPressedCallback(Collection<Integer> keyCodes, Runnable runnable) {
        keyBindings.add(new KeyBinding(keyCodes, runnable));
    }

    public void checkPressedKeys(Input input) {
        for (var binding : keyBindings) {
            if (binding.getKeyCodes().stream().anyMatch(input::isKeyPressed))
                binding.getCallBack().run();
        }
    }

    private static class KeyBinding {

        public KeyBinding(Collection<Integer> keyCodes, Runnable callBack) {
            this.keyCodes = keyCodes;
            this.callBack = callBack;
        }

        private final Collection<Integer> keyCodes;
        private final Runnable callBack;

        public Collection<Integer> getKeyCodes() {
            return keyCodes;
        }

        public Runnable getCallBack() {
            return callBack;
        }
    }

}
