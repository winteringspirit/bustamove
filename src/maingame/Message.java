package maingame;

public enum Message {
	LOGIN_SUCCESSFUL(1),
	LOGIN_FAIL_ALREADY_LOGIN(0),
	LOGIN_FAIL_WROND_PASS(-1),
	LOGIN(-2),
	GET_HOST_LIST(2),
	HOST_GAME(3),
	JOIN_GAME(4),
	EDIT_PLAYER(5),
	START_GAME(6),
	CANCEL_HOST(7),
	QUIT_GAME(8),
	JOIN_FAIL(9),
	JOIN_SUCCESSFULL(10),
	ABANDON_HOST(11),
	ABANDON_JOIN(12),
	KEY_LEFT(13), 
	KEY_RIGHT(14),
	KEY_FIRE(15),
	NO_KEY(16),
	KEY_PRESS(17),
	BUBBLE_COLOR(18),
	BUBBLE_BULLET(19),
	HOST_ABANDON_GAME(20),
	CLIEN_ABANDON_GAME(21);
	
	private int _value;
	
	private Message(int value) {
		this._value = value;
	}
	
	public int value()
	{
		return this._value;
	}
}
