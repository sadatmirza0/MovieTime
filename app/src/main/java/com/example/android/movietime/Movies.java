package com.example.android.movietime;

/**
 * Created by sadat on 2/12/18.
 */

public class Movies {

    String movieName;
    String imageURL;

    public Movies(String movietitle, String moviePosterURL) {
        this.movieName = movietitle;
        this.imageURL = moviePosterURL;
    }
    public String getMovieName(){
        return movieName;
    }
    public String getImageURL(){
        return imageURL;
    }
}
