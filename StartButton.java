import greenfoot.*;

public class StartButton extends Actor
{
    public void act()
    {
        World world = getWorld();
        createText(world);
        start(world);
    }
    
    public void createText(World w) {
        w.showText("Start", this.getX(), this.getY());
    }
    
    public void start(World w) {
        if (Greenfoot.mouseClicked(this)) {
            w.showText("", this.getX(), this.getY());
            w.removeObject(this);

            Greenfoot.setWorld(new GameWorld());
        }
    }
}
