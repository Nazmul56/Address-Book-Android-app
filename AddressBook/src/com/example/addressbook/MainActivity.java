package com.example.addressbook; 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem; 
import android.view.View;
import android.widget.AdapterView; 
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


public class MainActivity extends Activity { 
	String phone = new String();
	public final static String EXTRA_MESSAGE = "com.example.AddressBook.MESSAGE"; 
	private ListView obj;
	DBHelper mydb;
	//openDatabase("assets/MyDBName.db" ,SQLiteDatabase.CursorFactory factory,null);
	
	@SuppressLint("NewApi")
	@Override 
	protected void onCreate(Bundle savedInstanceState) 
	{ 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);
		//ActionBar actionBar = getActionBar();
		//actionBar.hide();
		
		mydb = new DBHelper(this);
		//DBHelper db = new DBHelper(this);
		try {
		String destPath = "/data/data/" + getPackageName() +"/databases";
		File f = new File(destPath);
		if (!f.exists()) {
		f.mkdirs();
		f.createNewFile();
		//---copy the db from the assets folder into
		// the databases folder---
		CopyDB(getBaseContext().getAssets().open("mydbname"),
		new FileOutputStream(destPath + "/MyDBName"));
		}
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		ArrayList array_list = mydb.getAllCotacts();
		ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list); 
		//adding it to the list view. 
		obj = (ListView)findViewById(R.id.listView1);
		obj.setAdapter(arrayAdapter); 
		
		
		
		obj.setOnItemClickListener(new OnItemClickListener(){ 
			
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
				//TODO Auto-generated method stub 
				int id_To_Search = arg2 + 1; 
				Bundle dataBundle = new Bundle(); 
				dataBundle.putInt("id", id_To_Search);
				
				
			
				/*Intent intent = new Intent(getApplicationContext(),com.example.addressbook.DisplayContact.class);
				intent.putExtras(dataBundle);
				startActivity(intent); 
				*/
				
				
				//makeCall(id_To_Search) ;
				

				Intent intent = new Intent(MainActivity.this,DialogActivity.class);
				
				startActivity(intent); 
				
				}
			
			
			}); 
		}
		
		
		public void CopyDB(InputStream inputStream,OutputStream outputStream) throws IOException {
				//---copy 1K bytes at a time---
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
				}
				inputStream.close();
				outputStream.close();
				}
		
		
		
		protected void makeCall(int id) 
		{
			Log.i("Make call", "");
			int Value= id;
			
			if(Value>0){
				//means this is the view part not the add contact part.
				
					Cursor rs = mydb.getData(Value);
				
				//	id_To_Update = Value; 
				
					rs.moveToFirst(); 
					
					String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
					String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
					String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL)); 
					String stree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
					String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
				
					phone = phon;
					
					if (!rs.isClosed())
					{ 
						rs.close();
					
					} 
					
			}
			
			Intent phoneIntent = new Intent(Intent.ACTION_CALL);
			phoneIntent.setData(Uri.parse("tel:"+phone));
			
			
			
			try { 
				startActivity(phoneIntent);
				finish(); 
				Log.i("Finished making a call...", "");
				} 
			catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(MainActivity.this, "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
				
			}
			}
		
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmenu, menu); 
		//return true; 
		
		
	
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    // Configure the search info and add any event listeners
	  
	    return super.onCreateOptionsMenu(menu);

		
		}
	@Override public boolean onOptionsItemSelected(MenuItem item) { 
		super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
			case R.id.item1:
				Bundle dataBundle = new Bundle();
				dataBundle.putInt("id", 0); 
				
				
				Intent intent = new Intent(getApplicationContext(),com.example.addressbook.DisplayContact.class);
				intent.putExtras(dataBundle); startActivity(intent);
				
				
				return true; 
				default: 
					return super.onOptionsItemSelected(item);
					}
		}
	public boolean onKeyDown(int keycode, KeyEvent event) { 
		if (keycode == KeyEvent.KEYCODE_BACK)
		{ moveTaskToBack(true); }
		return super.onKeyDown(keycode, event); 
		}
	
	
	
	}
	

	
	
	