package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.view.Obstacle;
import ru.mipt.bit.platformer.view.ShellUpdateSubscriber;

import java.util.Collection;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class ShellModel implements Obstacle {

    // set player initial position
    private GridPoint2 destinationCoordinates;
    // player current position coordinates on level 10x8 grid (e.g. x=0, y=1)
    private final GridPoint2 coordinates;
    private final float rotation;
    private float movementProgress = 1f;
    private final MovementDirection direction;

    private final LevelModel level;

    private ShellUpdateSubscriber shellUpdateSubscriber;

    public ShellModel(GridPoint2 initialCoordinates, MovementDirection direction, LevelModel level) {
        destinationCoordinates = new GridPoint2(initialCoordinates);
        this.direction = direction;
        rotation = direction.getRotation();
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

    public void moveModel() {
        if (isEqual(movementProgress, 1f)) {
            // check potential player destination for collision with obstacles
            GridPoint2 potentialDestination = new GridPoint2(coordinates).add(direction.getMovementVector());
            if (level.getObstacles().stream()
                    .noneMatch(obstacle -> obstacle.getTakenCoordinates().stream()
                            .anyMatch(potentialDestination::equals)) && !level.isOutOfBorder(potentialDestination)) {
                destinationCoordinates = potentialDestination;
                movementProgress = 0f;
            } else {
                destroy();
                var obstacle = level.getObstacles().stream()
                        .filter(o -> o.getTakenCoordinates().stream().anyMatch(potentialDestination::equals))
                        .findFirst().orElse(null);
                if (obstacle == null)
                    return;
                if (obstacle instanceof ShellModel)
                    ((ShellModel) obstacle).destroy();
                if (obstacle instanceof TankModel)
                    ((TankModel) obstacle).takeHealth(30);
            }
        }
    }

    private void destroy() {
        shellUpdateSubscriber.onShellDestroyed(this);
        level.removeShell(this);
    }

    public void updateMovementProgress(float deltaTime, float movementSpeed) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            // record that the player has reached his/her destination
            coordinates.set(destinationCoordinates);
            moveModel();
        }
    }

    @Override
    public Collection<GridPoint2> getTakenCoordinates() {
        return List.of(coordinates, destinationCoordinates);
    }

    public void setShellUpdateSubscriber(ShellUpdateSubscriber shellUpdateSubscriber) {
        this.shellUpdateSubscriber = shellUpdateSubscriber;
    }

}
