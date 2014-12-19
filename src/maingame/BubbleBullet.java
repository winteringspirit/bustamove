package maingame;

import java.util.*;

import org.newdawn.slick.SlickException;

public class BubbleBullet extends AnimateSprite{

	static String bubblesearchpath = "resources//sprite//bubble//";
	public int _Type;
	public Box _BigBox;
	boolean _FallingForce = false;
	boolean dead= false;
	public BubbleBullet() {
		super();
	}
	
	public BubbleBullet(String filepath, int columns, int rows) throws SlickException {
		super(bubblesearchpath + filepath, columns, rows);
		// TODO Auto-generated constructor stub
		_Box = new Box();
		_Box.w = 26;
		_Box.h = 30;
		_BigBox = new Box();
		_BigBox.w = 38;
		_BigBox.h = 38;
	}

	static BubbleBullet create()
	{
		BubbleBullet bl = new BubbleBullet();
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(Globals.BubbleColor);
		
		String filename = randomInt +  ".png";		
		try {
			bl = new BubbleBullet(filename, 4, 3 );
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bl._Type = randomInt;
		return bl;
	}
	
	static BubbleBullet create(int color)
	{
		BubbleBullet bl = new BubbleBullet();
		String filename = color +  ".png";		
		try {
			bl = new BubbleBullet(filename, 4, 3 );
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bl._Type = color;
		return bl;
	}
	
	public void update(float deltatime)
	{
		if (!dead) {
			if(!isCollide)
			{
			_Box.vx += _Box.ax;
			_Box.vy += _Box.ay;
			_Box.x += _Box.vx;
			_Box.y += _Box.vy;
			_BigBox.x = _Box.x;
			_BigBox.y = _Box.y;
			}
			else
			{
				isCollide = false;
			}
			if (_Box.y < -500||_Box.y > 1000) {
				//auto release this child if it fall down
				this.getParent().removeChild(this);
				dead = true;
			}
			
		}
	}
	
	public void setPositionX(float x){
		_Box.x = x;
		_BigBox.x = x;
	}
	
	public void setPositionY(float y){
		_Box.y = y;
		_BigBox.y = y;
	}
	
	public void setPosition(float x, float y){
		_Box.x = x;
		_BigBox.x = x;
		_Box.y = y;
		_BigBox.y = y;
	}
	
	public void updateBigBox()
	{
		_BigBox.x = _Box.x;
		_BigBox.y = _Box.y;
	}
}
