package com.worldofworkout.playertest;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends GameObject {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int DEFAULT_STAT = 2;
	//private static final long[] ANIMATE_DURATION = new long[]{200, 200, 200};
	
	// ===========================================================
	// Fields
	// ===========================================================
	private int HP;
	private int stamina;
	private int strength;
	private int speed;
	private int fatigue;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public Player(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		HP = DEFAULT_STAT;
		strength = DEFAULT_STAT;
		speed = DEFAULT_STAT;
		stamina = DEFAULT_STAT;
		fatigue = DEFAULT_STAT;
	}
	
	public Player(final int[] stats, final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		HP = stats[0];
		strength = stats[1];
		speed = stats[2];
		stamina = stats[3];
		fatigue = stats[4];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
		
	public int getHP() {
		return HP;
	}
	
	public void changeHP(int amount) {
		HP += amount;
	}
	
	public int getStren() {
		return strength;
	}
	
	public void changeStren(int amount) {
		strength += amount;
	}
	
	public int getSpd() {
		return speed;
	}
	
	public void changeSpd(int amount) {
		speed += amount;
	}
	
	public int getStam() {
		return stamina;
	}
	
	public void changeStam(int amount) {
		stamina += amount;
	}
	
	public int getFatg() {
		return fatigue;
	}
	
	public void changeFatg(int amount) {
		fatigue += amount;
	}
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	/*public void move(int direction) {
		if(direction == 1) {
			// Up
			if (getObjDirection() != ObjDirection.UP){
				animate(ANIMATE_DURATION, 0, 2, true);
				setObjDirection(ObjDirection.UP);
			}
		} else if(direction == 2) {	
			// Down
			if (getObjDirection() != ObjDirection.DOWN){
				animate(ANIMATE_DURATION, 9, 11, true);
				setObjDirection(ObjDirection.DOWN);
			}
		} else if(direction == 3) {	
			// Left
			if (getObjDirection() != ObjDirection.LEFT){
				animate(ANIMATE_DURATION, 3, 5, true);
				setObjDirection(ObjDirection.LEFT);
			}
		} else if(direction == 4) {	
			// Right
			if (getObjDirection() != ObjDirection.RIGHT){
				animate(ANIMATE_DURATION, 6, 8, true);
				setObjDirection(ObjDirection.RIGHT);
			}
		} else {
			// Stop
			if (isAnimationRunning()){
				stopAnimation();
				setObjDirection(ObjDirection.NONE);
			}
		}
	}*/
	
	@Override
	public void attack() {
		
	}
	
	@Override
	public void interact() {
		
	}
	
	public void defend() {
		
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isDead() {
		if(this.HP<=0) return true;
		else return false;
		
	}
	
}
