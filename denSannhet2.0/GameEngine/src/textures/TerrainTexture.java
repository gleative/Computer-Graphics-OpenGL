package textures;

/**
 *  Simple class to hold onto the textureID of the Terrain texture
 *  
 * @author Håkon S. Bøckman
 *
 */
public class TerrainTexture {
	
	private int textureID;

	public TerrainTexture(int textureID) {
		this.textureID = textureID;
	}

	public int getTextureID() {
		return textureID;
	}

}
