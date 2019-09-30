package com.example.proto_korzo.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proto_korzo.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    // declare variables
    private List<String> mImages = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(List<String> mImages, List<String> mTitles, Context mContext) {
        this.mImages = mImages;
        this.mTitles = mTitles;
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

        // get the images & load them into image position
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        // get titles & load them in title position
        holder.title.setText(mTitles.get(position));

        // create an onClick so something happens when user clicks on list item
        // will have to be an intent that switches to the movie
        // whatever list the holder gets will be a list of movie objects, not a list of strings

        holder.movieItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked list item");
                Toast.makeText(mContext, "Title: " + mTitles.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }


    public /*static?*/ class ViewHolder extends RecyclerView.ViewHolder {

        // declare what needs to be used
        CircleImageView image;
        TextView title;
        // TextView description;
        // star button
        RelativeLayout movieItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // why not this.image, this.title, et?

            image = itemView.findViewById(R.id.movie_image);
            title = itemView.findViewById(R.id.movie_title);
            movieItemLayout = itemView.findViewById(R.id.layout_movie_item);

        }
    }
}
