Box2DControllers
================
This is a project that contains generic Box2D Controllers that are compatible with the 
libgdx implementation of Box2D.  The following controllers are currently provided:

- Buoyancy controller, simulating water effects
- Gravity controller, simulating environments that have gravitational effects

The buoyancy algorithms originated in wck which in turn was derived from Boris The Brave's work.

For a great tutorial on buoyancy, see iforce2d's page on the subject:

https://www.iforce2d.net/b2dtut/buoyancy

For info on RUBE, the editor used to create the example, go here:

https://www.iforce2d.net/rube/

(I've no stake in RUBE -- just a happy user!)

General
=======
B2Controller is the base class that the various controllers are derived from.  The controllers
should implement the step() method.  The step() method for all controllers should be executed
whenever the physics world step() method is invoked.

The easiest approach to utilize the controllers is to create sensor fixtures that test
when a beginContact / endContact event has occurred with a body.  In the beginContact
method, add the body to the controller.  In the endContact method, remove the body.
That's all there is to it.

Setup
=====
The following project includes a test for Box2dControllers:

https://github.com/tescott/RubeLoader

Follow these steps to configure your Eclipse project:

1) Clone the Box2DControllers repo.
2) Clone the RubeLoader repo.
3) Open a new workspace in Eclipse.
4) Import Box2DControllers to your workspace.
5) Import RubeLoader to your workspace.
6) Import RubeLoaderTest-android to your workspace.
7) Import RubeLoaderTestWithBox2dControllers to your workspace.
8) Import RubeLoaderTestWithBox2dControllers-desktop to your workspace.
9) Resolve any dependency issues.  You may have to close / reopen Eclipse here.
10) Right-click on RubeLoaderTestWithBox2dControllers-desktop > Run As > Java Application > RubeLoaderTestDesktop.

The included example shows four "pools" of water at the bottom of the view, showing the effects of a buoyancy controller for light objects
and heavy objects, as well as currents.  At the top of the view you will see the effects of a gravity controller.

Tips
====
* The various controller parameters are defined as custom properties in the JSON file exported from RUBE.
* For buoyancy, less dense fixture values will cause objects to submerge further.
* For buoyancy, higher linear drag values will cause objects to bob / oscillate more. 

Limitations
===========
* Buoyancy requires a fixed surface height
* Buoyancy needs to be scaled based on densities.  Bodies with very small densities will 'popcorn' from controllers that behave normally for bodies with larger densities.  

Screenshot of Example
=====================
![Screenshot](https://raw.github.com/tescott/box2dcontrollers/master/screenshot.png)





 