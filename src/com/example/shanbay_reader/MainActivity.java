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
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

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
	
	//获取所有文章标题
	private void getTitles()
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
	        NodeList nodes = root.getElementsByTagName("string");
	        for (int i = 0; i < nodes.getLength(); i++)
	        {
	        	Node node = nodes.item(i);
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
	

}
