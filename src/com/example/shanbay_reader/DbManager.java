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
	
	//�����ݿⲻ���ڣ���assetsĿ¼�µ�words.db���ݿ��ļ����Ƶ�androidϵͳ�в���
	private SQLiteDatabase openDataBase(String dbfile)
	{
		try
		{
			if(checkDataBase())
			{
				//���ݿ��Ѵ��ڣ���������
			}
			else//���ݿⲻ���ڣ��������ݿ�
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
				
				// ����asseets�е�db�ļ���DB_PATH��
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
	
	//������ݿ��Ƿ���Ч
	//��Ч����true
	//��Ч����false
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
