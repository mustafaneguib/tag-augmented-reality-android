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

package com.tagaugmentedreality.cache;

import java.io.Serializable;

import com.tagaugmentedreality.openglobjectloader.GenerateNumberModel;
import com.tagaugmentedreality.openglobjectloader.GenerateTagModel;


public class OpenGLModelCache extends VectorDataCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -539977204197287210L;
	/***********
	 * vector 0:x 1:y 2:z 3:x1 4:y1 5:z1
	 **************/
	private int angleDIff;// this will contain the correction value to rotate by
							// to make them face forwards.
	private GenerateTagModel tagOpenGLModel;
	private GenerateNumberModel numberOpenGLModel;

	public OpenGLModelCache() {
		super();
	}

	public int getAngleDIff() {
		return angleDIff;
	}

	public void setAngleDIff(int angleDIff) {
		this.angleDIff = angleDIff;
	}

	public GenerateTagModel getTagOpenGLModel() {
		return tagOpenGLModel;
	}

	public void setTagOpenGLModel(GenerateTagModel tagOpenGLModel) {
		this.tagOpenGLModel = tagOpenGLModel;
	}

	public GenerateNumberModel getNumberOpenGLModel() {
		return numberOpenGLModel;
	}

	public void setNumberOpenGLModel(GenerateNumberModel numberOpenGLModel) {
		this.numberOpenGLModel = numberOpenGLModel;
	}

	public void resetVector() {
		float[] vector = new float[6];
		vector[0] = 0;
		vector[1] = 0;
		vector[2] = -20;
		vector[3] = 0;
		vector[4] = 0;
		vector[5] = 0;

		this.setVector(vector);
	}
	
}
