/**
The MIT License (MIT)

Copyright (c) 2014 MN Tech Solutions
Copyright (c) 2014 tagAugmentedReality
Copyright (c) 2014 Mustafa Neguib

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.tagaugmentedreality;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tagaugmentedreality.cache.CameraDataCache;
import com.tagaugmentedreality.cache.CombinedTagListCache;
import com.tagaugmentedreality.camerapreview.CameraPreview;
import com.tagaugmentedreality.openglobjectloader.GenerateNumberModel;
import com.tagaugmentedreality.openglobjectloader.GenerateTagModel;
import com.tagaugmentedreality.openglobjectloader.LoadModelsFromAssets;
import com.tagaugmentedreality.openglobjectloader.ModelData;
import com.tagaugmentedreality.utilties.Utilities;

public class Browser extends ActionBarActivity implements
		GLSurfaceView.Renderer, SensorEventListener, OnTouchListener,
		OnClickListener {

	private GLSurfaceView gLSurfaceView;
	private Camera camera;
	private CameraDataCache cameraData;
	private CameraDataCache cameraDataInitial;
	private SensorManager mSensorManager;
	private float bearing = 0.0f;
	private float oldBearing = 0.0f;
	private float pitch = 0.0f;
	private float oldPitch = 0.0f;
	private boolean mFailed;
	private float[] gravity = { 0.0f, 0.0f, 0.0f };
	private float[] geoMagnetic = { 0.0f, 0.0f, 0.0f };// new float[3];
	private float[] orientation = new float[3];
	private float[] rotationMatrix = new float[9];
	private float[] remapedRotationMatrix = new float[9];
	private float[] accelerometer = new float[3];
	private int counter = 0;
	private TextView tv;
	private android.support.v7.app.ActionBar actionBar;
	private Context context;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private ImageView crossHair;
	private float diffBearing = 0.0f;
	private float initialBearing = 0.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_UI);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("");

		context = this;

		// Create our Preview view and set it as the content of our
		// Activity
		gLSurfaceView = new GLSurfaceView(context);
		// We want an 8888 pixel format because that's required for
		// a translucent window.
		// And we want a depth buffer.
		gLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		// Tell the cube renderer that we want to render a translucent version
		// of the cube:
		// gLSurfaceView.setRenderer(new
		// OpenGLModelObjectLoader.GraphicsRenderer(this));
		gLSurfaceView.setRenderer(this);
		gLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

		// Use a surface format with an Alpha channel:
		gLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		setContentView(gLSurfaceView);

		gLSurfaceView.setOnTouchListener(this);

		tv = new TextView(context);
		tv.setTextColor(Color.WHITE);

		camera = CameraPreview.getCameraInstance();
		CameraPreview cameraView = new CameraPreview(this, camera);

		// i have to rotate the camera because by default in portrait mode the
		// camera is
		// being rotated.
		camera.setDisplayOrientation(90);

		addContentView(cameraView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		addContentView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		RelativeLayout rl = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams crossHairParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		crossHairParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		crossHair = new ImageView(context);
		crossHair.setBackgroundResource(R.drawable.cross_hair);
		crossHair.setLayoutParams(crossHairParams);

		rl.addView(crossHair);
		addContentView(rl, rlParams);

		/*
		 * FrameLayout fl=new FrameLayout(this); fl.setLayoutParams(new
		 * LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT ));
		 * fl.addView(cameraView); fl.addView(gLSurfaceView);
		 * setContentView(fl);
		 */

		// setContentView(gLSurfaceView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ar_view, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case android.R.id.home:

			startActivity(new Intent(context, Lists.class));
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		gLSurfaceView.onResume();

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_UI);
		cameraData = new CameraDataCache();
		cameraDataInitial = new CameraDataCache();

		cameraData.resetVector();
		cameraDataInitial.resetVector();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		gLSurfaceView.onPause();
		mSensorManager.unregisterListener(this);

		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}

		startActivity(new Intent(context, Lists.class));
		finish();

	}

	@Override
	public void onDrawFrame(GL10 gl) {

		/*
		 * Usually, the first thing one might want to do is to clear the screen.
		 * The most efficient way of doing this is to use glClear().
		 */

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// GLU.gluLookAt(gl, 0, 0, 30, 0, 0, 0, 0, 1, 0);
		GLU.gluLookAt(gl, cameraData.getVector()[0], cameraData.getVector()[1],
				cameraData.getVector()[2], cameraData.getVector()[3],
				cameraData.getVector()[4], cameraData.getVector()[5], 0, 1, 0);

		if (null != CombinedTagListCache.getCombinedList()
				&& CombinedTagListCache.getCombinedList().size() > 0) {
			int i = 0;
			for (i = 0; i < CombinedTagListCache.getCombinedList().size(); i++) {

				gl.glPushMatrix();
				gl.glTranslatef(CombinedTagListCache.getCombinedList().get(i)
						.getOpenGLModel().getVector()[0],
						CombinedTagListCache.getCombinedList().get(i)
								.getOpenGLModel().getVector()[1],
						CombinedTagListCache.getCombinedList().get(i)
								.getOpenGLModel().getVector()[2]);

				// Log.e(Utilities.TAG,
				// CombinedTagListCache.getCombinedList().get(i).getLatitude()+","+CombinedTagListCache.getCombinedList().get(i).getLongitude()+" "+CombinedTagListCache.getCombinedList().get(i).getOpenGLModel().getVector()[0]+","+CombinedTagListCache.getCombinedList().get(i).getOpenGLModel().getVector()[1]+","+CombinedTagListCache.getCombinedList().get(i).getOpenGLModel().getVector()[2]+","+CombinedTagListCache.getCombinedList().get(i).getOpenGLModel().getVector()[3]+","+CombinedTagListCache.getCombinedList().get(i).getOpenGLModel().getVector()[4]+","+CombinedTagListCache.getCombinedList().get(i).getOpenGLModel().getVector()[5]);
				
				/**
				 * When i rotated the tags around the origin, for some reason
				 * the tags also rotated about their centers by the amount they
				 * were rotated. Due to this the tags were rotated about their
				 * points with varying angles. Due to this the tags did not face
				 * the origin. After observing the behavior of the code, i came
				 * up with the solution to rotate the tags about their point by
				 * that same angle. I have to apply the rotation in the opposite
				 * direction, hence the multiplication by -.
				 */
				gl.glRotatef(-CombinedTagListCache.getCombinedList().get(i)
						.getOpenGLModel().getAngleDIff(), 0, 1, 0);
				// gl.glRotatef(180,0,0,1);
				// gl.glRotatef(180,0,1,0);
				// gl.glScalef(1.2f, 1.2f, 1f);
				gl.glScalef(0.5f, 0.5f, 0.5f);
				/**
				 * I am generating the textures once only in the
				 * onSurfaceCreated function. When the model is to be drawn i
				 * will use the textures which have already been generated and
				 * bind them to the model. I do not have to generate the
				 * textures again. I do not need to load the textures from the
				 * file again as they already have been done.
				 */

				/*
				 * if(CombinedTagList.getCombinedList().get(i).getTagList().size(
				 * )>1) { gl.glPushMatrix(); gl.glTranslatef(-0.3f, -0.2f, 0);
				 * CombinedTagList
				 * .getCombinedList().get(i).getOpenGLModel().getTagOpenGLModel
				 * ().bindTextures(gl);
				 * CombinedTagList.getCombinedList().get(i).
				 * getOpenGLModel().getTagOpenGLModel().draw(gl);
				 * gl.glPopMatrix(); }//end if
				 */
				CombinedTagListCache.getCombinedList().get(i).getOpenGLModel()
						.getTagOpenGLModel().bindTextures(gl);
				CombinedTagListCache.getCombinedList().get(i).getOpenGLModel()
						.getTagOpenGLModel().draw(gl);

				gl.glPushMatrix();
				gl.glTranslatef(1.613f, 0.946f, 0.0f);

				gl.glRotatef(180, 1, 0, 0);
				if (CombinedTagListCache.getCombinedList().get(i).getTagList()
						.size() > 9) {
					gl.glScalef(2, 2, 2);
				}// end if

				gl.glTranslatef(-1.613f, -0.946f, 0.0f);

				CombinedTagListCache.getCombinedList().get(i).getOpenGLModel()
						.getNumberOpenGLModel().bindTextures(gl);
				CombinedTagListCache.getCombinedList().get(i).getOpenGLModel()
						.getNumberOpenGLModel().draw(gl);
				gl.glPopMatrix();
				gl.glPopMatrix();

			}// end for
		}// end if

		gl.glFlush();

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				50000);
		// gl.glFrustumf(-4, 4, -4, 4, 3, 10);
		// gl.glFrustumf(-400, 400, -221, 221, 0.1f, 100);
		// gl.glOrthof(-10, 10, -50, 50, -10, 10);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity();

		screenWidth = width;
		screenHeight = height;

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		gl.glDisable(GL10.GL_DITHER);

		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// gl.glClearColor(0.62f, 0.75f, 1.0f, 0);
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		// **********************load from the internal file********************

		LoadModelsFromAssets load;

		try {// check if the internal file exists. if it does, then the data has
				// already been loaded from the assets in the past and i do not
				// need to load them again.

			openFileInput("tagARModelsData");
			load = new LoadModelsFromAssets();

		}// end try
		catch (Exception e) {// the file does not exist, so i have to load the
								// models in the file from the assets

			load = new LoadModelsFromAssets();

			int lengthOfData = load.loadFromAssets(this, "tagARModelsData");
			String buffer = ((Integer) lengthOfData).toString();

			FileOutputStream fos;
			try {
				fos = this.openFileOutput("tagARModelsDataLength",
						Context.MODE_PRIVATE);
				fos.write(buffer.getBytes());
				fos.close();

			}// end try
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}// end catch

		}// end catch

		int length = load.getLengthOfDataInBytes(this, "tagARModelsDataLength");
		List<ModelData> list = load.loadFromInternalMemory(this, length,
				"tagARModelsData");
		if (null != CombinedTagListCache.getCombinedList()
				&& CombinedTagListCache.getCombinedList().size() > 0) {
			int i = 0;
			for (i = 0; i < CombinedTagListCache.getCombinedList().size(); i++) {

				/**
				 * Create a new tag model. This model contains all the required
				 * drawing capabilities.
				 */
				CombinedTagListCache.getCombinedList().get(i).getOpenGLModel()
						.setTagOpenGLModel(new GenerateTagModel(this, list));
				CombinedTagListCache
						.getCombinedList()
						.get(i)
						.getOpenGLModel()
						.setNumberOpenGLModel(
								new GenerateNumberModel(this, list));
				// net.mntechsolutions.tagar.cache.TagsList.getTagsList().get(i).getNumberOpenGLModel().setTagModel(new
				// GenerateTagModel(this,list));

				/**
				 * Generate a new texture for each and every tag. After the
				 * texture has been generated i will just need to bind the
				 * texture to the model just before i draw it.
				 */

				CombinedTagListCache.getCombinedList().get(i).getOpenGLModel()
						.getTagOpenGLModel().loadTextures(gl, this, "");
				CombinedTagListCache
						.getCombinedList()
						.get(i)
						.getOpenGLModel()
						.getNumberOpenGLModel()
						.loadTextures(
								gl,
								this,
								((Integer) CombinedTagListCache
										.getCombinedList().get(i).getTagList()
										.size()).toString());

				// Log.e("title:",net.mntechsolutions.tagar.cache.TagsList.getTagsList().get(i).getTitle());

			}// end for
		}// end if
			// textModel=new GenerateTextModel(this,list);
			// textModel.loadTextures(gl,this);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	protected float[] lowPass(float[] input, float[] output) {
		if (output == null)
			return input;
		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + Utilities.ALPHA * (input[i] - output[i]);
		}
		return output;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:

			/*
			 * for (int i = 0; i < 3; i++) { gravity[i] = event.values[i];
			 * }//end for
			 */
			gravity = lowPass(event.values.clone(), gravity);
			// short zCurr=(short)event.values[2];

			accelerometer = gravity;// event.values.clone();

			/**
			 * Move the camera up and down
			 */

			/*
			 * if(zCurr>=-1 && zCurr<=1) {
			 * Log.e("baseZ",((Short)zCurr).toString()); }//end if else
			 * if(zCurr>1) { cameraData.resetToIdentity();
			 * cameraData.rotation(1,-10); cameraData.updateXYZ();
			 * cameraData.resetToIdentity();
			 * Log.e("baseZ+",((Short)zCurr).toString());
			 * 
			 * }//end else if else {
			 * 
			 * cameraData.resetToIdentity(); cameraData.rotation(1,10);
			 * cameraData.updateXYZ(); cameraData.resetToIdentity();
			 * Log.e("baseZ-",((Short)zCurr).toString());
			 * 
			 * }//end else if
			 */

			break;

		case Sensor.TYPE_MAGNETIC_FIELD:
			/*
			 * for (int i = 0; i < 3; i++) { geoMagnetic[i] = event.values[i];
			 * }// end for
			 */
			geoMagnetic = lowPass(event.values.clone(), geoMagnetic);

			break;
		default:
			return;
		}

		if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity,
				geoMagnetic)) {
			// Rotate to the camera's line of view (Y axis along the camera's
			// axis)

			SensorManager.remapCoordinateSystem(rotationMatrix,
					SensorManager.AXIS_X, SensorManager.AXIS_Z,
					remapedRotationMatrix);
			SensorManager.getOrientation(remapedRotationMatrix, orientation);
			onSuccess();

		}// end if
		else {

			onFailure();

			/*
			 * // SensorManager.getRotationMatrixFromVector(rotationMatrix, //
			 * event.values);
			 * 
			 * SensorManager.getRotationMatrix(rotationMatrix, null,
			 * event.values, event.values);
			 * 
			 * SensorManager.remapCoordinateSystem(rotationMatrix,
			 * SensorManager.AXIS_X, SensorManager.AXIS_Z,
			 * remapedRotationMatrix);
			 * SensorManager.getOrientation(remapedRotationMatrix, orientation);
			 * // Optionally convert the result from radians to degrees
			 * 
			 * orientation[0] = (float) Math.toDegrees(orientation[0]);
			 * orientation[1] = (float) Math.toDegrees(orientation[1]);
			 * orientation[2] = (float) Math.toDegrees(orientation[2]);
			 * 
			 * onSuccess();
			 */
		}// end else

	}

	void onSuccess() {
		if (mFailed)
			mFailed = false;

		/*
		 * 
		 * This function works if the device has accelerometer and magnetic
		 * field sensors working. This function calculates the amount by which
		 * to rotate the OpenGL camera. This function also implements the auto
		 * correcting functionality when the system corrects the vector which
		 * tracks the angle of the camera. I have to do this because i am doing
		 * relative positioning of the camera. As the device is rotated about
		 * the y-axis(while in portrait mode). As we continue to rotate the
		 * device the values compound and add up to the vector, which causes the
		 * vector to be offset with wrong values, which as a result shows the
		 * tags in the wrong places, so i have implemented an algorithm which
		 * auto corrects the vector. This still needs testing, and further
		 * improvement.
		 */

		// i have to save the last bearing which was used.
		oldBearing = bearing;
		oldPitch = pitch;

		int angle = 0;

		// Convert the azimuth to degrees in 0.5 degree resolution.
		bearing = (float) Math.round((Math.toDegrees(orientation[0])) * 2) / 2;

		// Adjust the range: 0 < range <= 360 (from: -180 < range <= 180).
		bearing = (bearing + 360) % 360;

		// calculate the pitch (up and down movement of the device)

		// Convert the azimuth to degrees in 0.5 degree resolution.
		pitch = (float) Math.round((Math.toDegrees(orientation[1])) * 2) / 2;

		// Adjust the range: 0 < range <= 360 (from: -180 < range <= 180).
		pitch = (pitch + 360) % 360;

		float diff = 0;

		if (Math.abs(bearing - oldBearing) > Math.abs((pitch - oldPitch))) {

			diff = Math.abs(bearing - oldBearing);

			// Log.e(Utilities.TAG, "oldBearing: " + oldBearing);
			// Log.e(Utilities.TAG, "bearing: " + bearing);
			// Log.e(Utilities.TAG, "diff: " + diff);

			// Log.e(Utilities.TAG, "vector x,z: " + cameraData.getVector()[3] +
			// "," + cameraData.getVector()[5]);

			/*
			 * vectorAngle is the angle of the vector in the 3D world. This
			 * angle is the actual angle of the cameraData vector in the 3D
			 * world. This is the mapping that we are implementing between the
			 * 3D world and the real world.
			 */
			double vectorAngle = Math.round(Math.toDegrees(cameraData
					.calculateAngle()) * 2) / 2;

			/*
			 * The following code is part of the auto correcting mechanism.
			 * cameraDataInitial is the vector of the camera as it should be at
			 * this instant. I am calculating this by subtracting the current
			 * bearing given by bearing and the initialBearing of the device
			 * when the browser was opened for the first time. We have to use
			 * initialBearing because the OpenGL camera is to be mapped to the
			 * real world 3d bearing coordinates.
			 * 
			 * Suppose the bearing is 100 degrees initialBearing is 230 degrees
			 * the initial vector of the camera is pointing at 0,0,0,0,0,-10
			 * 
			 * The vector that we are using is of the format x,y,z,x1,y1,z1 The
			 * vector that we are using is the mathematical vector where a
			 * vector has a direction and a size. OpenGL also uses vectors as
			 * the unit in drawing 2D and 3D models.
			 */

			/*
			 * diffTest is the difference at which the vector would be rotate at
			 * if all goes well. cameraDataInitial after the execution of the
			 * following block of code points to the bearing to where the vector
			 * should be. Do note the use of initialBearing is critical to the
			 * calculation. All additions to the cameraData (the vector actually
			 * being used for the OpenGL camera manipulation) vector are based
			 * on the initialVector.
			 */

			float diffTest = Math.abs(bearing - initialBearing);

			if ((initialBearing - bearing) > 0) {
				cameraDataInitial.resetToIdentity();
				cameraDataInitial.rotation(2, -diffTest);
				cameraDataInitial.updateXYZ();
				cameraDataInitial.resetToIdentity();
			}// end if
			else if ((initialBearing - bearing) < 0) {
				cameraDataInitial.resetToIdentity();
				cameraDataInitial.rotation(2, diffTest);
				cameraDataInitial.updateXYZ();
				cameraDataInitial.resetToIdentity();
			}// end else if

			/*
			 * Log.e(Utilities.TAG, "correct x,z: " +
			 * cameraDataInitial.getVector()[3] + "," +
			 * cameraDataInitial.getVector()[5]);
			 */
			/*
			 * correctVectorAngle is the angle of the vector in the 3D world
			 * which should be the angle of cameraData if everything is correct.
			 * This angle is the actual angle of the cameraDataInitial vector in
			 * the 3D world. This is the mapping that we are implementing
			 * between the 3D world and the real world.
			 */

			double correctVectorAngle = Math.round(Math
					.toDegrees(cameraDataInitial.calculateAngle()) * 2) / 2;

			/*
			 * I have to change the sign of the vectorAngle and
			 * coorectVectorAngle to be positive.
			 */

			if (vectorAngle > 0) {
				if (correctVectorAngle > 0) {

				}// end if
				else {

					correctVectorAngle = correctVectorAngle * (-1);
				}// end else
			}// end if
			else {
				if (correctVectorAngle > 0) {
					vectorAngle = vectorAngle * (-1);
					// correctVectorAngle = correctVectorAngle * (-1);
				}// end if
				else {
					vectorAngle = vectorAngle * (-1);
					correctVectorAngle = correctVectorAngle * (-1);
				}// end else
			}// end else

			Log.e(Utilities.TAG, "angle of vector: " + vectorAngle);
			Log.e(Utilities.TAG, "correct angle of vector: "
					+ correctVectorAngle);
			Log.e(Utilities.TAG, "difference angle of vector: "
					+ (correctVectorAngle - vectorAngle));

			Log.e(Utilities.TAG, "initialBearing: " + initialBearing);

			float diff1 = (float) Math.abs(correctVectorAngle - vectorAngle);

			if (Math.abs(correctVectorAngle - vectorAngle) > Utilities.ERROR_DEGREE) {
				/*
				 * The error is more than Utilities.ERROR_DEGREE degrees. We
				 * have allowed an error of less than Utilities.ERROR_DEGREE
				 * degrees. If the error is more than Utilities.ERROR_DEGREE
				 * degrees then we have to correct the cameraData vector.
				 */

				/*
				 * This is where all the magic is being done. The camera is
				 * being rotated over here. cameraData is being used in the
				 * onDraw function in the GLU.gluLookAt function. The following
				 * code is also using the correctedVectorAngle and vectorAngle
				 * calculated earlier.
				 */

				// Log.e(Utilities.TAG, "correct now");

				if ((correctVectorAngle - vectorAngle) > 0) {
					/*
					 * correctVectorAngle is greater than vectorAngle we have to
					 * add into vectorAngle to bring the vector closer to
					 * correctVectorAngle.
					 */

					// Log.e(Utilities.TAG, "correct now+");
					cameraData.resetToIdentity();
					cameraData.rotation(2, diff1);
					cameraData.updateXYZ();
					cameraData.resetToIdentity();
				}// end if
				else if ((correctVectorAngle - vectorAngle) < 0) {
					/*
					 * correctVectorAngle is less than vectorAngle we have to
					 * subtract from vectorAngle to bring the vector closer to
					 * correctVectorAngle.
					 */

					// Log.e(Utilities.TAG, "correct now-");
					cameraData.resetToIdentity();
					cameraData.rotation(2, -diff1);
					cameraData.updateXYZ();
					cameraData.resetToIdentity();
				}// end else if
			}// end if
			else {
				/*
				 * There is no problem in the cameraData vector. No need to
				 * perform any correction.
				 */

				// Log.e(Utilities.TAG, "no problem");
				if ((bearing - oldBearing) > 0) {// left

					cameraData.resetToIdentity();
					cameraData.rotation(2, -diff);
					cameraData.updateXYZ();
					cameraData.resetToIdentity();

				}// end if
				else if ((bearing - oldBearing) < 0) {// right

					cameraData.resetToIdentity();
					cameraData.rotation(2, diff);
					cameraData.updateXYZ();
					cameraData.resetToIdentity();

				}// end else if
				else {// still

				}// end else

			}// end else

			// Log.e(Utilities.TAG, "difference diff1: " + diff1);

			/**
			 * We have to reset the cameraDataInitial to the original
			 * 0,0,0,0,0,-10 vector. This vector will then be used in the next
			 * iteration of this function to perform the auto correct
			 * calculations.
			 */
			cameraDataInitial.resetVector();

		}// end if
		else {

			diff = Math.abs(pitch - oldPitch);

			// cameraData.resetToIdentity();
			// cameraData.rotation(1, pitch);
			// cameraData.updateXYZ();
			// cameraData.resetToIdentity();

			if ((pitch - oldPitch) > 0) {// down

			}// end if
			else if ((pitch - oldPitch) < 0) {// up

			}// end else if
			else {// still

			}// end else

		}// end else

		/*
		 * The following code is displaying the text in the left top right
		 * corner of the screen for debugging purposes.
		 */

		tv.setText("bearing device: " + bearing + " oldBearing: " + oldBearing
				+ ", diffBearing: " + (int) Math.abs(bearing - oldBearing)
				+ ", pitch device: " + pitch + ", old pitch: " + oldPitch
				+ ", diffPitch: " + (int) Math.abs(pitch - oldPitch)
				+ ", accelerometer x: " + accelerometer[0]
				+ ", accelerometer y: " + accelerometer[1]
				+ ", accelerometer z: " + accelerometer[2]);

		for (int i = 0; i < CombinedTagListCache.getCombinedList().size(); i++) {
			diffBearing = Math.abs(CombinedTagListCache.getCombinedList()
					.get(i).getDirection()
					- bearing);
			if (diffBearing < 45f || diffBearing > 315f) {

				if (diffBearing <= 5) {// if the tag is within 5 degrees of
										// difference

					crossHair.setBackgroundResource(R.drawable.cross_hair_fire);

				}// end if
				else {
					crossHair.setBackgroundResource(R.drawable.cross_hair);
				}// end else

			}// end if
		}// end for

		/**
		 * I need to set the cameraData vector for the first time the browser is
		 * opened. From this point on the camera will be rotated around. I am
		 * also setting the initialBearing. This block of code allows causes the
		 * camera to point to the real world angle. The condition below should
		 * only be executed once which is at the start of the browser.
		 */

		if (counter < 1) {

			initialBearing = bearing;
			cameraData.resetToIdentity();
			cameraData.rotation(2, bearing);
			cameraData.updateXYZ();
			cameraData.resetToIdentity();

			/**
			 * The tags are rotated around the y-axis to their actual location
			 * and are shown at which they are supposed to be at. From now
			 * onwards the vector of the tag will not be rotated. Only the
			 * camera will be rotated.
			 */

			angle = 0;

			if (null != CombinedTagListCache.getCombinedList()
					&& CombinedTagListCache.getCombinedList().size() > 0) {

				for (int i = 0; i < CombinedTagListCache.getCombinedList()
						.size(); i++) {

					angle = (int) (CombinedTagListCache.getCombinedList()
							.get(i).getOpenGLModel().getHeading() - bearing);

					CombinedTagListCache.getCombinedList().get(i)
							.getOpenGLModel().resetToIdentity();
					CombinedTagListCache.getCombinedList().get(i)
							.getOpenGLModel().rotation(2, -angle);
					CombinedTagListCache.getCombinedList().get(i)
							.getOpenGLModel().updateXYZ();
					CombinedTagListCache.getCombinedList().get(i)
							.getOpenGLModel().resetToIdentity();
					CombinedTagListCache.getCombinedList().get(i)
							.getOpenGLModel().setAngleDIff(angle);

				}// end for

			}// end if

			counter++;

		}// end if

	}

	void onFailure() {
		if (!mFailed) {
			mFailed = true;

		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		/**
		 * todo: implement touch functionality where the tags when touched show
		 * their data
		 */

		return false;
	}

	@Override
	public void onClick(View v) {

	}

}
