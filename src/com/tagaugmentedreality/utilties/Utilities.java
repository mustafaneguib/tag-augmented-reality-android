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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class Utilities {

	/*
	 * All of your constants and links to web services apis should be declared
	 * here.
	 */
	public static final String TAG = "tagAugmentedReality";
	/* This will point to the shared preferences of tagAugmentedReality */
	public static final String PREFS_NAME = "tagAugmentedRealityPrefs";
	public static final String DEVICE_OS = "android";
	public static final String SDK_VERSION = "0.0";
	/*
	 * this is the bearing difference two tags which will determine if they are
	 * to be grouped into one tag for the Browser
	 */
	public static final int COMBINED_DIRECTION_CRITERIA = 10;
	public static Double LATITUDE = 0.0;
	public static Double LONGITUDE = 0.0;
	/*
	 * if ALPHA = 1 OR 0, no filter applies.
	 */
	public static final float ALPHA = 0.05f;
	/*
	 * This is the amount of error that is allowed between the angle of the
	 * actual vector and the vector that should be at that instant. See
	 * com.tagaugmentedreality.Browser.java for more information.
	 */
	public static final int ERROR_DEGREE = 5;

	/*
	 * This function checks if the Internet network is available.
	 */
	public static boolean networkStatus(Context context) {

		/**
		 * 
		 * check for internet connection. if the internet connection is not
		 * enabled then ask the user to enable it.
		 * http://stackoverflow.com/questions
		 * /4238921/android-detect-whether-there
		 * -is-an-internet-connection-available
		 * 
		 */

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkStatus = connectivityManager.getActiveNetworkInfo();

		if (null != networkStatus) {
			return true;
		}// end if
		else {
			return false;
		}// end else

	}

	/*
	 * This function checks if the location provider, gps and network provider
	 * are available. If they are not the user should be alerted.
	 */
	public static boolean locationProviderStatus(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			return true;
		}// end if
		else if (locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}// end if
		else if (locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			return true;
		}// end else if
		else {
			return false;
		}// end else
	}

	/*
	 * This function is to set up the configurations of the image loader that we
	 * are using. We are using Universal Image Loader by Nostra13 available on
	 * github at https://github.com/nostra13/Android-Universal-Image-Loader
	 * 
	 * This particular sdk of tag augmented reality is using version 1.8.6
	 */
	@SuppressLint("NewApi")
	public static ImageLoaderConfiguration configImageLoader(Context context) {

		ImageLoaderConfiguration config;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			config = new ImageLoaderConfiguration.Builder(context)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
					.threadPoolSize(3)
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		}// end if
		else {

			config = new ImageLoaderConfiguration.Builder(context)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.threadPoolSize(3)
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		}// end else
		return config;
	}

	/*
	 * This function gets the options of the image loader. This function is used
	 * when we want to load the image.
	 */
	public static DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();

		return options;
	}

	public static boolean emailFormatChecker(String email) {
		/**
		 * 
		 * I am checking for the format of the email address source:
		 * http://www.mkyong
		 * .com/regular-expressions/how-to-validate-email-address
		 * -with-regular-expression/
		 * 
		 */
		Pattern p = Pattern
				.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static void setSharedPreferencesString(Context context, String key,
			String value) {

		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value).commit();

	}

	public static void setSharedPreferencesInteger(Context context, String key,
			Integer value) {

		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value).commit();

	}

	public static void setSharedPreferencesFloat(Context context, String key,
			Float value) {

		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value).commit();

	}

	public static void setSharedPreferencesBoolean(Context context, String key,
			Boolean value) {

		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value).commit();

	}

	public static void setSharedPreferencesLong(Context context, String key,
			Long value) {

		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value).commit();

	}

	public static Object getSharedPreferences(Context context, String key,
			char type) {
		/**
		 * s: String i: Integer b: Boolean l: Long f: Float
		 */

		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);

		if ('s' == type) {
			return (Object) settings.getString(key, "");
		}// end if
		else if ('i' == type) {
			return (Object) settings.getInt(key, 0);

		}// end else if
		else if ('b' == type) {
			return (Object) settings.getBoolean(key, false);
		}// end else if
		else if ('l' == type) {
			return (Object) settings.getLong(key, 0);

		}// end else if
		else if ('f' == type) {
			return (Object) settings.getFloat(key, 0.0f);

		}// end else if
		else {
			return null;
		}// end else

	}

	public static void removePreferences(Context context) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		settings.edit().clear().commit();

	}

	public static void showToast(final Context context, final String message) {

		Handler handler = new Handler();

		/**
		 * 
		 * source for showing Toast notification to the ui thread from a thread.
		 * http://www.codeproject.com/Articles/109735/Toast-A-User-Notification
		 * 
		 * I need to pass a handler which can send messages to the ui thread.
		 * 
		 */

		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/*
	 * This function is for sending GET HTTP requests
	 */
	public static String getHTTPResponse(String path) {

		String response = null;
		try {
			// source for http 1.1
			// http://stackoverflow.com/questions/3046424/http-post-requests-using-httpclient-take-2-seconds-why
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			HttpClient client = new DefaultHttpClient(params);
			HttpGet get = new HttpGet(path);
			HttpResponse responseGet = client.execute(get);
			HttpEntity resEntity = responseGet.getEntity();
			response = EntityUtils.toString(resEntity, "utf-8").trim();
			return response;
		}// end try
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}// end catch

	}

	/*
	 * This function is for sending POST HTTP requests
	 */

	public static String postHTTP(String path, ArrayList<String> parameters,
			ArrayList<String> values) {

		String response = null;
		try {
			// source for http 1.1
			// http://stackoverflow.com/questions/3046424/http-post-requests-using-httpclient-take-2-seconds-why
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			HttpClient httpclient = new DefaultHttpClient(params);
			HttpPost httpPost = new HttpPost(path);
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(
					parameters.size());

			int i = 0;
			for (i = 0; i < parameters.size(); i++) {
				nameValuePairs.add(new BasicNameValuePair(parameters.get(i),
						values.get(i)));
			}// end for

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpclient.execute(httpPost);
			// Execute HTTP Post Request
			HttpEntity resEntity = httpResponse.getEntity();
			response = EntityUtils.toString(resEntity, "utf-8").trim();
			Log.e("error", response);
			return response;

		}// end try
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}// end catch

	}

}
