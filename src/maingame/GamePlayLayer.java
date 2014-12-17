package maingame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import org.newdawn.slick.*;

import MutilSocket.SocketClient;

public class GamePlayLayer extends Layer {

	Sprite GameBoard;
	AnimateSprite test;

	String cannonsearchpath = "resources//sprite//cannon//";
	String charactersearchpath = "resources//sprite//character//";
	String backgroundsearchpath = "resources//sprite//background//";

	// declare bubble cannon
	Sprite _Machine, _Barrel, _Dais, _Wheel, _Engine2,_Basket1, _Basket2;
	AnimateSprite _Arrows, _Engine, _Bub, _Mog;
	Sprite _PlayBoard;
	Fence _LeftFence, _RightFence, _TopFence, _BotFence;
	float CannonPositionX = 0;
	float CannonPositionY = -260;
	float SpawnPositionX = 0;
	float SpawnPositionY = -64;
	float _RotateAngle = 0;

	List<BubbleBullet> _ListBullet = new ArrayList<BubbleBullet>();
	List<BubbleBullet> _ListBubble = new ArrayList<BubbleBullet>();

	List<List<List<Integer>>> _ListSameBubblesGroups = new ArrayList<List<List<Integer>>>();
	List<List<Integer>> _ListDifferentBubblesGroups = new ArrayList<List<Integer>>();

	List<BubbleBullet> _ListDestroyableBubble = new ArrayList<BubbleBullet>();
	// declare player action status
	ActionStatus _Status = ActionStatus.STOPTURN;

	float _SpawnTime = 5000;
	int _SpawnTimeCount;
	int _SpawnCount = 8;
	float _BubbleBulletSpeed =10.0f;
	float _BubbleMovingSpeed = 0;
	float _DeltaSpawnPosition = 0;
	float _RotateSpeed = 1;
	int SameColorDestroyCount = 2;

	BubbleBullet _BBWaitingFire;
	BubbleBullet _NewBBWaiting;
	// total same color bubbles that you need to fire at to destroy, value 2
	// means 3 bubble(include 2 instance bubble and one you fire)
	public GamePlayLayer() throws SlickException{

		_Machine 	= new Sprite(cannonsearchpath + "machine.png");
		_Barrel 	= new Sprite(cannonsearchpath + "barrel.png");
		_Dais 		= new Sprite(cannonsearchpath + "dais.png");
		_Wheel 		= new Sprite(cannonsearchpath + "wheel.png");
		_Engine2 	= new Sprite(cannonsearchpath + "engine2.png");
		_Basket1	= new Sprite(cannonsearchpath + "basket0.png");
		_Basket2	= new Sprite(cannonsearchpath + "basket1.png");
		_PlayBoard 	= new Sprite(backgroundsearchpath + "board2.png");
		_Arrows 	= new AnimateSprite(cannonsearchpath + "arrows.png", 3, 1);
		_Engine 	= new AnimateSprite(cannonsearchpath + "engine.png", 4, 1);
		_Bub 	= new AnimateSprite(charactersearchpath + "Bub.png", 3, 3);
		_Mog 	= new AnimateSprite(charactersearchpath + "Mog.png", 8, 1);
		
		_LeftFence = new Fence("vertical.png");
		_RightFence = new Fence("vertical.png");
		_TopFence = new Fence("horizontal.png");
		_BotFence = new Fence("bhorizontal.png");

		_PlayBoard.setPosition(CannonPositionX - 15, CannonPositionY + 250);
		SpawnPositionX = _PlayBoard.getPositionX() - _SpawnCount * 32 / 2 + 11;
		SpawnPositionY = _PlayBoard.getPositionY() + 5 * 32 + 8;
		this.addChild(_PlayBoard);

		_Dais.setPosition(CannonPositionX - 20, CannonPositionY - 60);
		this.addChild(_Dais);

		_Engine2.setPosition(CannonPositionX + 10, CannonPositionY);
		_Engine2.setCenterRotate(40, 40);
		this.addChild(_Engine2);

		_Bub.setPosition(CannonPositionX + 25, CannonPositionY);
		this.addChild(_Bub);

		_Machine.setPosition(CannonPositionX, CannonPositionY);
		this.addChild(_Machine);
		
		_Basket1.setPosition(CannonPositionX + 40, CannonPositionY- 15);
		this.addChild(_Basket1);
		
		_Basket2.setPosition(CannonPositionX + 40, CannonPositionY -15);
		this.addSpecialChild(_Basket2);

		_Engine.setPosition(CannonPositionX - 60, CannonPositionY - 10);
		_Engine.animate(new int[] { 0 }, 100);
		this.addChild(_Engine);

		_Wheel.setPosition(CannonPositionX - 73, CannonPositionY - 10);
		_Wheel.setCenterRotate(24, 24);
		this.addChild(_Wheel);

		_Mog.setPosition(CannonPositionX - 146, CannonPositionY + 7);
		_Mog.animate(new int[] { 0 }, 100);
		this.addChild(_Mog);

		_Barrel.setPosition(CannonPositionX - 17, CannonPositionY + 40);
		_Barrel.setCenterRotate(21, 63);
		this.addChild(_Barrel);

		_Arrows.setPosition(CannonPositionX - 31, CannonPositionY + 25);
		_Arrows.setCenterRotate(13.5f, 74.5f);
		_Arrows.animate(new int[] { 0 }, 100);
		this.addChild(_Arrows);

		_LeftFence.setPosition(CannonPositionX - 185, CannonPositionY + 200);
		_RightFence.setPosition(CannonPositionX + 154, CannonPositionY + 200);
		this.addSpecialChild(_LeftFence);
		this.addSpecialChild(_RightFence);

		_TopFence.setPosition(CannonPositionX - 15, CannonPositionY + 470);
		_BotFence.setPosition(CannonPositionX - 15, CannonPositionY + 13);
		this.addSpecialChild(_TopFence);
		this.addSpecialChild(_BotFence);

		_SpawnTimeCount = 0;
		_BubbleMovingSpeed = ((28000 / 60) / _SpawnTime);

		autoGenerateBuble(_SpawnTime);
		
		//create bubble bullet first
		creatNewBubbleBullet();
		setWaitingBubbleBullet();
	
	}
		
