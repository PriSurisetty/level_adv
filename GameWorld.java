import greenfoot.*;
import java.util.List;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class GameWorld extends World {
    private int currentLevel = 1;
    private int enemiesToSpawn = 5;
    private boolean waveInProgress = false;
    private Label levelLabel;  // Declare the label

    public GameWorld() {    
        super(800, 600, 1);
        Greenfoot.setSpeed(50);
        prepare(); 
        spawnEnemies(enemiesToSpawn);
        
        // Create label and position it at the bottom left with a larger font
        levelLabel = new Label("Level: " + currentLevel, 100);  // Use a larger font size (e.g., 36)
        addObject(levelLabel, 200, getHeight() - 50);  // Position at the bottom left (50 pixels from the left and 50 pixels above the bottom)

    }

    private void prepare() {
        Player player = new Player(this);
        addObject(player, getWidth() / 2, getHeight() / 2);
        Timer timer = new Timer();
        addObject(timer, getWidth() / 2, 20);
        timer.startTimer();
    }

    private void spawnEnemies(int count) {
    for (int i = 0; i < count; i++) {
        // Randomly scatter enemies across the screen, but ensure they are not overlapping too much
        int x = Greenfoot.getRandomNumber(getWidth() - 100) + 50;  // Spread out horizontally
        int y = Greenfoot.getRandomNumber(getHeight() - 100) + 50; // Spread out vertically

        // Ensure that enemies are not spawned on top of each other
        Enemy enemy = new Enemy();
        enemy.scaleDifficulty(currentLevel);
        addObject(enemy, x, y);
    }
    waveInProgress = true;
}

    private void checkWaveStatus() {
        List<Enemy> enemies = getObjects(Enemy.class);
        if (enemies.isEmpty() && waveInProgress) {
            waveInProgress = false;
            Greenfoot.delay(100);
            currentLevel++;
            enemiesToSpawn += 2;
            spawnEnemies(enemiesToSpawn);
        }
    }

    public void act() {
        checkWaveStatus();
        levelLabel.setText("Level: " + currentLevel);  // Update the level label text
    }
}
