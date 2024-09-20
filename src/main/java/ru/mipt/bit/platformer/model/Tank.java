package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.view.GraphicsObject;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank implements GraphicsObject {

    // Texture decodes an image file and loads it into GPU memory, it represents a native resource
    private final Texture texture = new Texture("images/tank_blue.png");
    // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
    private final TextureRegion graphics = new TextureRegion(texture);
    private final Rectangle rectangle = createBoundingRectangle(graphics);
    // set player initial position
    private GridPoint2 destinationCoordinates = new GridPoint2(1, 1);
    // player current position coordinates on level 10x8 grid (e.g. x=0, y=1)
    private final GridPoint2 coordinates = new GridPoint2(destinationCoordinates.x, destinationCoordinates.y);
    private float rotation = 0f;
    private float movementProgress = 1f;

    private static final float MOVEMENT_SPEED = 0.4f;

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }

    public void dispose() {
        texture.dispose();
    }

    public void moveImage(TileMovement tileMovement, float deltaTime) {
        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(rectangle,
                coordinates, destinationCoordinates, movementProgress);

        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f)) {
            // record that the player has reached his/her destination
            coordinates.set(destinationCoordinates);
        }
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

}
