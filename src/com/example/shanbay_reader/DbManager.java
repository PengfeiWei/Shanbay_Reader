package com.example.shanbay_reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbManager extends SQLiteOpenHelper {

	private static final String TAG = "DbManager";
	private static final String DB_PATH = "/data/data/com.example.shanbay_reader/databases/";
	private static final String DB_NAME = "words.db";
	private static final int DB_VERSION = 1;
	private Context myContext;
	private SQLiteDatabase database;
	
	public DbManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		this.myContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public static String getDbPath()
	{
		return DB_PATH;
	}
	
	public static String getDbName()
	{
		return DB_NAME;
	}
	
	public void openDataBase()
	{
		this.database = openDataBase(DB_PATH + DB_NAME);
	}
	
	public void closeDataBase()
	{
		this.database.close();
	}
	
	//若数据库不存在，将assets目录下的words.db数据库文件复制到android系统中并打开
	private SQLiteDatabase openDataBase(String dbfile)
	{
		try
		{
			if(checkDataBase())
			{
				//数据库已存在，不做处理
			}
			else//数据库不存在，创建数据库
			{
				File dir = new File(DB_PATH);
				if(!dir.exists())
				{
					dir.mkdirs();
				}
				
				File dbf = new File(dbfile);
				if(dbf.exists())
				{
				    dbf.delete();
				}
				
				// 复制asseets中的db文件到DB_PATH下
				copyDataBase();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return db;
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
		return null;
	}
	
	private void copyDataBase()
	{
		try
		{
			InputStream myInput = myContext.getAssets().open(DB_NAME);
			
			String outFileName = DB_PATH + DB_NAME;
			OutputStream myOutput = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0)
			{
			    myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
	}
	
	//检查数据库是否有效
	//有效返回true
	//无效返回false
	private boolean checkDataBase()
	{
		SQLiteDatabase checkDB = null;
		String myPath = DB_PATH + DB_NAME;
		
		try
		{
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
		
		if(null != checkDB)
		{
			checkDB.close();
			return true;
		}
		else
		{
			return false;
		}
	}

}
