package animation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import animation.AnimatedModel;
import models.RawModel;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import toolbox.Maths;

/**
 * Handles the rendering of a animated model/entity
 * The pose that the animated model will be rendererd in 
 * is determined by the joint transforms
 * 
 * @author Glenn Arne Christensen
 */
public class AnimatedModelRenderer {

	private AnimatedModelShader shader;

	/**
	 * Initializes the shader program used for rendering animated models.
	 */
	public AnimatedModelRenderer(AnimatedModelShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		
		// Loads the shader, only has to be done once
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Renders the animated entity. Works the same as rendering a entity,
	 * but notice with a animated model we have to enable five attributes
	 * of the VAO before we render the animated entity. This is because 
	 * we need to have the indices and weights
	 * @param entity
	 */
	public void render(AnimatedModel entity) {
		prepareTexturedModel(entity);
		prepareInstance(entity);
		shader.loadJointTransforms(entity.getJointTransforms()); // The joint transforms get loaded up to the shader in a uniform array
		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		unbindTexturedModel(); // Unbinds all textures after its done
	}
	
	/**
	 * Prepares the animated model by enabling five attributes of the VAO.
	 * We also load up damper and reflectivity onto texture, before binding it.
	 * @param entity
	 */
	public void prepareTexturedModel (AnimatedModel entity) {
		RawModel rawModel = entity.getRawModel();
		
		// Binds the VAO we want to use
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		
		// Load up the texture and apply the shinedamper and reflectivity on it
		ModelTexture texture = entity.getModelTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		// Tells OpenGL which texture we want to render, and put it to one of the texture banks (GL_TEXTURE0 is one of the banks) ! 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModelTexture().getID());
	}
	
	/**
	 * Creates a transformationMatrix from the entities position
	 * @param entity
	 */
	private void prepareInstance(AnimatedModel entity) {
		Matrix4f transfomationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transfomationMatrix);
		shader.loadOffset(0, 0);
		
	}
	
	/**
	 *  Disable the attribute list when everything is finished.
	 */
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		
		// Also have to unbind the VAO by putting in a 0
		GL30.glBindVertexArray(0);
		
	}


	/**
	 * Starts the shader program and loads up the projection view matrix, as
	 * well as the light direction. Enables and disables a few settings which
	 * should be pretty self-explanatory.
	 * 
	 * @param camera
	 *            - the camera being used.
	 * @param lightDir
	 *            - the direction of the light in the scene.
	 */
//	private void prepare(ICamera camera, Vector3f lightDir) {
//		shader.start();
//		shader.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
//		shader.lightDirection.loadVec3(lightDir);
//		OpenGlUtils.antialias(true);
//		OpenGlUtils.disableBlending();
//		OpenGlUtils.enableDepthTesting(true);
//	}

	/**
	 * Stops the shader program after rendering the entity.
	 */
//	private void finish() {
//		shader.stop();
//	}

}
