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

package com.tagaugmentedreality.utilties;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class LocationRetriever  implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, com.google.android.gms.location.LocationListener{


	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL =
			MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL =
			MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

	// Define an object that holds accuracy and frequency parameters
	private LocationClient  mLocationClient=null;
	LocationRequest mLocationRequest=null;
	private Double latitude=0.0;
	private Double longitude=0.0;
	private Context context;
	private static LocationRetriever instance=null;

	private static List<LocationRetrieverListener> listenerInstance=new ArrayList<LocationRetrieverListener>();
	private static List<String> instanceIdentifier=new ArrayList<String>();

	private LocationRetriever(LocationClient mLocationClient,
			LocationRequest mLocationRequest, Double latitude,
			Double longitude, Context context) {
		super();
		this.mLocationClient = mLocationClient;
		this.mLocationRequest = mLocationRequest;
		this.latitude = latitude;
		this.longitude = longitude;
		this.context = context;

	}


	private LocationRetriever()
	{
		super();

	}

	private LocationRetriever(Context context) {
		super();

		this.context=context;
		this.mLocationClient = new LocationClient(context,this,this);

		// Create the LocationRequest object
		this.mLocationRequest = LocationRequest.create();
		// Use high accuracy
		this.mLocationRequest.setPriority(
				LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		this.mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval to 1 second
		this.mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		this.latitude=0.0;
		this.longitude=0.0;


	}


	/**
	 * 
	 * @param context
	 * @return instance of the LocationRetriever class
	 * 
	 * I have implemented LocationRetriever as a singleton.
	 */
	public static LocationRetriever getLocationRetriever(Context context)
	{
		if(null==instance)
		{
			instance=new LocationRetriever(context);
			return instance;
		}//end if
		else
		{
			return instance;
		}//end else

	}

	public void connectToClient()
	{
		if(null!=mLocationClient)
		{
			if(!mLocationClient.isConnected())
			{
				mLocationClient.connect();
			}//end if
		}//end if

	}

	public void disconnectFromClient()
	{
		if(null!=mLocationClient)
		{
			if(mLocationClient.isConnected())
			{
				mLocationClient.removeLocationUpdates( this);
				mLocationClient.disconnect();
			}//end if
		}//end if
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


	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {


		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult((Activity) this.context,CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the
			 * user with the error.
			 */

			Log.e("error","error");
			
		}


	}

	@Override
	public void onConnected(Bundle arg0) {

		if(null!=mLocationClient)
		{
			//Toast.makeText(this.context, "Connected", Toast.LENGTH_SHORT).show();
			Location mCurrentLocation = mLocationClient.getLastLocation();
			if(null!=mCurrentLocation)
			{
				latitude=mCurrentLocation.getLatitude();
				longitude=mCurrentLocation.getLongitude();

				//Toast.makeText(this.context, latitude.toString()+" "+longitude.toString(),Toast.LENGTH_SHORT).show();
				mLocationClient.requestLocationUpdates(mLocationRequest, this);
				Log.e("last",latitude.toString()+" "+longitude.toString()+" "+mCurrentLocation.getAccuracy());
			}//end if
			else
			{
				connectToClient();
			}//end else

		}//end if
		else
		{
			mLocationClient = new LocationClient(context,this,this);
			connectToClient();

		}//end else

	}

	@Override
	public void onDisconnected() {

		Toast.makeText(this.context, "Disconnected. Please re-connect.",Toast.LENGTH_SHORT).show();

	}


	@SuppressLint("NewApi")
	@Override
	public void onLocationChanged(Location mCurrentLocation) {
		// TODO Auto-generated method stub
		latitude=mCurrentLocation.getLatitude();
		longitude=mCurrentLocation.getLongitude();

		//Toast.makeText(this.context,"updated "+latitude.toString()+" "+longitude.toString()+" "+mCurrentLocation.getAccuracy(), 1000).show();
		Log.e("updated",latitude.toString()+" "+longitude.toString()+" "+mCurrentLocation.getProvider()+" "+mCurrentLocation.getAccuracy());

		int numberOfInstaces=listenerInstance.size(),k=0;
		for(k=0;k<numberOfInstaces;k++)
		{
			if(instanceIdentifier.get(k).equalsIgnoreCase("TL"))
			{//the tags list has been downloaded, so send it to the TagsList class

				Log.e("tagAR","TL");
				listenerInstance.get(k).latitudeLongitudeReady(latitude, longitude);
			}//end if
			else
			{
				Log.e("tagAR ","not TL");
			}//end else
		}//end for

		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
		{
			new GetAddressTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}//end if
		else
		{
			new GetAddressTask().execute();
		}//end else

	}


	public class GetAddressTask extends AsyncTask<String, Void, Integer> {		

		private String response;
		private ProgressDialog dialog;
		private List<Address> myList;

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute(){
			super.onPreExecute();

			/*dialog = new ProgressDialog(MainActivity.this);
			dialog.setTitle("Logging you in");
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
			 */
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			//dialog.dismiss();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Integer doInBackground(String... params) {

			try{

				/*Geocoder myLocation = new Geocoder(MainActivity.this, Locale.getDefault());   

				myList = myLocation.getFromLocation(latitude,longitude, 1);
				 */

				response= Utilities.getHTTPResponse("http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true");
				//Log.e("reverse geocoding",response);

			}//end try
			catch(Exception e)
			{
				e.printStackTrace();
			}//end catch

			return 1;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Integer result){
			super.onPostExecute(result);

			//dialog.dismiss();
			try
			{
				/*	String country=myList.get(0).getCountryName();
				String locality=myList.get(0).getLocality();
				 */				

				Log.e("location address","retrieved");
				JSONObject object=new JSONObject(response);
				JSONArray resultData=object.getJSONArray("results");
				String status=object.getString("status");
				JSONArray addressComponents;
				if(status.equalsIgnoreCase("OK"))
				{

					int i=0;
					JSONObject dataObject;
					String type;
					JSONArray types;
					JSONObject objectAddressComponents;
					JSONArray typesAddressComponents;
					String name;

					for(i=0;i<resultData.length();i++)
					{
						dataObject=(JSONObject)resultData.get(i);
						types=dataObject.getJSONArray("types");

						for(int j=0;j<types.length();j++)
						{
							type=types.getString(j);
							if(type.equalsIgnoreCase("locality"))
							{

								addressComponents=dataObject.getJSONArray("address_components");
								for(int k=0;k<addressComponents.length();k++)
								{
									objectAddressComponents=addressComponents.getJSONObject(k);
									typesAddressComponents=objectAddressComponents.getJSONArray("types");

									for(int l=0;l<typesAddressComponents.length();l++)
									{
										name=objectAddressComponents.getString("long_name");
										if(typesAddressComponents.getString(l).equalsIgnoreCase("locality"))
										{
											//	Toast.makeText(context,"city: "+name,Toast.LENGTH_LONG).show();
										}//end if
										else if(typesAddressComponents.getString(l).equalsIgnoreCase("administrative_area_level_1"))
										{
											//	Toast.makeText(context,"province: "+name,Toast.LENGTH_LONG).show();
										}//end else if
										else if(typesAddressComponents.getString(l).equalsIgnoreCase("country"))
										{
											//  Toast.makeText(context,"country: "+name,Toast.LENGTH_LONG).show();
										}//end else if

									}//end for


								}//end for

							}//end if


						}//end for


						dataObject=null;

					}//end for

					//the data has been received, so disconnect
					disconnectFromClient();

				}//end if
				else
				{
					Toast.makeText(context,"Sorry could not perform reverse geo coding",1000).show();	

				}//end if



			}//end try
			catch(Exception e)
			{

				e.printStackTrace();
			}


		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);



		}


	}

	public interface LocationRetrieverListener{


		public void latitudeLongitudeReady(Double latitude, Double longitude); 

	}

	public static void setListener(Object object,String objectIdentifier)
	{
		if(!instanceIdentifier.contains(objectIdentifier))
		{
			listenerInstance.add((LocationRetrieverListener)object);
			instanceIdentifier.add(objectIdentifier);
		}//end if


	}


	public static void removeListeners()
	{

		listenerInstance.clear();
		instanceIdentifier.clear();
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
