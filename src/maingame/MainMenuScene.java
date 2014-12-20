package maingame;

public class MainMenuScene extends Scene{
	
	private MainMenuLayer _MainMenuLayer;
	private BackGroundLayer _BackGroundLayer;
	public MainMenuScene()
	{
		_BackGroundLayer = new BackGroundLayer();
		this.addChild(_BackGroundLayer);
		_MainMenuLayer = new MainMenuLayer();
		this.addChild(_MainMenuLayer);
		Globals.gameStatus = GameStatus.MAINMENU;
	}

	public void keyPressed(int key)
	{
		_MainMenuLayer.keyPressed(key);
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
