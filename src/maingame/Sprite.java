package maingame;

import org.newdawn.slick.*;

public class Sprite  extends Node{

	public Sprite(String filepath) throws SlickException {
		super(filepath);
	}

	public void render()
	{
		float YTransform = (_Box.y - Globals.SCREENHEIGHT + this.getParent().getPositionY()) * (-1)  ;
		float XTransform = _Box.x + this.getParent().getPositionX();
		this.setCenterOfRotation(_CenterRotateX, _CenterRotateY);
		super.drawCentered(XTransform, YTransform) ;
	}
}
