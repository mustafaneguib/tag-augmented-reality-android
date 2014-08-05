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

import java.util.ArrayList;

public class CombinedTagCache {

	private String id;
	private OpenGLModelCache openGLModel;
	private ArrayList<TagCache>tagList=new ArrayList<TagCache>();
	private Float direction;
	private Double latitude;
	private Double longitude;


	public CombinedTagCache() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CombinedTagCache(String id, OpenGLModelCache openGLModel, 
			Float direction, Double latitude, Double longitude
			) {
		super();
		this.id = id;
		this.openGLModel = openGLModel;
		this.direction = direction;
		this.latitude = latitude;
		this.longitude = longitude;

	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public OpenGLModelCache getOpenGLModel() {
		return openGLModel;
	}


	public void setOpenGLModel(OpenGLModelCache openGLModel) {
		this.openGLModel = openGLModel;
	}



	public ArrayList<TagCache> getTagList() {
		return tagList;
	}


	public void setTagList(ArrayList<TagCache> tagList) {
		this.tagList = tagList;
	}


	public Float getDirection()
	{
		return direction;
	}

	public void setDirection(Float direction)
	{
		this.direction = direction;
	}

	
	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public void destroyTag()
	{
		this.id = null;
		this.openGLModel = null;
		this.direction = null;
		this.tagList.clear();
		this.tagList = null;
	}

}
