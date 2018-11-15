package com.munachimsoani.android.personalfeedapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munachimsoani.android.personalfeedapp.model.News;
import com.munachimsoani.android.personalfeedapp.utilities.JSONUtils;
import com.munachimsoani.android.personalfeedapp.utilities.NetworkUtils;
import com.munachimsoani.android.personalfeedapp.utilities.NewsAdapter;
import com.munachimsoani.android.personalfeedapp.utilities.SimpleDividerItemDecoration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SportsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    /* A constant to save and restore the URL that is being displayed */
    private static final String NEWS_QUERY_URL_EXTRA = "query";

    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */
    private static final int NEWS_LOADER = 22;

    private final String TAG = "SportsActivity";


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;

    private TextView mDisplayName;
    private TextView mUserEmail;

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private ArrayList<News> news = new ArrayList<>();

    private ProgressBar mProgressBar;

    private FirebaseUser user;

    private FirebaseAuth mAuth;

    TextView mtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtitle = findViewById(R.id.heading);
        mtitle.setText(R.string.sports_string_title);
        mProgressBar = findViewById(R.id.progressbar);


        mDrawerLayout = findViewById(R.id.drawerLayout);

        mToolbar = findViewById(R.id.nav_actionbar);
        setSupportActionBar(mToolbar);


        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Add the logged in user to the navigation view

        @SuppressLint("WrongViewCast") NavigationView navigationView = findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);


        mDisplayName = navHeaderView.findViewById(R.id.username);
        mUserEmail = navHeaderView.findViewById(R.id.user_email);


        mAuth = FirebaseAuth.getInstance();


        //     Get user from firebase and display
        user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address
            String name = user.getDisplayName();
            String email = user.getEmail();

            mDisplayName.setText(name);
            mUserEmail.setText(email);

        }


        mRecyclerView = findViewById(R.id.my_recycler_view);
        mAdapter = new NewsAdapter(this, news);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

//      call makeNetworkQuerySports
        makeNetworkQuerySports();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.nav_top_stories:
                        Intent intent = new Intent(SportsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_sports:
                        Intent intentSoccer = new Intent(SportsActivity.this, SportsActivity.class);
                        startActivity(intentSoccer);
                        finish();
                        break;

                    case R.id.nav_movie_reviews:
                        showSearchDialogBox();
                        break;

//                    case R.id.nav_books:
//                        Intent intentBook = new Intent(getApplicationContext(), BooksActivity.class);
//                        startActivity(intentBook);
//                        finish();
//                        break;

                    case R.id.nav_chat:
                        Intent intenChat = new Intent(SportsActivity.this,ChatActivity.class);
                        startActivity(intenChat);
                        break;

                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intentLogout = new Intent(SportsActivity.this, LoginActivity.class);
                        startActivity(intentLogout);
                        finish();
                        break;

                    default:
                        break;


                }
                return false;
            }
        });


    }

    // Create a method to get the news URL
    public void makeNetworkQuerySports() {
        URL networkURL = NetworkUtils.buildUrlSports();
//        Log.d(TAG, networkURL.toString());
        // mSearchResultsTextView.setText(networkURL.toString());

//        Create a Bundle to hold the  URL because loaders use bundle
        Bundle queryBundle = new Bundle();
//        Put the value of the URL inside the bundle
        queryBundle.putString(NEWS_QUERY_URL_EXTRA, networkURL.toString());

//        Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> networkQueryTopStories = loaderManager.getLoader(NEWS_LOADER);
//        new NewsTask().execute(networkURL);

//        If the Loader was null, initialize it. Else, restart it.
        if (networkQueryTopStories == null) {
            loaderManager.initLoader(NEWS_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(NEWS_LOADER, queryBundle, this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mActionBarDrawerToggle.onOptionsItemSelected(item))
            return true;


        return super.onOptionsItemSelected(item);
    }


    //    Override onCreateLoader
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {


            //             Override onStartLoading
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
//                Set the progress bar visible while fetching data( initally loading in background)
                mProgressBar.setVisibility(View.VISIBLE);

//                Force a load
                forceLoad();
            }

//            Override loadInBackground

            @Nullable
            @Override
            public String loadInBackground() {


//           Parse the URL from the passed in String and fetch the data
                String searchQueryUrlString = bundle.getString(NEWS_QUERY_URL_EXTRA);
                String newsResults = null;

                try {
                    URL searchUrl = new URL(searchQueryUrlString);
                    newsResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return newsResults;


            }
        };
    }

//    Override onLoadFinished

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String newsResult) {

//        Remove(hide) the progress bar after the data have been fetched
        mProgressBar.setVisibility(View.GONE);

//        Get the POJO of the data
        news = JSONUtils.makeNewsListTopStories(newsResult);

//        Add the data to Recycler Adapter
        mAdapter.mNews.addAll(news);

//        Notify the Adapter if there is any change
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void showSearchDialogBox(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(SportsActivity.this);
        builder.setView(R.layout.activity_movie_search)
                .setPositiveButton(android.R.string.search_go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextInputEditText ti = ((AlertDialog)dialogInterface).findViewById(R.id.movieSearchQuery);
                        String searchQuery = ti.getText().toString();

                        Intent movieIntent = new Intent(SportsActivity.this,MovieShowActivity.class);
                        movieIntent.putExtra("searchQuery",searchQuery);
                        startActivity(movieIntent);
                        //finish();


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


}
