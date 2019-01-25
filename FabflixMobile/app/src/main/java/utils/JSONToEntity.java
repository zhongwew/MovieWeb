package utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entity.Movie;
import entity.Star;

/**
 * Created by haiguai on 2/27/18.
 */

public class JSONToEntity {

    /**
     * Convert a JSON Array to movie list
     * @param JSONArr
     * @return
     */
    public static ArrayList<Movie> toMovies(JSONArray JSONArr) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            int length = JSONArr.length();

            for(int i = 0 ; i < length ; i++) {
                movies.add(toMovie(JSONArr.getJSONObject(i)));
            }
        }
        catch(JSONException e) {
            Log.e("test", e.toString());
        }

        return movies;
    }

    /**
     * Convert a JSON Array to star list
     * @param JSONArr
     * @return
     */
    public static ArrayList<Star> toStars(JSONArray JSONArr) {
        ArrayList<Star> stars = new ArrayList<>();

        try {
            int length = JSONArr.length();

            for(int i = 0 ; i < length ; i++) {
                stars.add(toStar(JSONArr.getJSONObject(i)));
            }
        }
        catch(JSONException e) {
            Log.e("test", e.toString());
        }

        return stars;
    }

    /**
     * Convert a JSON Object to a Movie
     * @param JSONObj
     * @return
     */
    public static Movie toMovie(JSONObject JSONObj) {
        Movie movie = null;

        try {
            String id = JSONObj.get("id").toString();
            String title = JSONObj.get("title").toString();
            int year = Integer.parseInt(JSONObj.get("year").toString());
            String director = JSONObj.get("director").toString();

            movie = new Movie(id, title, year, director);
        }
        catch(JSONException e) {
            Log.e("test", e.toString());
        }

        return movie;
    }

    /**
     * Convert a JSON Object to a Star
     * @param JSONObj
     * @return
     */
    public static Star toStar(JSONObject JSONObj) {
        Star star = null;

        try {
            String id = JSONObj.get("id").toString();
            String name = JSONObj.get("name").toString();
            int birthYear = Integer.parseInt(JSONObj.get("birthYear").toString());

            star = new Star(id, name, birthYear);
        }
        catch(JSONException e) {
            Log.e("test", e.toString());
        }

        return star;
    }
}
