package maingame;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class LoginLayer extends Layer{

	private Sprite loginform;
	private Sprite selectboard;
	private String searchpath;
	
	private Text username;
	private Text password;
	private Text loginmessage;
	private String user;
	private String pass;
	int currentselect = 0;
	
	public LoginLayer()
	{
		searchpath = "resources//sprite//other//";
		try {
			loginform = new Sprite(searchpath + "loginform.png");
			loginform.setPosition(Globals.SCREENWIDTH / 2, Globals.SCREENHEIGHT /2 );
			this.addChild(loginform);
			
			selectboard = new Sprite(searchpath + "selection.png");
			selectboard.setPosition(Globals.SCREENWIDTH / 2 + 68, Globals.SCREENHEIGHT /2 + 40);
			this.addChild(selectboard);
			
			username = new Text(Globals.SCREENWIDTH / 2 , Globals.SCREENHEIGHT /2 -50,"");
			password = new Text(Globals.SCREENWIDTH / 2, Globals.SCREENHEIGHT /2-10 ,"");
			loginmessage = new Text(Globals.SCREENWIDTH / 2 - 100 , Globals.SCREENHEIGHT /2 + 30, "");
			this.addChild(username);
			this.addChild(password);
			this.addChild(loginmessage);
			pass = "";
			user = "";
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(float deltatime) {
		super.update(deltatime);
		
		switch(currentselect)
		{
		case 0:
			selectboard.setPositionY(Globals.SCREENHEIGHT /2 + 40);
			break;
		case 1:
			selectboard.setPositionY(Globals.SCREENHEIGHT /2 );
			break;
		case 2:
			selectboard.setPositionY(Globals.SCREENHEIGHT /2 - 40);
			break;
		}
		
		for (int i = 0; i < Globals.ServerMessage.size(); i++) {
			int result = Integer.parseInt(Globals.ServerMessage.get(i));
			if (result == Message.LOGIN_SUCCESSFUL.value()) {
				Globals.userName = user;
				Globals.ServerMessage.clear();
				MainGame.setScene(new MainMenuScene());
			} else if (result == Message.LOGIN_FAIL_WROND_PASS.value()) {
				Globals.ServerMessage.clear();
				loginmessage.setText("Wrong Password");
				loginmessage.setPosition(Globals.SCREENWIDTH / 2 - 150,
						Globals.SCREENHEIGHT / 2 + 30);
			} else if (result == Message.LOGIN_FAIL_ALREADY_LOGIN.value()) {
				Globals.ServerMessage.clear();
				loginmessage.setText("Already Logined");
				loginmessage.setPosition(Globals.SCREENWIDTH / 2 - 150,
						Globals.SCREENHEIGHT / 2 + 30);
			}
		}
	}
	
	public void keyPressed(int key)
	{
		String intputkey = Input.getKeyName(key);
		switch(key)
		{
			case Input.KEY_UP:
				currentselect--;
				if(currentselect < 0)
					currentselect = 2;
				break;
			case Input.KEY_DOWN:
				currentselect++;
				if(currentselect > 2)
					currentselect = 0;
				break;
			case Input.KEY_ENTER:
			case Input.KEY_NUMPADENTER:
				currentselect++;
				if(currentselect > 2)
				{
					currentselect = 2;
					String lmessage = maingame.Message.LOGIN.value() + "\t" + user.toLowerCase() + "\t" + pass.toLowerCase();
					Globals.sendData(lmessage);
				}
			break;
			case Input.KEY_BACK:
				switch(currentselect)
				{
				case 0:
					String temp = username.getText();
					String temp2 ="";
					for(int i = 0; i <temp.length() -1; i++)
					{
						temp2 += temp.charAt(i);
					}
					username.setText(temp2);
					user =  username.getText();
					break;
				case 1:
					String temp1 = password.getText();
					String temp3 ="";
					for(int i = 0; i < temp1.length() -1; i++)
					{
						temp3 += temp1.charAt(i);
					}
					String temp4 ="";
					for(int i =0; i< pass.length() -1; i++)
					{
						temp4 += pass.charAt(i);
					}
					pass = temp4;
					password.setText(temp3);
					break;
				}
				break;
		}
		
		if(intputkey.length() == 1)
		{
			switch(currentselect)
			{
			case 0:
				String temp = username.getText();
				if(temp.length()<15)
				{
				temp += intputkey;
				username.setText(temp);
				user =  username.getText();
				}
				break;
			case 1:
				String temp1 = password.getText();
				if(temp1.length() < 15)
				{
					pass += intputkey;
					temp1 +="*" ;
					password.setText(temp1);
				}
				break;
			}
		}
	}

	public void render()
	{
		super.render();
	}

}
