package com.mkds622.android.asyncloadertestapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Main_Menu extends Fragment {


    public Main_Menu() {
        // Required empty public constructor
    }
    ImagePreviewListAdapter imagePreviewListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View imageBoardView=inflater.inflate(R.layout.main_menu,container,false);
        List<ImagePreview> imageList=new ArrayList<ImagePreview>();
        imagePreviewListAdapter =new ImagePreviewListAdapter(getActivity(),imageList);
        GridView imagePreviewGrid=(GridView)imageBoardView.findViewById(R.id.Image_Board_grid);
        imagePreviewGrid.setAdapter(imagePreviewListAdapter);
        imagePreviewGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    ImagePreview selected= imagePreviewListAdapter.getItem(position);
//                    Intent I1=new Intent(getActivity(),DetailActivity.class).putExtra(Intent.EXTRA_TEXT, String.valueOf(selected.movie_id));
//                    startActivity(I1);
            }

        });


        return imageBoardView;
    }


    private void updateImageList()
    {
        final ExtractImageTask imageTask=new ExtractImageTask();
        imageTask.execute();

    }

    @Override
    public void onStart()
    {
        super.onStart();
        updateImageList();
    }
    public class ExtractImageTask extends AsyncTask<Void,Void,ImagePreview[]> {
        private final String LOG_TAG =ExtractImageTask.class.getSimpleName();
            private ImagePreview[] getImageLinkFromJSON(String jsonImageString)throws JSONException {
                final String results="results";
                final String urls="urls";
                final String regular="regular";
                final String full="full";
                final String id="id";

                        JSONArray J1 = new JSONArray(jsonImageString);
                        JSONObject temp;
                        ImagePreview[] RESULT = new ImagePreview[J1.length()];
                        for (int i = 0; i < RESULT.length; ++i) {
                            temp = J1.getJSONObject(i);

                            String img_id = temp.getString(id);
                            temp= temp.getJSONObject(urls);
                            String imgRegularUrl= temp.getString(regular);
                            String imgFullUrl=temp.getString(full);
                            ImagePreview tempImagePreview = new ImagePreview(img_id,imgRegularUrl,imgFullUrl);
                            RESULT[i] = tempImagePreview;

                        }

                        return RESULT;
            }

        protected ImagePreview[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonImageString = null;
            try {
                final String base_url = "http://pastebin.com/raw/wgkJgazE";
                final String Api_key_param = "api_key";
                final String apikey = BuildConfig.UnSplash_Api_Key;
                Uri builturi = Uri.parse(base_url).buildUpon().build();
                 //       .appendQueryParameter(Api_key_param, apikey).build();
                Log.e(LOG_TAG,builturi.toString());
                URL url = new URL(builturi.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonImageString = buffer.toString();

            } catch (IOException e) {
                Log.w(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.w(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getImageLinkFromJSON(jsonImageString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
            protected  void onPostExecute(ImagePreview[] result){
                if(result!=null){
                    imagePreviewListAdapter.clear();
                    for(ImagePreview M1 : result) {
                        imagePreviewListAdapter.add(M1);
                    }
                }
            }
    }

}
