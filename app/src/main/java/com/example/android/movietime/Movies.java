package com.example.android.movietime;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sadat on 2/12/18.
 */

public class Movies implements Parcelable {

    private String movieName;
    private String imageURL;
    private String voteaverage;
    private String movieoverview;
    private String releasedate;
    private int id;

    /*
                String movietitle = currentMovie.getString(MDB_TITLE);
                String movieoverview = currentMovie.getString(MDB_OVERVIEW);
                String releasedate = currentMovie.getString(MDB_RELEASEDATE);
*/
    public Movies(String movieName, String imageURL) {
        this.movieName = movieName;
        this.imageURL = imageURL;
    }

    public Movies(String movieName,String imageURL,String voteaverage,String movieoverview,String releasedate,int id){
        this.movieName = movieName;
        this.imageURL = imageURL;
        this.voteaverage = voteaverage;
        this.movieoverview = movieoverview;
        this.releasedate = releasedate;
        this.id = id;
    }

    protected Movies(Parcel in) {
        movieName = in.readString();
        imageURL = in.readString();
        voteaverage = in.readString();
        movieoverview = in.readString();
        releasedate = in.readString();
        id = in.readInt();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public String getMovieName() {
        return movieName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getVoteAverage() {
        return voteaverage;
    }

    public String getMovieOverview() {
        return movieoverview;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public int getId(){return id;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(imageURL);
        dest.writeString(voteaverage);
        dest.writeString(movieoverview);
        dest.writeString(releasedate);
        dest.writeInt(id);
    }
}
