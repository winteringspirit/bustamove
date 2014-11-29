package maingame;

import org.newdawn.slick.SlickException;

public class Fence extends Sprite{

	static String bubblesearchpath = "resources//sprite//background//";
	
	public Fence(String  filename) throws SlickException {
		
		super(bubblesearchpath + filename);
		// TODO Auto-generated constructor stub
		_Box = new Box();
		_Box.w = this.getWidth();
		_Box.h = this.getHeight();
	}
}
