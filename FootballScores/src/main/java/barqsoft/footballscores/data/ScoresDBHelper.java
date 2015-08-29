package barqsoft.footballscores.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.data.DatabaseContract.ScoresTable;

public class ScoresDBHelper extends SQLiteOpenHelper {
// ------------------------------ FIELDS ------------------------------

    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 4;

// --------------------------- CONSTRUCTORS ---------------------------

    public ScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String scoresTable = "CREATE TABLE " + DatabaseContract.SCORES_TABLE + " ("
                + ScoresTable._ID + " INTEGER PRIMARY KEY,"
                + ScoresTable.DATE_COL + " TEXT NOT NULL,"
                + ScoresTable.TIME_COL + " INTEGER NOT NULL,"
                + ScoresTable.HOME_COL + " TEXT NOT NULL,"
                + ScoresTable.AWAY_COL + " TEXT NOT NULL,"
                + ScoresTable.LEAGUE_COL + " INTEGER NOT NULL,"
                + ScoresTable.HOME_GOALS_COL + " TEXT NOT NULL,"
                + ScoresTable.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + ScoresTable.HOME_CREST_COL + " TEXT NOT NULL,"
                + ScoresTable.AWAY_CREST_COL + " TEXT NOT NULL,"
                + ScoresTable.MATCH_ID_COL + " INTEGER NOT NULL,"
                + ScoresTable.MATCH_DAY_COL + " INTEGER NOT NULL,"
                + " UNIQUE (" + ScoresTable.MATCH_ID_COL + ") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(scoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SCORES_TABLE);
        onCreate(db);
    }
}
