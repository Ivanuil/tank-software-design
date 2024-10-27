package ru.mipt.bit.platformer.levelloaders;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.LevelModel;
import ru.mipt.bit.platformer.model.TankModel;
import ru.mipt.bit.platformer.model.TreeModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileLevelLoader implements LevelLoader {

    private final File initialiserFile;

    public FileLevelLoader(File levelInitialiserFile) {
        this.initialiserFile = levelInitialiserFile;
    }

    @Override
    public LevelModel loadLevel() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(initialiserFile)))) {
            return readInitializerFile(br);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read level initializer file", e);
        }
    }

    private static LevelModel readInitializerFile(BufferedReader br) throws IOException {
        LevelModel levelModel = new LevelModel();
        List<TreeModel> trees = new ArrayList<>();
        TankModel tankModel = null;
        int rowCounter = 0;
        int columnsInLevel = -1;
        while (br.ready()) {
            String line = br.readLine();
            if (line.isBlank() || line.isEmpty())
                continue;

            if (columnsInLevel == -1)
                columnsInLevel = line.length();
            else if (columnsInLevel != line.length())
                throw new RuntimeException("Number of columns in different rows doesn't match");

            for (int columnCounter = 0; columnCounter < line.length(); columnCounter++) {
                char c = line.charAt(columnCounter);
                switch (c) {
                    case 'T' : {
                        trees.add(new TreeModel(new GridPoint2(rowCounter, columnCounter)));
                        break;
                    }
                    case 'X' : {
                        if (tankModel != null)
                            throw new RuntimeException("Multiple tanks in initializer file");
                        tankModel = new TankModel(new GridPoint2(rowCounter, columnCounter), levelModel);
                        break;
                    }
                }

            }
            rowCounter++;
        }
        if (tankModel == null)
            throw new RuntimeException("No tank in initializer file");
        levelModel.setTrees(trees);
        levelModel.setPlayerTank(tankModel);
        levelModel.setLevelSize(rowCounter, columnsInLevel);
        return levelModel;
    }

}
