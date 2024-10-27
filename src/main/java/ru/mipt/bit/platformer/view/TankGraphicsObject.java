package ru.mipt.bit.platformer.view;

import ru.mipt.bit.platformer.model.MovementDirection;
import ru.mipt.bit.platformer.util.TileMovement;

public interface TankGraphicsObject extends GraphicsObject {

    void moveImage(TileMovement tileMovement, float deltaTime);

    void moveModel(MovementDirection movementDirection);

}
