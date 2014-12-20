package maingame;

import java.util.*;

import org.newdawn.slick.Input;

public class GamePlayScene extends Scene {
	GamePlayLayer player[];
	Text clientName[];

	// list bubble color per Row
	static String bubbleColorPerRow[];

	public GamePlayScene(String listPlayers[], ArrayList<Integer> listBullet) {
		this.addChild(new BackGroundLayer());
		Globals.gameStatus = GameStatus.PLAYINGGAME;
		player = new GamePlayLayer[listPlayers.length];
		clientName = new Text[listPlayers.length];
		for (int i = 0; i < listPlayers.length; i++) {
			if (listPlayers[i] != null) {
				this.clientName[i] = new Text(100 + 300 * i, 20,
						listPlayers[i].toUpperCase());
				this.addChild(clientName[i]);
				this.player[i] = new GamePlayLayer(listBullet, i);
				this.player[i].setPosition(165 + i * 300, 350);
				this.player[i].name = listPlayers[i];
				this.addChild(player[i]);
			}
		}

		if (Globals.isHost && this.player[0] != null)
			this.player[0].isHost = true;
	}

	public void keyReleased(int key) {
		switch (key) {
		case Input.KEY_SPACE:
			Globals.sendData(String.valueOf(Message.KEY_FIRE.value()));
			break;
		}
	}

	public void keyHold(List<Integer> _HeldKeys) {
		if (_HeldKeys.size() == 0) {
			Globals.sendData(String.valueOf(Message.NO_KEY.value()));
		} else {
			for (int i = 0; i < _HeldKeys.size(); i++) {
				switch (_HeldKeys.get(i)) {
				case Input.KEY_LEFT:
					Globals.sendData(String.valueOf(Message.KEY_LEFT.value()));
					break;
				case Input.KEY_RIGHT:
					Globals.sendData(String.valueOf(Message.KEY_RIGHT.value()));
					break;
				default:
					Globals.sendData(String.valueOf(Message.NO_KEY.value()));
					break;
				}
			}
		}
	}

	public void update(float deltatime) {
		super.update(deltatime);

		for (int i = 0; i < Globals.ServerMessage.size(); i++) {
			if (Globals.ServerMessage.get(i) != null
					&& Globals.ServerMessage.get(i).compareTo("null") != 0) {
				String parts[] = Globals.ServerMessage.get(i).split("\t");
				int result = Integer.parseInt(parts[0]);
				if (result == Message.KEY_PRESS.value()) {
					for (int j = 1; j < parts.length; j++) {
						if (parts[j].compareTo("null") != 0) {
							int KeyConvert = Integer.parseInt(parts[j]);
							if (KeyConvert == Message.KEY_FIRE.value()) {
								this.player[j - 1].fire();
							} else if (KeyConvert == Message.KEY_LEFT.value()) {
								this.player[j - 1].turnLeft();
							} else if (KeyConvert == Message.KEY_RIGHT.value()) {
								this.player[j - 1].turnRight();
							} else if (KeyConvert == Message.NO_KEY.value()) {
								this.player[j - 1].stopTurn();
							}
						}
					}
					Globals.ServerMessage.remove(i);
					i--;
				} else if (result == Message.BUBBLE_COLOR.value()) {
					String[] _BubbleColor = new String[8];
					for (int j = 0; j < 8; j++) {
						if (parts[j + 1].compareTo("null") != 0)
							_BubbleColor[j] = parts[j + 1];
					}
					bubbleColorPerRow = _BubbleColor;
					Globals.ServerMessage.remove(i);
					i--;
				} else if (result == Message.BUBBLE_BULLET.value()) {
					for (int j = 1; j < parts.length; j++) {
						for (int k = 0; k < player.length; k++) {
							if (this.player[k] != null) {
								this.player[k].addWaitingBullet(Integer
										.parseInt(parts[j]));
							}
						}
					}
					Globals.ServerMessage.remove(i);
					i--;
				} else if (result == Message.HOST_ABANDON_GAME.value()) {
					MainGame.setScene(new MainMenuScene());
					Globals.ServerMessage.clear();
				} else if (result == Message.CLIEN_ABANDON_GAME.value()) {
					if (parts[1].compareTo(Globals.userName.toLowerCase()) == 0) {
						MainGame.setScene(new MainMenuScene());
					} else {
						for (int k = 0; k < player.length; k++) {
							if (player[k] != null
									&& player[k].name.compareTo(parts[1]) == 0) {
								this.removeChild(player[k]);
								clientName[k].setText("Quit Game");
							}
						}
					}
				}
			}
		}
	}

	public void render() {
		super.render();
	}

}
