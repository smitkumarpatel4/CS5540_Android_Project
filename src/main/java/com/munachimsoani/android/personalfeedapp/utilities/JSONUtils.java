package com.munachimsoani.android.personalfeedapp.utilities;

import android.util.Log;

import com.munachimsoani.android.personalfeedapp.model.MovieReview;
import com.munachimsoani.android.personalfeedapp.model.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {


    public static ArrayList<News> makeNewsListTopStories(String jsonResult) {

        ArrayList<News> newsList = new ArrayList<>();

        String title = "";
        String abstracts = "";
        String url = "";
        String published_date = "";
        String image_url = "";

        try {


            JSONObject mainJSONObject = new JSONObject(jsonResult);
            JSONArray results = mainJSONObject.getJSONArray("results");

//            Log.d("test", String.valueOf(results.length()));
            for (int i = 0; i < results.length(); i++) {

                JSONObject item = results.getJSONObject(i);

                title = item.getString("title");
                abstracts = item.getString("abstract");
                url = item.getString("url");
                published_date = item.getString("published_date");

                JSONArray multimedia = item.getJSONArray("multimedia");

//                Log.d("test", String.valueOf(multimedia.length()));

                if (multimedia.length() == 0) {

                    // When the news has no image set a default image right now it's blank
                    image_url = "";
                }

                for (int j = 0; j < multimedia.length(); j++) {

                    JSONObject itemImage = multimedia.getJSONObject(j);

                    image_url = itemImage.getString("url");


                }

//                    Log.d("test", image_url);


                newsList.add(new News(title, abstracts, url, published_date, image_url));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;

    }

    public static ArrayList<MovieReview> makeMovieReviewList(String jsonResult) {

        ArrayList<MovieReview> movieList = new ArrayList<>();


        String display_title = "";
        String byLine = "";
        String headline = "";
        String summary_short = "";
        String publication_date = "";
        String imageUrl = "";
        String url = "";

        try {

            JSONObject mainJSONObject = new JSONObject(jsonResult);
            JSONArray results = mainJSONObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject item = results.getJSONObject(i);
                display_title = item.getString("display_title");
                byLine = item.getString("byline");
                headline = item.getString("headline");
                summary_short = item.getString("summary_short");
                publication_date = item.getString("publication_date");

                JSONObject link = item.getJSONObject("link");
                url = link.getString("url");

                JSONObject multimedia = item.getJSONObject("multimedia");


                if (multimedia.length() == 0) {

                    // When the news has no image set a default image right now it's blank
                    imageUrl = "";
                }
                imageUrl = multimedia.getString("src");

                movieList.add(new MovieReview(display_title,byLine,headline,summary_short,publication_date,url,imageUrl));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movieList;
    }
}
