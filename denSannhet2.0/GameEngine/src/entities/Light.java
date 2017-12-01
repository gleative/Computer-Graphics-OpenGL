package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f colour;
	private Vector3f atttenuation = new Vector3f(1, 0, 0);
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attentuation) {
		this.position = position;
		this.colour = colour;
		this.atttenuation = attentuation;
	}
	
	public Vector3f getAttentuation() {
		return atttenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	
	
}
