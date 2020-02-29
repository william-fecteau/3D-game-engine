package org.will.opengl.render;

import java.awt.List;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.will.opengl.entities.Entity;
import org.will.opengl.models.RawModel;
import org.will.opengl.models.TexturedModel;
import org.will.opengl.shaders.TerrainShader;
import org.will.opengl.terrain.Terrain;
import org.will.opengl.textures.ModelTexture;
import org.will.opengl.utils.Maths;

public class TerrainRenderer 
{
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(ArrayList<Terrain> terrains)
	{
		for(Terrain terrain : terrains)
		{
			//Bind models and bind texture
			prepareTerrain(terrain);
			
			//Load transformation matrix
			loadModelMatrix(terrain);
			
			//Draw call
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTerrain();
		}
	}
	
	private void prepareTerrain(Terrain terrain)
	{
		RawModel rawModel = terrain.getModel();
		
		//Binding the vao of the model
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		//For specular lightning
		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
				
		//Activating and binding texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindTerrain()
	{
		//Disabling attribs and unbinding vao
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain)
	{
		//Creating and loading the transformation matrix (in uniform)
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
