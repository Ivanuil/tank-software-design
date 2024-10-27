package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.view.TankGraphics;

public class AIController {

    public static void control(TankGraphics tank) {
        int i = (int) (Math.random() * 4);
        if (i == 0)
            tank.moveModel(MovementDirection.UP);
        else if (i == 1) {
            tank.moveModel(MovementDirection.DOWN);
        } else if (i == 2) {
            tank.moveModel(MovementDirection.LEFT);
        } else if (i == 3) {
            tank.moveModel(MovementDirection.RIGHT);
        }
    }

}
