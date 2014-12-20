package maingame;

import java.util.ArrayList;

public class WaitingHostScene extends Scene {

	private WaitingHostLayer[] _WaitingHostLayer;
	Text clientName[];
	int[] bubbleColor;
	ArrayList<Integer> bubbleBullet = new ArrayList<Integer>();
	private BackGroundLayer _BackGroundLayer;
	public WaitingHostScene()
	{
		_BackGroundLayer = new BackGroundLayer();
		this.addChild(_BackGroundLayer);
		_WaitingHostLayer = new WaitingHostLayer[4];
		clientName = new Text[4];
		for(int i = 0 ; i < 4;i++)
		{
			_WaitingHostLayer[i] = new WaitingHostLayer(i);
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
		for (int i = 0; i < Globals.ServerMessage.size(); i++) {
			String parts[] = Globals.ServerMessage.get(i).split("\t");
			int result = Integer.parseInt(parts[0]);
			if (result == Message.EDIT_PLAYER.value()) {
				String userJoin[] = new String[4];
				for (int k = 0; k < userJoin.length; k++) {
					if (parts[k + 1].compareTo("null") != 0)
						userJoin[k] = parts[k + 1];
					else
						userJoin[k] = null;
				}
				this.setPlayer(userJoin);
			} else if (result == Message.ABANDON_HOST.value()) {
				Globals.ServerMessage.clear();
				MainGame.setScene(new MainMenuScene());
			} else if (result == Message.BUBBLE_BULLET.value()) {
				for(int j = 1; j < parts.length; j++){
					bubbleBullet.add(Integer.parseInt(parts[j]));
				}
			} else if (result == Message.BUBBLE_COLOR.value()) {
				String tempBubbleColor[] = new String[8];
				for(int j = 1; j < parts.length; j++)
				{
					if(parts[j].compareTo("null") != 0)
						tempBubbleColor[j - 1] = parts[j];
				}
				GamePlayScene.bubbleColorPerRow = tempBubbleColor;
			} else if (result == Message.START_GAME.value()) {
				String listUser[] = new String[4];
				for (int j = 1; j < parts.length; j++) {
					if (parts[j].compareTo("null") != 0)
						listUser[j - 1] = parts[j];
				}
				MainGame.setScene(new GamePlayScene(listUser, bubbleBullet));
				Globals.ServerMessage.clear();
			}
		}
	}
	
	public void render()
	{
		super.render();
	}
}
