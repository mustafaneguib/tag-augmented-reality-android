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


import java.io.Serializable;

public class ModelData implements Serializable{
	
	
	private static final long serialVersionUID = -2249607888406869604L;
	
	private float [] vertexBuffer;
	private float [] textureBuffer;
	private short[] facesBuffer;
	private int textureImageResourceId;
	
	
	public ModelData(float[] vertexBuffer, float[] textureBuffer,
			short[] facesBuffer, int textureImageResourceId) {
		super();
		this.vertexBuffer = vertexBuffer;
		this.textureBuffer = textureBuffer;
		this.facesBuffer = facesBuffer;
		this.textureImageResourceId = textureImageResourceId;
	}
	public float[] getVertexBuffer() {
		return vertexBuffer;
	}
	public void setVertexBuffer(float[] vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
	}
	public float[] getTextureBuffer() {
		return textureBuffer;
	}
	public void setTextureBuffer(float[] textureBuffer) {
		this.textureBuffer = textureBuffer;
	}
	public short[] getFacesBuffer() {
		return facesBuffer;
	}
	public void setFacesBuffer(short[] facesBuffer) {
		this.facesBuffer = facesBuffer;
	}
	public int getTextureImageResourceId() {
		return textureImageResourceId;
	}
	public void setTextureImageResourceId(int textureImageResourceId) {
		this.textureImageResourceId = textureImageResourceId;
	}
	
	

}
