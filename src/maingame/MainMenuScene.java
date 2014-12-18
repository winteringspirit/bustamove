package maingame;

public class MainMenuScene extends Scene{
	
	private MainMenuLayer _MainMenuLayer;
	public MainMenuScene()
	{
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
