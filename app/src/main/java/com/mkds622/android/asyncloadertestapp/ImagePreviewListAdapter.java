package com.mkds622.android.asyncloadertestapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
//import com.squareup.picasso.Picasso;

import com.mkds622.android.asynclibrary.ASyncLibrary;
import com.mkds622.android.asynclibrary.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagePreviewListAdapter extends ArrayAdapter<ImagePreview> {
    private static final String LOG_TAG= ImagePreviewListAdapter.class.getSimpleName();

    public ImagePreviewListAdapter(Activity context, List<ImagePreview> imagePreviews)
    {
        super(context,0,imagePreviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImagePreview imagePreview=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.image_preview_layout,parent,false);
        }
        PhotoView image=(PhotoView) convertView.findViewById(R.id.ImagePreviewButton1);
        if(imagePreview.imgRegularUrl==null && imagePreview.imgFullUrl==null)
        {
        image.setImageResource(R.mipmap.image_not_found);
        }
        else
        //Picasso.with(getContext()).load(imagePreview.imgRegularUrl).into(image);
        ASyncLibrary.with(getContext()).load(imagePreview.imgRegularUrl).into(image);
        return convertView;
    }
}
