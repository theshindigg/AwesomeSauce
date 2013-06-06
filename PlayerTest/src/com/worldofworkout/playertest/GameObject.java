package com.worldofworkout.playertest;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class GameObject extends AnimatedSprite {
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	public enum ObjDirection{
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	private static final long[] ANIMATE_DURATION = new long[]{200, 200, 200};
	private static ObjDirection[] directions = ObjDirection.values();
	
	// ===========================================================
	// Fields
	// ===========================================================

	public PhysicsHandler mPhysicsHandler;
	private ObjDirection objDirection = ObjDirection.NONE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GameObject(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ObjDirection getObjDirection() {
		return objDirection;
	}
	
	public String getObjDirectionString() {
		int i=0;
		for(int j=0; j<directions.length; j++){
			if(directions[j]==objDirection) {
				i=j;
				break;
			}
		}
		switch(i){
		case 0:
			return "NONE";
		case 1:
			return "UP";
		case 2:
			return "DOWN";
		case 3:
			return "LEFT";
		case 4:
			return "RIGHT";
		default:
			return "UNKNOWN!";
		}
	}

	public int getObjDirectionInt() {
		for(int j=0; j<directions.length; j++){
			if(directions[j]==objDirection) {
				return j;
			}
		}
		return 0;
	}

	public void setObjDirection(ObjDirection objDirection) {
		this.objDirection = objDirection;
	}
	
	public void setObjDirection(int objDirection) {
		this.objDirection = directions[objDirection];
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void move(int direction) {
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
	}
	
	public abstract void attack();
	
	public abstract void interact();
}
