package maingame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class LoginScene extends Scene {
	
	private LoginLayer _LoginLayer;
	public LoginScene()
	{
		_LoginLayer = new LoginLayer();
		this.addChild(_LoginLayer);
	}

	public void keyPressed(int key)
	{
		_LoginLayer.keyPressed(key);
	}
	
	public void update(float deltatime)
	{
		super.update(deltatime);
	}
	
	public void render()
	{
		super.render();
	}
}
