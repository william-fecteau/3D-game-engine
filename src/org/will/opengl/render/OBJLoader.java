package org.will.opengl.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.will.opengl.models.RawModel;

public class OBJLoader 
{
	public static RawModel LoadObjModel(String file, Loader loader)
	{
		FileReader fr = null;
		try
		{
			fr = new FileReader(new File("res/models/"+file+".obj"));
		} 
		catch(FileNotFoundException e)
		{
			System.err.print("Model file not found");
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line = null;
		
		ArrayList<Vector3f> verteces = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		float[] vertecesArray = null;
		float[] texturesArray = null;
		float[] normalsArray = null;
		int[] indicesArray = null;
		
		try
		{
			while(true)
			{
				line = reader.readLine();
				String[] data = line.split(" ");
				
				if(line.startsWith("v "))
				{
					verteces.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])));

				} 
				else if(line.startsWith("vt "))
				{
					textures.add(new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2])));

				}
				else if(line.startsWith("vn "))
				{
					normals.add(new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])));

				}
				else if(line.startsWith("f "))
				{
					texturesArray = new float[verteces.size() * 2];
					normalsArray = new float[verteces.size() * 3];
					break;
				}
			}
			
			while(line != null)
			{
				if(!line.startsWith("f "))
				{
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
				
				line = reader.readLine();
			}
			
			reader.close();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		vertecesArray = new float[verteces.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex:verteces)
		{
			vertecesArray[vertexPointer++] = vertex.x;
			vertecesArray[vertexPointer++] = vertex.y;
			vertecesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		
		return loader.loadToVAO(vertecesArray, texturesArray, normalsArray, indicesArray);
	}
	
	
	private static void processVertex(String[] vertexData, ArrayList<Integer> indices,
			ArrayList<Vector2f> textures, ArrayList<Vector3f> normals, float[] texturesArray, float[] normalsArray) 
	{
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		Vector2f tex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		texturesArray[currentVertexPointer*2] = tex.x;
		texturesArray[currentVertexPointer*2+1] = 1 - tex.y;
		
		Vector3f norm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer*3] = norm.x;
		normalsArray[currentVertexPointer*3+1] = norm.y;
		normalsArray[currentVertexPointer*3+2] = norm.z;
		
		
	}

}
