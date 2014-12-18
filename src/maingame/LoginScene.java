package maingame;

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
