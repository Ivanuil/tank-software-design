package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.model.ObjectWithHealthModel;
import ru.mipt.bit.platformer.model.TankModel;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

public class HealthBarDecorator implements MovingGraphicsObject {

    private final MovingGraphicsObject wrappee;

    public HealthBarDecorator(MovingGraphicsObject wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public TankModel getModel() {
        return wrappee.getModel();
    }

    @Override
    public void moveImage(TileMovement tileMovement, float deltaTime) {
        wrappee.moveImage(tileMovement, deltaTime);
    }

    @Override
    public void moveModel(MovementDirection movementDirection) {
        wrappee.moveModel(movementDirection);
    }

    @Override
    public void render(Batch batch) {
        wrappee.render(batch);
        renderHealthbar(batch);
    }

    @Override
    public void dispose() {
        wrappee.dispose();
    }

    @Override
    public Rectangle getRectangle() {
        return wrappee.getRectangle();
    }

    void renderHealthbar(Batch batch) {
        if (!HealthBarsToggle.getToggle())
            return;

        var healthbarTexture = getHealthbarTexture(((ObjectWithHealthModel) getModel()).getHealth() / 100);
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
        var rectangle = new Rectangle(wrappee.getRectangle());
        rectangle.y += 90;
        return rectangle;
    }

}
