package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import animation.AnimatedPlayer;

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
	
	
	public void Move() {
		calculateZoom();
		calculatePitch();
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
	
//	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
//		float theta = player.getRotY() + angleAroundPlayer;
//		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
//		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
//		position.x = player.getPosition().x - offsetX;
//		position.z = player.getPosition().z - offsetZ;
//		position.y = player.getPosition().y + verticalDistance;
//	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = animatedPlayer.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = animatedPlayer.getPosition().x - offsetX;
		position.z = animatedPlayer.getPosition().z - offsetZ;
		position.y = animatedPlayer.getPosition().y + verticalDistance;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch  -= pitchChange;
		}
	}
	
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