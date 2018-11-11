package com.munachimsoani.android.personalfeedapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.munachimsoani.android.personalfeedapp.R;
import com.munachimsoani.android.personalfeedapp.WebViewActivity;
import com.munachimsoani.android.personalfeedapp.model.MovieReview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewHolder> {

    public Context mContext;
    public ArrayList<MovieReview> mMovieReviews;

    public MovieReviewAdapter(Context context, ArrayList<MovieReview> movieReviews) {
        mContext = context;
        mMovieReviews = movieReviews;
    }

    @NonNull
    @Override
    public MovieReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_review_item, viewGroup, shouldAttachToParentImmediately);

        MovieReviewHolder movieReviewHolder = new MovieReviewHolder(view);


        return movieReviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewHolder movieReviewHolder, int i) {
        movieReviewHolder.bind(i);

    }

    @Override
    public int getItemCount() {
        return mMovieReviews.size();
    }

    public class MovieReviewHolder extends RecyclerView.ViewHolder {

        TextView byLine;
        TextView headline;
        TextView summary;
        ImageView image;


        public MovieReviewHolder(@NonNull View itemView) {

            super(itemView);
            image = itemView.findViewById(R.id.imageViewThumbnail);
            byLine = itemView.findViewById(R.id.byLine);
            headline = itemView.findViewById(R.id.headline);
            summary = itemView.findViewById(R.id.summary);

        }

        public void bind(final int lastIndex) {

            byLine.setText(mMovieReviews.get(lastIndex).getByLine());
            headline.setText(mMovieReviews.get(lastIndex).getHeadline());
            summary.setText(mMovieReviews.get(lastIndex).getSummaryShort());

            Uri uri = Uri.parse(mMovieReviews.get(lastIndex).getImageUrl());
            if(uri !=null){
                Picasso.get().load(uri).into(image);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String urlString = mMovieReviews.get(lastIndex).getUrl();

                    Intent intent = new Intent(mContext,WebViewActivity.class);
                    intent.putExtra("urlString",urlString);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
