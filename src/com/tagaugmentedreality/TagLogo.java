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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tagaugmentedreality.utilties.Utilities;

public class TagLogo extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 2000; // time to display the splash screen in ms
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.tag_augmented_reality_logo);

		context = this;

		if (ConnectionResult.SUCCESS == GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context)) {

			if (Utilities.networkStatus(context)) {
				if (Utilities.locationProviderStatus(context)) {

					// thread for displaying the SplashScreen
					Thread splashTread = new Thread() {
						@Override
						public void run() {
							try {
								int waited = 0;
								while (_active && (waited < _splashTime)) {
									sleep(100);
									if (_active) {
										waited += 100;
									}
								}
							} catch (InterruptedException e) {
								// do nothing
							} finally {

								startActivity(new Intent("MNLogo"));
								finish();

							}
						}
					};
					splashTread.start();
				}// end if
				else {

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					// set title
					alertDialogBuilder
							.setTitle("Location Providers Not Enabled");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Please turn on your GPS and(or) network providers.")
							.setCancelable(false)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											finish();
										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}// end else
			}// end if
			else {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set title
				alertDialogBuilder
						.setTitle("Internet Connection Not Available");

				// set dialog message
				alertDialogBuilder
						.setMessage("Please turn on your Internet network.")
						.setCancelable(false)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}// end else
		}// end if
		else if (ConnectionResult.SERVICE_MISSING == GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context)) {
			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_MISSING, (Activity) context, 100)
					.show();
		}// end else if
		else if (ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED == GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context)) {

			GooglePlayServicesUtil.getErrorDialog(
					ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
					(Activity) context, 100);

		}// end else if

	}
}
