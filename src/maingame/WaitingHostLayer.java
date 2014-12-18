package maingame;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class WaitingHostLayer extends Layer {
private String searchpath;
	
	private Text HostList;
	int currentselect = 0;
	int totalhostcount = 0;
	private Sprite background;

	float CannonPositionX = 0;
	float CannonPositionY = -260;

	String cannonsearchpath = "resources//sprite//cannon//";
	String charactersearchpath = "resources//sprite//character//";
	String backgroundsearchpath = "resources//sprite//background//";

	// declare bubble cannon
	Sprite _Dais;
	AnimateSprite _Mog;
	Sprite _StartButton, selectArrow;

	public WaitingHostLayer()
	{
		searchpath = "resources//sprite//other//";
		try {
		_Dais 		= new Sprite("resources//sprite//cannon//dais.png");
		_Dais.setPosition(CannonPositionX - 20, CannonPositionY - 60);
		this.addChild(_Dais);
		
		_Mog 	= new AnimateSprite("resources//sprite//other//mogwating.png", 5, 1);
		_Mog.setPosition(CannonPositionX - 146, CannonPositionY + 7);
		_Mog.animate(new int[] { 0,1,2,3,4,3,2,1 }, 100);
		this.addChild(_Mog);
		
		if(Globals.isHost)
		{
			_StartButton = new Sprite("resources//sprite//other//start.png");
			_StartButton.setPosition(CannonPositionX + MainGame.SCREENWIDTH - 250, 
					CannonPositionY + MainGame.SCREENHEIGHT - 130);
			this.addChild(_StartButton);
			
			selectArrow = new Sprite("resources//sprite//other//arrow.png");
			selectArrow.setPosition(CannonPositionX + MainGame.SCREENWIDTH - 350, 
					CannonPositionY + MainGame.SCREENHEIGHT - 130);
			this.addChild(selectArrow);
		}
		
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(float deltatime) {
		super.update(deltatime);
		for (int i = 0; i < Globals.ServerMessage.size(); i++) {
			String parts[] = Globals.ServerMessage.get(i).split("\t");
			int result = Integer.parseInt(parts[0]);
			if (result == Message.STARTGAME.value()) {
				String listUser[] = new String[4];
				for(int j = 1; j < parts.length; j++)
				{
					if(parts[j].compareTo("null")!=0)
						listUser[j - 1] = parts[j];
				}
				MainGame.setScene(new GamePlayScene(listUser));
				Globals.ServerMessage.clear();
			}
		}
	}
	
	public void keyPressed(int key)
	{
		switch(key)
		{
			case Input.KEY_UP:
				currentselect++;
				if(currentselect > totalhostcount)
					currentselect = 0;
				break;
			case Input.KEY_DOWN:
				currentselect--;
				if(currentselect < 0)
					currentselect = totalhostcount;
				break;
			case Input.KEY_ENTER:
			case Input.KEY_NUMPADENTER:
				if(Globals.isHost)
				{
					Globals.sendData(String.valueOf(Message.STARTGAME.value()));
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
