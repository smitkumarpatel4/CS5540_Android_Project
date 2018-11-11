package com.munachimsoani.android.personalfeedapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
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
import com.munachimsoani.android.personalfeedapp.model.MovieReview;
import com.munachimsoani.android.personalfeedapp.model.News;
import com.munachimsoani.android.personalfeedapp.utilities.JSONUtils;
import com.munachimsoani.android.personalfeedapp.utilities.MovieReviewAdapter;
import com.munachimsoani.android.personalfeedapp.utilities.NetworkUtils;
import com.munachimsoani.android.personalfeedapp.utilities.NewsAdapter;
import com.munachimsoani.android.personalfeedapp.utilities.SimpleDividerItemDecoration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieShowActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    TextView mTextView;
    String searchQuery;

    /* A constant to save and restore the URL that is being displayed */
    private static final String NEWS_QUERY_URL_EXTRA = "query";

    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */
    private static final int NEWS_LOADER = 22;

    private final String TAG = "MainActivity";


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;

    private TextView mDisplayName;
    private TextView mUserEmail;

    private RecyclerView mRecyclerView;
    private MovieReviewAdapter mAdapter;
    private ArrayList<MovieReview> movieReview = new ArrayList<>();

    private ProgressBar mProgressBar;

    private FirebaseUser user;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_show);

       // mTextView = findViewById(R.id.tester);

        Intent intent = getIntent();

        searchQuery = null;
        if (intent.hasExtra("searchQuery")) {
            searchQuery = intent.getStringExtra("searchQuery");
            //  mTextView.setText(searchQuery);
            Log.d("test", searchQuery);
        }

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


        mRecyclerView = findViewById(R.id.my_recycler_view_movie_review);
        mAdapter = new MovieReviewAdapter(this, movieReview);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

//      call makeNetworkQueryMovieReview
        makeNetworkQueryMovieReview();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.nav_top_stories:
                        Intent intent = new Intent(MovieShowActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_sports:
                        Intent intentSoccer = new Intent(MovieShowActivity.this, SportsActivity.class);
                        startActivity(intentSoccer);
                        finish();
                        break;

                    case R.id.nav_movie_reviews:
                        showSearchDialogBox();
                        break;

                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intentLogout = new Intent(MovieShowActivity.this, LoginActivity.class);
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
    public void makeNetworkQueryMovieReview() {
        URL networkURL = NetworkUtils.buildMovieReview(searchQuery);
//        Log.d(TAG, networkURL.toString());
        // mSearchResultsTextView.setText(networkURL.toString());

//        Create a Bundle to hold the  URL because loaders use bundle
        Bundle queryBundle = new Bundle();
//        Put the value of the URL inside the bundle
        queryBundle.putString(NEWS_QUERY_URL_EXTRA, networkURL.toString());

//        Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        android.support.v4.content.Loader<String> networkQueryTopStories = loaderManager.getLoader(NEWS_LOADER);
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
            @Nullable
            @Override
            public String loadInBackground() {


//           Parse the URL from the passed in String and fetch the data
                String searchQueryUrlString = bundle.getString(NEWS_QUERY_URL_EXTRA);
                String movieResults = null;

                try {
                    URL searchUrl = new URL(searchQueryUrlString);
                    movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return movieResults;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String movieResults) {


//        Remove(hide) the progress bar after the data have been fetched
        mProgressBar.setVisibility(View.GONE);

//        Get the POJO of the data
        movieReview = JSONUtils.makeMovieReviewList(movieResults);



//        Add the data to Recycler Adapter
        mAdapter.mMovieReviews.addAll(movieReview);

//        Notify the Adapter if there is any change
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }


    public void showSearchDialogBox(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(MovieShowActivity.this);
        builder.setView(R.layout.activity_movie_search)
                .setPositiveButton(android.R.string.search_go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextInputEditText ti = ((AlertDialog)dialogInterface).findViewById(R.id.movieSearchQuery);
                        String searchQuery = ti.getText().toString();

                        Intent movieIntent = new Intent(MovieShowActivity.this,MovieShowActivity.class);
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
