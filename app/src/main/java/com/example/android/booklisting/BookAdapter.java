package com.example.android.booklisting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Om on 20-Apr-17.
 */

public class BookAdapter extends ArrayAdapter<bookListing> {

    public BookAdapter(Activity context, List<bookListing> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        bookListing currentBook = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        name.setText(currentBook.getmBook());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentBook.getmAuthor());

        TextView publisher = (TextView) listItemView.findViewById(R.id.publisher);
        publisher.setText(currentBook.getmPublisher());

        return listItemView;
    }
}
