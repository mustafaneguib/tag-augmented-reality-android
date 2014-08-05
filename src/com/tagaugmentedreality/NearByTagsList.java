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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.tagaugmentedreality.cache.CombinedTagCache;
import com.tagaugmentedreality.cache.CombinedTagListCache;
import com.tagaugmentedreality.cache.OpenGLModelCache;
import com.tagaugmentedreality.cache.TagCache;
import com.tagaugmentedreality.cache.TagListCache;
import com.tagaugmentedreality.utilties.LocationRetriever;
import com.tagaugmentedreality.utilties.LocationRetriever.LocationRetrieverListener;
import com.tagaugmentedreality.utilties.Utilities;

public class NearByTagsList extends Fragment implements
		LocationRetrieverListener {

	private String email, name, username, id, accessToken;
	private ListView tagListView;
	private View view;
	private DisplayImageOptions options;
	private StableArrayAdapter adapter;
	private ArrayList<String> temporaryList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.tags_list, container, false);
		tagListView = (ListView) view.findViewById(R.id.tagListView);
		tagListView.setDivider(null);

		LocationRetriever.setListener(this, "TL");

		options = Utilities.getOptions();

		temporaryList = new ArrayList<String>();
		adapter = new StableArrayAdapter(getActivity(), temporaryList);
		tagListView.setAdapter(adapter);

		/**
		 * I have to return the view that i had retrieved earlier. For some
		 * reason just returning the inlfater.inflate will not update the UI. I
		 * will have to return the view in which i am updating.
		 */
		// Inflate the layout for this fragment
		return view;// inflater.inflate(R.layout.tagslist, container, false);
	}

	static NearByTagsList newInstance(int num) {
		NearByTagsList f = new NearByTagsList();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	/**
	 * http://stackoverflow.com/questions/10024739/how-to-determine-when-
	 * fragment-becomes-visible-in-viewpager I have overridden the function
	 * setUserVisibleHint to know when the fragment is shown to the user when
	 * the viewpager is swiped.
	 */

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {

		}// end if
		else {

		}// end else

	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		private Context context;

		private class ViewHolder {
			public TextView title;
			public TextView comment;
			public TextView distance;
			public TextView accuracy;
			public TextView bearing;
			public ImageView image;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final ViewHolder holder;

			View rowView = convertView;

			if (null == convertView) {
				holder = new ViewHolder();
				rowView = inflater.inflate(R.layout.tag_item, parent, false);

				holder.title = (TextView) rowView.findViewById(R.id.title);
				holder.comment = (TextView) rowView.findViewById(R.id.comment);
				holder.distance = (TextView) rowView
						.findViewById(R.id.distance);
				holder.accuracy = (TextView) rowView
						.findViewById(R.id.accuracy);
				holder.bearing = (TextView) rowView.findViewById(R.id.bearing);
				holder.image = (ImageView) rowView.findViewById(R.id.pic);
				rowView.setTag(holder);
			}// end if
			else {
				holder = (ViewHolder) convertView.getTag();
			}// end else

			if (null != TagListCache.getTagsList()
					&& TagListCache.getTagsList().size() > 0) {

				holder.title.setText(TagListCache.getTagsList().get(position)
						.getTitle());
				holder.comment.setText(TagListCache.getTagsList().get(position)
						.getComment());

				holder.distance.setText("Distance from current location: "
						+ new BigDecimal(Float.parseFloat(TagListCache
								.getTagsList().get(position).getDistance()))
								.setScale(2, BigDecimal.ROUND_HALF_UP)
								.toString() + " m away");
				holder.accuracy.setText("Accuracy: "
						+ new BigDecimal(Float.parseFloat(TagListCache
								.getTagsList().get(position).getAccuracy()))
								.setScale(2, BigDecimal.ROUND_HALF_UP)
								.toString() + " m");

				holder.bearing.setText("Bearing of tag: "+TagListCache.getTagsList().get(position)
						.getDirection()
						+ " degrees");

				final int index = position;

				final Bitmap noimage = BitmapFactory.decodeResource(
						getResources(), R.drawable.no_image);
				final ProgressBar progressBar = (ProgressBar) rowView
						.findViewById(R.id.progressBar);

				if (!TagListCache.getTagsList().get(position).getTagImageLink()
						.trim().equalsIgnoreCase("")) {

					ImageLoader.getInstance().displayImage(
							TagListCache.getTagsList().get(position)
									.getTagImageLink(), holder.image, options,
							new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri,
										View view) {
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
									progressBar.setVisibility(View.GONE);
									// view.setVisibility(View.VISIBLE);
									// ((ImageView)view).setImageBitmap(noimage);
								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {

									progressBar.setVisibility(View.GONE);
									view.setVisibility(View.VISIBLE);
									((ImageView) view)
											.setImageBitmap(loadedImage);
								}

								@Override
								public void onLoadingCancelled(String imageUri,
										View view) {
									progressBar.setVisibility(View.GONE);
									// view.setVisibility(View.VISIBLE);
									// ((ImageView)view).setImageBitmap(noimage);
								}
							});

				}// end if
				else {
					progressBar.setVisibility(View.GONE);
					holder.image.setVisibility(View.GONE);

				}// end else

			}// end if

			return rowView;

		}

		public StableArrayAdapter(Context context, List<String> objects) {
			super(context, R.layout.tag_item, objects);

			this.context = context;

		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	@Override
	public void latitudeLongitudeReady(Double latitude, Double longitude) {

		Utilities.LATITUDE = latitude;
		Utilities.LONGITUDE = longitude;

		Log.e("location", latitude.toString() + ", " + longitude.toString());

		Lists.location.disconnectFromClient();

		/*
		 * You can use an asynctask to get the data from your data source. For
		 * demonstration services i am using hard coded data.
		 */

		prepareTagsList(latitude.toString(), longitude.toString());

	}

	public void prepareTagsList(String latitude, String longitude) {
		tagListView = (ListView) view.findViewById(R.id.tagListView);
		// tagListView.setVisibility(View.GONE);

		/*
		 * I am building the tags list
		 */

		for (int i = 0; i < TagListCache.getTagsList().size(); i++) {
			TagListCache.getTagsList().get(i).destroyTag();
			TagListCache.getTagsList().remove(i);
		}// end for

		TagListCache.getTagsList().clear();

		TagCache tag;
		OpenGLModelCache openGLModel;

		Location tagLocation = new Location(" ");
		Location deviceLocation = new Location(" ");
		deviceLocation.setLatitude(Utilities.LATITUDE);
		deviceLocation.setLongitude(Utilities.LONGITUDE);
		Float bearing = 0.0f;
		Float distance = 0.0f;
		BigDecimal bearingRounded;
		/*
		 * TagCache(String id, OpenGLModelCache openGLModel, String title,
		 * String comment, Double latitude, Double longitude, String
		 * tagImageLink, String accuracy, String distance, Float direction,
		 * String firstName, String lastName, String userFacebookId, String
		 * city, String province, String country, boolean grouped )
		 */

		// tag1

		tag = new TagCache("1", null, "Tag1", "Tag1 is here.",
				Double.parseDouble(latitude), Double.parseDouble(longitude),
				"", "10", "10", 0.0f, "Lahore", "Punjab", "Pakistan", false);

		openGLModel = new OpenGLModelCache();
		openGLModel.resetVector();

		/**
		 * I am getting the bearing of the tag with the device right here, by
		 * using the latitude and longitude of both the tag and the device. Then
		 * i am saving the value as the tag's heading.
		 */
		tagLocation.setLatitude(Double.parseDouble("31.459379"));
		tagLocation.setLongitude(Double.parseDouble("74.368984"));
		distance = deviceLocation.distanceTo(tagLocation);
		bearing = deviceLocation.bearingTo(tagLocation);

		if (bearing < 0) {
			bearing = (-1) * bearing;
			bearing = 180 + (180 - bearing);
		}// end if

		bearingRounded = new BigDecimal(bearing).setScale(0,
				BigDecimal.ROUND_HALF_UP);
		bearing = Float.parseFloat(bearingRounded.toString());

		Log.e(Utilities.TAG, "bearing: " + bearing.toString());

		openGLModel.setHeading(bearing);// dataObject.getString("direction")));
		tag.setDirection(bearing);
		tag.setOpenGLModel(openGLModel);
		tag.setDistance(distance.toString());
		TagListCache.getTagsList().add(tag);

		// tag2

		tag = new TagCache("2", null, "Tag2", "Tag2 is here.",
				Double.parseDouble(latitude), Double.parseDouble(longitude),
				"", "10", "10", 0.0f, "Lahore", "Punjab", "Pakistan", false);

		openGLModel = new OpenGLModelCache();
		openGLModel.resetVector();

		/**
		 * I am getting the bearing of the tag with the device right here, by
		 * using the latitude and longitude of both the tag and the device. Then
		 * i am saving the value as the tag's heading.
		 */
		tagLocation.setLatitude(Double.parseDouble("31.459379"));
		tagLocation.setLongitude(Double.parseDouble("74.368984"));
		distance = deviceLocation.distanceTo(tagLocation);
		bearing = deviceLocation.bearingTo(tagLocation);

		if (bearing < 0) {
			bearing = (-1) * bearing;
			bearing = 180 + (180 - bearing);
		}// end if

		bearingRounded = new BigDecimal(bearing).setScale(0,
				BigDecimal.ROUND_HALF_UP);
		bearing = Float.parseFloat(bearingRounded.toString());

		Log.e(Utilities.TAG, "bearing: " + bearing.toString());

		openGLModel.setHeading(bearing);// dataObject.getString("direction")));
		tag.setDirection(bearing);
		tag.setOpenGLModel(openGLModel);
		tag.setDistance(distance.toString());
		TagListCache.getTagsList().add(tag);

		// tag3

		tag = new TagCache("3", null, "Tag3", "Tag3 is here.",
				Double.parseDouble(latitude), Double.parseDouble(longitude),
				"", "10", "10", 0.0f, "Lahore", "Punjab", "Pakistan", false);

		openGLModel = new OpenGLModelCache();
		openGLModel.resetVector();

		/**
		 * I am getting the bearing of the tag with the device right here, by
		 * using the latitude and longitude of both the tag and the device. Then
		 * i am saving the value as the tag's heading.
		 */
		tagLocation.setLatitude(Double.parseDouble("31.458804"));
		tagLocation.setLongitude(Double.parseDouble("74.368874"));
		distance = deviceLocation.distanceTo(tagLocation);
		bearing = deviceLocation.bearingTo(tagLocation);

		if (bearing < 0) {
			bearing = (-1) * bearing;
			bearing = 180 + (180 - bearing);
		}// end if

		bearingRounded = new BigDecimal(bearing).setScale(0,
				BigDecimal.ROUND_HALF_UP);
		bearing = Float.parseFloat(bearingRounded.toString());

		Log.e(Utilities.TAG, "bearing: " + bearing.toString());

		openGLModel.setHeading(bearing);// dataObject.getString("direction")));
		tag.setDirection(bearing);
		tag.setOpenGLModel(openGLModel);
		tag.setDistance(distance.toString());
		TagListCache.getTagsList().add(tag);

		// tag4

		tag = new TagCache("4", null, "Tag4", "Tag4 is here.",
				Double.parseDouble(latitude), Double.parseDouble(longitude),
				"", "10", "10", 0.0f, "Lahore", "Punjab", "Pakistan", false);

		openGLModel = new OpenGLModelCache();
		openGLModel.resetVector();

		/**
		 * I am getting the bearing of the tag with the device right here, by
		 * using the latitude and longitude of both the tag and the device. Then
		 * i am saving the value as the tag's heading.
		 */
		tagLocation.setLatitude(Double.parseDouble("31.458651"));
		tagLocation.setLongitude(Double.parseDouble("74.369171"));
		distance = deviceLocation.distanceTo(tagLocation);
		bearing = deviceLocation.bearingTo(tagLocation);

		if (bearing < 0) {
			bearing = (-1) * bearing;
			bearing = 180 + (180 - bearing);
		}// end if

		bearingRounded = new BigDecimal(bearing).setScale(0,
				BigDecimal.ROUND_HALF_UP);
		bearing = Float.parseFloat(bearingRounded.toString());

		Log.e(Utilities.TAG, "bearing: " + bearing.toString());

		openGLModel.setHeading(bearing);// dataObject.getString("direction")));
		tag.setDirection(bearing);
		tag.setOpenGLModel(openGLModel);
		tag.setDistance(distance.toString());
		TagListCache.getTagsList().add(tag);

		/*
		 * I am building the combined tags list to be used by the Browser
		 */

		temporaryList.clear();
		CombinedTagListCache.getCombinedList().clear();
		CombinedTagCache combinedTag;
		int counter = 1;

		for (int i = 0; i < TagListCache.getTagsList().size(); ++i) {
			temporaryList.add(TagListCache.getTagsList().get(i).getTitle());

			/**
			 * I am creating the combined tags list on the basis that each tag
			 * in the group is separated (direction of each tag is away from
			 * each other by the value given by
			 * Utilities.combinedDirectionCriteria degrees) by
			 * Utilities.combinedDirectionCriteria degrees. I am grouping
			 * together tags because there is limited real estate in the
			 * Augmented Reality View Screen.
			 */
			combinedTag = new CombinedTagCache();

			if (!TagListCache.getTagsList().get(i).isGrouped()) {
				TagListCache.getTagsList().get(i).setGrouped(true);
				combinedTag.setId(((Integer) counter).toString());
				combinedTag.getTagList().add(TagListCache.getTagsList().get(i));
				combinedTag.setOpenGLModel(TagListCache.getTagsList().get(i)
						.getOpenGLModel());
				combinedTag.setDirection(TagListCache.getTagsList().get(i)
						.getDirection());
				combinedTag.setLatitude(TagListCache.getTagsList().get(i)
						.getLatitude());
				combinedTag.setLongitude(TagListCache.getTagsList().get(i)
						.getLongitude());

				CombinedTagListCache.getCombinedList().add(combinedTag);

				for (int j = 0; j < TagListCache.getTagsList().size(); j++) {
					if (!TagListCache.getTagsList().get(j).isGrouped()) {
						if (Math.abs(TagListCache.getTagsList().get(i)
								.getDirection()
								- TagListCache.getTagsList().get(j)
										.getDirection()) < Utilities.COMBINED_DIRECTION_CRITERIA) {
							combinedTag.getTagList().add(
									TagListCache.getTagsList().get(j));
							TagListCache.getTagsList().get(j).setGrouped(true);
						}// end if
					}// end if
				}// end for

				counter++;
			}// end if

		}// end for

		Log.e("combined tags size: ", ""
				+ CombinedTagListCache.getCombinedList().size());

		for (int i = 0; i < CombinedTagListCache.getCombinedList().size(); i++) {
			Log.e("combined: ", ""
					+ CombinedTagListCache.getCombinedList().get(i).getId());

			for (int j = 0; j < CombinedTagListCache.getCombinedList().get(i)
					.getTagList().size(); j++) {

				Log.e("combined tag: ", ""
						+ CombinedTagListCache.getCombinedList().get(i)
								.getTagList().get(j).getDirection());

			}// end for

		}// end for

		adapter.notifyDataSetChanged();

		// tagListView.setVisibility(View.VISIBLE);

		if (null != Lists.getActionProgressBar()) {
			Lists.getActionProgressBar().collapseActionView();
			Lists.getActionProgressBar().setActionView(null);

		}// end if

	}

}
