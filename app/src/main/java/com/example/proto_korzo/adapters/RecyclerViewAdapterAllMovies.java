package com.example.proto_korzo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proto_korzo.R;
import com.example.proto_korzo.Utils;
import com.example.proto_korzo.database.model.Movie;
import com.example.proto_korzo.fragments.Listeners;

import java.util.List;

public class RecyclerViewAdapterAllMovies extends RecyclerView.Adapter<RecyclerViewAdapterAllMovies.ViewHolder>{

    private static final String TAG = "Adapter - all movies";

    // declare variables
    private List<Movie> mDummyMovies;
    private List<Movie> mUserFaves;
    private Context mContext;
    private Listeners.OnFaveClick onFaveInterface;

    public RecyclerViewAdapterAllMovies(List<Movie> mDummyMovies, List<Movie> mUserFaves,
                                        Context mContext, Listeners.OnFaveClick onFaveInterface) {
        this.mDummyMovies = mDummyMovies;
        this.mUserFaves = mUserFaves;
        this.mContext = mContext;
        this.onFaveInterface = onFaveInterface;
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
        Log.d(TAG, "onBindViewHolder() " + position);
        Log.e(TAG, "onBindViewHolder: TITLE: " + mDummyMovies.get(position).getTitle());
        final boolean isFave;

        holder.btnFave.setOnCheckedChangeListener(null);
        if (isFave(mDummyMovies.get(position))) {
            holder.btnFave.setChecked(true);
            holder.setIsFave(true);
        } else {
            holder.btnFave.setChecked(false);
            holder.setIsFave(false);
        }
        isFave = holder.getIsFave();

        Glide.with(mContext)
                .asBitmap()
                .load(Utils.BASE_IMG_URL + mDummyMovies.get(position).getPoster_path())
                .into(holder.image);

        holder.title.setText(mDummyMovies.get(position).getTitle());

        holder.description.setText(mDummyMovies.get(position).getOverview());

        holder.movieItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open movie activity
                onFaveInterface.onMovieItemClick(mDummyMovies.get(position).getId(), isFave);
            }
        });

        holder.btnFave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onFaveInterface.onFave(mDummyMovies.get(position));
                } else {
                    onFaveInterface.onUnfave(mDummyMovies.get(position));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDummyMovies.size();
    }

    public void setAllMoviesList(List<Movie> movies) {
        mDummyMovies.clear();
        mDummyMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> moreMovies) {
        mDummyMovies.addAll(moreMovies);
        notifyDataSetChanged();
    }

    public void setFaveMovies(List<Movie> faves) {
        mUserFaves.clear();
        mUserFaves.addAll(faves);
        notifyDataSetChanged();
    }

    public /*static?*/ class ViewHolder extends RecyclerView.ViewHolder {

        // declare what needs to be used
        ImageView image;
        TextView title;
        TextView description;
        ToggleButton btnFave;
        View movieItemLayout;
        boolean isMovieFave;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.movie_image);
            title = itemView.findViewById(R.id.movie_title);
            description = itemView.findViewById(R.id.movie_description);
            btnFave = itemView.findViewById(R.id.btn_favorite);
            movieItemLayout = itemView.findViewById(R.id.layout_movie_item);
        }

        public void setIsFave(boolean isFave) {
            this.isMovieFave = isFave;
        }

        public boolean getIsFave(){
            return this.isMovieFave;
        }
    }

    private /*static*/ boolean isFave(Movie movie) {
        for (Movie item:mUserFaves){
            if (item.getTitle().equals(movie.getTitle()))
                return true;
        }

        // stream is better

       /* Optional<Movie> fave = mUserFaves.stream()
        						.filter(m -> m.id == movie.id)
        						.findFirst();
        if (fave.isPresent()){
        	return true;
        }*/
        						
        //isMovieFave = false;
        return  false;
    }

}
