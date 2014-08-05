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



package com.tagaugmentedreality.openglobjectloader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLUtils;


/**
 * This class is an object representation of 
 * a model containing the vertex information,
 * texture coordinates, the vertex indices
 * and drawing functionality, which is called 
 * by the renderer.
 * 
 * @originalAuthor Savas Ziplies (nea/INsanityDesign)
 * @improvedAndModified  Mustafa Neguib (www.mntechsolutions.net)
 */
public abstract class OpenGLModel {

	/** The buffer holding the vertices */
	protected FloatBuffer vertexBufferBuffer;
	/** The buffer holding the texture coordinates */
	protected FloatBuffer textureBufferBuffer;
	protected ShortBuffer indexBufferBuffer;
	protected int textureImageResourceId;

	/** Our texture pointer */
	protected int[] textureId = new int[1];

	/** 
	 * The initial vertex definition
	 * 
	 * Note that each face is defined, even
	 * if indices are available, because
	 * of the texturing we want to achieve 
	 */	
	protected float vertices[];

	/** The initial texture coordinates (u, v) */	
	protected float textures[];

	protected short indices[];

	protected Bitmap textureBitmap;

	/**
	 * The Model constructor.
	 * 
	 * Initiate the buffers.
	 */
	public OpenGLModel() {


	}


	public OpenGLModel(float [] vertexBuffer, float [] textureBuffer, short [] facesBuffer, int textureImageResourceId) {


		this.vertices=vertexBuffer;
		this.textures=textureBuffer;
		this.indices=facesBuffer;
		this.textureImageResourceId=textureImageResourceId;


		ByteBuffer byteBuf = ByteBuffer.allocateDirect(this.vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		this.vertexBufferBuffer = byteBuf.asFloatBuffer();
		this.vertexBufferBuffer.put(this.vertices);
		this.vertexBufferBuffer.position(0);


		byteBuf = ByteBuffer.allocateDirect(this.textures.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		this.textureBufferBuffer = byteBuf.asFloatBuffer();
		this.textureBufferBuffer.put(this.textures);
		this.textureBufferBuffer.position(0);


		// short is 2 bytes, therefore we multiply the number if vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(this.indices.length * 3);
		ibb.order(ByteOrder.nativeOrder());
		this.indexBufferBuffer = ibb.asShortBuffer();
		this.indexBufferBuffer.put(this.indices);
		this.indexBufferBuffer.position(0);


	}


	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void draw(GL10 gl) {


		//Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);


		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);

		//Enable the vertex and texture state
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBufferBuffer);

		//gl.glDrawArrays(GL10.GL_TRIANGLES, 0, numOfVertices);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,GL10.GL_UNSIGNED_SHORT, indexBufferBuffer);

		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	/**
	 * Load the textures
	 * 
	 * @param gl - The GL Context
	 * @param context - The Activity context
	 */
	public abstract void loadGLTexture(GL10 gl, Context context,String text);
	
	public void bindTextures(GL10 gl)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);
	}
	
}
