package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Input;

import java.util.*;

public class KeyListener {

    Map<Integer, List<Runnable>> callbacks = new HashMap<>();

    public void addKeyPressedCallback(Collection<Integer> keyCodes, Runnable runnable) {
        keyCodes.forEach(keyCode -> addKeyPressedCallback(keyCode, runnable));
    }

    public void addKeyPressedCallback(int keyCode, Runnable runnable) {
        var runnableList = callbacks.getOrDefault(keyCode, new ArrayList<>());
        runnableList.add(runnable);
        callbacks.put(keyCode, runnableList);
    }

    public void checkPressedKeys(Input input) {
        for (var entry : callbacks.entrySet()) {
            if (input.isKeyPressed(entry.getKey()))
                entry.getValue().forEach(Runnable::run);
        }
    }

}
