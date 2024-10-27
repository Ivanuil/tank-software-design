package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collection;

public interface Obstacle {

    Collection<GridPoint2> getTakenCoordinates();

}
