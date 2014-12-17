package maingame;

import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.ShapeRenderer;

public class Text extends Node {

    private float x;
    private float y;
    private String drawText;

    public Text(float x, float y, String text)
    {
        this.x = x;
        this.y = y;
        this.drawText = text;
    }

    public void setText(String text)
    {
    	drawText = text;
    }
    
    public String getText()
    {
    	return drawText;
    }
    
    public void setPosition(float x, float y)
    {
    	this.x = x;
        this.y = y;
    }
    
    public void render()
    {
		MainGame._g.drawString(drawText, x, y);
    }
}