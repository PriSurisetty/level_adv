import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class Player extends Actor {
    private World world;
    private List<GreenfootImage> idleSprites = new ArrayList<>();
    private List<GreenfootImage> runRightSprites = new ArrayList<>();
    private List<GreenfootImage> runLeftSprites = new ArrayList<>();
    private List<GreenfootImage> runUpSprites = new ArrayList<>();
    private List<GreenfootImage> runDownSprites = new ArrayList<>();
    private List<GreenfootImage> attackSprites = new ArrayList<>();
    
    private int frame = 0; 
    private int animationSpeed = 5; 
    private String state = "idle";
    private GreenfootImage idleImg;

    private Weapons currentWeapon; 
    private Knife knife;
    private Gun gun;

    private int health = 100; 

    public Player(World world) {
        // Initialize player sprites
        idleImg = new GreenfootImage("Idle.png");
        setImage(idleImg);
        
        loadSprites(runRightSprites, "Run.png", 10); 
        loadSprites(runLeftSprites, "RunL.png", 10);
        loadSprites(runUpSprites, "Run.png", 10); 
        loadSprites(runDownSprites, "RunL.png", 10);
        loadSprites(attackSprites, "Attack.png", 5);
        this.world = world;
        gun = new Gun(world);
        currentWeapon = gun;
    }

    public void act() {
        movePlayer();
        moveKnife(knife);
        updateState();
        animate();
        handleWeaponInput();
        world.showText(state, 400, 50);
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
    
    private void moveKnife(Knife knife){
        if(knife != null){
            knife.setPosition(getX(), getY());
        }
    }

    private void movePlayer()
    {
        int[] velocity = new int[]{0,0}; //x, y
        //Increase or Decrease X or Y depedent on the key pressed
        if (Greenfoot.isKeyDown("w")) 
        {
            velocity[1]--;
            state = "up";
        }
        if (Greenfoot.isKeyDown("s")) 
        {
            velocity[1]++;
            state = "down";
        }
        if (Greenfoot.isKeyDown("a")) 
        {
            velocity[0]--;
            state = "left";
        }
        if (Greenfoot.isKeyDown("d")) 
        {
            velocity[0]++;
            state = "right";
        }
        
        //If the sum of velocity is greater than 1, diagonal movement is normalized
        if(Math.abs(velocity[0]) + Math.abs(velocity[1]) > 1){
            setLocation(getX() + velocity[0]*4, getY() + velocity[1]*3);
        }else{
            setLocation(getX() + velocity[0]*5, getY() + velocity[1]*5);
        }        
    }

    private void updateState() {
        if (!Greenfoot.isKeyDown("a") && !Greenfoot.isKeyDown("d") &&
            !Greenfoot.isKeyDown("w") && !Greenfoot.isKeyDown("s") && !state.equals("attack")) {
            state = "idle";
        }
    }

    private void animate()
    {
        List<GreenfootImage> currentSprites = null;
        
        if (state.equals("attack")){
            currentSprites = attackSprites;
        }else if (state.equals("up")) {
            currentSprites = runUpSprites;
        }else if (state.equals("down")) {
            currentSprites = runDownSprites;
        }else if (state.equals("left")) {
            currentSprites = runLeftSprites;
        }else if (state.equals("right")) {
            currentSprites = runRightSprites;
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

    private void handleWeaponInput() {
        if (Greenfoot.isKeyDown("1") && knife != null) {
            currentWeapon = knife;  
        }
        if (Greenfoot.isKeyDown("2") && gun != null) {
            currentWeapon = gun;  
        }

        if (Greenfoot.mouseClicked(world)) {
            MouseInfo mouseInfo = Greenfoot.getMouseInfo();
            attack(mouseInfo);
        }
    }

    private void attack(MouseInfo mouseInfo) {
        if (currentWeapon != null) {
            state = "attack";
            currentWeapon.use(this, mouseInfo);  
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("Player has died!");
        }
    }
}
