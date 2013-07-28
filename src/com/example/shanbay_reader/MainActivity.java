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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setAdapter();
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/

	private static final String TAG = "Shanbay_Reader";
	private List<String> listTitle = new ArrayList<String>();
	private NodeList nodesTitle;
	
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
		bundle.putString("LESSON", titleLesson);
		
		Intent intent = new Intent();
		intent.setClass(this, TextActivity.class);
		intent.putExtras(bundle);
		
		startActivity(intent);
	}
	

}
