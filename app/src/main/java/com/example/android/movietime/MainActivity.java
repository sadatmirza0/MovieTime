package com.example.android.movietime;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private final URL URL1 = new URL("https://api.themoviedb.org/3/movie/popular?api_key=e6119fc0e6963d6ee2300a97c6d1cf22");

    private final URL URL2 = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=e6119fc0e6963d6ee2300a97c6d1cf22");

    private RecyclerView myRecyclerView;

    private RecyclerView.Adapter adapter;

    private List<Movies> AllMovies;

    private TextView Error_TV;


    public MainActivity() throws MalformedURLException {
    }

    public static final boolean CheckInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;

        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView_main);
        Error_TV = (TextView) findViewById(R.id.error_TV);
        myRecyclerView.setHasFixedSize(true);

        myRecyclerView.setLayoutManager(new GridLayoutManager((getApplicationContext()), 2));

        if (CheckInternetConnection(this)){
            try {
                loadRecyclerViewData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            Error_TV .setVisibility(View.VISIBLE);
        }

    }

    private void loadRecyclerViewData() throws MalformedURLException {
        new MoviesAsyncTask().execute(URL1);
    }


/*    http://androidcss.com/android/fetch-json-data-android/*/

    /*ConnectivityManager cm = (ConnectivityManager) (context != null ? context.getSystemService(CONNECTIVITY_SERVICE) : null);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private class MoviesAsyncTask extends AsyncTask<URL, Void, String> {
        //URL urlTopRated = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=e6119fc0e6963d6ee2300a97c6d1cf22");
        Context context;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private MoviesAsyncTask() throws MalformedURLException {
        }
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        private List<Movies> getMovieData(String input)
                throws JSONException {
            final String MDB_RESULT = "results";
            final String MDB_TITLE = "title";
            final String MDB_POSTER = "poster_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_RATING = "vote_average";
            final String MDB_RELEASEDATE ="release_date";

            List<Movies> listOfMovies = new ArrayList<>();

            JSONObject moviejson = new JSONObject(input);
            JSONArray movieArray = moviejson.getJSONArray(MDB_RESULT);
            String baseURL = "http://image.tmdb.org/t/p/w500/";
            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject currentMovie = movieArray.getJSONObject(i);
                String voteaverage = currentMovie.getString(MDB_RATING);
                int movieID = Integer.parseInt(currentMovie.getString("id"));
                String movietitle = currentMovie.getString(MDB_TITLE);
                String movieoverview = currentMovie.getString(MDB_OVERVIEW);
                String releasedate = currentMovie.getString(MDB_RELEASEDATE);
                String moviePosterendURL = currentMovie.getString(MDB_POSTER);
                String moviePosterURL = baseURL + moviePosterendURL;
                Movies items = new Movies(movietitle, moviePosterURL,voteaverage,movieoverview,releasedate,movieID);
                listOfMovies.add(items);
            }
            return listOfMovies;
        }

        @Override
        protected void onPostExecute(String s) {
            AllMovies = new ArrayList<>();
            try {
                AllMovies = getMovieData(s);
            } catch (JSONException e) {e.printStackTrace();}

            adapter = new MoviesAdapter(AllMovies, getApplicationContext());

            myRecyclerView.setAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
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
        protected String doInBackground(URL... urls) {
            URL testURL = urls[0];
            try {
                jsonResponse = getResponseFromHttpUrl(testURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Error_TV = findViewById(R.id.error_TV);
        switch (item.getItemId()) {
            case R.id.menu_favourites:
                if (CheckInternetConnection(this)){
                    try {
                        new MoviesAsyncTask().execute(URL2);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();}
                } else{myRecyclerView.setVisibility(View.INVISIBLE);
                    Error_TV.setVisibility(View.VISIBLE);}
                return true;
            default:
                return false;
        }
    }
}