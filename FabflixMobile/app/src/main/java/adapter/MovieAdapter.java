package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.fabflixmobile.R;
import entity.Movie;
import entity.Star;

/**
 * Created by haiguai on 2/26/18.
 */

public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Movie> movies;
    private static final HashMap<Integer, Integer> LABEL_COLORS = new HashMap<Integer, Integer>() {
        {
            put(0, R.color.colorLowCarb);
            put(1, R.color.colorLowFat);
            put(2, R.color.colorLowSodium);
            put(3, R.color.colorMediumCarb);
            put(4, R.color.colorVegetarian);
            put(5, R.color.colorBalanced);
        }
    };
    private static class ViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public TextView detailTextView;
        public ImageView thumbnailImageView;
    }

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        this.movies = movies;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View counterView, ViewGroup parent) {
        ViewHolder holder = null;
        View convertView = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.result_item, parent, false);

            holder = new ViewHolder();
            // Get title element
            holder.titleTextView = (TextView) convertView.findViewById(R.id.result_item_title);

            // Get subtitle element
            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.result_item_subtitle);

            // Get detail element
            holder.detailTextView = (TextView) convertView.findViewById(R.id.result_item_detail);

            // Get thumbnail element
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.result_item_thumbnail);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        TextView detailTextView = holder.detailTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;

        // Get each movie
        Movie movie = (Movie) getItem(position);

        // Set info for each item
        titleTextView.setText(movie.getTitle());
        subtitleTextView.setText("By: " + movie.getDirector() + " | In " + movie.getYear());
        thumbnailImageView.setImageResource(R.drawable.movie_avatar);

        // Set font
        Typeface titleTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/JosefinSans-Bold.ttf");
        Typeface subtitleTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/JosefinSans-SemiBoldItalic.ttf");
        Typeface detailTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/Quicksand-Bold.otf");
        titleTextView.setTypeface(titleTypeFace);
        subtitleTextView.setTypeface(subtitleTypeFace);
        detailTextView.setTypeface(detailTypeFace);

        // Set color
        subtitleTextView.setTextColor(LABEL_COLORS.get(movie.getTitle().length() % 5));

        return convertView;
    }
}
