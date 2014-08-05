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

public class TagCache {

	private String id;
	private OpenGLModelCache openGLModel;
	/**
	 * data concerned with the tag
	 */
	private String title;
	private String comment;
	private Double latitude;
	private Double longitude;
	private String tagImageLink;
	private String accuracy;
	private String distance;
	private Float direction;
	private String city;
	private String province;
	private String country;

	/**
	 * data concerned with the combined tags
	 */

	private boolean grouped;

	public TagCache() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TagCache(String id, OpenGLModelCache openGLModel, String title,
			String comment, Double latitude, Double longitude,
			String tagImageLink, String accuracy, String distance,
			Float direction, String city, String province, String country,
			boolean grouped) {
		super();
		this.id = id;
		this.openGLModel = openGLModel;
		this.title = title;
		this.comment = comment;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tagImageLink = tagImageLink;
		this.accuracy = accuracy;
		this.distance = distance;
		this.direction = direction;
		this.city = city;
		this.province = province;
		this.country = country;
		this.grouped = grouped;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getTagImageLink() {
		return tagImageLink;
	}

	public void setTagImageLink(String tagImageLink) {
		this.tagImageLink = tagImageLink;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public Float getDirection() {
		return direction;
	}

	public void setDirection(Float direction) {
		this.direction = direction;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isGrouped() {
		return grouped;
	}

	public void setGrouped(boolean grouped) {
		this.grouped = grouped;
	}

	public void destroyTag() {
		this.id = null;
		this.openGLModel = null;
		this.title = null;
		this.comment = null;
		this.latitude = null;
		this.longitude = null;
		this.tagImageLink = null;
		this.accuracy = null;
		this.distance = null;
		this.direction = null;
		this.city = null;
		this.province = null;
		this.country = null;
		this.grouped = false;

	}

}
