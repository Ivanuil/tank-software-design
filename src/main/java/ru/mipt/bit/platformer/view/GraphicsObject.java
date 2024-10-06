package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface GraphicsObject {

    void render(Batch batch);

    void dispose();

}
