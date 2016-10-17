package com.example.android.bookfinder;

import android.graphics.Bitmap;

/**
 * Created by Noah on 7/31/2016.
 */
public class Book {

    private String mTitle;

    private String mAuthor;

    private Bitmap mThumbnail;

    private String mUrl;

    public Book(String title, String author){
        mTitle = title;
        mAuthor = author;
    }

    public Book(String title, String author, Bitmap thumbnail, String url){
        mTitle = title;
        mAuthor = author;
        mThumbnail = thumbnail;
        mUrl = url;
    }

    public String getTitle() { return mTitle; }

    public String getAuthor() { return mAuthor; }

    public Bitmap getThumbnail() { return mThumbnail; }

    public String getUrl() { return mUrl; }


}
