/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private AudioManager am;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            ColorsActivity.this.releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener afChangeListener  = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange==AUDIOFOCUS_LOSS_TRANSIENT || focusChange==AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                //pause playback
                mp.pause();
                mp.seekTo(0);
            } else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                //resume playback
                mp.start();
            } else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
                //permanent loss
                //am.unregisterMediaButtonEventReceiver();
                releaseMediaPlayer();
                am.abandonAudioFocus(afChangeListener);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        am=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red","wetetti", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green","chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown","ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray","ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black","kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white","kelelli", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("dusty yellow","ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow","chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter itemAdapter = new WordAdapter(this,words,R.color.category_colors);
        ListView colorsView = (ListView)findViewById(R.id.list_colors);
        colorsView.setAdapter(itemAdapter);

        colorsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                releaseMediaPlayer();

                int result=am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    //am.registerMediaButtonEventReceiver();
                    Word word=words.get(position);
                    mp=MediaPlayer.create(ColorsActivity.this, word.getmSoundId());
                    mp.start();
                    mp.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer(){
        if(mp!=null){
            mp.release();
            mp=null;
            am.abandonAudioFocus(afChangeListener);
        }
    }
}
