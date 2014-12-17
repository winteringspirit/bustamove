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
	Sprite _PlayBoard;
	
	public WaitingHostLayer()
	{
		searchpath = "resources//sprite//other//";
		try {
		//background = new Sprite("resources//sprite//other//background.png");
		//background.setPosition(MainGame.SCREENWIDTH / 2, MainGame.SCREENHEIGHT / 2);
		//this.addChild(background);

		//_PlayBoard 	= new Sprite("resources//sprite//background//board2.png");
		//_PlayBoard.setPosition(CannonPositionX - 15, CannonPositionY + 250);
		//this.addChild(_PlayBoard);

		_Dais 		= new Sprite("resources//sprite//cannon//dais.png");
		_Dais.setPosition(CannonPositionX - 20, CannonPositionY - 60);
		this.addChild(_Dais);
		
		_Mog 	= new AnimateSprite("resources//sprite//other//mogwating.png", 5, 1);
		_Mog.setPosition(CannonPositionX - 146, CannonPositionY + 7);
		_Mog.animate(new int[] { 0,1,2,3,4,3,2,1 }, 100);
		this.addChild(_Mog);
		
		
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void active()
	{

		_Dais.setColor(TOP_RIGHT, 1, 1, 1);
		_Dais.setColor(TOP_LEFT, 1, 1, 1);
		_Dais.setColor(BOTTOM_RIGHT, 1, 1,1);
		_Dais.setColor(BOTTOM_LEFT, 1, 1,1);
		
		_Mog.setColor(TOP_RIGHT, 1, 1,1);
		_Mog.setColor(TOP_LEFT, 1, 1,1);
		_Mog.setColor(BOTTOM_RIGHT, 1, 1,1);
		_Mog.setColor(BOTTOM_LEFT, 1, 1,1);
	}
	
	public void update(float deltatime) {
		super.update(deltatime);
		
		if(currentselect ==0)
		{
			
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
				if(currentselect == 0)
				{
					//Globals.sendData(String.valueOf(Message.HOSTGAME.value()));
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
