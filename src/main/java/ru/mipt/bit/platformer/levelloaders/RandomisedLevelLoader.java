package ru.mipt.bit.platformer.levelloaders;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.LevelModel;
import ru.mipt.bit.platformer.model.TankModel;
import ru.mipt.bit.platformer.model.TreeModel;

import java.util.ArrayList;
import java.util.List;

public class RandomisedLevelLoader implements LevelLoader {

    private final double obstacleDensity;
    private final int npcTankCount;
    private final int rowCount;
    private final int columnCount;

    public RandomisedLevelLoader(double obstacleDensity, int npcTankCount, int rowCount, int columnCount) {
        this.obstacleDensity = obstacleDensity;
        this.npcTankCount = npcTankCount;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    @Override
    public LevelModel loadLevel() {
        LevelModel levelModel = new LevelModel();
        List<TreeModel> trees = new ArrayList<>();
        List<TankModel> npcTanks = new ArrayList<>();
        TankModel playerTank = new TankModel(getRandomCoordinates(), levelModel);

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (playerTank.getCoordinates().equals(new GridPoint2(row, column)))
                    continue;
                if (Math.random() < obstacleDensity)
                    trees.add(new TreeModel(new GridPoint2(row, column)));
            }
        }

        for (int i = 0; i < npcTankCount; i++) {
            while (true) {
                var coordinates = getRandomCoordinates();
                if (checkIfCoordinatesAreTaken(coordinates, playerTank, trees, npcTanks))
                    continue;
                npcTanks.add(new TankModel(coordinates, levelModel));
                System.out.println(coordinates.x + " " + coordinates.y);
                break;
            }
        }

        levelModel.setTrees(trees);
        levelModel.setPlayerTank(playerTank);
        levelModel.setLevelSize(rowCount, columnCount);
        levelModel.setNpcTanks(npcTanks);
        return levelModel;
    }

    private static boolean checkIfCoordinatesAreTaken(GridPoint2 coordinates, TankModel playerTank, List<TreeModel> trees, List<TankModel> npcTanks) {
        if (coordinates.equals(playerTank.getCoordinates()))
            return true;
        if (trees.stream().anyMatch(treeModel -> coordinates.equals(treeModel.getCoordinates())))
            return true;
        return npcTanks.stream().anyMatch(tankModel -> tankModel.getCoordinates().equals(coordinates));
    }

    private GridPoint2 getRandomCoordinates() {
        return new GridPoint2(
                (int) (Math.random() * rowCount),
                (int) (Math.random() * columnCount));
    }

}
