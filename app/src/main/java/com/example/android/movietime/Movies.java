package com.example.android.movietime;

/**
 * Created by sadat on 2/12/18.
 */

public class Movies {

    public String getMovieName() {
        return movieName;
    }

    public String getImageURL() {
        return imageURL;
    }

    private String movieName;
    private String imageURL;

    public Movies(String movieName, String imageURL) {
        this.movieName = movieName;
        this.imageURL = imageURL;
    }



}
