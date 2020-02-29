package org.will.opengl.render;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.will.opengl.entities.Camera;
import org.will.opengl.entities.Entity;
import org.will.opengl.entities.Light;
import org.will.opengl.models.TexturedModel;
import org.will.opengl.shaders.StaticShader;
import org.will.opengl.shaders.TerrainShader;
import org.will.opengl.terrain.Terrain;

public class MasterRenderer 
{
	//Projection matrix
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	private TerrainShader terrainShader;
	
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	
	private HashMap<TexturedModel, ArrayList<Entity>> entities;
	private ArrayList<Terrain> terrains;
	
	public MasterRenderer()
	{
		shader = new StaticShader();
		terrainShader = new TerrainShader();
		createProjectionMatrix();
		
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		
		entities = new HashMap<TexturedModel, ArrayList<Entity>>();
		terrains = new ArrayList<Terrain>();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	
	public void render(Light sun, Camera camera)
	{
		prepare();
		
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		
		renderer.render(entities);
		
		shader.stop();
		entities.clear();
		
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		
		terrainRenderer.render(terrains);
		
		shader.stop();
		terrains.clear();
	}
	
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		ArrayList<Entity> batch = entities.get(entityModel);
		if(batch != null) batch.add(entity);
		else
		{
			ArrayList<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}
	
	//Called once every frame
	public void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}
	
	private void createProjectionMatrix()
	{
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
	
	public void cleanUp()
	{
		shader.cleanUp();
		terrainShader.cleanUp();
	}
}
