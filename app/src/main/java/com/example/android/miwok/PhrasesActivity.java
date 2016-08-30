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

public class PhrasesActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private AudioManager am;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
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
        setContentView(R.layout.activity_phrases);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?","minto wuksus",-1, R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","Tinna ayaase'na",-1, R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","ayaaset...", -1,R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michaksas?", -1, R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm feeling good","kuchi achit", -1, R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","aanas'aa", -1, R.raw.phrase_are_you_coming));
        words.add(new Word("yes, I'm coming","haa'aanam", -1, R.raw.phrase_yes_im_coming));
        words.add(new Word("I'm coming","aanam", -1, R.raw.phrase_im_coming));
        words.add(new Word("let's go","yoowutis", -1, R.raw.phrase_lets_go));
        words.add(new Word("Come here","anni'nem", -1, R.raw.phrase_come_here));

        WordAdapter itemAdapter = new WordAdapter(this, words, R.color.category_phrases);
        ListView phrasesView = (ListView) findViewById(R.id.list_phrases);
        phrasesView.setAdapter(itemAdapter);
        am=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        phrasesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                releaseMediaPlayer();

                int result=am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    //am.registerMediaButtonEventReceiver();
                    Word word=words.get(position);
                    mp=MediaPlayer.create(PhrasesActivity.this, word.getmSoundId());
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
