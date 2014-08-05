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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.tagaugmentedreality.R;


public class LoadModelsFromAssets {

	private List<ModelData> list;

	public LoadModelsFromAssets()
	{	

	}

	
	public int loadFromAssets(Context context,String fileName)
	{

		try {

			FileOutputStream fos;

			list=new ArrayList<ModelData>();
			/**
			 * model for the tag
			 * at location 0
			 */
			list.add(LoadFromAssets.loadFromAsset("tag.obj", R.drawable.taglogoback, context.getAssets()));
			/**
			 * model for the number
			 * at location 1
			 */
			list.add(LoadFromAssets.loadFromAsset("number.obj", R.drawable.number, context.getAssets()));
				
			
			/**
			 * I am writing the list of objects to the internal memory.
			 * The main idea of doing this is to read the coordinates of the 
			 * models from the wave front object files only once during the 
			 * lifetime of the application (installation till uninstallation)
			 * and then to save the models into the internal memory. Reading the
			 * wave front object files is a time consuming task, so we are only
			 * performing that task once. Then for subsequent executions the 
			 * data is read from the internal memory. This improves the performance
			 * and flow of the application. 
			 */
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			byte[] buffer;
			int length=0;

			ObjectOutput out = new ObjectOutputStream(bos); 
			out.writeObject(list); 
			out.close(); 
			// Get the bytes of the serialized object 
			buffer  = bos.toByteArray(); 
			fos = context.openFileOutput(fileName,context.MODE_PRIVATE);
			fos.write(buffer);

			length=buffer.length;
			return length;


		}//end try
		catch(IOException ioe) { 
			Log.e("serializeObject", "error", ioe); 
			return -1;
		}//end catch


	}


	@SuppressWarnings("unchecked")
	public List<ModelData> loadFromInternalMemory(Context context, int length, String fileName)
	{
		try {
		byte [] buffer=new byte[length];

		FileInputStream fis;
		
			fis = context.openFileInput(fileName);
		

		fis.read(buffer);

		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer)); 
		Object object = in.readObject(); 
		in.close(); 

		List<ModelData> list=new ArrayList<ModelData>();

		list=(List<ModelData>)object;

		return list;
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("null")
	public int getLengthOfDataInBytes(Context context,String fileName)
	{
		
		try {
		StringBuffer buffer= new StringBuffer();
		FileInputStream fis = context.openFileInput(fileName);
    	InputStreamReader isr = new InputStreamReader(fis);
    	Reader in = new BufferedReader(isr);
    	
    	int ch;
    	
    	while ((ch = in.read()) > -1) {
    		buffer.append((char)ch);
    	}
    	
			in.close();
			
			return Integer.parseInt(buffer.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

		
	}
	
}
