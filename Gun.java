import greenfoot.*;

public class Gun extends Weapons {
    private World world;

    public Gun(World world) {
        // Gun's damage level
        this.damage = 20;  
        this.sprite = new GreenfootImage("gun.png");
        this.world = world;
    }

    @Override
    public void use(Player player, MouseInfo mouseInfo) {
        shootBullet(player, mouseInfo);
    }

    private void shootBullet(Player player, MouseInfo mouseInfo) {  
        // Create a new bullet in the direction the player is facing
        Bullet bullet = new Bullet(mouseInfo.getX(), mouseInfo.getY());
        world.addObject(bullet, player.getX(), player.getY());
        bullet.setStart(bullet.getX(), bullet.getY());
    }
}