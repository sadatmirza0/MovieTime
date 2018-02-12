package com.example.android.movietime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.movietime.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sadat on 2/12/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movies> moviesItems;
    private Context context;

    public MoviesAdapter (List<Movies> moviesItems, Context context){
        this.moviesItems = moviesItems;
        this.context = context;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false);
    return new MoviesAdapter.MoviesAdapterViewHolder(V);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {

        Movies movie = moviesItems.get(position);

        holder.mMovieTextView.setText(movie.getMovieName());
        Picasso.with(context)
                .load(movie.getImageURL())
                .into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        return moviesItems.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mMovieImageView;
        public final TextView mMovieTextView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);

            mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie);
            mMovieTextView = (TextView) itemView.findViewById(R.id.tv_movie_name);

        }
    }

}
