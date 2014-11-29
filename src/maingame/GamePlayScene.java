package maingame;

import java.util.*;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GamePlayScene extends Scene{

	int PlayerId;
	List<GamePlayerLayer> _ListPlayers = new ArrayList<GamePlayerLayer>();
	GamePlayerLayer player;
	GamePlayerLayer player2;
	GamePlayerLayer player3;
	GamePlayerLayer player4;
	public GamePlayScene() throws SlickException
	{
		PlayerId = 0;
		player = new GamePlayerLayer();
		player.setPosition(165,350);
		addChild(player);
			
		player2 = new GamePlayerLayer();
		player2.setPosition(465,350);
		addChild(player2);
		
		player3 = new GamePlayerLayer();
		player3.setPosition(765,350);
		addChild(player3);
		
		player4 = new GamePlayerLayer();
		player4.setPosition(1065,350);
		addChild(player4);
		
	}
	
	public void keyPressed(int key)
	{
		
	}
	
	public void keyReleased(int key)
	{
		switch(key)
		{
			case Input.KEY_SPACE:
				player.fire();
				break;
		}
	}
	
	public void keyHold(List<Integer> _HeldKeys)
	{
		if(_HeldKeys.size() == 0)
		{
			player.stopTurn();
		}
		else
		{
			for(int i = 0 ; i < _HeldKeys.size(); i++)
			{
				switch(_HeldKeys.get(i))
				{
					case Input.KEY_LEFT:
						player.turnLeft();
						break;
					case Input.KEY_RIGHT:
						player.turnRight();
						break;
					default:
						player.stopTurn();
						break;
				}
			}
		}
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
