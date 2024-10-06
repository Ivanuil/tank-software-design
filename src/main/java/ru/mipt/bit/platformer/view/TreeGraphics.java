package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.TreeModel;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class TreeGraphics implements GraphicsObject {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    private final TreeModel model;

    public TreeGraphics(TiledMapTileLayer groundLayer, String texturePath, GridPoint2 coordinates) {
        texture = new Texture(texturePath);
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
        model = new TreeModel(coordinates);
        moveRectangleAtTileCenter(groundLayer, rectangle, model.getCoordinates());
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    public void dispose() {
        texture.dispose();
    }

    public GridPoint2 getCoordinates() {
        return model.getCoordinates();
    }

}
