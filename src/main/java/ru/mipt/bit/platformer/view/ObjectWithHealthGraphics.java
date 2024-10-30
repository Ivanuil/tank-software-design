package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.GameLogicException;
import ru.mipt.bit.platformer.model.ObjectWithHealthModel;
import ru.mipt.bit.platformer.util.GdxGameUtils;

public interface ObjectWithHealthGraphics {

    ObjectWithHealthModel getModel();

    default void renderHealthbar(Batch batch) {
        if (!HealthBarsToggle.getToggle())
            return;

        var healthbarTexture = getHealthbarTexture(getModel().getHealth());
        var rectangle = createRectangle();
        GdxGameUtils.drawTextureRegionUnscaled(batch, healthbarTexture, rectangle, 0f);
    }

    private TextureRegion getHealthbarTexture(float relativeHealth) {
        var pixmap = new Pixmap(90, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, 90, 20);
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(0, 0, (int) (90 * relativeHealth), 20);
        var texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(texture);
    }

    private Rectangle createRectangle() {
        if (!(this instanceof GraphicsObject))
            throw new GameLogicException("Object implementing ObjectWithHealthGraphics must implement GraphicsObject");
        GraphicsObject thisGraphicsObject = (GraphicsObject) this;

        var rectangle = new Rectangle(thisGraphicsObject.getRectangle());
        rectangle.y += 90;
        return rectangle;
    }

}
