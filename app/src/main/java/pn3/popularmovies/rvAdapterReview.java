package pn3.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pulkitnarwani on 02/06/16.
 */
public class rvAdapterReview extends RecyclerView.Adapter<rvAdapterReview.ViewHolder> {

    static JSONArray rs;

    static Context mc;

    rvAdapterReview(JSONArray dataSet, Context c)
    {
        rs=dataSet;
        mc=c;
    }

    @Override
    public rvAdapterReview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(rvAdapterReview.ViewHolder holder, int position) {
        try {
            holder.mTextView1.setText(rs.getJSONObject(position).get("author").toString());
            holder.mTextView2.setText(rs.getJSONObject(position).get("content").toString());
            Log.e("Review",rs.getJSONObject(position).get("content").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return rs.length();


    }




    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView1,mTextView2;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mTextView1=(TextView)v.findViewById(R.id.author);
            mTextView2=(TextView)v.findViewById(R.id.content);


        }

        @Override
        public void onClick(View v) {
            int position=getPosition();
            try {
                JSONObject a=rs.getJSONObject(position);
                String url=a.get("url").toString();

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mc.startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
