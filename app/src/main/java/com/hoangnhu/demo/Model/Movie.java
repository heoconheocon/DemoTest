package com.hoangnhu.demo.Model;

import android.util.Log;

import com.hoangnhu.demo.Until.IConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class Movie
{
    private String title;

    private List<String> image;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setImage(List<String> image){
        this.image = image;
    }
    public List<String> getImage(){
        return this.image;
    }


    public static Movie getData(String jsonData) {
        Movie movie = new Movie();
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            //get title movie
            String title = "";
            if(jsonObj.has(IConstant.PREFIX_TITLE)) {
                 title = jsonObj.getString(IConstant.PREFIX_TITLE);
            }
            if(!title.isEmpty()){
                movie.setTitle(title);
            }
            // Getting JSON Array movie
            List<String> imageList = new ArrayList<>();
            JSONArray imageArray = null;
            if(jsonObj.has(IConstant.PREFIX_IMAGE)) {
                 imageArray = jsonObj.getJSONArray(IConstant.PREFIX_IMAGE);
            }
            if(imageArray == null)
                return  movie;
            for(int j=0; j< imageArray.length(); j++){
                String imagePath = (String) imageArray.get(j);
                if(!imagePath.isEmpty()) {
                    imageList.add(imagePath);
                    Log.d("TAG","imagePath:"+imagePath);
                }
            }
            movie.setImage(imageList);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }
}