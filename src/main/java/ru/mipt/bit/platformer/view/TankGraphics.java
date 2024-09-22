package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.model.TankModel;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class TankGraphics implements GraphicsObject {

    // Texture decodes an image file and loads it into GPU memory, it represents a native resource
    private final Texture texture = new Texture("images/tank_blue.png");
    // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
    private final TextureRegion graphics = new TextureRegion(texture);
    private final Rectangle rectangle = createBoundingRectangle(graphics);

    private final TankModel model = new TankModel();

    private static final float MOVEMENT_SPEED = 0.4f;

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, model.getRotation());
    }

    public void dispose() {
        texture.dispose();
    }

    public void moveImage(TileMovement tileMovement, float deltaTime) {
        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(rectangle,
                model.getCoordinates(), model.getDestinationCoordinates(), model.getMovementProgress());

        model.updateMovementProgress(deltaTime, MOVEMENT_SPEED);
    }

    public void moveModel(MovementDirection movementDirection, GridPoint2 obstacleCoordinates) {
        model.moveModel(movementDirection, obstacleCoordinates);
    }

}
