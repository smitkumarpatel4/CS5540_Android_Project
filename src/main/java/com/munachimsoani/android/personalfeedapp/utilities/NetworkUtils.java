package com.munachimsoani.android.personalfeedapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TOP_STORIES_BASE_URL = "https://api.nytimes.com/svc/topstories/v2/home.json";
    private static final String SPORTS_BASE_URL = "https://api.nytimes.com/svc/topstories/v2/sports.json";
    private static final String BOOKS_BEST_SELLER_NAMES = "https://api.nytimes.com/svc/books/v3/lists/names.json";
    private static final String MOVIE_REVIEW_BASE_URL = "https://api.nytimes.com/svc/movies/v2/reviews/search.json";


    private static final String PARAM_QUERY = "query";


    private final static String PARAM_API_KEY = "api-key";


    private final static String API_KEY = "c9cb67a86a3e459898e7eba53cc1662b";


    public static URL buildUrlTopStories() {

        Uri builtUri = Uri.parse(TOP_STORIES_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlSports() {

        Uri builtUri = Uri.parse(SPORTS_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieReview(String movieQuery) {
        Uri builtUri = Uri.parse(MOVIE_REVIEW_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, movieQuery)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildBestSellerBooks() {

        Uri builtUri = Uri.parse(BOOKS_BEST_SELLER_NAMES).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
}
