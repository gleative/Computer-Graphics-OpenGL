package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

	/**
	 * This class is called upon for every second of the game, to make sure every model is rendered accordingly to the VAO.
	 * @author Håkon S. Bøckman
	 *
	 */
public class EntityRenderer {
	
	//private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		
		//createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	
	/**
	 * Loop through all the keys in the hashMap, all the texturedModels as well.
	 * For each textured model we need to prepare the textured model. Then we are going to get all the 
	 * entities that use that texturedModel. And finally draw it.
	 * 
	 * @param entities
	 */
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				prepareInstances(entity);
				
				// run the final texture model, "draw"
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		
	}
	/**
	 * Bind the VAO and Texture.
	 * We also load up damper and reflectivity onto texture, before binding it
	 * @param model
	 */
	private void  prepareTexturedModel(TexturedModel model) {
		// Bind the model(VAO)
		GL30.glBindVertexArray(model.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);	
		GL20.glEnableVertexAttribArray(1);	
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		
		if(texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		
	// Load up shine settings
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
	// Bind the texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstances(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
	


}

/************************************************************* CODE GRAVEYARD *********************************************************************






	//   OLD  Render, split up into smaller parts. @see Render  "new" Render
	// This method is called for every model in the game for every frame.
	   
	   public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		
	// Bind the model
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);	
		GL20.glEnableVertexAttribArray(1);	
		GL20.glEnableVertexAttribArray(2);
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		ModelTexture texture = model.getTexture();
		
	// Load up shine settings
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
	// Bind the texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
	// Unbind everything
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}

















 ************************************************************** CODE GRAVEYARD *********************************************************************/