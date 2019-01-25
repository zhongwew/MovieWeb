package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import utils.ACache;

/**
 * Created by haiguai on 2/26/18.
 */

public class SearchActivity extends Activity {

    // Define ViewHolder
    private static class ViewHolder {
        public EditText searchEditText;
    }
    private ViewHolder viewHolder;
    private String keyword;
    private Context mContext;
    private ACache mCache;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize ViewHolder
        viewHolder = new ViewHolder();
        viewHolder.searchEditText = (EditText) findViewById(R.id.search_input);

        // Initialize ACache
        mCache = ACache.get(this);

        mContext = this;
    }

    /**
     * Send keyword to MovieResultsActivity,
     * MovieResultsActivity will send request to get results in the onCreate hook
     * @param view
     */
    public void search(View view) {
        // Check if it's loading
        if (isLoading) {
            return ;
        }

        // Do validation
        String validationInfo = validate(viewHolder);
        if (!validationInfo.equals("Success")) {
            Toast.makeText(this, validationInfo, Toast.LENGTH_SHORT).show();
            return ;
        }

        // Get keyword from input
        keyword = viewHolder.searchEditText.getText().toString();

        // Cache keyword
        mCache.put("keyword", keyword);

        gotoMovieResults();
    }

    /**
     * Check validation for search EditText
     * @param viewHolder
     * @return
     */
    private String validate(ViewHolder viewHolder) {
        if (viewHolder.searchEditText.getText().toString().equals("")) {
            return "Please enter a keyword!";
        }
        else {
            return "Success";
        }
    }

    /**
     * Move to MovieResultsActivity
     */
    private void gotoMovieResults() {
        Intent intent = new Intent(this, MovieResultsActivity.class);

        startActivity(intent);
    }
}
