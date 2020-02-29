package org.will.opengl.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light 
{
	private Vector3f position;
	private Vector3f colour;
	
	public Light(Vector3f positions, Vector3f colour) 
	{
		super();
		this.position = positions;
		this.colour = colour;
	}

	public Vector3f getPositions() 
	{
		return position;
	}

	public void setPosition(Vector3f position) 
	{
		this.position = position;
	}

	public Vector3f getColour() 
	{
		return colour;
	}

	public void setColour(Vector3f colour) 
	{
		this.colour = colour;
	}
}
