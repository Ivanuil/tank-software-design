package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.view.Obstacle;

import java.util.Collection;
import java.util.List;

public class LevelModel {

    private Collection<TreeModel> trees;
    private TankModel tank;

    private final int rowCount;
    private final int columnCount;

    public LevelModel(Collection<TreeModel> trees, TankModel tank, int rowCount, int columnCount) {
        this.trees = trees;
        this.tank = tank;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public Collection<? extends Obstacle> getObstacles() {
        return List.copyOf(trees);
    }

    public Collection<TreeModel> getTrees() {
        return List.copyOf(trees);
    }

    public TankModel getTank() {
        return tank;
    }

}
