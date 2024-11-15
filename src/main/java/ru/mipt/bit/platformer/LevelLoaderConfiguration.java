package ru.mipt.bit.platformer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.levelloaders.FileLevelLoader;
import ru.mipt.bit.platformer.levelloaders.LevelLoader;
import ru.mipt.bit.platformer.levelloaders.RandomisedLevelLoader;
import ru.mipt.bit.platformer.model.LevelModel;

import java.io.File;

@Configuration
public class LevelLoaderConfiguration {

    @Value("${app.levelloader}")
    private String levelLoaderName;

    @Value("${app.levelloader.initfilepath}")
    private String filePath;

    private Double obstacleDensity = 0.1;
    private final Integer ROW_COUNT = 10;
    private final Integer COLUMN_COUNT = 8;
    private Integer npcTanksCount = 3;

    @Bean
    public LevelLoader levelLoader() {
        if (FileLevelLoader.class.getName().endsWith(levelLoaderName)) {
            File intitializerFile = new File(filePath);
            return new FileLevelLoader(intitializerFile);
        } else if (RandomisedLevelLoader.class.getName().endsWith(levelLoaderName)) {
            return new RandomisedLevelLoader(obstacleDensity, npcTanksCount, ROW_COUNT, COLUMN_COUNT);
        } else {
            throw new RuntimeException("Level loader not specified");
        }
    }

    @Bean
    public LevelModel levelModel() {
        return levelLoader().loadLevel();
    }

}
