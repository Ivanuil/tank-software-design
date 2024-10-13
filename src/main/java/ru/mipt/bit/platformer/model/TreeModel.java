package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.view.Obstacle;

public class TreeModel implements Obstacle {

    private final GridPoint2 coordinates;

    public TreeModel(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

}
