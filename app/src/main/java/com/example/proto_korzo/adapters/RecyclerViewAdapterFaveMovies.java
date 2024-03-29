package com.example.proto_korzo.adapters;

import android.content.Context;
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

public class RecyclerViewAdapterFaveMovies extends RecyclerView.Adapter<RecyclerViewAdapterFaveMovies.ViewHolder> {


    private List<Movie> mFaveMovies;
    private Context mContext;
    private Listeners.OnFaveClick onFaveListener;

    public RecyclerViewAdapterFaveMovies(List<Movie> mFaveMovies, Context mContext, Listeners.OnFaveClick onFaveListener) {
        this.mFaveMovies = mFaveMovies;
        this.mContext = mContext;
        this.onFaveListener = onFaveListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_movie_item, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final boolean isFave;

        holder.btnFave.setOnCheckedChangeListener(null);
        holder.btnFave.setChecked(true);

        holder.setIsFave(true);
        isFave = holder.getIsFave();

        Glide.with(mContext)
                .asBitmap()
                .load(Utils.BASE_IMG_URL + mFaveMovies.get(position).getPoster_path())
                .into(holder.image);

        holder.title.setText(mFaveMovies.get(position).getTitle());
        holder.description.setText(mFaveMovies.get(position).getOverview());

        holder.movieItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open movie activity
                onFaveListener.onMovieItemClick(mFaveMovies.get(position).getId(), isFave);
            }
        });

        // click on favourite button
        holder.btnFave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // they are all checked anyway
                    } else {
                    onFaveListener.onUnfave(mFaveMovies.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFaveMovies.size();
    }

    public void setFaveMovies(List<Movie> newFaves) {
        mFaveMovies.clear();
        mFaveMovies.addAll(newFaves);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
            return isMovieFave;
        }

    }

}
