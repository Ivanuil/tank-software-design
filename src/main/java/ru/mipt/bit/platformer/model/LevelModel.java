package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.view.Obstacle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LevelModel {

    private Collection<TreeModel> trees;
    private TankModel playerTank;
    private Collection<TankModel> npcTanks;

    private int rowCount;
    private int columnCount;

    public LevelModel() {
    }

    public void setTrees(Collection<TreeModel> trees) {
        this.trees = trees;
    }

    public void setPlayerTank(TankModel playerTank) {
        this.playerTank = playerTank;
    }

    public void setLevelSize(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public Collection<? extends Obstacle> getObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.addAll(trees);
        obstacles.addAll(npcTanks);
        obstacles.add(getPlayerTank());
        return obstacles;
    }

    public Collection<TreeModel> getTrees() {
        return List.copyOf(trees);
    }

    public TankModel getPlayerTank() {
        return playerTank;
    }

    public void setNpcTanks(Collection<TankModel> npcTanks) {
        this.npcTanks = npcTanks;
    }

    public Collection<TankModel> getNpcTanks() {
        return new ArrayList<>(npcTanks);
    }

}
