package pn3.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pulkitnarwani on 12/04/16.
 */
public class Fetch extends AsyncTask<Context,String,String>{


    Context c;
    ProgressBar pd;
    public static String base_url;
    public static String sizes[];
    public String size;

    private final String api_key="";//Put API KEY HERE
    String config_url="http://api.themoviedb.org/3/configuration?api_key="+api_key;
    String out="";
    String out2="";
    String pop="http://api.themoviedb.org/3/movie/popular?api_key=";
    String top="http://api.themoviedb.org/3/movie/top_rated?api_key=";

    Fetch(ProgressBar a)
    {
        pd=a;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Context... params) {

        c=params[0];


        HttpURLConnection urlConnection;
        StringBuilder output=new StringBuilder();
        try {

            //Fetch base URL and Sizes JSON
            URL url=new URL(config_url);
            urlConnection = (HttpURLConnection)url.openConnection();
            InputStream in= new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader b=new BufferedReader(new InputStreamReader(in));

            String l;
            while((l=b.readLine())!=null)
            {
             output.append(l);

            }

            //Get Base URL
            JSONObject output_json=new JSONObject(output.toString());
            JSONObject output_part=new JSONObject(output_json.get("images").toString());
            base_url=output_part.get("base_url").toString();


            //Get Available Sizes
            size=output_part.get("logo_sizes").toString();
            sizes=size.substring(1, (size.length() - 1)).split(",");
            out=base_url+sizes[3].substring(1,sizes[3].length()-1);
            out2=base_url+sizes[5].substring(1, sizes[5].length() - 1);


            fetchPopular();
            fetchTop();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return "";

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pd.setVisibility(View.GONE);



    }

    //Fetch Popular Movies
    public void fetchPopular()
    {
        URL mov_url = null;
        try {
            mov_url = new URL(pop + api_key);
            HttpURLConnection urlConnection2 = (HttpURLConnection) mov_url.openConnection();
            InputStream is2 = new BufferedInputStream(urlConnection2.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is2));
            String l2;
            StringBuilder mov_out = new StringBuilder();
            while ((l2 = br.readLine()) != null) {
                mov_out.append(l2);
            }

            JSONObject movies = new JSONObject(mov_out.toString());


            JSONArray results = new JSONArray(movies.get("results").toString());


            //Add movie data to Shared Preferences with movie details
            SharedPreferences sp = c.getSharedPreferences("movie_det_popular", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();


            for (int i = 0; i < 20; i++) {
                JSONObject temp = new JSONObject(results.get(i).toString());

                editor.putString("img" + String.valueOf(i), out + temp.getString("poster_path").toString());
                editor.putString("overview" + String.valueOf(i), temp.get("overview").toString());
                editor.putString("title" + String.valueOf(i), temp.get("title").toString());
                editor.putString("rating" + String.valueOf(i), temp.get("vote_average").toString());
                editor.putString("release" + String.valueOf(i), temp.get("release_date").toString());

            }
            editor.apply();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void fetchTop()
    {
        URL mov_url = null;
        try {
            mov_url = new URL(top + api_key);
            HttpURLConnection urlConnection2 = (HttpURLConnection) mov_url.openConnection();
            InputStream is2 = new BufferedInputStream(urlConnection2.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is2));
            String l2;
            StringBuilder mov_out = new StringBuilder();
            while ((l2 = br.readLine()) != null) {
                mov_out.append(l2);
            }

            JSONObject movies = new JSONObject(mov_out.toString());


            JSONArray results = new JSONArray(movies.get("results").toString());


            //Add movie data to Shared Preferences with movie details
            SharedPreferences sp = c.getSharedPreferences("movie_det_top", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();


            for (int i = 0; i < 20; i++) {
                JSONObject temp = new JSONObject(results.get(i).toString());

                editor.putString("img" + String.valueOf(i), out + temp.getString("poster_path").toString());
                editor.putString("overview" + String.valueOf(i), temp.get("overview").toString());
                editor.putString("title" + String.valueOf(i), temp.get("title").toString());
                editor.putString("rating" + String.valueOf(i), temp.get("vote_average").toString());
                editor.putString("release" + String.valueOf(i), temp.get("release_date").toString());

            }
            editor.apply();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
