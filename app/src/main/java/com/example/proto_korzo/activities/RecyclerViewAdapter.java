package com.example.proto_korzo.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proto_korzo.R;
import com.example.proto_korzo.database.model.Movie;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    // declare variables
    private List<Movie> mDummyMovies;
    private List<Movie> mUserFaves;
    private Context mContext;

    public RecyclerViewAdapter(List<Movie> mDummyMovies, List<Movie> mUserFaves, Context mContext) {
        this.mDummyMovies = mDummyMovies;
        this.mUserFaves = mUserFaves;
        this.mContext = mContext;
    }

    @NonNull
    @Override // this one inflates the view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_movie_item,
                            parent,
                            false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.btnFave.setOnCheckedChangeListener(null);

        if (isFave(mDummyMovies.get(position))) {
            holder.btnFave.setChecked(true);
        } else {
            holder.btnFave.setChecked(false);
        }
        // get the images & load them into image position
        Glide.with(mContext)
                .asBitmap()
                .load(mDummyMovies.get(position).getImgUrl())
                .into(holder.image);

        // get titles & load them in title position
        holder.title.setText(mDummyMovies.get(position).getTitle());
        holder.description.setText(mDummyMovies.get(position).getDescription());
        // create an onClick so something happens when user clicks on list item
        // will have to be an intent that switches to the movie
        // whatever list the holder gets will be a list of movie objects, not a list of strings

        // click on the whole of the movie
        holder.movieItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked list item");
                Toast.makeText(mContext, "Title: " + mDummyMovies.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        // click on favourite button
        holder.btnFave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // create usermovie join object
                    mUserFaves.add(mDummyMovies.get(position));
                    Toast.makeText(mContext, "checked this: " + mDummyMovies.get(position).getTitle(), Toast.LENGTH_LONG).show();
                } else {
                    mUserFaves.remove(mDummyMovies.get(position));
                    Toast.makeText(mContext, "UNchecked this: " + mDummyMovies.get(position).getTitle(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDummyMovies.size();
    }

    public /*static?*/ class ViewHolder extends RecyclerView.ViewHolder {

        // declare what needs to be used
        ImageView image;
        TextView title;
        TextView description;
        ToggleButton btnFave;
        View movieItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // why not this.image, this.title, et?
            image = itemView.findViewById(R.id.movie_image);
            title = itemView.findViewById(R.id.movie_title);
            description = itemView.findViewById(R.id.movie_description);
            btnFave = itemView.findViewById(R.id.btn_favorite);
            movieItemLayout = itemView.findViewById(R.id.layout_movie_item);

        }
    }

    private /*static*/ boolean isFave(Movie movie) {
/*        for (Movie item:mUserFaves){
            if (item.getTitle().equals(movie.getTitle()))
                return true;
        }

        return  false;*/

        if (mUserFaves.contains(movie)) {
            return true;
        }
        return false;
    }
}
