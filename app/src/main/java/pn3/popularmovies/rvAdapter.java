package pn3.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by pulkitnarwani on 02/06/16.
 */
public class rvAdapter extends RecyclerView.Adapter<rvAdapter.ViewHolder> {

    static JSONArray rs;
    TextView tv;
    static Context mc;

    rvAdapter(JSONArray dataSet,Context c)
    {
        rs=dataSet;
        mc=c;
    }

    @Override
    public rvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailerlistitem,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(rvAdapter.ViewHolder holder, int position) {
        try {
            holder.mTextView.setText(rs.getJSONObject(position).get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return rs.length();


    }




    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mTextView=(TextView)v.findViewById(R.id.trailerName);

        }

        @Override
        public void onClick(View v) {
            int position=getPosition();
            try {
                JSONObject a=rs.getJSONObject(position);
                String key=a.get("key").toString();

                Uri uri = Uri.parse("https://www.youtube.com/watch?v="+key);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mc.startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
