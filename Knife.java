import greenfoot.*;

public class Knife extends Weapons {
    private World world;
    
    public Knife(World world) {
        this.world = world;
        // Knife's damage level
        this.damage = 10;  
        this.sprite = new GreenfootImage("knife.png");  
    }
    
    public void setPosition(int x, int y){
        setLocation(x, y);
    }

    @Override
    public void use(Player player, MouseInfo mouseInfo) {
        checkForCollisions(mouseInfo);
    }
    
    private void checkForCollisions(MouseInfo mouseInfo) {
        Enemy enemy = null;
        if(mouseInfo.getX() > getX()) enemy = (Enemy) getOneObjectAtOffset(130, 0, Enemy.class);
        if(mouseInfo.getX() < getX()) enemy = (Enemy) getOneObjectAtOffset(-130, 0, Enemy.class);
        if (enemy != null) {
            enemy.takeDamage(this.damage);
            return;
        }

        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }
}
