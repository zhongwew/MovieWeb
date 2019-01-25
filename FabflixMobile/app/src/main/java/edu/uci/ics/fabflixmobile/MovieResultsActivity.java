package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.MovieAdapter;
import entity.Movie;
import ui.LoadMoreListView;
import utils.ACache;
import utils.JSONToEntity;

/**
 * Created by haiguai on 2/26/18.
 */

public class MovieResultsActivity extends ActionBarActivity {
    // Define result ListView
    private ListView resultListView;
    // Define keyword
    private String keyword;
    // Cache instance
    private ACache mCache;
    // Movie list
    private ArrayList<Movie> movies;
    // Response JSON Object results
    private JSONArray results;
    // Define ViewHolder
    private static class ViewHolder {
        public LoadMoreListView resultListView;
    }
    private ViewHolder viewHolder;
    // Results Counter
    private int count = 10;
    // Loading
    private boolean isLoading;
    private Context mContext;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_results);

        // Initialize ACache
        mCache = ACache.get(this);

        mContext = this;

        // Initialize ViewHolder
        viewHolder = new ViewHolder();
        viewHolder.resultListView = (LoadMoreListView) findViewById(R.id.result_list);

        // Initialize data
        init();
    }

    /**
     * Method to initialize search results
     */
    private void init() {
        // Get keyword from cache
        keyword = mCache.getAsString("keyword");

        // Fetch results
        results = mCache.getAsJSONArray(keyword);

        // If results are not in cache, then retrieve from server
        if (results == null) {
            fetchResults();
        }
        else {
            setupListView();
        }
    }

    /**
     * To setup ListView Component after retrieving data
     */
    private void setupListView() {
        // Get movie list from results JSON Object
        movies = getMovies(results);

        // Get ListView Item
        // Initialize Adapter
        movieAdapter = new MovieAdapter(this, movies);
        // Set Adapter
        viewHolder.resultListView.setAdapter(movieAdapter);
        viewHolder.resultListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                loadMore();
            }
        });
    }

    /**
     * Load more search results
     */
    private void loadMore() {
        new Thread(){
            @Override
            public void run() {
                super.run();

                // Increase count to load next page
                count = count + 10;

                // Fetch more data
                fetchResults();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieAdapter.notifyDataSetChanged();
                        viewHolder.resultListView.setLoadCompleted();
                    }
                });
            }
        }.start();
    }

    /**
     * Send http request to fetch search results
     */
    private void fetchResults() {
        Toast.makeText(this, "Fetching " + count + " reuslts...", Toast.LENGTH_SHORT).show();

        isLoading = true;

        // Define parameter mapping
        final Map<String, String> params = new HashMap<String, String>();

        // No user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);

        // Define login route
        String parameters = "?keyword=" + keyword + "&count=" + count;
        String url = getResources().getString(R.string.host) + getResources().getString(R.string.search_route) + parameters;

        // Create request connection
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            /**
             * Response call back
             * @param response
             */
            @Override
            public void onResponse(String response) {
                // Handle JSON Exception
                try {
                    JSONObject responseObj = new JSONObject(response.toString());

                    if (responseObj.get("type").toString().equals("success")) {
                        // Set results
                        results = responseObj.getJSONObject("message").getJSONArray("movies");
                        // Cache this result
                        mCache.put(keyword, results, 10);
                        // Setup listview
                        setupListView();
                    } else {
                        // Show error message
                        Toast.makeText(mContext, new JSONObject(responseObj.get("message").toString()).get("errorMessage").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }

                isLoading = false;
            }
        }, new Response.ErrorListener() {
            /**
             * Error call back
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.e("test", error.toString());
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();

                isLoading = false;
            }
        }) {
            /**
             * Parameter call back
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };


        // Add the request to the RequestQueue.
        queue.add(postRequest);

        return;
    }

    /**
     * Create menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.results_menu, menu);
        return true;
    }

    /**
     * Menu item onclick call back
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.movie_results_item:
                return true;
            case R.id.search_item:
                intent = new Intent(this, SearchActivity.class);

                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * To get movies from cache JSON Object
     * @param JSONMovies
     * @return
     */
    private ArrayList<Movie> getMovies(JSONArray JSONMovies) {

        ArrayList<Movie> movies = null;

        JSONArray JSONMovieArr = JSONMovies;

        movies = JSONToEntity.toMovies(JSONMovieArr);

        return movies;
    }

    /**
     * Populate test data
     *
     * @return
     */
    private ArrayList<Movie> populateMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        for (int i = 0; i < 10; i++) {
            movies.add(new Movie("" + i, "Good Movie", 100 * i, "Jack"));
        }

        return movies;
    }
}
