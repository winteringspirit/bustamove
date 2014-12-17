package maingame;

import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MainMenuLayer extends Layer {
	private String searchpath;
	
	private Text HostList;
	
	int currentselect = 0;
	
	int totalhostcount = 0;
	private Sprite background;
	private Sprite hostbutton;
	private Sprite selectArrow;
	private ArrayList<String> listHostUser = new ArrayList<String>();
	public MainMenuLayer()
	{
		searchpath = "resources//sprite//other//";
		try {
			background = new Sprite("resources//sprite//other//background.png");
			background.setPosition(MainGame.SCREENWIDTH / 2, MainGame.SCREENHEIGHT / 2);
			this.addChild(background);
			
			hostbutton= new Sprite("resources//sprite//other//hostbutton.png");
			hostbutton.setPosition(MainGame.SCREENWIDTH - 150, MainGame.SCREENHEIGHT - 30);
			this.addChild(hostbutton);
			
			selectArrow = new Sprite("resources//sprite//other//arrow.png");
			selectArrow.setPosition(MainGame.SCREENWIDTH - 100, MainGame.SCREENHEIGHT - 30);
			this.addChild(selectArrow);
			
			HostList = new Text(100 , 50,"");
			this.addChild(HostList);
			
			//send request get host list
			Globals.sendData(String.valueOf(Message.GETHOSTLIST.value()));
			
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
			selectArrow.setPosition(MainGame.SCREENWIDTH - 250, MainGame.SCREENHEIGHT - 30);
		}
		else
		{
			selectArrow.setPosition(50, MainGame.SCREENHEIGHT - 43 - 20 * currentselect);
		}
	
		for(int i = 0; i < Globals.ServerMessage.size(); i++)
		{
			String parts[] = Globals.ServerMessage.get(i).split("\t");
			int result = Integer.parseInt(parts[0]);
			if(result == maingame.Message.GETHOSTLIST.value())
			{
				totalhostcount = Integer.parseInt(parts[1]);
				String listhostname = "";
				for(int j = 2; j < parts.length ; j += 2)
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
					int numberjoiner = Integer.parseInt(parts[1]);
					MainGame.setScene(new WaitingHostScene(numberjoiner));
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
					Globals.sendData(String.valueOf(Message.HOSTGAME.value()));
					Globals.ServerMessage.clear();
					MainGame.setScene(new WaitingHostScene(1));
				}
				else
				{
					Globals.sendData(String.valueOf(Message.JOINGAME.value()) + "\t" + listHostUser.get(currentselect-1));
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
