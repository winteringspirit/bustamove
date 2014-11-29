package maingame;

public class Box {

	public float	x, y;
	//dimension
	public float	w, h;
	//veclocity
	public float	vx, vy;
	public float 	ax, ay;
	int direction;
	public Box()
	{
		x = 0;
		y = 0;
		w = 0;
		h = 0;
		vx = 0;
		vy = 0;
		direction= -1;
	}

	public Box(Box b)
	{
		x = b.x;
		y = b.y;
		w = b.w;
		h = b.h;
		vx = b.vx;
		vy = b.vy;
		direction =-1;
	} 
	
	public Box(float _x, float _y, float _w, float _h, float _vx, float _vy)
	{
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		vx = _vx;
		vy = _vy;
		direction =-1;
	}
	public Box(float _x, float _y, float _w, float _h)
	{
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		vx = 0.0f;
		vy = 0.0f;
		direction =-1;
	}
	
	public void setVx(float _vx)
	{
		vx = _vx;
		
	}
	
	public void setVy(float _vy)
	{
		vy = _vy;
	}
	
	public void setAx(float _ax)
	{
		ax = _ax;
	}
	
	public void setAy(float _ay)
	{
		ay = _ay;
	}
}
