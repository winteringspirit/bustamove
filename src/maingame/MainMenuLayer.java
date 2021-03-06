package maingame;

import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MainMenuLayer extends Layer {
	private Text HostList;
	
	int currentselect = 0;
	
	int totalhostcount = 0;
	private Sprite hostbutton;
	private Sprite selectArrow;
	private ArrayList<String> listHostUser = new ArrayList<String>();
	public MainMenuLayer()
	{
		String searchpath = "resources//sprite//other//";
		try {
			hostbutton= new Sprite("resources//sprite//other//hostbutton.png");
			hostbutton.setPosition(Globals.SCREENWIDTH - 150, Globals.SCREENHEIGHT - 30);
			this.addChild(hostbutton);
			
			selectArrow = new Sprite("resources//sprite//other//arrow.png");
			selectArrow.setPosition(Globals.SCREENWIDTH - 100, Globals.SCREENHEIGHT - 30);
			this.addChild(selectArrow);
			
			HostList = new Text(100 , 50,"");
			this.addChild(HostList);
			
			//send request get host list
			Globals.sendData(String.valueOf(Message.GET_HOST_LIST.value()));
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(float deltatime) {
		super.update(deltatime);
		
		if(currentselect > totalhostcount)
			currentselect = totalhostcount;
		
		if(currentselect == 0)
		{
			selectArrow.setPosition(Globals.SCREENWIDTH - 250, Globals.SCREENHEIGHT - 30);
		}
		else
		{
			selectArrow.setPosition(50, Globals.SCREENHEIGHT - 43 - 20 * currentselect);
		}
	
		for(int i = 0; i < Globals.ServerMessage.size(); i++)
		{
			String parts[] = Globals.ServerMessage.get(i).split("\t");
			int result = Integer.parseInt(parts[0]);
			if(result == maingame.Message.GET_HOST_LIST.value())
			{
				totalhostcount = Integer.parseInt(parts[1]);
				String listhostname = "";
				for(int j = 2; j < parts.length ; j += 5)
				{
					listhostname += parts[j] + " (" + parts[j + 1]+ "/4)\n";
					listHostUser.add(parts[j]);
				}
				HostList.setText(listhostname);
				Globals.ServerMessage.remove(i);
				i--;
			}
			else
				if(result == maingame.Message.JOIN_SUCCESSFULL.value())
				{
					String userJoin[] = new String[4];
					for (int k = 0; k < userJoin.length ;k++)
					{
						if(parts[k + 1].compareTo("null")!=0)
							userJoin[k] = parts[k + 1];
						else
							userJoin[k] = null;
					}
					WaitingHostScene _Scene = new WaitingHostScene();
					_Scene.setPlayer(userJoin);
					MainGame.setScene(_Scene);
				}
				else
					if(result == maingame.Message.JOIN_FAIL.value())
					{
						
					}
			Globals.ServerMessage.clear();
		}
	}
	
	public void keyPressed(int key)
	{
		switch(key)
		{
			case Input.KEY_UP:
				currentselect--;
				if(currentselect < 0)
					currentselect = totalhostcount;
				break;
			case Input.KEY_DOWN:

				currentselect++;
				if(currentselect > totalhostcount)
					currentselect = 0;
				break;
			case Input.KEY_ENTER:
			case Input.KEY_NUMPADENTER:
				if(currentselect == 0)
				{
					Globals.isHost = true;
					Globals.sendData(String.valueOf(Message.HOST_GAME.value()));
					Globals.ServerMessage.clear();
					WaitingHostScene _Scene = new WaitingHostScene();
					String []userJoin = new String[4];
					userJoin[0] = Globals.userName;
					_Scene.setPlayer(userJoin);
					MainGame.setScene(_Scene);
				}
				else
				{
					Globals.sendData(String.valueOf(Message.JOIN_GAME.value()) + "\t" + listHostUser.get(currentselect-1));
					Globals.isHost = false;
				}
				break;
			case Input.KEY_BACK:
				break;
		}
	}

	public void render()
	{
		super.render();
	}
}
