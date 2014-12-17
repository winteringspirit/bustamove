package maingame;

import java.lang.reflect.Array;
import java.util.*;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GamePlayScene extends Scene{

	String PlayerId1;
	String PlayerId2;
	String PlayerId3;
	String PlayerId4;
	List<GamePlayLayer> _ListPlayers = new ArrayList<GamePlayLayer>();
	GamePlayLayer player;
	GamePlayLayer player2;
	GamePlayLayer player3;
	GamePlayLayer player4;
	public GamePlayScene() throws SlickException
	{
		PlayerId1 = "10";
		player = new GamePlayLayer();
		player.setPosition(165,350);
		//player.initSocket(PlayerId1);
		addChild(player);
		
		PlayerId2 = "20";
		player2 = new GamePlayLayer();
		player2.setPosition(465,350);
		//player2.initSocket(PlayerId2);
		addChild(player2);
		
		PlayerId3 = "30";
		player3 = new GamePlayLayer();
		player3.setPosition(765,350);
		//player3.initSocket(PlayerId3);
		addChild(player3);
		
		PlayerId4 = "40";
		player4 = new GamePlayLayer();
		player4.setPosition(1065,350);
		//player4.initSocket(PlayerId4);
		addChild(player4);
		
	}
	
	public void keyReleased(int key)
	{
		switch(key)
		{
			case Input.KEY_SPACE:
				//player.sendData(PlayerId1);
				player.fire();
				break;
		}
	}
	
/**
 *  Hàm dùng để đồng bộ sự kiện keyReleased phím với các player
 * idPlayer : player cần đồng bộ
 */
	public void keyRelease(String idPlayer)
	{
		switch(idPlayer)
		{		
		  case "10":
			  	 player.fire();
		     break;
		  case "20":
			     player2.fire();
			     break;
		  case "30":
			     player3.fire();
			     break;
		  case "40":
			     player4.fire();			  
			     break;
		}
	}
		
	public void keyHold(List<Integer> _HeldKeys)
	{
		ArrayList<String> data = new ArrayList<String>();		
		data.add(PlayerId1);		
		if(_HeldKeys.size() == 0)
		{
			player.stopTurn();
			data.add("0");
			//player.sendData(data);
		}
		else
		{							
			for(int i = 0 ; i < _HeldKeys.size(); i++)
			{
				switch(_HeldKeys.get(i))
				{
					case Input.KEY_LEFT:															
						player.turnLeft();
						data.add(_HeldKeys.get(i).toString());
						//player.sendData(data);
						break;
					case Input.KEY_RIGHT:												
						player.turnRight();
						data.add(_HeldKeys.get(i).toString());
						//player.sendData(data);
						break;
					default:
						player.stopTurn();
						break;
				}
			}	
		}
	}

/**
 * Hàm dùng để synchronous sự kiện nhấn giữ phím
 * _HeldKeys danh sạch các phím được nhấn giữ
 */
	public void keyHoldPlayer(String idPlayer,int _HeldKeys)
	{
		switch(idPlayer)
		{
		 case "10":
				 this.holdKeys(player, _HeldKeys);
				 System.out.println("Action player: " + idPlayer );
			 break;
			 
		 case "20":
				 this.holdKeys(player2, _HeldKeys);
				 System.out.println("Action player:  " + idPlayer );
			 break;
		 case "30":
				 this.holdKeys(player3, _HeldKeys);
				 System.out.println("Action player:  " + idPlayer );
			 break;
		 case "40":
				 this.holdKeys(player4, _HeldKeys);
				 System.out.println("Action player:  " + idPlayer );
			 break;
			 
		}
	}
	
	/**
	 * Bất sự kiện nhấn phím ứng với từng player
	 * @param Playerx  : 
	 * @param _HeldKeys
	 */
	public void holdKeys(GamePlayLayer Playerx, int _HeldKeys)
	{
		switch(_HeldKeys)
		{
			case Input.KEY_LEFT:
				Playerx.turnLeft();
				break;
			case Input.KEY_RIGHT:
				Playerx.turnRight();
				break;
			default:
				Playerx.stopTurn();
				break;
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
