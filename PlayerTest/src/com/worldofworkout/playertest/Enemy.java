package com.worldofworkout.playertest;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.worldofworkout.playertest.GameObject;

public class Enemy extends GameObject {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int DEFAULT_STAT = 1;
	private static final int[] DEFAULT_PATH = new int[]{0};
	private static final long[] ANIMATE_DURATION = new long[]{200, 200, 200};
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private int HP;
	private int strength;
	private int speed;
	private int stamina;
	private int[] path;
	private int pathIndex = 0;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public Enemy(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		HP = DEFAULT_STAT;
		strength = DEFAULT_STAT;
		speed = DEFAULT_STAT;
		stamina = DEFAULT_STAT;
		path = DEFAULT_PATH;
	}
	
	public Enemy(final int[] pStats, final float pX, final float pY, final int[] pPath, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		HP = pStats[0];
		strength = pStats[1];
		speed = pStats[2];
		stamina = pStats[3];
		path = pPath;
		
	}
	
	public Enemy(final int[] pStats, final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		HP = pStats[0];
		strength = pStats[1];
		speed = pStats[2];
		stamina = pStats[3];
		path = DEFAULT_PATH;
		
	}
	
	public Enemy(final float pX, final float pY, final int[] pPath, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		HP = DEFAULT_STAT;
		strength = DEFAULT_STAT;
		speed = DEFAULT_STAT;
		stamina = DEFAULT_STAT;
		path = pPath;
	}

	// ===========================================================
	// Getter and Setter
	// ===========================================================
	
	public int getHP() {
		return HP;
	}

	public void changeHP(int amount) {
		HP += amount;
	}

	public int getStamina() {
		return stamina;
	}

	public void changeStamina(int amount) {
		stamina += amount;
	}

	public int getStrength() {
		return strength;
	}

	public void changeStrength(int amount) {
		strength += amount;
	}

	public int getSpeed() {
		return speed;
	}

	public void changeSpeed(int amount) {
		speed += amount;
	}

	public int getPath(int index) {
		return path[index];
	}

	public void setPath(int index, int dir) {
		path[index] = dir;
	}
	
	public int getPathIndex() {
		return pathIndex;
	}

	public void setPathIndex(int newIndex) {
		pathIndex = newIndex;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
		
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isDead() {
		if(this.HP<=0) return true;
		else return false;
		
	}
	
	public void followPath() { // Increments to next direction in the path or loops back to beginning 
		if(pathIndex < path.length-1) pathIndex++;
		else if(pathIndex == path.length-1) pathIndex = 0;
		else {
			pathIndex = 0;
		}
		System.out.println(getX() + ", " + getY() + "\n");
	}
	
	public void move2(int direction) {
		if(direction == 1) {
			// Up
			if (getObjDirection() != ObjDirection.UP){
				animate(ANIMATE_DURATION, 6, 8, true);
				setObjDirection(ObjDirection.UP);
			}
		} else if(direction == 2) {	
			// Down
			if (getObjDirection() != ObjDirection.DOWN){
				animate(ANIMATE_DURATION, 0, 2, true);
				setObjDirection(ObjDirection.DOWN);
			}
		} else if(direction == 3) {	
			// Left
			if (getObjDirection() != ObjDirection.LEFT){
				animate(ANIMATE_DURATION, 9, 11, true);
				setObjDirection(ObjDirection.LEFT);
			}
		} else if(direction == 4) {	
			// Right
			if (getObjDirection() != ObjDirection.RIGHT){
				animate(ANIMATE_DURATION, 3, 5, true);
				setObjDirection(ObjDirection.RIGHT);
			}
		} else {
			// Stop
			if (isAnimationRunning()){
				stopAnimation();
				setObjDirection(ObjDirection.NONE);
			}
		}
	}
	
}
