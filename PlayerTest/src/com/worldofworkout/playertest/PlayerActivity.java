package com.worldofworkout.playertest;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Manifold;

public class PlayerActivity extends SimpleBaseGameActivity{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 480;
    private static final int CAMERA_HEIGHT = 320;
	private static final float TILE_DIM = 32;
    //private static final long[] ANIMATE_DURATION = new long[]{200, 200, 200};
	
	// ===========================================================
	// Fields
	// ===========================================================

	private TMXTiledMap mTMXTiledMap;
	private BoundCamera mBoundChaseCamera;
    private Scene mScene;
	private BitmapTextureAtlas mTexturePlayer, mTextureEnemy, mTextureFace;
	private Body mPlayerBody, mEnemyBody, mFaceBody;
	private TiledTextureRegion mPlayerTextureRegion, mEnemyTextureRegion, mFaceTextureRegion;
	private Enemy enemy, face;
	private Player player;
	private BitmapTextureAtlas mOnScreenControlTexture, mOnScreenRunTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion, mOnScreenControlKnobTextureRegion, mOnScreenRunTextureRegion;
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private ButtonSprite mRunButton;
	private PhysicsWorld mPhysicsWorld;
	private Timer mEnemyTimer, mEnemyTimer2;
	private boolean spdIncreasing = true;

	//private SurfaceScrollDetector mScrollDetector;
	//private PinchZoomDetector mPinchZoomDetector;
	//private float mPinchZoomStartedCameraZoomFactor;
	
	// ===========================================================
	// Constructors
	// ===========================================================


