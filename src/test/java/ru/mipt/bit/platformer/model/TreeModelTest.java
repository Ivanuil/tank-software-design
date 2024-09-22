package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TreeModelTest {

    @Test
    public void testTree() {
        var tree = new TreeModel();

        Assertions.assertEquals(new GridPoint2(1, 3),tree.getCoordinates());
    }

}
