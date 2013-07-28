package com.example.shanbay_reader;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class TextActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		
		findViews();
		showDetail();
	}
	
	private static final String TAG = "Shanbay_Reader";
	private TextView view_text;
	private void findViews()
	{
		view_text = (TextView)findViewById(R.id.text);
	}
	
	private void showDetail()
	{
		try
		{
			Bundle bundle = this.getIntent().getExtras();
			AssetManager am = null;
			am = getAssets();
			InputStream is = am.open(bundle.getString("LESSON") + ".txt");
			int length = is.available();
			byte[] buffer = new byte[length];
			is.read(buffer);
			view_text.setMovementMethod(new ScrollingMovementMethod());
			view_text.setText(EncodingUtils.getString(buffer, "GB2312"));
			is.close();
		}
		catch(Exception err)
		{
			Log.v(TAG, err.toString());
		}
	}
}
