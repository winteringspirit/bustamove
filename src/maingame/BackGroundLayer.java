package maingame;

import org.newdawn.slick.SlickException;

public class BackGroundLayer extends Layer {
	private AnimateSprite background;
	public BackGroundLayer() {
		try {
			background = new AnimateSprite(
					"resources//sprite//background//background.png", 3, 1);
			background.setPosition(0,
					MainGame.SCREENHEIGHT / 2);
			background.animate(new int[] { 0, 1, 2 }, 100);
			this.addChild(background);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(float deltatime) {
		super.update(deltatime);
	}

	public void render() {
		super.render();
	}
}
