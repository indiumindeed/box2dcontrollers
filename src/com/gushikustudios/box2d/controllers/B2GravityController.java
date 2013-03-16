package com.gushikustudios.box2d.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class B2GravityController extends B2Controller
{
   private Vector2 mControllerGravity;
   
   private static final float GRAVITY_CONSTANT = 9.8f;
   
   public B2GravityController()
   {
      mControllerGravity = new Vector2();
      mControllerGravity.x = 0;
      mControllerGravity.y = GRAVITY_CONSTANT;
      
      mControllerType = B2Controller.GRAVITY_CONTROLLER;
   }
   
   /**
    * This sets the controller gravity.  Invert the signs if you want to make it different from the world gravity
    * 
    * @param gravityX
    * @param gravityY
    */
   public void setGravity(float gravityX, float gravityY)
   {
      mControllerGravity.x = gravityX;
      mControllerGravity.y = gravityY;
   }
   
   public void step(float timeStep)
   {
      if (m_bodyList != null)
      {
         for (int i = 0; i < m_bodyList.size; i++)
         {
            Body body = m_bodyList.get(i);
            float mass = body.getMass();
            float forceX = mass * mControllerGravity.x;
            float forceY = mass * mControllerGravity.y;
            Vector2 pos = body.getPosition();
            float pointX = pos.x;
            float pointY = pos.y;
            body.applyForce(forceX, forceY, pointX, pointY);
         }
      }
   }
}
