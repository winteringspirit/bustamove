package maingame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Scene extends Node {
	
	public Scene(){
		
	}
	
	public void init(GameContainer gc)
	{
		
	}
	
	public void update(float deltatime){
		for(int i = 0; i <_ListChild.size(); i++)
			_ListChild.get(i).update(deltatime);
	}
	
	public void render()
	{
		for(int i = 0; i <_ListChild.size(); i++)
			_ListChild.get(i).render();
	}
}
