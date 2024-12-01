import greenfoot.*;  // (Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Label extends Actor {
    private GreenfootImage image;

    public Label(String text, int size) {
        image = new GreenfootImage(text, size, Color.WHITE, Color.BLACK);  // Create a label with white text and black background
        setImage(image);
    }

    public void setText(String text) {
        image.clear();  // Clear previous text
        image.drawString(text, 0, image.getHeight() / 2);  // Draw new text
    }
}