	public void turnLeft() {
		if (_RotateAngle > -80) {
			_RotateAngle -= _RotateSpeed;
			_Barrel.setRotation(_RotateAngle);
			_Arrows.setRotation(_RotateAngle);
			_Engine2.setRotation(-_RotateAngle*4);
			_Wheel.setRotation(-_Mog._Animation.getFrame()*45);
			if (_Status != ActionStatus.TURNLEFT) {
				_Status = ActionStatus.TURNLEFT;
				_Mog.animate(new int[] { 7, 6, 5, 4, 3, 2, 1, 0 }, 40);
				_Engine.animate(new int[] { 3,2,1,0 }, 50);
			}
		}
		else
		{
			stopTurn();
		}
	}

	public void turnRight() {
		if (_RotateAngle < 80) {
			_RotateAngle += _RotateSpeed;
			_Barrel.setRotation(_RotateAngle);
			_Arrows.setRotation(_RotateAngle);
			_Engine2.setRotation(-_RotateAngle*4);
			_Wheel.setRotation(_Mog._Animation.getFrame() * 45);
			if (_Status != ActionStatus.TURNRIGHT) {
				_Status = ActionStatus.TURNRIGHT;
				_Mog.animate(new int[] {  1, 2, 3, 4, 5, 6, 7 }, 40);
				_Engine.animate(new int[] { 0,1,2,3 },  50);
			}
		}
		else
		{
			stopTurn();
		}
	}

	public void stopTurn() {
		if (_Status != ActionStatus.STOPTURN) {
			_Wheel.setRotation(0);
			_Status = ActionStatus.STOPTURN;
			_Mog.animate(new int[] { 0 }, 1000);
			_Engine.animate(new int[] { 0 }, 100);
		}
	}

	public void fire() {
		float bbvx = (float) Math.sin((90 - _RotateAngle) * Math.PI / 180)
				* _BubbleBulletSpeed;
		float bbxy = (float) Math.cos((90 - _RotateAngle) * Math.PI / 180)
				* _BubbleBulletSpeed;
		
		int temp = (int)(bbvx * 1000);
		bbvx = (float)temp / (float)1000;
		temp = (int)(bbxy * 1000);
		bbxy = (float)temp / (float)1000;
		_BBWaitingFire._Box.setVy(bbvx);
		_BBWaitingFire._Box.setVx(bbxy);

		_ListBullet.add(_BBWaitingFire);
		_Arrows.animate(new int[] {1, 2, 0 }, 50, false);
		//this.addChild(_BBWaitingFire);
		setWaitingBubbleBullet();
	}
	
