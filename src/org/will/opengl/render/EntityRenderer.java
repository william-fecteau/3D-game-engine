package org.will.opengl.render;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.will.opengl.entities.Entity;
import org.will.opengl.models.RawModel;
import org.will.opengl.models.TexturedModel;
import org.will.opengl.shaders.StaticShader;
import org.will.opengl.textures.ModelTexture;
import org.will.opengl.utils.Maths;

public class EntityRenderer 
{
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(HashMap<TexturedModel, ArrayList<Entity>> entities)
	{
		for(TexturedModel model : entities.keySet())
		{
			prepareTexturedModel(model);
			ArrayList<Entity> batch = entities.get(model);
			for(Entity entity : batch)
			{
				prepareInstance(entity);
				
				//Draw call
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model)
	{
		RawModel rawModel = model.getRawModel();
		
		//Binding the vao of the model
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		//For specular lightning
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
				
		//Activating and binding texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindTexturedModel()
	{
		//Disabling attribs and unbinding vao
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity)
	{
		//Creating and loading the transformation matrix (in uniform)
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
