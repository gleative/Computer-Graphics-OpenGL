package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Contains everything a shader program needs
 */
public abstract class shaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	// Needed to load a Matrix to a uniform variable
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16); // 16 because we use 4 x 4 matrixes
	
	public shaderProgram(String vertexFile, String fragmentFile) {
		// Gets the ID
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		
		// When id is found, create a new program
		programID = GL20.glCreateProgram();
		
		// Attach fragmentShader and vertexShader to the program
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		// Bind attributes need to be called BEFORE the link program
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations(); // Gets called up each time shaderprogram is created
	}
	
	// Makes sure all shader program classes will have a method that gets all uniform locations, remember to put it in the constructor!
	protected abstract void getAllUniformLocations();
	
	// Gets the location of a uniform variable in shader code
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
		
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	// Class extending this must have this method
	protected abstract void bindAttributes();
	
	// Binds a shader to a attribute in the VAO
	protected void bindAttribute(int attribute, String variableName) {
		 GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	// Loads up the location of the uniform and the float value
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	// Loads up the location of the uniform and the int value
	public void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	// Loads up a vector to a uniform
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);  // Notice glUniform3f, because its 3 floats
	}
	
	protected void load2DVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	// Loads up a boolean, if its true we load up a 1, false we load up a 0
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if(value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	// Loads a matrix to a uniform variable, need a float buffer see on variables
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
		
	}
	
	/**
	 * Loads the shader, not important to know what it does.
	 * Prints errors if something goes wrong
	 */
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
	try {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;	
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
	int shaderID = GL20.glCreateShader(type);
	GL20.glShaderSource(shaderID, shaderSource);
	GL20.glCompileShader(shaderID);
	if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
		System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
		System.err.println("Could not compile shader.");
		System.exit(-1);
	}
	return shaderID;
	}
	
}
