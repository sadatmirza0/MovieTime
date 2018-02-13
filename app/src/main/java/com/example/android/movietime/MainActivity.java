package com.example.android.movietime;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;

    private RecyclerView.Adapter adapter;

    private List<Movies> AllMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView_main);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            loadRecyclerViewData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadRecyclerViewData() throws MalformedURLException {

        new MoviesAsyncTask().execute();

    }

/*    http://androidcss.com/android/fetch-json-data-android/*/


    private class MoviesAsyncTask extends AsyncTask<Void, Void, String> {

        //URL urlTopRated = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=e6119fc0e6963d6ee2300a97c6d1cf22");

        Context context;


        private MoviesAsyncTask() throws MalformedURLException {
        }

        private List<Movies> getMovieData(String input)
                throws JSONException {
            final String MDB_RESULT = "results";
            final String MDB_TITLE = "title";
            final String MDB_POSTER = "poster_path";

            List<Movies> listOfMovies = new ArrayList<>();

            JSONObject moviejson = new JSONObject(input);
            JSONArray movieArray = moviejson.getJSONArray(MDB_RESULT);
            String baseURL = "http://image.tmdb.org/t/p/w500/";
            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject currentMovie = movieArray.getJSONObject(i);

                String movietitle = currentMovie.getString(MDB_TITLE);
                String moviePosterendURL = currentMovie.getString(MDB_POSTER);
                String moviePosterURL = baseURL + moviePosterendURL;
                Movies items = new Movies(movietitle, moviePosterURL);
                listOfMovies.add(items);
            }

            return listOfMovies;
        }


        @Override
        protected void onPostExecute(String s) {
            AllMovies = new ArrayList<>();
            try {
                AllMovies = getMovieData(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new MoviesAdapter(AllMovies,getApplicationContext());

            myRecyclerView.setAdapter(adapter);

            super.onPostExecute(s);
        }

        public String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }

        String jsonResponse;

        @Override
        protected String doInBackground(Void... voids) {

            URL urlPopular = null;
            try {
                urlPopular = new URL("https://api.themoviedb.org/3/movie/popular?api_key=e6119fc0e6963d6ee2300a97c6d1cf22");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                jsonResponse = getResponseFromHttpUrl(urlPopular);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }

    }
}

