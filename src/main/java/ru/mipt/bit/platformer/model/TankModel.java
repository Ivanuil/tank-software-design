package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class TankModel {

    // set player initial position
    private GridPoint2 destinationCoordinates = new GridPoint2();
    // player current position coordinates on level 10x8 grid (e.g. x=0, y=1)
    private final GridPoint2 coordinates;
    private float rotation = 0f;
    private float movementProgress = 1f;

    public TankModel(GridPoint2 initialCoordinates) {
        destinationCoordinates = new GridPoint2(initialCoordinates);
        coordinates = new GridPoint2(destinationCoordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public float getRotation() {
        return rotation;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public void moveModel(MovementDirection movementDirection, GridPoint2 obstacleCoordinates) {
        if (isEqual(movementProgress, 1f)) {
            // check potential player destination for collision with obstacles
            GridPoint2 potentialDestination = new GridPoint2(coordinates).add(movementDirection.getMovementVector());
            if (!obstacleCoordinates.equals(potentialDestination)) {
                destinationCoordinates = potentialDestination;
                movementProgress = 0f;
            }
            rotation = movementDirection.getRotation();
        }
    }

    public void updateMovementProgress(float deltaTime, float movementSpeed) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            // record that the player has reached his/her destination
            coordinates.set(destinationCoordinates);
        }
    }

}
