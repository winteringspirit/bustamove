package maingame;

import java.util.*;
import org.newdawn.slick.Input;

public class GamePlayScene extends Scene {
	GamePlayLayer player[];
	Text clientName[];

	public GamePlayScene(String listPlayers[]) {
		Globals.gameStatus = GameStatus.PLAYINGGAME;
		player = new GamePlayLayer[listPlayers.length];
		clientName = new Text[listPlayers.length];
		for (int i = 0; i < listPlayers.length; i++) {
			if (listPlayers[i] != null) {
				this.clientName[i] = new Text(100 + 300 * i, 20,
						listPlayers[i].toUpperCase());
				this.addChild(clientName[i]);
				this.player[i] = new GamePlayLayer();
				this.player[i].setPosition(165 + i * 300, 350);
				this.addChild(player[i]);
			}
		}
	}

	public void keyReleased(int key) {
		switch (key) {
		case Input.KEY_SPACE:
			Globals.sendData(String.valueOf(Message.KEY_FIRE.value()));
			break;
		}
	}

	public void keyHold(List<Integer> _HeldKeys) {
		// ArrayList<String> data = new ArrayList<String>();
		// data.add(PlayerId1);
		if (_HeldKeys.size() == 0) {
			// player.stopTurn();
			// data.add("0");
			// player.sendData(data);
		} else {
			for (int i = 0; i < _HeldKeys.size(); i++) {
				switch (_HeldKeys.get(i)) {
				case Input.KEY_LEFT:
					// player.turnLeft();
					// data.add(_HeldKeys.get(i).toString());
					// player.sendData(data);
					Globals.sendData(String.valueOf(Message.KEY_LEFT.value()));
					break;
				case Input.KEY_RIGHT:
					// player.turnRight();
					// data.add(_HeldKeys.get(i).toString());
					// player.sendData(data);
					Globals.sendData(String.valueOf(Message.KEY_LEFT.value()));
					break;
				default:
					// player.stopTurn();
					break;
				}
			}
		}
	}

	public void update(float deltatime) {
		super.update(deltatime);

		for (int i = 0; i < Globals.ServerMessage.size(); i++) {
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
						}
					}
				}
				Globals.ServerMessage.remove(i);
				i--;
			}
		}
	}

	public void render() {
		super.render();
	}

}
