package maingame;

import org.newdawn.slick.*;

public class AnimateSprite extends Node {
	private SpriteSheet _SpriteSheet; 
	int _Columns, _Rows;

	Animation _Animation;
	public AnimateSprite(){
	}
	
	public AnimateSprite(String filepath, int columns , int rows) throws SlickException {
		super(filepath, columns, rows);
		Image _Image = new Image(filepath);
		int tileWidth = _Image.getWidth() / columns;
		int tileHeight = _Image.getHeight()/ rows;
		_SpriteSheet = new SpriteSheet(_Image, tileWidth , tileHeight);
		_Columns = columns;
		_Rows = rows;
		_SpriteSheet.setRotation(90);
		_Animation = new Animation(_SpriteSheet, 100);
	}

	public void animate(int[] frames, int animationdelay ) {
		int [] dutation = new int[frames.length];
		Image[] img = new Image[frames.length];
		
		for(int i = 0; i < frames.length; i++)
		{
			dutation[i] = animationdelay;
			img[i] = _SpriteSheet.getSubImage(frames[i] % _Columns, frames[i] / _Columns);
		}
		_Animation =  new Animation(img, dutation);
	}
	
	
	public void animate(int[] frames, int animationdelay, boolean loop ) {
		int [] dutation = new int[frames.length];
		Image[] img = new Image[frames.length];
		
		for(int i = 0; i < frames.length; i++)
		{
			dutation[i] = animationdelay;
			img[i] = _SpriteSheet.getSubImage(frames[i] % _Columns, frames[i] / _Columns);
		}
		_Animation =  new Animation(img, dutation);
		_Animation.setLooping(false);
	}
	
	public void setCurrentFrame(int index)
	{
		_Animation.setCurrentFrame(index);
	}
	
	public void render()
	{
		float YTransform = (_Box.y - MainGame.SCREENHEIGHT  + this.getParent().getPositionY()) * (-1) - _SpriteSheet.getHeight()/ _Rows / 2;
		float XTransform = _Box.x + this.getParent().getPositionX() - _Box.w / 2;
		
		_Animation.getCurrentFrame().setCenterOfRotation(_CenterRotateX, _CenterRotateY);
		_Animation.draw(XTransform, YTransform);
	}
	
	public void setRotation(float angle)
	{
		for(int i =0; i < _Animation.getFrameCount(); i++)
		{
			_Animation.getImage(i).setRotation(angle);;
		}
	}
	
	public void update(float delta) {
		if (autoRelease) {
			if (_Animation.isStopped()) {
				this.getParent().removeChild(this);
			}
		}
	}
}
