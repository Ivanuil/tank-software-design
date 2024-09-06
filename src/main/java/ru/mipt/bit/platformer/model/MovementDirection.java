package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public enum MovementDirection {

    UP, DOWN, LEFT, RIGHT;

    float getRotation() {
        switch (this) {
            case UP: return 90f;
            case DOWN: return -90f;
            case LEFT: return -180f;
            case RIGHT: return 0f;
        }
        throw new RuntimeException("Unknown Movement Direction");
    }

    GridPoint2 getDestinationCoordinates(GridPoint2 coordinates) {
        switch (this) {
            case UP: return incrementedY(coordinates);
            case DOWN: return decrementedY(coordinates);
            case LEFT: return decrementedX(coordinates);
            case RIGHT: return incrementedX(coordinates);
        }
        throw new RuntimeException("Unknown Movement Direction");
    }

}
