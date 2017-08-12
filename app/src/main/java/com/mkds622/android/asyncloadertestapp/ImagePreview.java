package com.mkds622.android.asyncloadertestapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vansh on 8/21/2016.
 */
public class ImagePreview implements Parcelable {
    private final String LOG_TAG =ImagePreview.class.getSimpleName();
    String imgRegularUrl;
    String imgFullUrl;
    String img_id;

    public ImagePreview(String img_id,String imgRegularUrl, String imgFullUrl)throws NullPointerException {
        this.img_id=img_id;
        this.imgRegularUrl=imgRegularUrl;
        this.imgFullUrl=imgFullUrl;
    }
    private ImagePreview(Parcel in){
        this.img_id=in.readString();
        this.imgRegularUrl= in.readString();
        this.imgFullUrl=in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(img_id);
        parcel.writeString(imgRegularUrl);
        parcel.writeString(imgFullUrl);
    }
    public final Parcelable.Creator<ImagePreview> CREATOR=new Parcelable.Creator<ImagePreview>(){
        @Override
        public ImagePreview createFromParcel(Parcel parcel){
            return new ImagePreview(parcel);
        }
        @Override
        public ImagePreview[] newArray(int i){
            return new ImagePreview[i];
        }
    };

}
