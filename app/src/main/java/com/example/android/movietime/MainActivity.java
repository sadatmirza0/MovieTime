package com.example.android.movietime;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    private List<Movies> listOfMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listOfMovies = new ArrayList<>();

        loadRecyclerViewData();
    }

    private void loadRecyclerViewData(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loooading....");
        progressDialog.show();
    }

/*    http://androidcss.com/android/fetch-json-data-android/*/


    private static class MoviesAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        URL urlPopular = new URL("https://api.themoviedb.org/3/movie/popular?api_key=e6119fc0e6963d6ee2300a97c6d1cf22");

        //URL urlTopRated = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=e6119fc0e6963d6ee2300a97c6d1cf22");

        String response;

        ArrayList<String> output = new ArrayList<>();

        ArrayList<String> result = new ArrayList<>();

        private MoviesAsyncTask() throws MalformedURLException {
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

        private ArrayList getMovieData(String input)
                throws JSONException {
            final String MDB_RESULT = "results";
            final String MDB_TITLE = "title";
            final String MDB_POSTER = "poster_path";
            String[] movieDetails = new String[20];

            JSONObject moviejson = new JSONObject(input);
            JSONArray movieArray = moviejson.getJSONArray(MDB_RESULT);
            String baseURL = "http://image.tmdb.org/t/p/w500/";
            for (int i = 0; i < 20; i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);
                String movietitle = currentMovie.getString(MDB_TITLE);
                String moviePosterendURL = currentMovie.getString(MDB_POSTER);
                String moviePosterURL = baseURL + moviePosterendURL;
                output.add(moviePosterURL);
            }
            return output;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            try {
                response = getResponseFromHttpUrl(urlPopular);
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                output = getMovieData(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("Do in background", String.valueOf(output));
            for (int i = 0 ; i<output.size();i++){
                result.add(String.valueOf(output));
            }
            return result;
        }

        /*@Override
        protected void onPostExecute(Array input) {

            Log.v("Onpostexecute", String.valueOf(output));
                for (int i = 0 ; i<output.size();i++){
                    result.add(String.valueOf(output));
                }
                return result;
        }*/
    }
}

