package com.example.android.miwok;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by pustikom on 18/08/16.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColorResourceId;
    private MediaPlayer mp;

    public WordAdapter(Context context, ArrayList<Word> wordList) {
        super(context, 0, wordList);
    }

    public WordAdapter(Context context, ArrayList<Word> wordList, int mColorResourceId) {
        super(context, 0, wordList);
        this.mColorResourceId=mColorResourceId;
    }

    public WordAdapter(Context context, ArrayList<Word> wordList, int mColorResourceId, MediaPlayer mp) {
        super(context, 0, wordList);
        this.mColorResourceId=mColorResourceId;
        this.mp=mp;

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
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.mImage);
        if(currentWord.hasImage()) {
            imageView.setImageResource(currentWord.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        }
        else
            imageView.setVisibility(View.GONE);

        RelativeLayout layout = (RelativeLayout) listItemView.findViewById(R.id.inner_layout);
        //set color
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        layout.setBackgroundColor(color);

        return listItemView;
    }
}