	private ContactListener createContactListener()
	{
	    ContactListener contactListener = new ContactListener()
	    {
	        @Override
	        public void beginContact(Contact contact)
	        {
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	            if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("monster")) {
	                Log.i("CONTACT", "BETWEEN PLAYER AND MONSTER!");
	                if(enemyFacingPlayer(enemy, player)){
	                	player.changeHP(-enemy.getStrength());
	                } else enemy.changeHP(-player.getStren());
	                Log.i("MONSTER HP", Integer.toString(enemy.getHP()));
	                Log.i("PLAYER HP", Integer.toString(player.getHP()));
	                mEnemyBody.setLinearVelocity(0,0);
	            } 
	            if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("monster2")) {
	                Log.i("CONTACT", "BETWEEN PLAYER AND MONSTER!");
	                if(enemyFacingPlayer(face, player)){
	                	player.changeHP(-face.getStrength());
	                } else face.changeHP(-player.getStren());
	                Log.i("MONSTER HP", Integer.toString(face.getHP()));
	                Log.i("PLAYER HP", Integer.toString(player.getHP()));
	                mEnemyBody.setLinearVelocity(0,0);
	            } 
	            else if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("shape")) {
	                Log.i("CONTACT", "BETWEEN PLAYER AND SHAPE!");
	            } 
	            else if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("bounds")) {
	                Log.i("CONTACT", "BETWEEN PLAYER AND BOUNDS!");
	            } 
	            else if (x1.getBody().getUserData().equals("monster")&&x2.getBody().getUserData().equals("player")) {
	                Log.i("CONTACT", "BETWEEN MONSTER AND PLAYER!");
	                enemy.changeHP(-player.getStren());
	                Log.i("ENEMY HP", Integer.toString(enemy.getHP()));
	                mEnemyBody.setLinearVelocity(0,0);
	            } 
	            else if (x1.getBody().getUserData().equals("shape")&&x2.getBody().getUserData().equals("player")) {
	                Log.i("CONTACT", "BETWEEN SHAPE AND PLAYER!");
	            } 
	            else if (x1.getBody().getUserData().equals("bounds")&&x2.getBody().getUserData().equals("player")) {
	                Log.i("CONTACT", "BETWEEN BOUNDS AND PLAYER!");
	            } 
	            else if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
	            	Log.i("CONTACT", "BETWEEN UNKNOWN OBJECTS!");
	            	Log.i((String)x1.getBody().getUserData(), (String)x2.getBody().getUserData());
	            }
	        }

	        @Override
	        public void endContact(Contact contact)
	        {
	        	final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	            if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("monster"))
	            {
	                Log.i("END_CONTACT", "BETWEEN PLAYER AND MONSTER!");
	                mPlayerBody.setLinearVelocity(0,0);
	                if(enemy.isDead()) {
	                	destroyEnemy(enemy, mEnemyBody);
	                }
	                if(player.isDead()) {
	                	destroyPlayer(player);
	                }
	            }if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("monster2"))
	            {
	                Log.i("END_CONTACT", "BETWEEN PLAYER AND MONSTER!");
	                mPlayerBody.setLinearVelocity(0,0);
	                if(face.isDead()) {
	                	destroyEnemy(face, mFaceBody);
	                }
	                if(player.isDead()) {
	                	destroyPlayer(player);
	                }
	            } else if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("shape"))
	            {
	                Log.i("END_CONTACT", "BETWEEN PLAYER AND SHAPE!");
	            } else if (x1.getBody().getUserData().equals("player")&&x2.getBody().getUserData().equals("bounds"))
	            {
	                Log.i("END_CONTACT", "BETWEEN PLAYER AND BOUNDS!");
	            } else if (x1.getBody().getUserData().equals("monster")&&x2.getBody().getUserData().equals("player"))
	            {
	                Log.i("END_CONTACT", "BETWEEN MONSTER AND PLAYER!");
	                mPlayerBody.setLinearVelocity(0,0);
	                if(enemy.isDead()) {
	                	mScene.detachChild(enemy);
	                	destroyEnemy(enemy, mEnemyBody);
	                }
	            } else if (x1.getBody().getUserData().equals("shape")&&x2.getBody().getUserData().equals("player"))
	            {
	                Log.i("END_CONTACT", "BETWEEN SHAPE AND PLAYER!");
	            } else if (x1.getBody().getUserData().equals("bounds")&&x2.getBody().getUserData().equals("player"))
	            {
	                Log.i("END_CONTACT", "BETWEEN BOUNDS AND PLAYER!");
	            } else if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	            	Log.i("END_CONTACT", "BETWEEN UNKNOWN OBJECTS!");
	            }
	        }

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
				
			}
	    };
	    return contactListener;
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);
	}
	
	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// Control texture
		mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

		// Run Button texture
		mOnScreenRunTexture = new BitmapTextureAtlas(this.getTextureManager(), 96, 96, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mOnScreenRunTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenRunTexture, this, "next.png", 0, 0);
		
		// Player sprite texture
		mTexturePlayer = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.DEFAULT);
		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTexturePlayer, this, "hero.png", 0, 0, 3, 4);
		
		// Enemy sprite texture
		mTextureEnemy = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.DEFAULT);
		mEnemyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTextureEnemy, this, "enemy.png", 0, 0, 3, 4);
		
		mTextureFace = new BitmapTextureAtlas(this.getTextureManager(), 128, 128, TextureOptions.DEFAULT);
		mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTextureFace, this, "enemy.png", 0, 0, 3, 4);
		
		// Load the textures
		//this.mEngine.getTextureManager().loadTexture(this.mTexturePlayer);
		//this.mEngine.getTextureManager().loadTexture(this.mOnScreenControlTexture);
		mOnScreenControlTexture.load();
		mOnScreenRunTexture.load();
		mTexturePlayer.load();
		mTextureEnemy.load();
		mTextureFace.load();
	}
	
	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		// Create physics world
		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		this.mPhysicsWorld.setContactListener(createContactListener());
		
		// Create the scene and register the physics world
		mScene = new Scene();
		mScene.registerUpdateHandler(this.mPhysicsWorld);
		mScene.setBackground(new Background(1, 1, 1));
		
		
		// Load the TMX map
		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager());
			this.mTMXTiledMap = tmxLoader.loadFromAsset("strength2.tmx");
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}

		// Add the non-object layers to the scene
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++){
			TMXLayer layer = this.mTMXTiledMap.getTMXLayers().get(i);
			if (!layer.getTMXLayerProperties().containsTMXProperty("wall", "true"))
			mScene.attachChild(layer);
		}

		// Read in the unwalkable blocks from the object layer and create boxes for each
		this.createUnwalkableObjects(mTMXTiledMap);
		
		// Make the camera not exceed the bounds of the TMXEntity.
		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		this.mBoundChaseCamera.setBounds(0, 0, tmxLayer.getWidth(), tmxLayer.getHeight());
		this.mBoundChaseCamera.setBoundsEnabled(true);
		// Add outer walls
		this.addBounds(tmxLayer.getWidth(), tmxLayer.getHeight());
		

		// Calculate the coordinates for the player, so it's centred on the camera.
		//final float centerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getWidth()) / 2;
		//final float centerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion.getHeight()) / 2;
		
		
		
		// Create the player sprite and add it to the scene.
		player = new Player(new int[]{10,2,3,3,2}, 23*TILE_DIM, 31*TILE_DIM, this.mPlayerTextureRegion, this.getVertexBufferObjectManager());
		this.mBoundChaseCamera.setChaseEntity(player);
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
		mPlayerBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, player, BodyType.DynamicBody, playerFixtureDef);
		mPlayerBody.setUserData("player");
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(player, mPlayerBody, true, false){
			@Override
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
				mBoundChaseCamera.updateChaseEntity();
			}
		});
		mScene.attachChild(player);
		
		// Create the enemy sprite and add it to the scene
		enemy = new Enemy(new int[]{2,1,1,2}, 26*TILE_DIM, 25*TILE_DIM, this.mEnemyTextureRegion, this.getVertexBufferObjectManager());
		final FixtureDef enemyFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
		mEnemyBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, enemy, BodyType.KinematicBody, enemyFixtureDef);
		mEnemyBody.setLinearVelocity(0, 0);
		mEnemyBody.setUserData("monster");
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy, mEnemyBody, true, false){
			@Override
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
			}
		});
		
		mEnemyTimer = new Timer(2, new Timer.ITimerCallback() {
		    @Override
			public void onTick() {
		        // move enemy towards player
		    	double dx = player.getX() - enemy.getX();
		    	double dy = player.getY() - enemy.getY();
		    	if(Math.abs(dx)>Math.abs(dy)) {
		    		if(dx>0) enemy.move2(4);
		    		else enemy.move2(3);
		    	}
		    	else {
		    		if(dy>0) enemy.move2(1);
		    		else enemy.move2(2);
		    	}
		    	setBodyVelocity(enemy.getObjDirectionInt(), mEnemyBody, enemy.getSpeed());
		    }
		});		
		enemy.registerUpdateHandler(mEnemyTimer);
		mScene.attachChild(enemy);
		
		// Create the second enemy sprite and add it to the scene
		face = new Enemy(new int[]{4,1,1,4}, 29*TILE_DIM, 28*TILE_DIM, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		final FixtureDef faceFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
		mFaceBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.KinematicBody, faceFixtureDef);
		mFaceBody.setLinearVelocity(0, 0);
		mFaceBody.setUserData("monster2");
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, mFaceBody, true, false){
			@Override
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
			}
		});
		
		mEnemyTimer2 = new Timer(3, new Timer.ITimerCallback() {
		    @Override
			public void onTick() {
		        // move enemy towards player
		    	double dx = player.getX() - face.getX();
		    	double dy = player.getY() - face.getY();
		    	if(Math.abs(dx)>Math.abs(dy)) {
		    		if(dx>0) face.move2(4);
		    		else face.move2(3);
		    	}
		    	else {
		    		if(dy>0) face.move2(1);
		    		else face.move2(2);
		    	}
		    	setBodyVelocity(face.getObjDirectionInt(), mFaceBody, face.getSpeed());
		    }
		});		
		face.registerUpdateHandler(mEnemyTimer2);
		mScene.attachChild(face);
		
		// Add the control
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				// Set the correct walking animation				
				if (pValueY == 1){
					// Up
					player.move(1);
				}else if (pValueY == -1){
					// Down
					player.move(2);
				}else if (pValueX == -1){
					// Left
					player.move(3);
				}else if (pValueX == 1){
					// Right
					player.move(4);
				}else{
					// Stop
					player.move(0);
				}
				
				// Set the player's velocity
				mPlayerBody.setLinearVelocity(pValueX * player.getSpd(), pValueY * player.getSpd());
			}
		});
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(1.25f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(1.25f);
		this.mDigitalOnScreenControl.getControlKnob().setAlpha(0.5f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();
		mScene.setChildScene(this.mDigitalOnScreenControl);
		
		this.mRunButton = new ButtonSprite(CAMERA_WIDTH - this.mOnScreenRunTextureRegion.getWidth(), CAMERA_HEIGHT - this.mOnScreenRunTextureRegion.getHeight(), this.mOnScreenRunTextureRegion, this.getVertexBufferObjectManager(), new OnClickListener() {

			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(spdIncreasing) player.changeSpd(1);
				else player.changeSpd(-1);
				
				if(player.getSpd()==6) spdIncreasing = false;
				else if(player.getSpd()==2) spdIncreasing = true;
			}
			
		});
		mDigitalOnScreenControl.attachChild(this.mRunButton);
		mDigitalOnScreenControl.registerTouchArea(this.mRunButton);
		
		return mScene;
	}
	
	private void createUnwalkableObjects(TMXTiledMap map){
		// Loop through the object groups
		 for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) {
			 if(group.getTMXObjectGroupProperties().containsTMXProperty("wall", "true")){
				 // This is our "wall" layer. Create the boxes from it
				 for(final TMXObject object : group.getTMXObjects()) {
					final Rectangle rect = new Rectangle(object.getX(), object.getY(),object.getWidth(), object.getHeight(), this.getVertexBufferObjectManager());
					final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f);
					PhysicsFactory.createBoxBody(this.mPhysicsWorld, rect, BodyType.StaticBody, boxFixtureDef).setUserData("shape");;
					rect.setVisible(false);
					mScene.attachChild(rect);
				 }
			 }
		 }
	}
	
	private void addBounds(float width, float height){
		final Shape bottom = new Rectangle(0, height - 2, width, 2, this.getVertexBufferObjectManager());
		bottom.setVisible(false);
		final Shape top = new Rectangle(0, 0, width, 2, this.getVertexBufferObjectManager());
		top.setVisible(false);
		final Shape left = new Rectangle(0, 0, 2, height, this.getVertexBufferObjectManager());
		left.setVisible(false);
		final Shape right = new Rectangle(width - 2, 0, 2, height, this.getVertexBufferObjectManager());
		right.setVisible(false);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, (IAreaShape) bottom, BodyType.StaticBody, wallFixtureDef).setUserData("bounds");
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, (IAreaShape) top, BodyType.StaticBody, wallFixtureDef).setUserData("bounds");;
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, (IAreaShape) left, BodyType.StaticBody, wallFixtureDef).setUserData("bounds");;
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, (IAreaShape) right, BodyType.StaticBody, wallFixtureDef).setUserData("bounds");;

		this.mScene.attachChild(bottom);
		this.mScene.attachChild(top);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);
	}
	
	private void setBodyVelocity(int direction, Body body, float velocity) {
		switch(direction){
			case 0:
				// NONE
				body.setLinearVelocity(0, 0);
				break;
			case 1:
				// UP
				body.setLinearVelocity(0, 1*velocity);
				break;
			case 2:
				// DOWN
				body.setLinearVelocity(0, -1*velocity);
				break;
			case 3:
				// LEFT
				body.setLinearVelocity(-1*velocity, 0);
				break;
			case 4:
				// RIGHT
				body.setLinearVelocity(1*velocity, 0);
				break;
			default:
				// NOT A DIRECTION
				break;
		}
	}
	
	private boolean enemyFacingPlayer(Enemy enemy, Player player) {
		int playerDir;
		double dx = player.getX() - enemy.getX();
    	double dy = player.getY() - enemy.getY();
    	if(Math.abs(dx)>Math.abs(dy)) {
    		if(dx>0) playerDir = 4;
    		else playerDir = 3;
    	}
    	else {
    		if(dy>0) playerDir = 1;
    		else playerDir = 2;
    	}
    	if(enemy.getObjDirectionInt() == playerDir) return true;
    	else return false;
	}
	
	private void destroyEnemy(final Enemy enemy, final Body body)
    {
        this.runOnUpdateThread(new Runnable(){

            @Override
            public void run() {
            	
                mPhysicsWorld.unregisterPhysicsConnector(mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(enemy));
                mPhysicsWorld.destroyBody(body);
                mScene.detachChild(enemy);
            }});

    }
	
	private void destroyPlayer(final Player player)
    {
        this.runOnUpdateThread(new Runnable(){

            @Override
            public void run() {

                final Body body = mPlayerBody;
                mPhysicsWorld.unregisterPhysicsConnector(mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(player));
                mPhysicsWorld.destroyBody(body);
                mScene.detachChild(player);
            }});

    }
	
}
