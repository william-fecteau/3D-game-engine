package org.will.opengl.render;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.will.opengl.models.RawModel;

public class Loader 
{
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices)
	{
		//Creating vao
		int vaoID = createVAO();
		
		//Binding index buffer
		bindIndicesVBO(indices);
		
		//Storing positions in attrib 0
		storeDataInAttribList(0, 3, positions);
		//Storing texture coords in attrib 1
		storeDataInAttribList(1, 2, textureCoords);
		//Storing normals in attrib 2
		storeDataInAttribList(2, 3, normals);
		
		//Unbinding
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	//Loading textures to use them at anytime
	public int loadTexture(String fileName)
	{
		Texture texture = null;
		try 
		{
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	
		int textureID = texture.getTextureID();
		//Keeping track to delete at the end
		textures.add(textureID);
		
		return textureID;
	
	}
	
	//Delete everything that was loaded
	public void cleanUp()
	{
		for(int vao : vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
		for(int tex : textures)
		{
			GL11.glDeleteTextures(tex);
		}
	}
	
	private int createVAO()
	{
		//Creating and binding a vao
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		
		//Keeping track to delete at the end
		vaos.add(vaoId);
		
		return vaoId;
	}
	
	private void storeDataInAttribList(int attribNum, int coordinateSize, float[] data)
	{
		//Creating and binding a vbo
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		
		//Keeping track to delete at the end
		vbos.add(vboId);
		
		//Putting float data into a float buffer to put in the vbo
		FloatBuffer buffer = dataToFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		//Put the vbo in the vba
		GL20.glVertexAttribPointer(attribNum, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		
		//Unbind the vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void bindIndicesVBO(int[] indices)
	{
		//Creating and binding vbo
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		//Keeping track to delete at the end
		vbos.add(vboID);
		
		//Putting int data into an int buffer to put in the vbo
		IntBuffer buffer = dataToIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer dataToIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private FloatBuffer dataToFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
}
