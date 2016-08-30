package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

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
                releaseMediaPlayer();
                am.abandonAudioFocus(afChangeListener);
            }
        }
    };

    public PhrasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_phrases, container, false);
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

        WordAdapter itemAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);
        ListView phrasesView = (ListView) rootView.findViewById(R.id.list_phrases);
        phrasesView.setAdapter(itemAdapter);
        am=(AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        phrasesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                releaseMediaPlayer();

                int result=am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    Word word=words.get(position);
                    mp=MediaPlayer.create(getActivity(), word.getmSoundId());
                    mp.start();
                    mp.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop(){
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
