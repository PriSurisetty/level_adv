import greenfoot.*;  
import java.util.List;
import java.util.ArrayList;

public class Enemy extends Actor {
    private List<GreenfootImage> runRSprites = new ArrayList<>();
    private List<GreenfootImage> runLSprites = new ArrayList<>();
    private List<GreenfootImage> attackSprites = new ArrayList<>();
    private List<GreenfootImage> fallSprites = new ArrayList<>();

    private int frame = 0; 
    private int animationSpeed = 5; 
    private String state = "idle";
    private GreenfootImage idleImg;

    private boolean chasing = false;
    private int patrolRange = 50; 
    private int startX; 
    private int speed = 2; 
    private Actor player; 
    private int health = 50;

    public Enemy() {
        idleImg = new GreenfootImage("EnemyIdle.png");
        setImage(idleImg);

        // Load sprites for different actions
        loadSprites(runRSprites, "EnemyRun.png", 10);
        loadSprites(runLSprites, "EnemyRunL.png", 10);
        loadSprites(attackSprites, "RightAttack.png", 4);
        loadSprites(fallSprites, "EnemyFall.png", 5);
    }

    public void act() {
        if (health > 0) {
            updateState(); 
            moveEnemy();   
            animate();
        }
        if (state.equals("fall")) {
            animateFall();
        }
    }
    
    public void setStartX() {
        startX = getX();
    }

    private void loadSprites(List<GreenfootImage> spriteList, String sheetFileName, int frameCount) {
        GreenfootImage spriteSheet = new GreenfootImage(sheetFileName);
        int frameWidth = spriteSheet.getWidth() / frameCount;
        int frameHeight = spriteSheet.getHeight();

        for (int i = 0; i < frameCount; i++) {
            GreenfootImage frame = new GreenfootImage(frameWidth, frameHeight);
            frame.drawImage(spriteSheet, -i * frameWidth, 0);
            spriteList.add(frame);
        }
    }

    private void updateState() {
        player = getWorld().getObjects(Player.class).isEmpty() ? null : getWorld().getObjects(Player.class).get(0);

        if (player != null && Math.abs(player.getX() - getX()) < 200 && Math.abs(player.getY() - getY()) < 200) {
            if (player.getX() > getX()) state = "runR";
            if (player.getX() < getX()) state = "runL";
            speed = 2;
            chasing = true;
        } else {
            if (chasing) {
                chasing = false;
                setStartX();
            }
            if (speed > 0) state = "searchR";
            if (speed < 0) state = "searchL";
        }
    }

    private void moveEnemy() {
        if (state.equals("runR") || state.equals("runL")) {
            // Check if the player is to the right or left of the enemy
            if (player != null) {
                if (player.getX() > getX()) {
                    setLocation(getX() + speed, getY());  // Move to the right
                } else if (player.getX() < getX()) {
                    setLocation(getX() - speed, getY());  // Move to the left
                }
    
                if (player.getY() > getY()) {
                    setLocation(getX(), getY() + speed);  // Move down if the player is below
                } else if (player.getY() < getY()) {
                    setLocation(getX(), getY() - speed);  // Move up if the player is above
                }
            }
        } else if (state.equals("searchR") || state.equals("searchL")) {
            if (getX() == startX + patrolRange) {
                speed = -2;
            }
            if (getX() == startX - patrolRange) {
                speed = 2;
            }
            move(speed);  // Continue patrolling
        }
    }


    private void animate() {
        List<GreenfootImage> currentSprites = null;
        switch (state) {
            case "runR":
                currentSprites = runRSprites;
                break;
            case "runL":
                currentSprites = runLSprites;
                break;
            case "searchR":
                currentSprites = runRSprites;
                break;
            case "searchL":
                currentSprites = runLSprites;
                break;
            case "attack":
                currentSprites = attackSprites;
                break;
        }

        if (currentSprites != null && !currentSprites.isEmpty()) {
            if (frame % animationSpeed == 0) {
                int index = (frame / animationSpeed) % currentSprites.size();
                setImage(currentSprites.get(index));
            }
            frame++;
        } else {
            setImage(idleImg);
        }
    }
    
    private void animateFall() {
        List<GreenfootImage> currentSprites = fallSprites;

        if (currentSprites != null && !currentSprites.isEmpty()) {
            if (frame % animationSpeed == 0) {
                int index = (frame / animationSpeed) % currentSprites.size();
                setImage(currentSprites.get(index));
                if (index == currentSprites.size() - 1) {
                    World world = getWorld();
                    world.removeObject(this);
                }
            }
            frame++;
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            fall();
        }
    }

    public void attack() {
        state = "attack";
    }

    public void fall() {
        state = "fall";
    }

    // New Method to Scale Difficulty
    public void scaleDifficulty(int level) {
        health += level * 10;    // Increase health by 10 per level
        speed = Math.min(5, speed + level / 2); // Cap speed at 5
    }
}
