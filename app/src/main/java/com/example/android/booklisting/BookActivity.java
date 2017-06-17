package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklisting.QueryUtils.LOG_TAG;


public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<bookListing>> {


    public static String BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=20";

    private int BOOK_LOADER_ID = 1;
    private BookAdapter mAdapter;

    private TextView emptyView;

    public BookActivity() {
        BOOK_LOADER_ID = 1;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ListView bookListView = (ListView) findViewById(R.id.list);

        emptyView = (TextView) findViewById(R.id.empty_view);

        mAdapter = new BookAdapter(this, new ArrayList<bookListing>());

        bookListView.setAdapter(mAdapter);

        LoaderManager managingLoader = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            emptyView.setVisibility(View.GONE);
            managingLoader = getLoaderManager();
            managingLoader.initLoader(BOOK_LOADER_ID, null, this);
            Log.i(LOG_TAG, "Initialising the loader");
        } else {
            View spinner = findViewById(R.id.spinner);
            spinner.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
        }

        Button search = (Button) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BOOK_LOADER_ID += 1;
                EditText editBookName = (EditText) findViewById(R.id.edit_book_name);
                String bookName = editBookName.getText().toString();
                bookName = bookName.replaceAll(" ", "").toLowerCase();
                BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=" + bookName + "&maxResults=20";

                LoaderManager managingLoader = null;

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    emptyView.setVisibility(View.GONE);
                    managingLoader = getLoaderManager();
                    managingLoader.initLoader(BOOK_LOADER_ID, null, BookActivity.this);
                    Log.i(LOG_TAG, "Initialising the loader");
                } else {
                    View spinner = findViewById(R.id.spinner);
                    spinner.setVisibility(View.GONE);
                    emptyView.setText(R.string.no_internet);
                }

            }
        });
    }

    @Override
    public Loader<List<bookListing>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, BOOK_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<bookListing>> loader, List<bookListing> data) {

        emptyView.setText(R.string.no_book);

        mAdapter.clear();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spinner);
        progressBar.setVisibility(View.GONE);

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

        Log.i(LOG_TAG, "Loader Finished");
    }


    @Override
    public void onLoaderReset(Loader<List<bookListing>> loader) {
        mAdapter.clear();
        Log.i(LOG_TAG, "Loader reset");
    }

}
