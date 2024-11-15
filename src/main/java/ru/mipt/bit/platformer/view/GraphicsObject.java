package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface GraphicsObject {

    void render(Batch batch);

    void dispose();

    Rectangle getRectangle();

}