	private void creatNewBubbleBullet()
	{
		_NewBBWaiting = BubbleBullet.create();
		_NewBBWaiting.setPosition(CannonPositionX + 38 , CannonPositionY - 3);
		this.addChild(_NewBBWaiting);
	}
	
	private void setWaitingBubbleBullet()
	{
		_BBWaitingFire = _NewBBWaiting;
		_BBWaitingFire.setPosition(_Arrows.getPositionX() + 11 , _Arrows.getPositionY() - 7);
		creatNewBubbleBullet();
	}

	public void update(float deltatime) {
		super.update(deltatime);

		autoGenerateBuble(deltatime);

		for (int i = 0; i < _ListBullet.size(); i++) {
			float collideresult = Globals.SweptAABBCheck(
					_ListBullet.get(i)._Box, _LeftFence._Box).CollisionTime;

			if (collideresult < 1 ) 
			{
				if(collideresult > 0)
				{
				_ListBullet.get(i).isCollide = true;
				_ListBullet.get(i)._Box.x += _ListBullet.get(i)._Box.vx * collideresult;
				_ListBullet.get(i)._Box.y += _ListBullet.get(i)._Box.vy * collideresult;
				_ListBullet.get(i)._Box.setVx(Math.abs(_ListBullet.get(i)._Box.vx));
				}
				else
				{
					if(_ListBullet.get(i)._Box.vx < 0)
						_ListBullet.get(i)._Box.setVx(Math.abs(_ListBullet.get(i)._Box.vx));
				}
			} else {
				collideresult = Globals.SweptAABBCheck(_ListBullet.get(i)._Box,
						_RightFence._Box).CollisionTime;
				if (collideresult < 1) 
				{
					if(collideresult > 0){
					_ListBullet.get(i).isCollide = true;
					_ListBullet.get(i)._Box.x += _ListBullet.get(i)._Box.vx * collideresult;
					_ListBullet.get(i)._Box.y += _ListBullet.get(i)._Box.vy * collideresult;
					_ListBullet.get(i)._Box.setVx(-Math.abs(_ListBullet.get(i)._Box.vx));
					}
					else
					{
						if(_ListBullet.get(i)._Box.vx > 0)
							_ListBullet.get(i)._Box.setVx(- Math.abs(_ListBullet.get(i)._Box.vx));
					}
				}
				/*else {
					collideresult = Globals.SweptAABBCheck(
							_ListBullet.get(i)._Box, _TopFence._Box).CollisionTime;
					if (collideresult < 1) {
						_ListBullet.get(i)._Box.setVx(0);
						_ListBullet.get(i)._Box.setVy(0);
						_ListBubble.add(_ListBullet.get(i));
						_ListBullet.remove(i);
						break;
					}
				}
				*/
			}
		}

		grouping();

		for (int i = 0; i < _ListBullet.size(); i++) {
			float collideresult = 1;
			int thisnormal = -1;
			Box bb = new Box();
			for (int j = 0; j < _ListBubble.size(); j++) {
				CollisionInfor tempresult = Globals.SweptAABBCheck(
						_ListBullet.get(i)._Box, _ListBubble.get(j)._Box);
				if (tempresult.CollisionTime < collideresult) {
					collideresult = tempresult.CollisionTime;
					thisnormal = tempresult.Direction;
					bb = _ListBubble.get(j)._Box;
				}
			}

			// if there is a collision
			if (collideresult < 1) {
				switch (thisnormal) {

				case 1:
					_ListBullet.get(i)._Box.x = bb.x + 32;
					_ListBullet.get(i)._Box.y = bb.y;
					break;
				case 2:
					_ListBullet.get(i)._Box.x = bb.x - 32;
					_ListBullet.get(i)._Box.y = bb.y;
					break;
				case 3:
					break;
				case 0:
				case 4:
					if (_ListBullet.get(i)._Box.x < bb.x)
						_ListBullet.get(i)._Box.x = bb.x - 16;
					else
						_ListBullet.get(i)._Box.x = bb.x + 16;
					_ListBullet.get(i)._Box.y = bb.y - 28;
					break;
				}

				_ListBullet.get(i)._BigBox.x = _ListBullet.get(i)._Box.x;
				_ListBullet.get(i)._BigBox.y = _ListBullet.get(i)._Box.y;

				_ListBullet.get(i)._Box.setVx(0);
				_ListBullet.get(i)._Box.setVy(0);
				_ListBubble.add(_ListBullet.get(i));

				destroyBubble(_ListBullet.get(i));

				_ListBullet.remove(i);
			}
		}

	}

