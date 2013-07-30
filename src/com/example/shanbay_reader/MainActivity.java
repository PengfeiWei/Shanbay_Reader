package com.example.shanbay_reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	private static final String TAG = "Shanbay_Reader";
	private List<String> listTitle = new ArrayList<String>();
	private NodeList nodesTitle;
	public DbManager dbHelper;
	private static final String DB_NAME = "words.db";
	private static final int DB_VERSION = 1;
	public static final String KEY_LESSON = "LESSON";
	public static final String KEY_LISTWORDS = "LISTWORDS";
	
	private SQLiteDatabase database;
	private ArrayList<Word> listWords = new ArrayList<Word>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setAdapter();
		getWords();
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/
	
	//获取单词列表
	private void getWords()
	{
		try
		{
			//将assets文件夹下的单词数据库文件words。db导入到android系统中
			dbHelper = new DbManager(this, DB_NAME, null, DB_VERSION);
	        dbHelper.openDataBase();
	        dbHelper.closeDataBase();
	        
	        //读出数据并添加到listWords中
	        database = SQLiteDatabase.openOrCreateDatabase(DbManager.getDbPath() + DbManager.getDbName(), null);
	        
	        Cursor cur = database.rawQuery("SELECT word, level FROM words", null);
	        if(null != cur)
	        {
	        	if(cur.moveToFirst())
	        	{
	        		do
	        		{
	        			String strWord = cur.getString(cur.getColumnIndex("word"));
	        			int level = cur.getInt(cur.getColumnIndex("level"));
	        		    Word word = new Word();
	        			word.setWord(strWord);
	        			word.setLevel(level);
	        			
	        			this.listWords.add(word);
	        		}while(cur.moveToNext());
	        	}
	        }
	        
	        database.close();
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
	}
	
	//获取文章标题的节点列表
	private void getNodesTitle()
	{
		try
		{
			AssetManager am = null;
			am = getAssets();
			InputStream is = am.open("title.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder = factory.newDocumentBuilder();      
	        Document document = builder.parse(is);             
	        Element root = document.getDocumentElement();         
	        nodesTitle = root.getElementsByTagName("string");
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
	}
	
	//获取所有文章标题
	private void getTitles()
	{
		try
		{
			getNodesTitle();
	        for (int i = 0; i < nodesTitle.getLength(); i++)
	        {
	        	Node node = nodesTitle.item(i);
	        	String item = node.getFirstChild().getNodeValue();
	        	listTitle.add(item);
	        }
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
	}
	
	private void setAdapter()
	{
		getTitles();
		setListAdapter(new ArrayAdapter<String>(this,
						                        android.R.layout.simple_list_item_1,
						                        listTitle));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		//实际存储中position 1,2,3...n就对应着lesson1,lesson2,lesson3...lessonn， 为了易维护，还是对文章题目进行匹配
		String title = l.getItemAtPosition(position).toString();
		String titleLesson = new String();
		for(int i = 0; i < nodesTitle.getLength(); i++)
		{
			if(nodesTitle.item(i).getFirstChild().getNodeValue().equals(title))
			{
				titleLesson = nodesTitle.item(i).getAttributes().getNamedItem("name").getNodeValue();
			}
		}
		
		Bundle bundle = new Bundle();
		bundle.putString(KEY_LESSON, titleLesson);
		bundle.putParcelableArrayList(KEY_LISTWORDS, listWords);
		
		Intent intent = new Intent();
		intent.setClass(this, TextActivity.class);
		intent.putExtras(bundle);
		
		startActivity(intent);
	}
	

}
