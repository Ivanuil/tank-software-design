package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mipt.bit.platformer.model.MovementDirection.*;

public class TankModelTest {

    @Test
    public void testTankInitialState() {
        var tank = new TankModel(new GridPoint2(1,1));

        assertEquals(new GridPoint2(1,1), tank.getCoordinates());
        assertEquals(tank.getCoordinates(), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    public void testTankModeModelRight() {
        var tank = new TankModel(new GridPoint2(1,1));

        tank.moveModel(RIGHT, new GridPoint2(-100, -100));

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    public void testTankModeModelLeft() {
        var tank = new TankModel(new GridPoint2(1,1));

        tank.moveModel(LEFT, new GridPoint2(-100, -100));

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(-180f, tank.getRotation());
    }

    @Test
    public void testTankModeModelUp() {
        var tank = new TankModel(new GridPoint2(1,1));

        tank.moveModel(UP, new GridPoint2(-100, -100));

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(90f, tank.getRotation());
    }

    @Test
    public void testTankModeModelDown() {
        var tank = new TankModel(new GridPoint2(1,1));

        tank.moveModel(DOWN, new GridPoint2(-100, -100));

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 0), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(-90f, tank.getRotation());
    }

    @Test
    public void testTankNoMove() {
        var tank = new TankModel(new GridPoint2(1,1));

        tank.updateMovementProgress(100, 1);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    public void testTankMovement() {
        var tank = new TankModel(new GridPoint2(1,1));

        tank.moveModel(RIGHT, new GridPoint2(-100, -100));
        tank.updateMovementProgress(0.1f, 1);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(0.1f, tank.getMovementProgress());
    }

    @Test
    public void testTankCollision() {
        var tank = new TankModel(new GridPoint2(1,1));

        tank.moveModel(RIGHT, new GridPoint2(2, 1));
        tank.updateMovementProgress(1f, 1);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

}
