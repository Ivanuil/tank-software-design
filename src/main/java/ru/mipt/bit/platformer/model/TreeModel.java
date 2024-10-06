package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class TreeModel {

    private final GridPoint2 coordinates;

    public TreeModel(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

}
