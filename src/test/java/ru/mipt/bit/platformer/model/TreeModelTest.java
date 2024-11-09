package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreeModelTest {

    @Test
    public void testTree() {
        var tree = new TreeModel(new GridPoint2(1, 3));

        assertEquals(new GridPoint2(1, 3),tree.getCoordinates());
    }

}
