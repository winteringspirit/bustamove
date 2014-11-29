package maingame;

public class Scene extends Node {
	
	public Scene(){
		
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
