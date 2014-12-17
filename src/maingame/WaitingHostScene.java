package maingame;

import org.newdawn.slick.SlickException;

public class WaitingHostScene extends Scene {

	private int joincount;
	private WaitingHostLayer[] _WaitingHostLayer;
	public WaitingHostScene(int count)
	{
		_WaitingHostLayer = new WaitingHostLayer[4];
		for(int i = 0 ; i < 4;i++)
		{
			_WaitingHostLayer[i] = new WaitingHostLayer();
			_WaitingHostLayer[i].setPosition(165 + 300 * i, 350);
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
			String part[] = Globals.ServerMessage.get(i).split("\t");
			int result = Integer.parseInt(part[0]);
			if(result == Message.ADDPLAYER.value())
			{ 
				joincount++;
				WaitingHostLayer newWaitingHostLayer = new WaitingHostLayer();
				newWaitingHostLayer.setPosition(165 + joincount * 300, 350);
			}
		}
	}
	
	public void render()
	{
		super.render();
	}
}
