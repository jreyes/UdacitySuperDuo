package barqsoft.footballscores.util;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import barqsoft.footballscores.R;
import barqsoft.footballscores.model.Match;

public class MatchUtil {
// ------------------------------ FIELDS ------------------------------

    private static final int COL_AWAY = 4;
    private static final int COL_AWAY_CREST = 9;
    private static final int COL_AWAY_GOALS = 7;
    private static final int COL_HOME = 3;
    private static final int COL_HOME_CREST = 8;
    private static final int COL_HOME_GOALS = 6;
    private static final int COL_LEAGUE = 5;
    private static final int COL_MATCH_DATE = 1;
    private static final int COL_MATCH_DAY = 11;
    private static final int COL_MATCH_ID = 10;
    private static final int COL_MATCH_TIME = 2;

// -------------------------- STATIC METHODS --------------------------

    public static CharSequence getDateDescription(Context context, Match match) {
        return context.getString(R.string.date_description, match.time);
    }

    public static ArrayList<Match> getMatches(Cursor cursor) {
        ArrayList<Match> matches = new ArrayList<>();
        while (cursor.moveToNext()) {
            Match match = new Match();
            match.matchId = cursor.getString(COL_MATCH_ID);
            match.date = cursor.getString(COL_MATCH_DATE);
            match.time = cursor.getString(COL_MATCH_TIME);
            match.homeTeamName = cursor.getString(COL_HOME);
            match.awayTeamName = cursor.getString(COL_AWAY);
            match.homeTeamGoals = cursor.getInt(COL_HOME_GOALS);
            match.awayTeamGoals = cursor.getInt(COL_AWAY_GOALS);
            match.homeTeamCrest = cursor.getString(COL_HOME_CREST);
            match.awayTeamCrest = cursor.getString(COL_AWAY_CREST);
            match.leagueName = cursor.getString(COL_LEAGUE);
            match.matchDay = cursor.getString(COL_MATCH_DAY);
            matches.add(match);
        }
        // close cursor
        cursor.close();

        // now return the values
        return matches;
    }

    public static String getScores(Match match) {
        if (match.homeTeamGoals < 0 || match.awayTeamGoals < 0) {
            return "-";
        } else {
            return String.valueOf(match.homeTeamGoals) + " - " + String.valueOf(match.awayTeamGoals);
        }
    }

    public static String getScoresDescription(Context context, Match match) {
        return context.getString(R.string.score_description, getScores(match));
    }

    public static String getShareMessage(Context context, Match match) {
        return context.getString(R.string.share_message, match.homeTeamName, match.awayTeamName, getScores(match));
    }
}
