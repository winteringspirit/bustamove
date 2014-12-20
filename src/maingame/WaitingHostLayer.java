package maingame;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class WaitingHostLayer extends Layer {
	private String searchpath;

	private Text HostList;
	int currentselect = 0;
	int totalhostcount = 0;

	float CannonPositionX = 0;
	float CannonPositionY = -260;

	String cannonsearchpath = "resources//sprite//cannon//";
	String charactersearchpath = "resources//sprite//character//";
	String backgroundsearchpath = "resources//sprite//background//";

	// declare bubble cannon
	Sprite _Dais;
	AnimateSprite _MainChar;
	Sprite _StartButton, selectArrow;
	int _CharacterID;

	public WaitingHostLayer(int characterid) {
		searchpath = "resources//sprite//other//";
		try {
			_Dais = new Sprite("resources//sprite//cannon//dais.png");
			_Dais.setPosition(CannonPositionX - 20, CannonPositionY - 60);
			this.addChild(_Dais);
			_CharacterID = characterid;
			switch (_CharacterID) {
			case 0:
				_MainChar = new AnimateSprite(
						"resources//sprite//other//mogwating.png", 5, 1);
				_MainChar.setPosition(CannonPositionX - 146,
						CannonPositionY + 7);
				_MainChar.animate(new int[] { 0, 1, 2, 3, 4, 3, 2, 1 }, 100);
				break;
			case 1:
				_MainChar = new AnimateSprite(
						"resources//sprite//other//catchwaiting.png", 5, 1);
				_MainChar.setPosition(CannonPositionX - 126,
						CannonPositionY + 42);
				_MainChar.animate(new int[] { 0, 1, 2, 3, 4, 4, 4, 4, 3, 2, 1,
						0, 0, 0 }, 100);
				break;
			case 2:
				_MainChar = new AnimateSprite(
						"resources//sprite//other//misstwaiting.png", 7, 1);
				_MainChar.setPosition(CannonPositionX - 150,
						CannonPositionY + 30);
				_MainChar.animate(new int[] { 0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2,
						1,0,0,0 }, 120);
				break;
			case 3:
				_MainChar = new AnimateSprite(
						"resources//sprite//other//mr@waiting.png", 7, 1);
				_MainChar.setPosition(CannonPositionX - 146,
						CannonPositionY + 7);
				_MainChar.animate(new int[] { 0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2,
						1 }, 100);
				break;
			default:
				_MainChar = new AnimateSprite(
						"resources//sprite//other//mogwating.png", 5, 1);
				_MainChar.setPosition(CannonPositionX - 146,
						CannonPositionY + 7);
				_MainChar.animate(new int[] { 0, 1, 2, 3, 4, 3, 2, 1 }, 100);
				break;
			}
			this.addChild(_MainChar);

			if (Globals.isHost) {
				_StartButton = new Sprite("resources//sprite//other//start.png");
				_StartButton.setPosition(CannonPositionX + MainGame.SCREENWIDTH
						- 250, CannonPositionY + MainGame.SCREENHEIGHT - 130);
				this.addChild(_StartButton);

				selectArrow = new Sprite("resources//sprite//other//arrow.png");
				selectArrow.setPosition(CannonPositionX + MainGame.SCREENWIDTH
						- 350, CannonPositionY + MainGame.SCREENHEIGHT - 130);
				this.addChild(selectArrow);
			}

		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(float deltatime) {
		super.update(deltatime);
	}

	public void keyPressed(int key) {
		switch (key) {
		case Input.KEY_UP:
			currentselect++;
			if (currentselect > totalhostcount)
				currentselect = 0;
			break;
		case Input.KEY_DOWN:
			currentselect--;
			if (currentselect < 0)
				currentselect = totalhostcount;
			break;
		case Input.KEY_ENTER:
		case Input.KEY_NUMPADENTER:
			if (Globals.isHost) {
				Globals.sendData(String.valueOf(Message.START_GAME.value()));
			}
			break;
		case Input.KEY_BACK:
			break;
		}
	}

	public void render() {
		super.render();
	}
}
