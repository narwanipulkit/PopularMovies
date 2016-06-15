package pn3.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;


public class DetailFragment extends Fragment {

    int position;
    int pref;
    ImageButton star;

    SharedPreferences s;
    String title,overview,img,rating,release,id;



    RecyclerView recyclerView,recyclerViewReview;
    private final String api_key="";//PUT API KEY HERE

    private OnFragmentInteractionListener mListener;


    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();

        return fragment;
    }
    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        pref = getArguments().getInt("pref");
        position = getArguments().getInt("position");

        View iView=inflater.inflate(R.layout.fragment_detail, container, false);

        TextView titleView=(TextView)iView.findViewById(R.id.mov_title);
        TextView overv=(TextView)iView.findViewById(R.id.overview);
        TextView tv1=(TextView)iView.findViewById(R.id.textView);
        TextView tv2=(TextView)iView.findViewById(R.id.textView3);
        ImageView iv =(ImageView)iView.findViewById(R.id.imageView);





        if(pref==1)
        {s=this.getActivity().getSharedPreferences("movie_det_popular", Context.MODE_PRIVATE);
            getSharedData();}

        else if(pref==2)
        {
            SQLiteDatabase favs=getActivity().openOrCreateDatabase("favs",getActivity().MODE_PRIVATE,null);
            Cursor cur=favs.rawQuery("Select * from favsdet",null);
            if(cur!=null)
            {
                if(cur.getCount()!=0)
                {
                    cur.moveToPosition(position);
                    id=cur.getString(0);
                    title=cur.getString(1);
                    img=cur.getString(2);
                    rating=cur.getString(3);
                    release=cur.getString(4);
                    overview=cur.getString(5);
                }
            }
        }
        else
        {
            s=this.getActivity().getSharedPreferences("movie_det_top", Context.MODE_PRIVATE);
            getSharedData();
        }







        star=(ImageButton)iView.findViewById(R.id.star);
        checkInDB();
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(star.getTag().equals("1"))
                {
                    star.setTag("0");
                    star.setImageResource(android.R.drawable.star_off);
                    Toast.makeText(getActivity().getBaseContext(),"Removed From Favourite",Toast.LENGTH_SHORT).show();
                    remove();
                }
                else {
                    star.setTag("1");
                    star.setImageResource(android.R.drawable.star_on);
                    Toast.makeText(getActivity().getBaseContext(), "Added To Favourite", Toast.LENGTH_SHORT).show();

                    addToDB();

                }

            }
        });


        titleView.setText(title);
        overv.setText(overview);
        tv1.setText(rating);
        tv2.setText(release);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url_trailer="http://api.themoviedb.org/3/movie/"+id+"/videos"+"?api_key="+api_key;
        String url_reviews="http://api.themoviedb.org/3/movie/"+id+"/reviews"+"?api_key="+api_key;

        JSONObject j= null;
        JSONObject jReview= null;
        try {
            j = new JSONObject("");
            jReview = new JSONObject("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url_trailer, j,new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray results=response.getJSONArray("results");


                    recyclerView=(RecyclerView)getView().findViewById(R.id.trailers);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    recyclerView.setAdapter(new rvAdapter(results,getActivity().getBaseContext()));





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(jsonObjectRequest);


        JsonObjectRequest jsonObjectRequestReview = new JsonObjectRequest(Request.Method.GET,url_reviews, jReview,new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray results=response.getJSONArray("results");


                    recyclerViewReview=(RecyclerView)getView().findViewById(R.id.reviews);
                    recyclerViewReview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    recyclerViewReview.setAdapter(new rvAdapterReview(results,getActivity().getBaseContext()));




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(jsonObjectRequestReview);


        if(img!="") {
            Picasso.with(getActivity().getApplicationContext()).load(img).into(iv);
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
        }





    return iView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void addToDB()
    {

        SQLiteDatabase favs=getActivity().openOrCreateDatabase("favs",getActivity().MODE_PRIVATE,null);
        favs.execSQL("create table if not exists favsdet(id text,name text,img text,rating text,release text,overview text)");
        favs.execSQL("insert into favsdet values('"+id+"','"+title+"','"+img+"','"+rating+"','"+release+"','"+overview.replace("'","")+"')");




    }

    public void checkInDB(){
        SQLiteDatabase favs=getActivity().openOrCreateDatabase("favs",getActivity().MODE_PRIVATE,null);
        favs.execSQL("create table if not exists favsdet(id text,name text,img text,rating text,release text,overview text)");
        Log.e("id",id);
        Cursor c=favs.rawQuery("Select * from favsdet where id='"+id+"'",null);
        if(c!=null)
        {
            if(c.getCount()!=0) {
                star.setTag("1");
                star.setImageResource(android.R.drawable.star_on);
            }
            else{
                star.setTag("0");
            }
        }
        else{
            star.setTag("0");
        }
    }

    public void remove()
    {
        SQLiteDatabase favs=getActivity().openOrCreateDatabase("favs",getActivity().MODE_PRIVATE,null);
        favs.execSQL("delete from favsdet where id='"+id+"'");


    }
    public void getSharedData(){
        title = s.getString("title" + String.valueOf(position), "");
        overview=s.getString("overview"+String.valueOf(position),"");
        img=s.getString("img"+String.valueOf(position),"");
        rating=s.getString("rating"+String.valueOf(position),"-");
        release=s.getString("release"+String.valueOf(position),"-");
        id=s.getString("id"+String.valueOf(position),"0");

    }
}