	private void autoGenerateBuble(float deltatime) {
		for (int i = 0; i < _ListBubble.size(); i++) {
			_ListBubble.get(i).setPositionY(
					_ListBubble.get(i).getPositionY() - _BubbleMovingSpeed);
		}

		_SpawnTimeCount += deltatime;
		if (_SpawnTimeCount >= _SpawnTime) {
			_SpawnTimeCount = 0;
			generateBubble();
		}
	}

	private void generateBubble() {
		if (_DeltaSpawnPosition < 18) {
			_DeltaSpawnPosition = 18;
			_SpawnCount = 7;
		} else {
			_DeltaSpawnPosition = 2;
			_SpawnCount = 8;
		}

		for (int i = 0; i < _SpawnCount; i++) {
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(100);
			if (randomInt < 90) {

				BubbleBullet bubble = BubbleBullet.create();
				bubble.setPosition(SpawnPositionX + i * 32
						+ _DeltaSpawnPosition, SpawnPositionY + 32);
				_ListBubble.add(bubble);
				this.addChild(bubble);
			}
		}

	}

	private boolean destroyBubble(BubbleBullet bb) {
		_ListDestroyableBubble.add(bb);
		_ListBubble.remove(bb);

		for (int i = 0; i < _ListBubble.size(); i++) {
			if (Globals.AABBCheck(bb._BigBox, _ListBubble.get(i)._BigBox)) {
				if (bb._Type == _ListBubble.get(i)._Type) {
					_ListDestroyableBubble.add(_ListBubble.get(i));
					_ListBubble.remove(i);
					findSameNearestDestroyableBubble(_ListDestroyableBubble
							.get(_ListDestroyableBubble.size() - 1));
				} else {
					_ListBubble.get(i)._FallingForce = true;
				}
			}
		}

		if (_ListDestroyableBubble.size() > SameColorDestroyCount) {

			for (int i = 0; i < _ListDestroyableBubble.size(); i++) {

				try {
					AnimateSprite explodesprite = new AnimateSprite(
							"resources//sprite//bubble//" + "e"
									+ _ListDestroyableBubble.get(i)._Type
									+ ".png", 3, 3);
					explodesprite.setPosition(
							_ListDestroyableBubble.get(i)._Box.x - 32,
							_ListDestroyableBubble.get(i)._Box.y);
					explodesprite.animate(new int[] { 0, 1, 2, 3, 4, 5, 6 },
							70, false);
					explodesprite.setAutoRelease();
					this.addChild(explodesprite);
				} catch (SlickException e) {
					e.printStackTrace();
				}

				_ListBubble.remove(_ListDestroyableBubble.get(i));
				this.removeChild(_ListDestroyableBubble.get(i));
			}
			_ListDestroyableBubble.clear();
			return true;
		} else {
			for (int i = 0; i < _ListDestroyableBubble.size(); i++) {
				_ListBubble.add(_ListDestroyableBubble.get(i));
			}

			for (int i = 0; i < _ListBubble.size(); i++) {
				_ListBubble.get(i)._FallingForce = false;
			}

			_ListDestroyableBubble.clear();
		}
		return false;

	}

	private void findSameNearestDestroyableBubble(BubbleBullet bb) {
		for (int i = 0; i < _ListBubble.size(); i++) {
			if (Globals.AABBCheck(bb._BigBox, _ListBubble.get(i)._BigBox)) {
				if (bb._Type == _ListBubble.get(i)._Type) {
					_ListDestroyableBubble.add(_ListBubble.get(i));
					_ListBubble.remove(i);
					findSameNearestDestroyableBubble(_ListDestroyableBubble
							.get(_ListDestroyableBubble.size() - 1));
				} else {
					_ListBubble.get(i)._FallingForce = true;
				}
			}
		}
	}

