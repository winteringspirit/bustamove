package maingame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.*;

public class Node extends Image{

	public Node _Parent;
	
	float _CenterRotateX;
	float _CenterRotateY;
	Box _Box;
	
	boolean autoRelease = false;
	boolean isCollide =false;
	List<Node> _ListChild = new ArrayList<Node>();
	
	List<Node> _SpecialChild = new ArrayList<Node>();
	
	public Node()
	{
		_Box = new Box();
		_CenterRotateX = 0;
		_CenterRotateY = 0;
	}
	public Node(String filepath, int columns , int rows) 
	{
		super();
		
		_Box = new Box();
		
		_CenterRotateX = 0;
		_CenterRotateY = 0;
	}
	
	public Node(String filepath) throws SlickException{
		super(filepath);
		
		_Box = new Box();
		
		_CenterRotateX = 0;
		_CenterRotateY = 0;
	}
	
	public void addChild(Node child)
	{
		_ListChild.add(child);
		child._Parent = this;
	}
	
	public void addSpecialChild(Node child)
	{
		_SpecialChild.add(child);
		child._Parent = this;
	}
	
	public void update(float delta){}
	
	public void render(){ }
	
	public Node getParent(){return _Parent;}
	
	public void setPosition(float x, float y){
		_Box.x = x;
		_Box.y = y;
	}
	
	public void setPositionX(float x){
		_Box.x = x;
	}
	
	public void setPositionY(float y){
		_Box.y = y;
	}
	
	public float getPositionX(){
		return _Box.x ;
	}
	
	public float getPositionY(){
		return _Box.y;
	}
	
	public void setCenterRotate(float x, float y)
	{
		_CenterRotateX = x;
		_CenterRotateY = y;
	}
	
	public void removeChild(Node node)
	{
		_ListChild.remove(node);
	}
	
	public void removeSpecialChild(Node node)
	{
		_SpecialChild.remove(node);
	}
	
	public void keyPressed(int key)
	{
	}
	public void keyHold(List<Integer> _HeldKeys)
	{
	}
	
	public void keyHoldPlayer(String idPlayer,int _HeldKeys)
	{
		
	}
	
	public void keyRelease(String key)
	{
		
	}
	public void keyReleased(int key)
	{
	}
	
	public void setAutoRelease()
	{
		autoRelease = true;
	}
}
