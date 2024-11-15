package ru.mipt.bit.platformer.view;

public class HealthBarsToggle {

    private static boolean toggle = true;

    public static boolean getToggle() {
        return toggle;
    }

    public static void switchToggle() {
        toggle = !toggle;
    }

}
