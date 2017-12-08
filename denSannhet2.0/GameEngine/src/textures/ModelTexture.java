package textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	/**
	 *  Have to tell if the model have transparency, if so its important that we don't 
	 *  do back face culling on the following model.
	 */
	private boolean hasTransparency = false;
	
	/**
	 * Since the grass models, or especially them. have normals that point really in very different ways
	 * we want to give all the normals of that model a same value, so the lightning of the model will
	 * be even if we choose so. Therefore we have a boolean to say so. so fakeLighting = set all the normals
	 * to point upwards, relating to the model getting even light all over. depending on the angle of the main light
	 * source, but if the main light source is from the side, it will still define a shadow on the models since the 
	 * model will only take inn the amount of light received from straight up, and not directly towards the source. So
	 * the source will shine brighter directly onto the model then the fakeLight will.
	 */
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	public int getID() {
		return this.textureID;
	}

	public int getTextureID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	

}
