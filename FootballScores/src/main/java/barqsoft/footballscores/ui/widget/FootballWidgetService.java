package barqsoft.footballscores.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import barqsoft.footballscores.R;
import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.model.Match;
import barqsoft.footballscores.util.ImageUtil;
import barqsoft.footballscores.util.MatchUtil;

import static barqsoft.footballscores.util.MatchUtil.getDateDescription;
import static barqsoft.footballscores.util.MatchUtil.getScoresDescription;

public class FootballWidgetService extends RemoteViewsService {
// ------------------------------ FIELDS ------------------------------

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

// -------------------------- OTHER METHODS --------------------------

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballViewsFactory(this);
    }

// -------------------------- INNER CLASSES --------------------------

    class FootballViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        FootballViewsFactory(Context context) {
            mContext = context.getApplicationContext();
        }

        private Context mContext;
        private ArrayList<Match> mMatches;

        private Cursor cursor() {
            return getContentResolver().query(
                    DatabaseContract.ScoresTable.buildScoreWithDate(),
                    null,
                    null,
                    new String[]{DATE_FORMATTER.print(DateTime.now())},
                    null
            );
        }

        @Override
        public void onCreate() {
            mMatches = MatchUtil.getMatches(cursor());
        }

        @Override
        public void onDataSetChanged() {
            onCreate();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return mMatches.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final Match match = mMatches.get(position);

            final RemoteViews row = new RemoteViews(getPackageName(), R.layout.list_item_widget_match);
            row.setTextViewText(R.id.widget_home_name, match.homeTeamName);
            row.setTextViewText(R.id.widget_away_name, match.awayTeamName);
            row.setTextViewText(R.id.widget_score, MatchUtil.getScores(match));
            row.setContentDescription(R.id.widget_score, getScoresDescription(mContext, match));
            row.setTextViewText(R.id.widget_date, match.time);
            row.setContentDescription(R.id.widget_date, getDateDescription(mContext, match));
            ImageUtil.load(mContext, match.homeTeamCrest, row, R.id.widget_home_crest);
            ImageUtil.load(mContext, match.awayTeamCrest, row, R.id.widget_away_crest);
            return row;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
