package com.example.shanbay_reader;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable 
{
	private String word;
	private int level;
	
	public void setWord(String word)
	{
		this.word = word;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public static final Parcelable.Creator<Word> CREATOR = new Creator<Word>()
			{
				public Word createFromParcel(Parcel source)
				{
					Word mWord = new Word();
					mWord.word = source.readString();
					mWord.level = source.readInt();
					return mWord;
				}
				
				public Word[] newArray(int size)
				{
					return new Word[size];
				}
			};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(word);
		dest.writeInt(level);
	}

}
