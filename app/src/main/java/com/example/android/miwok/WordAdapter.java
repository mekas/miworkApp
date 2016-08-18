package com.example.android.miwok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by pustikom on 18/08/16.
 */

public class WordAdapter extends ArrayAdapter<Word> {
    public WordAdapter(Context context, ArrayList<Word> wordList) {
        super(context, 0, wordList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Word currentWord = getItem(position);
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.mDefaultText);
        defaultTextView.setText(currentWord.getmDefaultTranslation());
        TextView miwokTextView =(TextView) listItemView.findViewById(R.id.mMiwokText);
        miwokTextView.setText(currentWord.getmMiwokTranslation());

        return listItemView;

    }
}
