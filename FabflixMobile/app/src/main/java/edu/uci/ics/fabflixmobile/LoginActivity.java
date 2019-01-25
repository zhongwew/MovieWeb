package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    // Define ViewHolder
    private static class ViewHolder {
        public EditText emailEditText;
        public EditText passwordEditText;
    }

    private ViewHolder viewHolder;
    private Context mContext;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private boolean isLoading;

    // No user is logged in, so we must connect to the server
    private RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = Volley.newRequestQueue(this);

        // Initialize ViewHolder
        viewHolder = new ViewHolder();
        viewHolder.emailEditText = (EditText) findViewById(R.id.email_input);
        viewHolder.passwordEditText = (EditText) findViewById(R.id.password_input);

        // Initialize SharedPreference
        settings = getSharedPreferences("setting", 0);
        editor = settings.edit();

        // Get this activity context
        mContext = this;

        // If user is login, then move to search activity
        checkLoginState();
    }

    /**
     * Send http request to login
     *
     * @param view
     */
    public void login(View view) {
        if (isLoading) {
            return ;
        }

        // Do validation
        String validationInfo = validate(viewHolder);
        if (!validationInfo.equals("Success")) {
            Toast.makeText(this, validationInfo, Toast.LENGTH_SHORT).show();
            return;
        }

        isLoading = true;

        // Define login route
        String url = getResources().getString(R.string.host) + getResources().getString(R.string.login_route);

        // Define parameter mapping
        final Map<String, String> params = new HashMap<String, String>();

        // Get parameters from EditText
        String email = viewHolder.emailEditText.getText().toString();
        String password = viewHolder.passwordEditText.getText().toString();
        // Put login parameters to http request
        params.put("email", email);
        params.put("password", password);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Handle JSON Exception
                try {
                    JSONObject responseObj = new JSONObject(response.toString());

                    if (responseObj.get("type").toString().equals("success")) {
                        Toast.makeText(mContext, "Login successfully!", Toast.LENGTH_SHORT).show();

                        // Cache login state
                        editor.putBoolean("isLogin", true);
                        editor.commit();

                        // Move to SearchActivity
                        goToSearch();
                    } else {
                        // Show error message
                        Toast.makeText(mContext, responseObj.getJSONObject("message").get("errorMessage").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }

                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("security.error", error.toString());

                isLoading = false;
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);

        return;
    }

    private void checkLoginState() {
        if (settings.getBoolean("isLogin", false)) {
            goToSearch();
        }
    }

    /**
     * Validate login information
     *
     * @param viewHolder
     * @return
     */
    private String validate(ViewHolder viewHolder) {
        if (viewHolder.emailEditText.getText().toString().equals("") || viewHolder.passwordEditText.getText().toString().equals("")) {
            return "Please enter your account!";
        } else {
            return "Success";
        }
    }

    /**
     * Move to search box activity
     */
    private void goToSearch() {
        // Initialize Intent
        Intent goToIntent = new Intent(this, SearchActivity.class);

        startActivity(goToIntent);
    }
}
