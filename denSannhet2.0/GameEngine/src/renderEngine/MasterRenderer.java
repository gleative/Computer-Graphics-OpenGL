package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import animation.AnimatedModel;
import animation.AnimatedModelRenderer;
import animation.AnimatedModelShader;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRenderer {
	
	/**
	 * FOV = Field of View
	 */
	private static final float FOV = 120;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 10000;
	
	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;
	
	
	/**
	 *  Determines width, height, and how far we can see. Projection matrix will also make sure a huge square upclose will
	 *  appear small in the distance. so we get the depth in the world.
	 */
	private Matrix4f projectionMatrix;
	
	
	private Matrix4f animatedProjectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	// ADDED Variables needed to render animated entity
	private AnimatedModelShader animatedModelShader = new AnimatedModelShader();
	private AnimatedModelRenderer animatedModelRenderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	
	/** 
	 * HashMap containing all the textured models and their entities. 
	 * Basically have a list of all the entities for each textured model thats going to be rendered. 
	 * We will be having a Key for one specific model with specific texture, so duplicates will be easy to make.
	 *
	 * 	TexturedModel			List<Entity>
	 *  					   _______
	 *  					  |	Cube1 |
	 *  Cubmodel	---> 	  |	Cube2 |
	 * 						  |	Cube3 |
	 * 						  |_______|	
	 * 
	 * Dragon Model	--->	  Dragon1
	 * ....					  Dragon2				
	 * ..					  ......
	 * .                       ....
	 */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	/**
	 *  By calling this constructor we automatically also call our specialized render classes constructors
	 *  We also enables the culling of models and creates the projection matrix of the world
	 */
	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		createAnimatedProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		animatedModelRenderer = new AnimatedModelRenderer(animatedModelShader, animatedProjectionMatrix); // ADDED
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		
	}
	
	/**
	 * Enabling Culling of models.
	 * This method stops rendering the back side of a model, when we are facing the front side of it
	 * Why render something you can't see?
	 */
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	/**
	 *  Disable Culling of models. We are interested in not culling the model, when it has transparency, 
	 *  because the viewer will be able to see the back side of the model through the transparent part of 
	 *  the model.
	 */
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	
	/**
	 * Now we have prevented calling the renderer once every frame per model, but instead we call the renderer once for every different model
	 * for every frame. so 100 similar models will only call the render once per frame and not 100 x frames. We pass this hashMap into the shader.render 
	 * there will the method will loop through the list and create texture models accordingly to the keys found within the hashMap.
	 * 
	 * This function will call all the methods related to rendering, first it will clear all the buffers. then call the respective shaders and
	 * methods to render the models correctly. The entities. or in this world the trees, plants, grass, flowers are first rendered. then the animatedPlayer
	 * is rendered. And last the terrain is rendered.
	 * 
	 * @param lights - list of lights in this world
	 * @param camera - the camera of the viewer of this world
	 * @param animatedPlayer - the player controlled by the viewer.
	 */
	public void render(List<Light> lights, Camera camera, AnimatedModel animatedPlayer) {
		prepare();
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		animatedModelShader.start();
		animatedModelShader.loadSkyColor(RED, GREEN, BLUE);
		animatedModelShader.loadLights(lights);
		animatedModelShader.loadViewMatrix(camera);
		animatedModelRenderer.render(animatedPlayer);
		animatedModelShader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		// clearing hashMap
		entities.clear();
		
	}
	
	/**
	 *  Add terrains to the local list, this list will be looped through in the method
	 *  terrainShader.render and handled properly there.
	 * @param terrain
	 */
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	/**
	 * Sort and put in entities for the HashMap
	 */
	public void processEntity(Entity entity) {
		// identifying the model
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null) {
			batch.add(entity);
		}
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
		
	}
	
	/**
	 * Cleanup the shaders, when game is closed.
	 */
	public void cleanUp() {
		shader.cleanUp();
		animatedModelShader.cleanUp();
		terrainShader.cleanUp();
	}
	
	/**
	 * Just preparing the Open GL for new graphical content.
	 * Clearing color and depth buffers.
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	/**
	 * Initialize the "box" we view the world in, and determines how far, wide and high we can see.
	 * 
	 */
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -(( 2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		
	}
	
	private void createAnimatedProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		animatedProjectionMatrix = new Matrix4f();
		animatedProjectionMatrix.m00 = x_scale;
		animatedProjectionMatrix.m11 = y_scale;
		animatedProjectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		animatedProjectionMatrix.m23 = -1;
		animatedProjectionMatrix.m32 = -(( 2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		animatedProjectionMatrix.m33 = 0;
		
	}

}
