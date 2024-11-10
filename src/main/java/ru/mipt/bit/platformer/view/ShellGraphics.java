package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.model.ShellModel;
import ru.mipt.bit.platformer.model.TankModel;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class ShellGraphics implements MovingGraphicsObject {

    // Texture decodes an image file and loads it into GPU memory, it represents a native resource
    private final Texture texture;
    // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    private final ShellModel model;

    private final float movementSpeed;

    public ShellGraphics(float movementSpeed, String texturePath, ShellModel model) {
        this.movementSpeed = movementSpeed;
        texture = new Texture(texturePath);
        graphics = new TextureRegion(texture);
        this.model = model;
        rectangle = createBoundingRectangle(graphics);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, model.getRotation());
    }

    public void dispose() {
        texture.dispose();
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void moveImage(TileMovement tileMovement, float deltaTime) {
        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(rectangle,
                model.getCoordinates(), model.getDestinationCoordinates(), model.getMovementProgress());

        model.updateMovementProgress(deltaTime, movementSpeed);
    }

    @Override
    public void moveModel(MovementDirection movementDirection) {
        model.moveModel();
    }

    // TODO: remove
    @Override
    public TankModel getModel() {
        return null;
    }

    public ShellModel getShellModel() {
        return model;
    }
}
