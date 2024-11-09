package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.view.Obstacle;

import java.util.Collection;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class TankModel implements Obstacle, ObjectWithHealthModel {

    // set player initial position
    private GridPoint2 destinationCoordinates = new GridPoint2();
    // player current position coordinates on level 10x8 grid (e.g. x=0, y=1)
    private final GridPoint2 coordinates;
    private float rotation = 0f;
    private float movementProgress = 1f;
    private float health = 100f;

    private final LevelModel level;

    public TankModel(GridPoint2 initialCoordinates, LevelModel level) {
        destinationCoordinates = new GridPoint2(initialCoordinates);
        this.level = level;
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

    public void moveModel(MovementDirection movementDirection) {
        if (isEqual(movementProgress, 1f)) {
            // check potential player destination for collision with obstacles
            GridPoint2 potentialDestination = new GridPoint2(coordinates).add(movementDirection.getMovementVector());
            if (level.getObstacles().stream()
                    .noneMatch(obstacle -> obstacle.getTakenCoordinates().stream()
                            .anyMatch(potentialDestination::equals)) && !level.isOutOfBorder(potentialDestination)) {
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

    @Override
    public Collection<GridPoint2> getTakenCoordinates() {
        return List.of(coordinates, destinationCoordinates);
    }

    @Override
    public float getHealth() {
        return health;
    }
}
