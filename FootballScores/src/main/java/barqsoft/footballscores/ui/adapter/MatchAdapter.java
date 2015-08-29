package barqsoft.footballscores.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import barqsoft.footballscores.R;
import barqsoft.footballscores.model.Match;
import barqsoft.footballscores.util.ImageUtil;

import static barqsoft.footballscores.util.MatchUtil.getDateDescription;
import static barqsoft.footballscores.util.MatchUtil.getScores;
import static barqsoft.footballscores.util.MatchUtil.getScoresDescription;
import static barqsoft.footballscores.util.MatchUtil.getShareMessage;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
// ------------------------------ FIELDS ------------------------------

    private Context mContext;
    private ArrayList<Match> mItems;

// --------------------------- CONSTRUCTORS ---------------------------

    public MatchAdapter(Context context, ArrayList<Match> items) {
        mContext = context;
        mItems = items;
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Match match = mItems.get(position);
        holder.homeName.setText(match.homeTeamName);
        holder.awayName.setText(match.awayTeamName);
        holder.score.setText(getScores(match));
        holder.score.setContentDescription(getScoresDescription(mContext, match));
        holder.date.setText(match.time);
        holder.date.setContentDescription(getDateDescription(mContext, match));
        ImageUtil.load(mContext, match.homeTeamCrest, holder.homeCrest);
        ImageUtil.load(mContext, match.awayTeamCrest, holder.awayCrest);
        holder.share.setOnClickListener(onShareClickListener(match));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_match, parent, false);
        return new ViewHolder(v);
    }

    public void setItems(ArrayList<Match> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private View.OnClickListener onShareClickListener(final Match match) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                } else {
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                }
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getShareMessage(mContext, match));
                v.getContext().startActivity(shareIntent);
            }
        };
    }

// -------------------------- INNER CLASSES --------------------------

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView awayCrest;
        public TextView awayName;
        public TextView date;
        public ImageView homeCrest;
        public TextView homeName;
        public TextView score;
        public Button share;

        public ViewHolder(View view) {
            super(view);
            homeName = (TextView) view.findViewById(R.id.home_name);
            awayName = (TextView) view.findViewById(R.id.away_name);
            score = (TextView) view.findViewById(R.id.score);
            date = (TextView) view.findViewById(R.id.date);
            homeCrest = (ImageView) view.findViewById(R.id.home_crest);
            awayCrest = (ImageView) view.findViewById(R.id.away_crest);
            share = (Button) view.findViewById(R.id.share);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            share.setVisibility(share.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }
}
