package maingame;

public enum Message {
	LOGIN_SUCCESSFUL(1),
	LOGIN_FAIL_ALREADY_LOGIN(0),
	LOGIN_FAIL_WROND_PASS(-1),
	LOGIN(-2),
	GETHOSTLIST(2),
	HOSTGAME(3),
	JOINGAME(4),
	EDITPLAYER(5),
	STARTGAME(6),
	CANCELHOST(7),
	QUITGAME(8),
	JOIN_FAIL(9),
	JOIN_SUCCESSFULL(10),
	ABANDON_HOST(11),
	ABANDON_JOIN(12),
	KEY_LEFT(100), 
	KEY_RIGHT(110),
	KEY_FIRE(120),
	NO_KEY(130),
	KEY_PRESS(140);
	private int _value;
	
	private Message(int value) {
		this._value = value;
	}
	
	public int value()
	{
		return this._value;
	}
}
