package animation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import animation.AnimatedModel;
import animation.Animation;
import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

// ADDED extends AnimatedModel
public class AnimatedPlayer extends AnimatedModel {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	private static final float TERRAIN_HEIGHT = 0;
	
	// ADDED
	Animation currentAnimation = null;
	
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;
	// ADDED
	private boolean isMoving = false;
	
	private Animation runAnimation;
	
//	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
//		super(model, position, rotX, rotY, rotZ, scale);
//	}
	
	// Constructor for Animated Entity
	public AnimatedPlayer(AnimatedModel model, Animation run) {
		super(model.getRawModel(), model.getModelTexture(), model.getRootJoint(), model.getJointCount(), model.getPosition(), model.getRotX(), model.getRotY(), model.getRotX(), model.getScale());
		this.runAnimation = run;
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		if(super.getPosition().y<terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if(!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	// ADDED
	private void setAnimation(Animation animation) {
		if(currentAnimation != animation) {
			currentAnimation = animation;
			doAnimation(animation);
		}
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
			// ADDED
			setAnimation(runAnimation);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		}else {
			this.currentSpeed = 0;
			// ADDED
			setAnimation(null);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		}else {
			this.currentTurnSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.currentSpeed = RUN_SPEED * 10;
		}
	}
	
	// ADDED
	public void doAnimation(Animation animation) {
		super.doAnimation(animation);
	}
	
	// ADDED
	public void update() {
		super.update();
	}

}

