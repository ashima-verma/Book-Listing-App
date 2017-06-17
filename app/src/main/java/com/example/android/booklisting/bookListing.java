package com.example.android.booklisting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class bookListing {
    private String mBook;
    private String mAuthor;
    private String mPublisher;


    public bookListing(String book, String author, String publisher) {
        mBook = book;
        mAuthor = author;
        mPublisher = publisher;
    }

    public String getmBook() {
        return mBook;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPublisher() {
        return mPublisher;
    }
}
