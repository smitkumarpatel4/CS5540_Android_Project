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
import com.munachimsoani.android.personalfeedapp.model.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    public Context mContext;
    public ArrayList<News> mNews;

    public NewsAdapter(Context context, ArrayList<News> news) {
        this.mContext = context;
        this.mNews = news;
    }


    @NonNull
    @Override
    public NewsAdapter.NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_item, viewGroup, shouldAttachToParentImmediately);
        NewsHolder viewHolder = new NewsHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsHolder newsHolder, int i) {

        newsHolder.bind(i);

    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView abstracts;
        ImageView image;
        TextView url;


        public NewsHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageViewThumbnail);
            title = itemView.findViewById(R.id.news_title);
            abstracts = itemView.findViewById(R.id.abstracts);


        }


        void  bind(final int listIndex){

            title.setText(mNews.get(listIndex).getTitle());
            abstracts.setText(mNews.get(listIndex).getAbstracts());
//            publishedDate.setReferenceTime(new Date(mNews.get(listIndex).getPublishedAt()).getTime());
            Uri uri = Uri.parse(mNews.get(listIndex).getImage_url());
            if(uri !=null){
                Picasso.get().load(uri).into(image);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String urlString = mNews.get(listIndex).getUrl();

                    Intent intent = new Intent(mContext,WebViewActivity.class);
                    intent.putExtra("urlString",urlString);
                    mContext.startActivity(intent);

                }
            });


        }


    }
}
