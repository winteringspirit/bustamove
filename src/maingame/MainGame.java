package maingame;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.newdawn.slick.*;

public class MainGame extends BasicGame
{
	List<GamePlayerLayer> _Players = new ArrayList<GamePlayerLayer>();
	static final int SCREENWIDTH = 1200;
	static final int SCREENHEIGHT = 600;
	
	static public Scene _Scene;
	
	List<Integer> _HeldKeys = new ArrayList<Integer>();
	
	//khai bao log va viet log
	public static final Logger LOGGER = Logger.getLogger(MainGame.class.getName());
	static {
		try {
			LOGGER.addHandler(new FileHandler("errors.log",true));
	    }
	    catch(IOException ex) {
	    	LOGGER.log(Level.WARNING,ex.toString(),ex);
	    }
	}
	
	public MainGame(String gamename)
	{
		super(gamename);
		_Scene = new Scene();
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		MainGame.setScene(new GamePlayScene());
	}
	
	@Override
	public void update(GameContainer gc, int deltatime) throws SlickException {
		_Scene.update(deltatime);
		_Scene.keyHold(_HeldKeys);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		_Scene.render();
		
	}
	
	public void keyPressed(int key, char c)
	{
		_Scene.keyPressed(key);
		_HeldKeys.add(key);
	}
	
	public void keyReleased(int key, char c)
	{
		_Scene.keyReleased(key);
		_HeldKeys.remove((Integer)key);
	}
	
	
	static Scene getScene()
	{
		return _Scene;
	}
	
	static void setScene(Scene scene)
	{
		_Scene = scene;
	}
	
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new MainGame("Bust a move"));
			appgc.setDisplayMode(SCREENWIDTH, SCREENHEIGHT, false);
			appgc.setTargetFrameRate(60);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}