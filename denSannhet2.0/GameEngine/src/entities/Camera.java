package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import animation.AnimatedPlayer;
import terrains.Terrain;

public class Camera {
	
	 private float distanceFromPlayer = 50;
	 private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;
	
	private Player player;
	private AnimatedPlayer animatedPlayer;
	
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public Camera(AnimatedPlayer animatedPlayer) {
		this.animatedPlayer = animatedPlayer;
	}
	
	
	public void Move(/*Terrain terrain*/) {
		calculateZoom();
		calculatePitch(/*terrain*/);
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
//		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		this.yaw = 180 - (animatedPlayer.getRotY() + angleAroundPlayer);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
/*	
//	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
//		float theta = player.getRotY() + angleAroundPlayer;
//		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
//		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
//		position.x = player.getPosition().x - offsetX;
//		position.z = player.getPosition().z - offsetZ;
//		position.y = player.getPosition().y + verticalDistance;
//	}
*/	
	
	/**
	 * After finding the horizontalDistance we can easily find the offset of both x and z.
	 * By geometry we know that the opposite angle of rotY, meaning behind the player will be equal to rotY.
	 * But since the angle can have been adjusted by the mouse input, we have to add both values together to
	 * determine the angle towards the camera. We save this angel to theta.
	 * then we find the offsets, then we subtract both of them from players current x and z positions and we 
	 * get the cameras position in x and z. Cameras y position is found with similar method, only we use the pitch
	 * aka the angle camera is looking down at the player. 
	 * @param horizontalDistance
	 * @param verticalDistance
	 */
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = animatedPlayer.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = animatedPlayer.getPosition().x - offsetX;
		position.z = animatedPlayer.getPosition().z - offsetZ;
		position.y = animatedPlayer.getPosition().y + verticalDistance + 6; // 6 is added, as the camera was pointing towards the legs, and we wanted it insted to point at the chest of the model
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	/**
	 *  Calculate the distanceFromPlayer based on the input from Mouse.getWheel. Multiply this number with 0.1f since
	 *  its so large by default. Make it more smoother.
	 */
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		// Just make sure you are not allowed to "zoom through the player and the ground, because that make no sense.
		if(distanceFromPlayer < -1) {
			distanceFromPlayer = -1 ;
		}
	}
	
	/**
	 * Calculate the pitch if the right button is clicked on the mouse. same as earlier. multiply with 0.1f to adjust the numbers
	 * and increase smoothness.
	 */
	private void calculatePitch(/*Terrain terrain*/) {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch  -= pitchChange;
		}
	}
	// Tried to stop the camera to go through the terrain, but if I prevent it in that way, it also will freeze the moment its outside the terrain
	// because it has no terrain to ask if it's y position is high enough.
/*
//		if(isCameraWithinTerrain(terrain, this)){
//			if( pitch < (terrain.getHeightOfTerrain(terrain.getX(), terrain.getZ()) + 5.0f) ) {
//				pitch = terrain.getHeightOfTerrain(terrain.getX(), terrain.getZ()) + 5.0f ;
//			}
//		}
//	}
//	
//	private boolean isCameraWithinTerrain(Terrain terrain, Camera camera) {
//		int px = (int) camera.getPosition().x;
//		int pz = (int) camera.getPosition().z;
//		
//		if( px <= 800 && px >= 0 && pz >= -800 && pz <= 0) {       // x: 0 to 800 and z: 0 to -800
//			return true;
//		}else if ( px >= -800 && px <= 0 && pz >= -800 && pz <= 0 ) {	// x: 0 to -800 and z: 0 to -800
//			return true;
//		}else if ( px >= -800 && px <= 0 && pz <= 800 && pz >= 0 ) {	// x: 0 to -800 and z: 0 to 800
//			return true;
//		}else if ( px <= 800 && px >= 0 && pz <= 800 && pz >= 0 ) {	// x: 0 to 800 and z: 0 to 800
//			return true;
//		}
//		return false;
//	}
*/	
	/**
	 * Calculate the yaw angle, when the player press the left mouse button
	 */
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange; 
		}
	}
	
}

/************************************************************* CODE GRAVEYARD *********************************************************************


		float arg_yaw = Mouse.getDX();
		float arg_roll = Mouse.getDY();
		
		yaw += arg_yaw / 10;
		pitch += - ( arg_roll / 10);
		
		Mouse.setGrabbed(true);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			 float toZ = ((float)Math.sin( Math.toRadians(yaw + 90)));
             float toX = ((float)Math.cos( Math.toRadians(yaw + 90)));
             position.x -= toX;
             position.z -= toZ;
             
			//position.z-=0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			float toZ = ((float)Math.sin( Math.toRadians(yaw)));
            float toX = ((float)Math.cos( Math.toRadians(yaw)));
            position.x += toX;
            position.z += toZ;
            
			//position.x+=0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			float toZ = ((float)Math.sin( Math.toRadians(yaw)));
            float toX = ((float)Math.cos( Math.toRadians(yaw)));
            position.x -= toX;
            position.z -= toZ;
			
			//position.x-=0.5f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			float toZ = ((float)Math.sin( Math.toRadians(yaw + 90)));
            float toX = ((float)Math.cos( Math.toRadians(yaw + 90)));
            position.x += toX;
            position.z += toZ;
			
			//position.z+=0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            position.y += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y -= 0.5f;
        }
        // hyper speed!
        if(Keyboard.isKeyDown(Keyboard.KEY_J)) {
			float toZ = ((float)Math.sin( Math.toRadians(yaw + 90)));
            float toX = ((float)Math.cos( Math.toRadians(yaw + 90)));
            position.x -= toX * 10;
            position.z -= toZ * 10;
		} 

************************************************************** CODE GRAVEYARD **********************************************************************/