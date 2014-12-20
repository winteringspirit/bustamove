package maingame;

public class LoginScene extends Scene {
	
	private LoginLayer _LoginLayer;
	private BackGroundLayer _BackGroundLayer;
	public LoginScene()
	{
		_BackGroundLayer = new BackGroundLayer();
		this.addChild(_BackGroundLayer);
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
