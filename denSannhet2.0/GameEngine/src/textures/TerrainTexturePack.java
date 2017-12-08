package textures;

/**
 * This class contain the four textures that will be representing the texture off the terrain
 * Meaning the class only holds onto 4 initialized TerrainTextures, and TerrainTextures class only holds onto
 * the textures ID. So this class is as the name would imply, a pack containing textures.
 * 
 * @author Håkon S. Bøckman
 *
 */
public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.bTexture = gTexture;
		this.gTexture = bTexture;
	}
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}
	public TerrainTexture getrTexture() {
		return rTexture;
	}
	public TerrainTexture getgTexture() {
		return gTexture;
	}
	public TerrainTexture getbTexture() {
		return bTexture;
	}
	
}
