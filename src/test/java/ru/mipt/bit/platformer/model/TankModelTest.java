package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mipt.bit.platformer.model.MovementDirection.*;

public class TankModelTest {

    @Test
    public void testTankInitialState() {
        LevelModel level = new LevelModel();
        var tank = new TankModel(new GridPoint2(1,1), level);

        assertEquals(new GridPoint2(1,1), tank.getCoordinates());
        assertEquals(tank.getCoordinates(), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    public void testTankModeModelRight() {
        var tank = setUpTank();

        tank.moveModel(RIGHT);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    private static TankModel setUpTank() {
        LevelModel level = new LevelModel();
        level.setTrees(List.of());
        level.setNpcTanks(List.of());
        var tank = new TankModel(new GridPoint2(1,1), level);
        level.setPlayerTank(tank);
        level.setLevelSize(10, 10);
        return tank;
    }

    @Test
    public void testTankModeModelLeft() {
        var tank = setUpTank();

        tank.moveModel(LEFT);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(-180f, tank.getRotation());
    }

    @Test
    public void testTankModeModelUp() {
        var tank = setUpTank();

        tank.moveModel(UP);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(90f, tank.getRotation());
    }

    @Test
    public void testTankModeModelDown() {
        var tank = setUpTank();

        tank.moveModel(DOWN);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 0), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(-90f, tank.getRotation());
    }

    @Test
    public void testTankNoMove() {
        var tank = setUpTank();

        tank.updateMovementProgress(100, 1);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    public void testTankMovement() {
        var tank = setUpTank();

        tank.moveModel(RIGHT);
        tank.updateMovementProgress(0.1f, 1);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(0.1f, tank.getMovementProgress());
    }

    @Test
    public void testTankCollision() {
        var tank = setUpTank();

        tank.moveModel(RIGHT);
        tank.updateMovementProgress(1f, 1);

        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

}
