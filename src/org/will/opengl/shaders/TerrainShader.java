package org.will.opengl.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.will.opengl.entities.Camera;
import org.will.opengl.entities.Light;
import org.will.opengl.utils.Maths;

public class TerrainShader extends ShaderProgram
{
	private static final String VERTEX_FILE = "src/org/will/opengl/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/org/will/opengl/shaders/terrainFragmentShader.txt";

	private int locTransformationMatrix;
	private int locProjectionMatrix;
	private int locViewMatrix;
	private int locLightPos;
	private int locLightColour;
	private int locShineDamper;
	private int locReflectivity;
	
	public TerrainShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes()
	{
		//Bind attributes from the vao to the shaders
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() 
	{
		locTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locViewMatrix = super.getUniformLocation("viewMatrix");
		locLightPos = super.getUniformLocation("lightPosition");
		locLightColour = super.getUniformLocation("lightColour");
		locShineDamper = super.getUniformLocation("shineDamper");
		locReflectivity = super.getUniformLocation("reflectivity");
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity)
	{
		super.loadFloat(locShineDamper, shineDamper);
		super.loadFloat(locReflectivity, reflectivity);
	}
	
	public void loadLight(Light light)
	{
		super.loadVector(locLightPos, light.getPositions());
		super.loadVector(locLightColour, light.getColour());
	}
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(locTransformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locViewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection)
	{
		super.loadMatrix(locProjectionMatrix, projection);
	}
}
