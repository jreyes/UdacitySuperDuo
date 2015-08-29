package barqsoft.footballscores.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
// ------------------------------ FIELDS ------------------------------

    public static final String CONTENT_AUTHORITY = "barqsoft.footballscores";
    public static final String PATH = "scores";
    public static final String SCORES_TABLE = "scores_table";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

// -------------------------- INNER CLASSES --------------------------

    public static final class ScoresTable implements BaseColumns {
        //Table data
        public static final String LEAGUE_COL = "league";
        public static final String DATE_COL = "date";
        public static final String TIME_COL = "time";
        public static final String HOME_COL = "home";
        public static final String AWAY_COL = "away";
        public static final String HOME_GOALS_COL = "home_goals";
        public static final String AWAY_GOALS_COL = "away_goals";
        public static final String MATCH_ID_COL = "match_id";
        public static final String MATCH_DAY_COL = "match_day";
        public static final String HOME_CREST_COL = "home_crest";
        public static final String AWAY_CREST_COL = "away_crest";

        // Types
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PATH;

        // Queries
        public static Uri buildScoreWithDate() {
            return BASE_CONTENT_URI.buildUpon().appendPath(DATE_COL).build();
        }
    }
}
