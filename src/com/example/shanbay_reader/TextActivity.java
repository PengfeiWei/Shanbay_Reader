package com.example.shanbay_reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TextActivity extends Activity {
	
	private static final String TAG = "Shanbay_Reader";
	private TextView view_text;
	private Button btnHighlight;
	private SeekBar seekBar;
	private String detailText;
	private ArrayList<Word> listWords = new ArrayList<Word>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		
		findViews();
		setListeners();
		
		getTextDetail();
		getWords();
		showDetail();
	}
	
	private void findViews()
	{
		view_text = (TextView)findViewById(R.id.text);
		btnHighlight = (Button)findViewById(R.id.btnHighlight);
		seekBar = (SeekBar)findViewById(R.id.seekbar);
		seekBar.setMax(6);
	}
	
	private void setListeners()
	{
		//设置按钮响应
		btnHighlight.setOnClickListener(
				new Button.OnClickListener()
				{
					public void onClick(View v)
					{
						SpannableString s = new SpannableString(detailText);
						for(int i = 0; i < listWords.size(); i++)
						{
							String regex ="\\b" + listWords.get(i).getWord() + "\\b";
							Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
							Matcher m = p.matcher(s);
							while (m.find()) 
						    {
						        int start = m.start();
						        int end = m.end();
						        s.setSpan(new BackgroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						    }
						}
						
						view_text.setText(s);
					}
				});
		
		//设置seekbar响应
		seekBar.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener()
				{

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						int barProcess = seekBar.getProgress();
						
						SpannableString s = new SpannableString(detailText);
						for(int i = 0; i < listWords.size(); i++)
						{
							if(listWords.get(i).getLevel() <= barProcess)
							{
								String regex ="\\b" + listWords.get(i).getWord() + "\\b";
								Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
								Matcher m = p.matcher(s);
								while (m.find()) 
							    {
							        int start = m.start();
							        int end = m.end();
							        s.setSpan(new BackgroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							    }
							}
						}
						
						view_text.setText(s);
					}
					
				});
	}
	
	//直接使用1,2,3.。这样的整型数字作为文章的ID或许会更好
	private void getTextDetail()
	{
		try
		{
			Bundle bundle = this.getIntent().getExtras();
			AssetManager am = null;
			am = getAssets();
			InputStream is = am.open(bundle.getString(MainActivity.KEY_LESSON) + ".txt");
			int length = is.available();
			byte[] buffer = new byte[length];
			is.read(buffer);
			detailText = EncodingUtils.getString(buffer, "GB2312");
			is.close();
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
	}
	
	private void getWords()
	{
		Bundle bundle = this.getIntent().getExtras();
		listWords = bundle.getParcelableArrayList(MainActivity.KEY_LISTWORDS);
	}
	
	private void showDetail()
	{
		try
		{
			view_text.setMovementMethod(new ScrollingMovementMethod());
			view_text.setText(detailText);
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
	}
}
