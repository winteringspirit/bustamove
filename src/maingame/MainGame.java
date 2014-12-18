package maingame;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.newdawn.slick.*;
public class MainGame extends BasicGame
{
	List<GamePlayLayer> _Players = new ArrayList<GamePlayLayer>();
	static final int SCREENWIDTH = 1200;
	static final int SCREENHEIGHT = 600;
	
	static public Scene _Scene;
	static public GameContainer _gc;
	static public Graphics _g;
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
		Globals.gameStatus = GameStatus.LOGIN;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		MainGame.setScene(new LoginScene());
	}
	
	@Override
	public void update(GameContainer gc, int deltatime) throws SlickException {
		_Scene.update(deltatime);
		_Scene.keyHold(_HeldKeys);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		MainGame._g = g;
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
	
	@Override
    public boolean closeRequested()
    {
		if (Globals.gameStatus != GameStatus.PLAYINGGAME) 
		{
			if (Globals.gameStatus == GameStatus.WAITINGSCENE) {
				if (!Globals.isHost)
					Globals.sendData(String.valueOf(Message.ABANDON_JOIN
							.value()));
				else
					Globals.sendData(String.valueOf(Message.ABANDON_HOST
							.value()));
			}
			try {
				Globals.closeConnection();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0); // Use this if you want to quit the app.
		}
		return false;
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
			Globals.initSocket();
			AppGameContainer appgc;
			appgc = new AppGameContainer(new MainGame("Bust a move"));
			appgc.setDisplayMode(SCREENWIDTH, SCREENHEIGHT, false);
			appgc.setTargetFrameRate(60);
			appgc.setUpdateOnlyWhenVisible(false);
			appgc.start();
			//init socket
		}
		catch (SlickException ex)
		{
			Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}