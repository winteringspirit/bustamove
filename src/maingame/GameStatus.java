package maingame;

public enum GameStatus {
	LOGIN(0),
	MAINMENU(1),
	WAITINGSCENE(2),
	PLAYINGGAME(3)
	;

	private int _Value;
	GameStatus(int value)
	{
		_Value = value;
	}
}
