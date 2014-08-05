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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tagaugmentedreality.cache.CombinedTagListCache;
import com.tagaugmentedreality.utilties.LocationRetriever;
import com.tagaugmentedreality.utilties.Utilities;

public class Lists extends ActionBarActivity {

	private Context context;
	private static final int NUM_ITEMS = 1;
	private MyAdapter mAdapter;
	private ViewPager mPager;
	public static LocationRetriever location;
	private ActionBar actionBar;
	private static MenuItem progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fragment_manager);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("");
		context = this;

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between pages, select the
				// corresponding tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction arg1) {

				mPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}
		};

		Tab tab = actionBar.newTab().setText(R.string.nearByTags)
				.setTabListener(tabListener);
		// .setTabListener(new TabListener<BrandScreen>(this, "Brand",
		// BrandScreen.class));
		actionBar.addTab(tab);

		/**
		 * LocationRetriever is a singleton
		 */
		location = LocationRetriever.getLocationRetriever(context);
		location.connectToClient();

		// https://github.com/nostra13/Android-Universal-Image-Loader
		ImageLoaderConfiguration config = Utilities.configImageLoader(context);// new
																				// ImageLoaderConfiguration.Builder(context).build();
		ImageLoader.getInstance().init(config);

		Log.e("oncreate", "oncreate");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LocationRetriever.removeListeners();
		location.disconnectFromClient();
		Log.e("onpause", "onpause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		location.connectToClient();
		Log.e("onresume", "onresume");
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e("onstop", "onstop");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_lists, menu);

		/**
		 * The progress bar is at position 1 of the menu.
		 */
		progressBar = menu.getItem(1);
		progressBar.setActionView(R.layout.progress_bar);

		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.actionRefresh:

			progressBar = item;
			progressBar.setActionView(R.layout.progress_bar);
			progressBar.expandActionView();

			location.connectToClient();
			return true;

		case R.id.actionAugmentedReallity:

			if (CombinedTagListCache.getCombinedList().size() > 0) {
				startActivity(new Intent(context, Browser.class));
				finish();
			}// end if
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			// return ArrayListFragment.newInstance(position);
			return NearByTagsList.newInstance(0);

		}

	}

	public static MenuItem getActionProgressBar() {

		return progressBar;

	}

}
