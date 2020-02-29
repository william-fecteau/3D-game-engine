package org.will.opengl.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera 
{
	private final float MOVE_SPEED = 0.5f;
	
	private Vector3f position = new Vector3f(0,1,0);
	private float pitch, yaw, roll;
	
	public void move()
	{
		if(Mouse.getX() < 1280/5 * 2)
		{
			yaw-=MOVE_SPEED;
		}
		if(Mouse.getX() > 1280/5 * 3)
		{
			yaw+=MOVE_SPEED;
		}
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            position.z-=MOVE_SPEED;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            position.z+=MOVE_SPEED;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            position.x+=MOVE_SPEED;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            position.x-=MOVE_SPEED;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
        	position.y+=MOVE_SPEED;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
        	position.y-=MOVE_SPEED;
        }
	}
	
	public Vector3f getPosition() 
	{
		return position;
	}
	
	public float getPitch() 
	{
		return pitch;
	}
	
	public float getYaw() 
	{
		return yaw;
	}
	
	public float getRoll() 
	{
		return roll;
	}
	
	

}
