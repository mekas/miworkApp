package com.example.android.miwok;

/**
 * Created by pustikom on 18/08/16.
 */

public class Word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int imageResourceId;
    private int mSoundId;
    private static final int NO_IMAGE_PROVIDED=-1;

    public Word(String mDefaultTranslation, String mMiwokTranslation){
        this.mDefaultTranslation=mDefaultTranslation;
        this.mMiwokTranslation=mMiwokTranslation;
        this.imageResourceId=NO_IMAGE_PROVIDED;
    }

    public Word(String mDefaultTranslation, String mMiwokTranslation, int resourceId){
        this.mDefaultTranslation=mDefaultTranslation;
        this.mMiwokTranslation=mMiwokTranslation;
        this.imageResourceId =resourceId;
    }

    public Word(String mDefaultTranslation, String mMiwokTranslation, int resourceId, int mSoundId){
        this.mDefaultTranslation=mDefaultTranslation;
        this.mMiwokTranslation=mMiwokTranslation;
        this.imageResourceId =resourceId;
        this.mSoundId=mSoundId;
    }

    public String getmDefaultTranslation(){
        return mDefaultTranslation;
    }

    public String getmMiwokTranslation(){
        return mMiwokTranslation;
    }

    public int getImageResourceId(){
        return imageResourceId;
    }

    public int getmSoundId(){
        return mSoundId;
    }

    public boolean hasImage(){
        return imageResourceId!=NO_IMAGE_PROVIDED;
    }
}
