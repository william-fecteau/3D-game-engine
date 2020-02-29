package org.will.opengl;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.will.opengl.entities.Camera;
import org.will.opengl.entities.Entity;
import org.will.opengl.entities.Light;
import org.will.opengl.models.RawModel;
import org.will.opengl.models.TexturedModel;
import org.will.opengl.render.DisplayManager;
import org.will.opengl.render.Loader;
import org.will.opengl.render.MasterRenderer;
import org.will.opengl.render.OBJLoader;
import org.will.opengl.terrain.Terrain;
import org.will.opengl.textures.ModelTexture;


public class GameLoop 
{

	public static void main(String[] args) 
	{
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();

		RawModel model = OBJLoader.LoadObjModel("dragon", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(5);
		texture.setReflectivity(1);
		Entity entity = new Entity(texturedModel, new Vector3f(0, 2,-25),0,0,0,1);
		
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("kerchoo")));
		
		
		Light light = new Light(new Vector3f(0, 2000, 0), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		while (!Display.isCloseRequested()) 
		{
			entity.increaseRotation(0, 1, 0);
			
			camera.move();
			
			renderer.processEntity(entity);
			renderer.processTerrain(terrain);
			
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	private static RawModel tempLoadTerrain(int vertexX, int vertexZ, Loader loader)
	{
		int y = 0;
		int lenght = 10;
		
		ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		for(int x = 0; x < vertexX; x++)
		{
			for(int z = 0; z < vertexX; z++)
			{
				positions.add(new Vector3f(x,y,z));
			}
		}
		
		for(int x = 0; x < vertexX; x++)
		{
			for(int z = 0; z < vertexX; z++)
			{
				Vector3f v1 = new Vector3f(x,y,z);
				Vector3f v2 = new Vector3f(x,y,z+lenght);
				Vector3f v3 = new Vector3f(x+lenght,y,z);
				
				if(positions.contains(v1))
				{
					indices.add(positions.indexOf(v1));
				}
				else
				{
					positions.add(v1);
				}
				
				if(positions.contains(v2))
				{
					indices.add(positions.indexOf(v2));
				}
				else
				{
					positions.add(v2);
				}
				
				if(positions.contains(v3))
				{
					indices.add(positions.indexOf(v3));
				}
				else
				{
					positions.add(v3);
				}
			}
		}
		
		//Convertir en float array
		float[] positionsArray = new float[positions.size() * 3];
		for(int i = 0; i < positionsArray.length; i+=3)
		{
			positionsArray[i] = positions.get(i).x;
			positionsArray[i+1] = positions.get(i+1).y;
			positionsArray[i+2] = positions.get(i+2).z;
		}
		
		int[] indicesArray = new int[indices.size()];
		for(int i = 0; i < indicesArray.length; i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		float[] texturesArray = new float[positions.size() * 2];
		for(int i = 0; i < texturesArray.length; i+=2)
		{
			texturesArray[i] = 0;
			texturesArray[i+1] = 0;
		}
		
		float[] normalsArray = new float[positions.size()*3];
		for(int i = 0; i < texturesArray.length; i+=3)
		{
			normalsArray[i] = 0;
			normalsArray[i+1] = 0;
			normalsArray[i+2] = 0;
		}
		
		return loader.loadToVAO(positionsArray, texturesArray, normalsArray, indicesArray);
	}

}