	private void grouping() {

		List<Integer> _ListBubbleID = new ArrayList<Integer>();
		// get indexs of all bubbles on screen

		if (_ListBubble.size() > 0) {
			for (int i = 0; i < _ListBubble.size(); i++) {
				_ListBubbleID.add(i);
			}

			_ListDifferentBubblesGroups.clear();
			// push a new Group
			_ListDifferentBubblesGroups.add(new ArrayList<Integer>());
			_ListDifferentBubblesGroups.get(0).add(_ListBubbleID.get(0));

			// grouping bubble
			for (int i = 0; i < _ListBubbleID.size(); i++) {
				boolean isOnGroup = false;
				for (int j = 0; j < _ListDifferentBubblesGroups.size(); j++) {
					for (int k = 0; k < _ListDifferentBubblesGroups.get(j)
							.size(); k++) {
						if (_ListBubbleID.get(i) != _ListDifferentBubblesGroups
								.get(j).get(k)
								&& Globals
										.AABBCheck(
												_ListBubble.get(_ListBubbleID
														.get(i))._BigBox,
												_ListBubble
														.get(_ListDifferentBubblesGroups
																.get(j).get(k))._BigBox)) {
							_ListDifferentBubblesGroups.get(j).add(
									_ListBubbleID.get(i));
							isOnGroup = true;
							break;
						} else {
							if (_ListBubbleID.get(i) == _ListDifferentBubblesGroups
									.get(j).get(k)) {
								isOnGroup = true;
							}
						}
					}
				}

				if (!isOnGroup) {
					List<Integer> _newgroup = new ArrayList<Integer>();
					_newgroup.add(_ListBubbleID.get(i));
					_ListDifferentBubblesGroups.add(_newgroup);

				}
			}
			// grouping group
			for (int i = 0; i < _ListDifferentBubblesGroups.size(); i++) {
				for (int j = 0; j < _ListDifferentBubblesGroups.get(i).size(); j++) {
					for (int m = i + 1; m < _ListDifferentBubblesGroups.size(); m++) {
						for (int n = 0; n < _ListDifferentBubblesGroups.get(m)
								.size(); n++) {
							if (_ListDifferentBubblesGroups.get(i).get(j) == _ListDifferentBubblesGroups
									.get(m).get(n)) {
								Set<Integer> newset = new HashSet<Integer>(
										_ListDifferentBubblesGroups.get(i));
								newset.addAll(_ListDifferentBubblesGroups
										.get(m));
								_ListDifferentBubblesGroups.get(i).clear();
								_ListDifferentBubblesGroups.get(i).addAll(
										newset);
								_ListDifferentBubblesGroups.remove(m);
								m--;
								break;
							}
						}
					}
				}
			}
			Set<Integer> _SetHardGroup = new HashSet<Integer>();

			for (int i = 0; i < _ListDifferentBubblesGroups.size(); i++) {
				for (int j = 0; j < _ListDifferentBubblesGroups.get(i).size(); j++) {
					if (_ListBubble.get((int) _ListDifferentBubblesGroups
							.get(i).get(j))._BigBox.y >= SpawnPositionY) {
						_SetHardGroup.add(i);
						break;
					}
				}
			}

			List<Integer> fallinglist = new ArrayList<Integer>();
			for (int i = 0; i < _ListDifferentBubblesGroups.size(); i++) {
				if (!_SetHardGroup.contains(i)) {
					fallinglist.addAll(_ListDifferentBubblesGroups.get(i));
				}
			}

			// sort list
			Collections.sort(fallinglist);

			for (int i = fallinglist.size() - 1; i >= 0; i--) {
				int id = fallinglist.get(i);
				if(id < _ListBubble.size())
				{
				_ListBubble.get(id)._Box.setVy(-3);
				_ListBubble.get(id)._Box.setAy(-0.7f);
				_ListBubble.remove(id);
				}
			}

			for (int i = 0; i < _ListBubble.size(); i++) {
				_ListBubble.get(i)._FallingForce = false;
			}
		}
	}

	public void render() {
		super.render();
	}

	public enum ActionStatus {
		TURNLEFT, TURNRIGHT, STOPTURN
	}

}
