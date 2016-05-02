package pn3.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by pulkitnarwani on 22/04/16.
 */
public class ImageAdapter extends BaseAdapter {

    Context c,main;

    ImageAdapter(Context con,Context m)
    {
        c=con;
        main=m;
    }
    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;
        imageView=(ImageView) convertView;
        if(imageView== null)
        {
            imageView = new ImageView(c);
            
        }
        imageView.setLayoutParams(new GridView.LayoutParams(600,700));
        SharedPreferences sp;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(main);
        String pref = sharedPref.getString("sort", "1");

        if(Integer.parseInt(pref)==1) {
            sp = c.getSharedPreferences("movie_det_popular", Context.MODE_PRIVATE);
        }else{
            sp = c.getSharedPreferences("movie_det_top", Context.MODE_PRIVATE);
        }


        Picasso.with(c).load(sp.getString("img" + String.valueOf(position),"R.mipmap.no_movie")).into(imageView);

        return imageView;
    }
}
