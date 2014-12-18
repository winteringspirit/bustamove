package maingame;

public class WaitingHostScene extends Scene {

	private WaitingHostLayer[] _WaitingHostLayer;
	Text clientName[];
	public WaitingHostScene()
	{
		_WaitingHostLayer = new WaitingHostLayer[4];
		clientName = new Text[4];
		for(int i = 0 ; i < 4;i++)
		{
			_WaitingHostLayer[i] = new WaitingHostLayer();
			_WaitingHostLayer[i].setPosition(165 + 300 * i, 350);
			clientName[i] = new Text(100 + 300 * i, 20, "");
		}
		Globals.gameStatus = GameStatus.WAITINGSCENE;
		
		
	}
	
	public void setPlayer(String[] parts)
	{
		for(int i = 0; i < parts.length; i++)
		{
			this.removeChild(_WaitingHostLayer[i]);
			this.removeChild(clientName[i]);
			if(parts[i] != null)
			{
				this.addChild(_WaitingHostLayer[i]);
				clientName[i].setText(parts[i]);
				this.addChild(clientName[i]);
			}
		}
	}	
	
	public void keyPressed(int key)
	{
		_WaitingHostLayer[0].keyPressed(key);
	}
	
	public void update(float deltatime)
	{
		super.update(deltatime);
		for(int i = 0; i < Globals.ServerMessage.size(); i++)
		{
			String parts[] = Globals.ServerMessage.get(i).split("\t");
			int result = Integer.parseInt(parts[0]);
			if(result == Message.EDITPLAYER.value())
			{ 
				String userJoin[] = new String[4];
				for (int k = 0; k < userJoin.length ;k++)
				{
					if(parts[k + 1].compareTo("null")!=0)
						userJoin[k] = parts[k + 1];
					else
						userJoin[k] = null;
				}
				this.setPlayer(userJoin);
			}
			else
				if(result == Message.ABANDON_HOST.value())
				{
					Globals.ServerMessage.clear();
					MainGame.setScene(new MainMenuScene());
				}
		}
	}
	
	public void render()
	{
		super.render();
	}
}
