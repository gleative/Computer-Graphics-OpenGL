package animation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.AnimatedModel;
import models.RawModel;
import textures.ModelTexture;
import toolbox.Maths;
import engineTester.OpenGlUtils;

/**
 * 
 * This class deals with rendering an animated entity. Nothing particularly new
 * here. The only exciting part is that the joint transforms get loaded up to
 * the shader in a uniform array.
 * 
 * @author Karl
 *
 */
public class AnimatedModelRenderer {

	private AnimatedModelShader shader;

	/**
	 * Initializes the shader program used for rendering animated models.
	 */
	// ADDED
	public AnimatedModelRenderer(AnimatedModelShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Renders an animated entity. The main thing to note here is that all the
	 * joint transforms are loaded up to the shader to a uniform array. Also 5
	 * attributes of the VAO are enabled before rendering, to include joint
	 * indices and weights.
	 * 
	 * @param entity
	 *            - the animated entity to be rendered.
	 * @param camera
	 *            - the camera used to render the entity.
	 * @param lightDir
	 *            - the direction of the light in the scene.
	 */
//	public void render(AnimatedModel entity, ICamera camera, Vector3f lightDir) {
//		prepare(camera, lightDir);
//		entity.getTexture().bindToUnit(0);
//		entity.getModel().bind(0, 1, 2, 3, 4);
//		shader.jointTransforms.loadMatrixArray(entity.getJointTransforms());
//		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
//		entity.getModel().unbind(0, 1, 2, 3, 4);
//		finish();
//	}
	
	// ADDED
	public void render(AnimatedModel entity) {
		RawModel rawModel = entity.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		ModelTexture texture = entity.getModelTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
//		if(texture.isHasTransparenty()) {
//			MasterRenderer.disableCulling();
//		}
//		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModelTexture().getID());
	}
	
	// ADDED
	private void prepareInstance(AnimatedModel entity) {
		Matrix4f transfomationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transfomationMatrix);
		shader.loadOffset(0, 0);
		
	}
	
	// ADDED
	private void unbindTexturedModel() {
//		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL30.glBindVertexArray(0);
		
	}

	/**
	 * Deletes the shader program when the game closes.
	 */
	public void cleanUp() {
		shader.cleanUp();
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
	private void finish() {
		shader.stop();
	}

}