package ru.mipt.bit.platformer.levelloaders;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.LevelModel;
import ru.mipt.bit.platformer.model.TankModel;
import ru.mipt.bit.platformer.model.TreeModel;

import java.util.ArrayList;
import java.util.List;

public class RandomisedLevelLoader implements LevelLoader {

    private final double obstacleDensity;
    private final int rowCount;
    private final int columnCount;

    public RandomisedLevelLoader(double obstacleDensity, int rowCount, int columnCount) {
        this.obstacleDensity = obstacleDensity;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    @Override
    public LevelModel loadLevel() {
        List<TreeModel> trees = new ArrayList<>();
        TankModel tank = new TankModel(new GridPoint2(
                (int) (Math.random() * rowCount),
                (int) (Math.random() * columnCount)));

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (tank.getCoordinates().equals(new GridPoint2(row, column)))
                    continue;
                if (Math.random() < obstacleDensity)
                    trees.add(new TreeModel(new GridPoint2(row, column)));
            }
        }

        return new LevelModel(trees, tank, rowCount, columnCount);
    }

}
