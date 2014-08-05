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

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;


public class GenerateNumberModel {

	
	private ModelData model;
	private OpenGLModel modelDraw;
	
	
	public GenerateNumberModel()
	{	
		
	}
	
	public GenerateNumberModel(Context context,List<ModelData> list)
	{
		loadFromAssets(context,list);
	}
	
	public void loadFromAssets(Context context,List<ModelData> list)
	{
		  model=  list.get(1);
	      modelDraw=new NumberOpenGLModel(model.getVertexBuffer(),model.getTextureBuffer(),model.getFacesBuffer(),model.getTextureImageResourceId());
	      
	}
	
	public void draw(GL10 gl)
	{
		modelDraw.draw(gl);
		
		
	}
	
	public void loadTextures(GL10 gl, Context context,String text)
	{
		modelDraw.loadGLTexture(gl,context,text);
		
		
	}

	public void bindTextures(GL10 gl)
	{
		modelDraw.bindTextures(gl);
		
		
	}
	
}

