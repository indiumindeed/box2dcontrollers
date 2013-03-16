package com.gushikustudios.box2d.controllers;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class B2BuoyancyController extends B2Controller
{
   /// The outer surface normal
   public Vector2 normal;
   /// The height of the fluid surface along the normal
   public float offset;
   /// The fluid density
   public float density = 2.0f;
   /// Fluid velocity, for drag calculations
   public Vector2 velocity;
   /// Linear drag co-efficient
   public float linearDrag = 5;
   /// Linear drag co-efficient
   public float angularDrag = 2;
   /// If false, bodies are assumed to be uniformly dense, otherwise use the shapes densities
   public boolean useDensity = false; //False by default to prevent a gotcha
   /// Gravity vector, if the world's gravity is not used
   public Vector2 gravity;
   
   private Vector2 tmp;
   private Vector2 sc;
   private Vector2 areac;
   private Vector2 massc;

   public B2BuoyancyController()
   {
      normal = new Vector2(0,1);
      velocity = new Vector2();
      gravity = new Vector2(0,-9.8f);
      
      mControllerType = B2Controller.BUOYANCY_CONTROLLER;
      
      tmp = new Vector2();
      sc = new Vector2();
      areac = new Vector2();
      massc = new Vector2();
   }
   
//   public override function Apply(body:b2Body):void {
//      if(!body.IsAwake() || !body.IsDynamic()){
//         return;
//      }
//      for(var f:b2Fixture = body.GetFixtureList(); f; f = f.GetNext()) {
//         ApplyToFixture(f);
//      }
//      
//   }
   
   public boolean ApplyToFixture(Fixture f)
   {
      Body body = f.getBody();
      areac.set(0,0);
      massc.set(0,0);
      float area = 0;
      float mass = 0;
      
      Shape shape = f.getShape();
      
      sc.set(0,0);
      float sarea;
      switch (shape.getType())
      {
         case Circle:
            sarea  = B2ShapeExtensions.ComputeSubmergedArea((CircleShape)shape,normal, offset, body.getTransform(), sc);
            break;
            
         case Chain:
            sarea  = B2ShapeExtensions.ComputeSubmergedArea((ChainShape)shape,normal, offset, body.getTransform(), sc);
            break;
            
         case Edge:
            sarea  = B2ShapeExtensions.ComputeSubmergedArea((EdgeShape)shape,normal, offset, body.getTransform(), sc);
            break;
            
         case Polygon:
            sarea  = B2ShapeExtensions.ComputeSubmergedArea((PolygonShape)shape,normal, offset, body.getTransform(), sc);
            break;
            
         default:
            sarea = 0;
            break;
      }
      
      area += sarea;
      areac.x += sarea * sc.x;
      areac.y += sarea * sc.y;
      float shapeDensity = useDensity ? f.getDensity() : density;
      mass += sarea * shapeDensity;
      massc.x += sarea * sc.x * shapeDensity;
      massc.y += sarea * sc.y * shapeDensity;         

      areac.x /= area;
      areac.y /= area;
      massc.x /= mass;
      massc.y /= mass;
      if(area < Float.MIN_VALUE) {
         return false;
      }

      // buoyancy force.
      body.applyForce(tmp.set(gravity).mul(-density * area), massc); // multiply by -density to invert gravity  (combining a couple of operations)
      // linear drag.
      body.applyForce(
         body.getLinearVelocityFromWorldPoint(areac)
         .sub(velocity)
         .mul(-linearDrag * area),
         areac);
      /// angular drag.
      float torque = -body.getInertia() / body.getMass() * area * body.getAngularVelocity() * angularDrag;
      body.applyTorque(torque);
      return true;
   }
   
   @Override
   public void step(float timeStep)
   {
      if (m_bodyList != null)
      {
         for (int i = 0; i < m_bodyList.size; i++)
         {
            ArrayList<Fixture> fixtureList = m_bodyList.get(i).getFixtureList();
            for (int j = 0; j < fixtureList.size(); j++)
            {
               ApplyToFixture(fixtureList.get(j));
            }
         }
      }
   }
}
