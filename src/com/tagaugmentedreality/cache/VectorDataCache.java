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

public class VectorDataCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8980020842913178182L;
	/***********
	 * vector 0:x 1:y 2:z 3:x1 4:y1 5:z1
	 **************/
	private float[] vector;// this holds the vector data of the vector
	private float[] backUpVector;// this is for saving the vector
	private float heading;// this tells the angle at which the vector is
							// pointing towards
	private float[] matrix;// this will contain the current matrix of the matrix

	public VectorDataCache() {
		super();

		this.matrix = new float[16];

		for (int i = 0; i < 16; i++) {
			this.matrix[i] = 0.0f;
		}// end for

		this.vector = new float[6];

		for (int i = 0; i < 6; i++) {
			this.vector[i] = 0.0f;
		}// end for

		this.backUpVector = new float[6];

		for (int i = 0; i < 6; i++) {
			this.backUpVector[i] = 0.0f;
		}// end for
	}

	public VectorDataCache(float[] vector, float[] backUpVector, float heading,
			float[] matrix) {
		super();
		this.vector = vector;
		this.backUpVector = backUpVector;
		this.heading = heading;
		this.matrix = matrix;
	}

	public float[] getVector() {
		return vector;
	}

	public void setVector(float[] vector) {
		this.vector = vector;
	}

	public float[] getBackUpVector() {
		return backUpVector;
	}

	public void setBackUpVector(float[] backUpVector) {
		this.backUpVector = backUpVector;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public float[] getMatrix() {
		return matrix;
	}

	public void setMatrix(float[] matrix) {
		this.matrix = matrix;
	}

	/*
	 * Following functions are the matrix calculations which are applied on
	 * vectors.
	 */

	public void resetToIdentity() {// i am reseting the matrix to the identity
									// matrix
		this.matrix[0] = 1;
		this.matrix[1] = 0;
		this.matrix[2] = 0;
		this.matrix[3] = 0;

		this.matrix[4] = 0;
		this.matrix[5] = 1;
		this.matrix[6] = 0;
		this.matrix[7] = 0;

		this.matrix[8] = 0;
		this.matrix[9] = 0;
		this.matrix[10] = 1;
		this.matrix[11] = 0;

		this.matrix[12] = 0;
		this.matrix[13] = 0;
		this.matrix[14] = 0;
		this.matrix[15] = 1;

	}

	public void translation(float tx, float ty, float tz) {// translation is
															// performed on the
															// matrix with the
															// provided
															// parameters

		float[] matrixNew = new float[16];
		float[] translationM = new float[16];

		// float a=this.matrix[0], b=this.matrix[1], c=this.matrix[2],
		// d=this.matrix[3], e=this.matrix[4], f=this.matrix[5],
		// g=this.matrix[6], h=this.matrix[7], i=this.matrix[8],
		// j=this.matrix[9], k=this.matrix[10],l=this.matrix[11],
		// m=this.matrix[12], n=this.matrix[13], o=this.matrix[14],
		// p=this.matrix[15];

		for (int i1 = 0; i1 < 16; i1++) {
			translationM[i1] = 0.0f;
			matrixNew[i1] = 0.0f;
		}// end for

		translationM[0] = 1;
		translationM[3] = tx;
		translationM[5] = 1;
		translationM[7] = ty;
		translationM[10] = 1;
		translationM[11] = tz;
		translationM[15] = 1;

		matrixNew[0] = (translationM[0] * this.matrix[0])
				+ (translationM[1] * this.matrix[4])
				+ (translationM[2] * this.matrix[8])
				+ (translationM[3] * this.matrix[12]);
		matrixNew[1] = (translationM[0] * this.matrix[1])
				+ (translationM[1] * this.matrix[5])
				+ (translationM[2] * this.matrix[9])
				+ (translationM[3] * this.matrix[13]);
		matrixNew[2] = (translationM[0] * this.matrix[2])
				+ (translationM[1] * this.matrix[6])
				+ (translationM[2] * this.matrix[10])
				+ (translationM[3] * this.matrix[14]);
		matrixNew[3] = (translationM[0] * this.matrix[3])
				+ (translationM[1] * this.matrix[7])
				+ (translationM[2] * this.matrix[11])
				+ (translationM[3] * this.matrix[15]);

		matrixNew[4] = (translationM[4] * this.matrix[0])
				+ (translationM[5] * this.matrix[4])
				+ (translationM[6] * this.matrix[8])
				+ (translationM[7] * this.matrix[12]);
		matrixNew[5] = (translationM[4] * this.matrix[1])
				+ (translationM[5] * this.matrix[5])
				+ (translationM[6] * this.matrix[9])
				+ (translationM[7] * this.matrix[13]);
		matrixNew[6] = (translationM[4] * this.matrix[2])
				+ (translationM[5] * this.matrix[6])
				+ (translationM[6] * this.matrix[10])
				+ (translationM[7] * this.matrix[14]);
		matrixNew[7] = (translationM[4] * this.matrix[3])
				+ (translationM[5] * this.matrix[7])
				+ (translationM[6] * this.matrix[11])
				+ (translationM[7] * this.matrix[15]);

		matrixNew[8] = (translationM[8] * this.matrix[0])
				+ (translationM[9] * this.matrix[4])
				+ (translationM[10] * this.matrix[8])
				+ (translationM[11] * this.matrix[12]);
		matrixNew[9] = (translationM[8] * this.matrix[1])
				+ (translationM[9] * this.matrix[5])
				+ (translationM[10] * this.matrix[9])
				+ (translationM[11] * this.matrix[13]);
		matrixNew[10] = (translationM[8] * this.matrix[2])
				+ (translationM[9] * this.matrix[6])
				+ (translationM[10] * this.matrix[10])
				+ (translationM[11] * this.matrix[14]);
		matrixNew[11] = (translationM[8] * this.matrix[3])
				+ (translationM[9] * this.matrix[7])
				+ (translationM[10] * this.matrix[11])
				+ (translationM[11] * this.matrix[15]);

		matrixNew[12] = (translationM[12] * this.matrix[0])
				+ (translationM[13] * this.matrix[4])
				+ (translationM[14] * this.matrix[8])
				+ (translationM[15] * this.matrix[12]);
		matrixNew[13] = (translationM[12] * this.matrix[1])
				+ (translationM[13] * this.matrix[5])
				+ (translationM[14] * this.matrix[9])
				+ (translationM[15] * this.matrix[13]);
		matrixNew[14] = (translationM[12] * this.matrix[2])
				+ (translationM[13] * this.matrix[6])
				+ (translationM[14] * this.matrix[10])
				+ (translationM[15] * this.matrix[14]);
		matrixNew[15] = (translationM[12] * this.matrix[3])
				+ (translationM[13] * this.matrix[7])
				+ (translationM[14] * this.matrix[11])
				+ (translationM[15] * this.matrix[15]);

		for (int i2 = 0; i2 < 16; i2++) {
			this.matrix[i2] = matrixNew[i2];
		}// end for

	}

	public void scaling(float sx, float sy, float sz) {// scaling is performed
														// on the matrix with
														// the provided
														// parameters

		float[] matrixNew = new float[16];
		float[] scalingM = new float[16];

		for (int i1 = 0; i1 < 16; i1++) {
			scalingM[i1] = 0.0f;
			matrixNew[i1] = 0.0f;
		}// end for

		scalingM[0] = sx;
		scalingM[5] = sy;
		scalingM[10] = sz;
		scalingM[15] = 1;

		matrixNew[0] = (scalingM[0] * this.matrix[0])
				+ (scalingM[1] * this.matrix[4])
				+ (scalingM[2] * this.matrix[8])
				+ (scalingM[3] * this.matrix[12]);
		matrixNew[1] = (scalingM[0] * this.matrix[1])
				+ (scalingM[1] * this.matrix[5])
				+ (scalingM[2] * this.matrix[9])
				+ (scalingM[3] * this.matrix[13]);
		matrixNew[2] = (scalingM[0] * this.matrix[2])
				+ (scalingM[1] * this.matrix[6])
				+ (scalingM[2] * this.matrix[10])
				+ (scalingM[3] * this.matrix[14]);
		matrixNew[3] = (scalingM[0] * this.matrix[3])
				+ (scalingM[1] * this.matrix[7])
				+ (scalingM[2] * this.matrix[11])
				+ (scalingM[3] * this.matrix[15]);

		matrixNew[4] = (scalingM[4] * this.matrix[0])
				+ (scalingM[5] * this.matrix[4])
				+ (scalingM[6] * this.matrix[8])
				+ (scalingM[7] * this.matrix[12]);
		matrixNew[5] = (scalingM[4] * this.matrix[1])
				+ (scalingM[5] * this.matrix[5])
				+ (scalingM[6] * this.matrix[9])
				+ (scalingM[7] * this.matrix[13]);
		matrixNew[6] = (scalingM[4] * this.matrix[2])
				+ (scalingM[5] * this.matrix[6])
				+ (scalingM[6] * this.matrix[10])
				+ (scalingM[7] * this.matrix[14]);
		matrixNew[7] = (scalingM[4] * this.matrix[3])
				+ (scalingM[5] * this.matrix[7])
				+ (scalingM[6] * this.matrix[11])
				+ (scalingM[7] * this.matrix[15]);

		matrixNew[8] = (scalingM[8] * this.matrix[0])
				+ (scalingM[9] * this.matrix[4])
				+ (scalingM[10] * this.matrix[8])
				+ (scalingM[11] * this.matrix[12]);
		matrixNew[9] = (scalingM[8] * this.matrix[1])
				+ (scalingM[9] * this.matrix[5])
				+ (scalingM[10] * this.matrix[9])
				+ (scalingM[11] * this.matrix[13]);
		matrixNew[10] = (scalingM[8] * this.matrix[2])
				+ (scalingM[9] * this.matrix[6])
				+ (scalingM[10] * this.matrix[10])
				+ (scalingM[11] * this.matrix[14]);
		matrixNew[11] = (scalingM[8] * this.matrix[3])
				+ (scalingM[9] * this.matrix[7])
				+ (scalingM[10] * this.matrix[11])
				+ (scalingM[11] * this.matrix[15]);

		matrixNew[12] = (scalingM[12] * this.matrix[0])
				+ (scalingM[13] * this.matrix[4])
				+ (scalingM[14] * this.matrix[8])
				+ (scalingM[15] * this.matrix[12]);
		matrixNew[13] = (scalingM[12] * this.matrix[1])
				+ (scalingM[13] * this.matrix[5])
				+ (scalingM[14] * this.matrix[9])
				+ (scalingM[15] * this.matrix[13]);
		matrixNew[14] = (scalingM[12] * this.matrix[2])
				+ (scalingM[13] * this.matrix[6])
				+ (scalingM[14] * this.matrix[10])
				+ (scalingM[15] * this.matrix[14]);
		matrixNew[15] = (scalingM[12] * this.matrix[3])
				+ (scalingM[13] * this.matrix[7])
				+ (scalingM[14] * this.matrix[11])
				+ (scalingM[15] * this.matrix[15]);

		for (int i2 = 0; i2 < 16; i2++) {
			this.matrix[i2] = matrixNew[i2];
		}// end for

	}

	public void rotation(int type, float angle) {
		/*
		 * rotation is performed on the matrix with the provided parameters
		 */

		float angleRadians = (float) ((angle / 180.0) * Math.PI);
		float[] matrixNew = new float[16];

		float[] rotationM = new float[16];

		for (int i1 = 0; i1 < 16; i1++) {
			rotationM[i1] = 0.0f;
			matrixNew[i1] = 0.0f;
		}// end for

		switch (type) {
		// rotation about the x-axis
		case 1:

			rotationM[0] = 1;
			rotationM[15] = 1;

			if (angle == 90 || angle == 270) {
				rotationM[5] = 0.0f;
				rotationM[10] = 0.0f;
			}// end if
			else {
				rotationM[5] = (float) Math.cos(angleRadians);
				rotationM[10] = (float) Math.cos(angleRadians);
			}// end else

			if (angle == 180 || angle == 360) {
				rotationM[6] = 0.0f;
				rotationM[9] = 0.0f;
			}// end if
			else {
				rotationM[6] = ((float) Math.sin(angleRadians)) * (-1);
				rotationM[9] = (float) Math.sin(angleRadians);
			}// end else

			matrixNew[0] = (rotationM[0] * this.matrix[0])
					+ (rotationM[1] * this.matrix[4])
					+ (rotationM[2] * this.matrix[8])
					+ (rotationM[3] * this.matrix[12]);
			matrixNew[1] = (rotationM[0] * this.matrix[1])
					+ (rotationM[1] * this.matrix[5])
					+ (rotationM[2] * this.matrix[9])
					+ (rotationM[3] * this.matrix[13]);
			matrixNew[2] = (rotationM[0] * this.matrix[2])
					+ (rotationM[1] * this.matrix[6])
					+ (rotationM[2] * this.matrix[10])
					+ (rotationM[3] * this.matrix[14]);
			matrixNew[3] = (rotationM[0] * this.matrix[3])
					+ (rotationM[1] * this.matrix[7])
					+ (rotationM[2] * this.matrix[11])
					+ (rotationM[3] * this.matrix[15]);

			matrixNew[4] = (rotationM[4] * this.matrix[0])
					+ (rotationM[5] * this.matrix[4])
					+ (rotationM[6] * this.matrix[8])
					+ (rotationM[7] * this.matrix[12]);
			matrixNew[5] = (rotationM[4] * this.matrix[1])
					+ (rotationM[5] * this.matrix[5])
					+ (rotationM[6] * this.matrix[9])
					+ (rotationM[7] * this.matrix[13]);
			matrixNew[6] = (rotationM[4] * this.matrix[2])
					+ (rotationM[5] * this.matrix[6])
					+ (rotationM[6] * this.matrix[10])
					+ (rotationM[7] * this.matrix[14]);
			matrixNew[7] = (rotationM[4] * this.matrix[3])
					+ (rotationM[5] * this.matrix[7])
					+ (rotationM[6] * this.matrix[11])
					+ (rotationM[7] * this.matrix[15]);

			matrixNew[8] = (rotationM[8] * this.matrix[0])
					+ (rotationM[9] * this.matrix[4])
					+ (rotationM[10] * this.matrix[8])
					+ (rotationM[11] * this.matrix[12]);
			matrixNew[9] = (rotationM[8] * this.matrix[1])
					+ (rotationM[9] * this.matrix[5])
					+ (rotationM[10] * this.matrix[9])
					+ (rotationM[11] * this.matrix[13]);
			matrixNew[10] = (rotationM[8] * this.matrix[2])
					+ (rotationM[9] * this.matrix[6])
					+ (rotationM[10] * this.matrix[10])
					+ (rotationM[11] * this.matrix[14]);
			matrixNew[11] = (rotationM[8] * this.matrix[3])
					+ (rotationM[9] * this.matrix[7])
					+ (rotationM[10] * this.matrix[11])
					+ (rotationM[11] * this.matrix[15]);

			matrixNew[12] = (rotationM[12] * this.matrix[0])
					+ (rotationM[13] * this.matrix[4])
					+ (rotationM[14] * this.matrix[8])
					+ (rotationM[15] * this.matrix[12]);
			matrixNew[13] = (rotationM[12] * this.matrix[1])
					+ (rotationM[13] * this.matrix[5])
					+ (rotationM[14] * this.matrix[9])
					+ (rotationM[15] * this.matrix[13]);
			matrixNew[14] = (rotationM[12] * this.matrix[2])
					+ (rotationM[13] * this.matrix[6])
					+ (rotationM[14] * this.matrix[10])
					+ (rotationM[15] * this.matrix[14]);
			matrixNew[15] = (rotationM[12] * this.matrix[3])
					+ (rotationM[13] * this.matrix[7])
					+ (rotationM[14] * this.matrix[11])
					+ (rotationM[15] * this.matrix[15]);

			break;

		// rotation about the y-axis
		case 2:

			rotationM[5] = 1;
			rotationM[15] = 1;

			if (angle == 90 || angle == 270) {
				rotationM[0] = 0.0f;
				rotationM[10] = 0.0f;
			}// end if
			else {
				rotationM[0] = (float) Math.cos(angleRadians);
				rotationM[10] = (float) Math.cos(angleRadians);
			}// end else

			if (angle == 180 || angle == 360) {
				rotationM[2] = 0.0f;
				rotationM[8] = 0.0f;
			}// end if
			else {
				rotationM[2] = ((float) Math.sin(angleRadians));
				rotationM[8] = ((float) Math.sin(angleRadians)) * (-1);
			}// end else

			matrixNew[0] = (rotationM[0] * this.matrix[0])
					+ (rotationM[1] * this.matrix[4])
					+ (rotationM[2] * this.matrix[8])
					+ (rotationM[3] * this.matrix[12]);
			matrixNew[1] = (rotationM[0] * this.matrix[1])
					+ (rotationM[1] * this.matrix[5])
					+ (rotationM[2] * this.matrix[9])
					+ (rotationM[3] * this.matrix[13]);
			matrixNew[2] = (rotationM[0] * this.matrix[2])
					+ (rotationM[1] * this.matrix[6])
					+ (rotationM[2] * this.matrix[10])
					+ (rotationM[3] * this.matrix[14]);
			matrixNew[3] = (rotationM[0] * this.matrix[3])
					+ (rotationM[1] * this.matrix[7])
					+ (rotationM[2] * this.matrix[11])
					+ (rotationM[3] * this.matrix[15]);

			matrixNew[4] = (rotationM[4] * this.matrix[0])
					+ (rotationM[5] * this.matrix[4])
					+ (rotationM[6] * this.matrix[8])
					+ (rotationM[7] * this.matrix[12]);
			matrixNew[5] = (rotationM[4] * this.matrix[1])
					+ (rotationM[5] * this.matrix[5])
					+ (rotationM[6] * this.matrix[9])
					+ (rotationM[7] * this.matrix[13]);
			matrixNew[6] = (rotationM[4] * this.matrix[2])
					+ (rotationM[5] * this.matrix[6])
					+ (rotationM[6] * this.matrix[10])
					+ (rotationM[7] * this.matrix[14]);
			matrixNew[7] = (rotationM[4] * this.matrix[3])
					+ (rotationM[5] * this.matrix[7])
					+ (rotationM[6] * this.matrix[11])
					+ (rotationM[7] * this.matrix[15]);

			matrixNew[8] = (rotationM[8] * this.matrix[0])
					+ (rotationM[9] * this.matrix[4])
					+ (rotationM[10] * this.matrix[8])
					+ (rotationM[11] * this.matrix[12]);
			matrixNew[9] = (rotationM[8] * this.matrix[1])
					+ (rotationM[9] * this.matrix[5])
					+ (rotationM[10] * this.matrix[9])
					+ (rotationM[11] * this.matrix[13]);
			matrixNew[10] = (rotationM[8] * this.matrix[2])
					+ (rotationM[9] * this.matrix[6])
					+ (rotationM[10] * this.matrix[10])
					+ (rotationM[11] * this.matrix[14]);
			matrixNew[11] = (rotationM[8] * this.matrix[3])
					+ (rotationM[9] * this.matrix[7])
					+ (rotationM[10] * this.matrix[11])
					+ (rotationM[11] * this.matrix[15]);

			matrixNew[12] = (rotationM[12] * this.matrix[0])
					+ (rotationM[13] * this.matrix[4])
					+ (rotationM[14] * this.matrix[8])
					+ (rotationM[15] * this.matrix[12]);
			matrixNew[13] = (rotationM[12] * this.matrix[1])
					+ (rotationM[13] * this.matrix[5])
					+ (rotationM[14] * this.matrix[9])
					+ (rotationM[15] * this.matrix[13]);
			matrixNew[14] = (rotationM[12] * this.matrix[2])
					+ (rotationM[13] * this.matrix[6])
					+ (rotationM[14] * this.matrix[10])
					+ (rotationM[15] * this.matrix[14]);
			matrixNew[15] = (rotationM[12] * this.matrix[3])
					+ (rotationM[13] * this.matrix[7])
					+ (rotationM[14] * this.matrix[11])
					+ (rotationM[15] * this.matrix[15]);

			break;

		// rotation about the z-axis
		case 3:

			rotationM[10] = 1;
			rotationM[15] = 1;

			if (angle == 90 || angle == 270) {
				rotationM[0] = 0.0f;
				rotationM[5] = 0.0f;
			}// end if
			else {
				rotationM[0] = (float) Math.cos(angleRadians);
				rotationM[5] = (float) Math.cos(angleRadians);
			}// end else

			if (angle == 180 || angle == 360) {
				rotationM[1] = 0.0f;
				rotationM[4] = 0.0f;
			}// end if
			else {
				rotationM[1] = ((float) Math.sin(angleRadians)) * (-1);
				rotationM[4] = (float) Math.sin(angleRadians);
			}// end else

			matrixNew[0] = (rotationM[0] * this.matrix[0])
					+ (rotationM[1] * this.matrix[4])
					+ (rotationM[2] * this.matrix[8])
					+ (rotationM[3] * this.matrix[12]);
			matrixNew[1] = (rotationM[0] * this.matrix[1])
					+ (rotationM[1] * this.matrix[5])
					+ (rotationM[2] * this.matrix[9])
					+ (rotationM[3] * this.matrix[13]);
			matrixNew[2] = (rotationM[0] * this.matrix[2])
					+ (rotationM[1] * this.matrix[6])
					+ (rotationM[2] * this.matrix[10])
					+ (rotationM[3] * this.matrix[14]);
			matrixNew[3] = (rotationM[0] * this.matrix[3])
					+ (rotationM[1] * this.matrix[7])
					+ (rotationM[2] * this.matrix[11])
					+ (rotationM[3] * this.matrix[15]);

			matrixNew[4] = (rotationM[4] * this.matrix[0])
					+ (rotationM[5] * this.matrix[4])
					+ (rotationM[6] * this.matrix[8])
					+ (rotationM[7] * this.matrix[12]);
			matrixNew[5] = (rotationM[4] * this.matrix[1])
					+ (rotationM[5] * this.matrix[5])
					+ (rotationM[6] * this.matrix[9])
					+ (rotationM[7] * this.matrix[13]);
			matrixNew[6] = (rotationM[4] * this.matrix[2])
					+ (rotationM[5] * this.matrix[6])
					+ (rotationM[6] * this.matrix[10])
					+ (rotationM[7] * this.matrix[14]);
			matrixNew[7] = (rotationM[4] * this.matrix[3])
					+ (rotationM[5] * this.matrix[7])
					+ (rotationM[6] * this.matrix[11])
					+ (rotationM[7] * this.matrix[15]);

			matrixNew[8] = (rotationM[8] * this.matrix[0])
					+ (rotationM[9] * this.matrix[4])
					+ (rotationM[10] * this.matrix[8])
					+ (rotationM[11] * this.matrix[12]);
			matrixNew[9] = (rotationM[8] * this.matrix[1])
					+ (rotationM[9] * this.matrix[5])
					+ (rotationM[10] * this.matrix[9])
					+ (rotationM[11] * this.matrix[13]);
			matrixNew[10] = (rotationM[8] * this.matrix[2])
					+ (rotationM[9] * this.matrix[6])
					+ (rotationM[10] * this.matrix[10])
					+ (rotationM[11] * this.matrix[14]);
			matrixNew[11] = (rotationM[8] * this.matrix[3])
					+ (rotationM[9] * this.matrix[7])
					+ (rotationM[10] * this.matrix[11])
					+ (rotationM[11] * this.matrix[15]);

			matrixNew[12] = (rotationM[12] * this.matrix[0])
					+ (rotationM[13] * this.matrix[4])
					+ (rotationM[14] * this.matrix[8])
					+ (rotationM[15] * this.matrix[12]);
			matrixNew[13] = (rotationM[12] * this.matrix[1])
					+ (rotationM[13] * this.matrix[5])
					+ (rotationM[14] * this.matrix[9])
					+ (rotationM[15] * this.matrix[13]);
			matrixNew[14] = (rotationM[12] * this.matrix[2])
					+ (rotationM[13] * this.matrix[6])
					+ (rotationM[14] * this.matrix[10])
					+ (rotationM[15] * this.matrix[14]);
			matrixNew[15] = (rotationM[12] * this.matrix[3])
					+ (rotationM[13] * this.matrix[7])
					+ (rotationM[14] * this.matrix[11])
					+ (rotationM[15] * this.matrix[15]);

			break;

		default:
			break;

		}// end switch

		for (int i2 = 0; i2 < 16; i2++) {
			this.matrix[i2] = matrixNew[i2];
		}// end for

	}

	public void updateXYZ() {// this function will update the x, y, and z values
								// by multiplying the current matrix with the
								// existing x, y, and z values

		// float a=this.matrix[0], b=this.matrix[1], c=this.matrix[2],
		// d=this.matrix[3], e=this.matrix[4], f=this.matrix[5],
		// g=this.matrix[6], h=this.matrix[7], i=this.matrix[8],
		// j=this.matrix[9], k=this.matrix[10],l=this.matrix[11],
		// m=this.matrix[12], n=this.matrix[13], o=this.matrix[14],
		// p=this.matrix[15];

		float x = 0.0f, y = 0.0f, z = 0.0f, x1 = 0.0f, y1 = 0.0f, z1 = 0.0f;

		x = (this.matrix[0] * this.vector[0])
				+ (this.matrix[1] * this.vector[1])
				+ (this.matrix[2] * this.vector[2]) + (this.matrix[3] * 1);
		y = (this.matrix[4] * this.vector[0])
				+ (this.matrix[5] * this.vector[1])
				+ (this.matrix[6] * this.vector[2]) + (this.matrix[7] * 1);
		z = (this.matrix[8] * this.vector[0])
				+ (this.matrix[9] * this.vector[1])
				+ (this.matrix[10] * this.vector[2]) + (this.matrix[11] * 1);

		x1 = (this.matrix[0] * this.vector[3])
				+ (this.matrix[1] * this.vector[4])
				+ (this.matrix[2] * this.vector[5]) + (this.matrix[3] * 1);
		y1 = (this.matrix[4] * this.vector[3])
				+ (this.matrix[5] * this.vector[4])
				+ (this.matrix[6] * this.vector[5]) + (this.matrix[7] * 1);
		z1 = (this.matrix[8] * this.vector[3])
				+ (this.matrix[9] * this.vector[4])
				+ (this.matrix[10] * this.vector[5]) + (this.matrix[11] * 1);

		this.vector[0] = x;
		this.vector[1] = y;
		this.vector[2] = z;

		this.vector[3] = x1;
		this.vector[4] = y1;
		this.vector[5] = z1;

	}

	public void updateBackupXYZ() {// this function will update the x, y, and z
									// values by multiplying the current matrix
									// with the existing x, y, and z values

		// float a=this.matrix[0], b=this.matrix[1], c=this.matrix[2],
		// d=this.matrix[3], e=this.matrix[4], f=this.matrix[5],
		// g=this.matrix[6], h=this.matrix[7], i=this.matrix[8],
		// j=this.matrix[9], k=this.matrix[10],l=this.matrix[11],
		// m=this.matrix[12], n=this.matrix[13], o=this.matrix[14],
		// p=this.matrix[15];

		float x = 0.0f, y = 0.0f, z = 0.0f, x1 = 0.0f, y1 = 0.0f, z1 = 0.0f;

		x = (this.matrix[0] * this.vector[0])
				+ (this.matrix[1] * this.vector[1])
				+ (this.matrix[2] * this.vector[2]) + (this.matrix[3] * 1);
		y = (this.matrix[4] * this.vector[0])
				+ (this.matrix[5] * this.vector[1])
				+ (this.matrix[6] * this.vector[2]) + (this.matrix[7] * 1);
		z = (this.matrix[8] * this.vector[0])
				+ (this.matrix[9] * this.vector[1])
				+ (this.matrix[10] * this.vector[2]) + (this.matrix[11] * 1);

		x1 = (this.matrix[0] * this.vector[3])
				+ (this.matrix[1] * this.vector[4])
				+ (this.matrix[2] * this.vector[5]) + (this.matrix[3] * 1);
		y1 = (this.matrix[4] * this.vector[3])
				+ (this.matrix[5] * this.vector[4])
				+ (this.matrix[6] * this.vector[5]) + (this.matrix[7] * 1);
		z1 = (this.matrix[8] * this.vector[3])
				+ (this.matrix[9] * this.vector[4])
				+ (this.matrix[10] * this.vector[5]) + (this.matrix[11] * 1);

		this.backUpVector[0] = x;
		this.backUpVector[1] = y;
		this.backUpVector[2] = z;

		this.backUpVector[3] = x1;
		this.backUpVector[4] = y1;
		this.backUpVector[5] = z1;

	}

	public void switchVectorToBackUpVector() {

		/*
		 * this function will copy the values of the vector into the
		 * backUpVector
		 */

		this.backUpVector[0] = this.vector[0];
		this.backUpVector[1] = this.vector[1];
		this.backUpVector[2] = this.vector[2];

		this.backUpVector[3] = this.vector[3];
		this.backUpVector[4] = this.vector[4];
		this.backUpVector[5] = this.vector[5];

	}

	public void swithBackUpVectorToVector() {
		/*
		 * this function will copy the values of the backUpVector into the
		 * vector
		 */

		this.vector[0] = this.backUpVector[0];
		this.vector[1] = this.backUpVector[1];
		this.vector[2] = this.backUpVector[2];

		this.vector[3] = this.backUpVector[3];
		this.vector[4] = this.backUpVector[4];
		this.vector[5] = this.backUpVector[5];

	}

	public double calculateAngle() {

		return Math.atan(this.vector[3] / this.vector[5]);

	}

	public void resetVector() {
		//this function is being overridden by the child classes.
	}

}
