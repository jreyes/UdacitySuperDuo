package barqsoft.footballscores.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder {
// ------------------------------ FIELDS ------------------------------

    public ImageView away_crest;
    public TextView away_name;
    public TextView date;
    public ImageView home_crest;
    public TextView home_name;
    public double match_id;
    public TextView score;

// --------------------------- CONSTRUCTORS ---------------------------

    public ViewHolder(View view) {
        home_name = (TextView) view.findViewById(R.id.home_name);
        away_name = (TextView) view.findViewById(R.id.away_name);
        score = (TextView) view.findViewById(R.id.score_textview);
        date = (TextView) view.findViewById(R.id.data_textview);
        home_crest = (ImageView) view.findViewById(R.id.home_crest);
        away_crest = (ImageView) view.findViewById(R.id.away_crest);
    }
}
