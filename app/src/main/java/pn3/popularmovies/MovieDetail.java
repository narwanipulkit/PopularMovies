package pn3.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetail extends AppCompatActivity {

    int position;
    int pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act);
        Bundle b=getIntent().getExtras();
        position=b.getInt("position");
        pref=b.getInt("pref");

        TextView titleView=(TextView)findViewById(R.id.mov_title);
        TextView overv=(TextView)findViewById(R.id.overview);
        TextView tv1=(TextView)findViewById(R.id.textView);
        TextView tv2=(TextView)findViewById(R.id.textView3);
        ImageView iv =(ImageView)findViewById(R.id.imageView);

        SharedPreferences s;


        if(pref==1)
        {s=getSharedPreferences("movie_det_popular", Context.MODE_PRIVATE);}
        else
        {
            s=getSharedPreferences("movie_det_top", Context.MODE_PRIVATE);
        }
        String title = s.getString("title" + String.valueOf(position), "");
        String overview=s.getString("overview"+String.valueOf(position),"");
        String img=s.getString("img"+String.valueOf(position),"");
        String rating=s.getString("rating"+String.valueOf(position),"-");
        String release=s.getString("release"+String.valueOf(position),"-");
        titleView.setText(title);
        overv.setText(overview);
        tv1.setText(rating);
        tv2.setText(release);

        if(img!="") {
            Picasso.with(this).load(img).into(iv);
        }
        else{
            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
        }


    }
}
