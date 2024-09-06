package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tree {

    private final Texture texture = new Texture("images/greenTree.png");
    private final TextureRegion graphics = new TextureRegion(texture);
    private final GridPoint2 coordinates = new GridPoint2(1, 3);
    private final Rectangle rectangle = createBoundingRectangle(graphics);

    public Tree(TiledMapTileLayer groundLayer) {
        moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    public void dispose() {
        texture.dispose();
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

}
