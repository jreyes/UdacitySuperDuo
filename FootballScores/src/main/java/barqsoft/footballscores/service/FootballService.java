package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import barqsoft.footballscores.FootballScoresApp;
import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.model.League;
import barqsoft.footballscores.model.Match;
import barqsoft.footballscores.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static barqsoft.footballscores.data.DatabaseContract.ScoresTable.*;
import static barqsoft.footballscores.util.NetworkUtil.isNetworkAvailable;

public class FootballService extends IntentService {
// ------------------------------ FIELDS ------------------------------

    public static final String LOG_TAG = FootballService.class.getSimpleName();

    private Map<String, String> mCrests;
    private FootballApi mFootballApi;
    private Map<String, String> mLeagueNames;

// --------------------------- CONSTRUCTORS ---------------------------

    public FootballService() {
        super(LOG_TAG);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        mFootballApi = FootballScoresApp.getFootballApi(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isNetworkAvailable(getApplicationContext())) {
            storeMatches(mFootballApi.getMatches("p2"));
            storeMatches(mFootballApi.getMatches("n4"));
        }
    }

    private String getLeagueName(String leagueId) {
        if (mLeagueNames == null) {
            mLeagueNames = new HashMap<>();
            for (League league : mFootballApi.getLeagues()) {
                mLeagueNames.put(league.leagueId, league.name);
            }
        }
        return mLeagueNames.get(leagueId);
    }

    private String getTeamCrest(String teamId) {
        if (mCrests == null) {
            mCrests = new HashMap<>();
            for (String leagueId : mLeagueNames.keySet()) {
                for (Team team : mFootballApi.getTeams(leagueId)) {
                    mCrests.put(team.teamId, team.crestUrl);
                }
            }
        }
        return mCrests.get(teamId);
    }

    private void storeMatches(List<Match> matches) {
        // create list of values to be stored
        List<ContentValues> matchList = new ArrayList<>();

        for (Match match : matches) {
            // now create the ContentValues
            ContentValues matchValues = new ContentValues();
            matchValues.put(MATCH_ID_COL, match.matchId);
            matchValues.put(DATE_COL, match.date);
            matchValues.put(TIME_COL, match.time);
            matchValues.put(HOME_COL, match.homeTeamName);
            matchValues.put(AWAY_COL, match.awayTeamName);
            matchValues.put(HOME_GOALS_COL, match.homeTeamGoals);
            matchValues.put(AWAY_GOALS_COL, match.awayTeamGoals);
            matchValues.put(LEAGUE_COL, getLeagueName(match.leagueId));
            matchValues.put(HOME_CREST_COL, getTeamCrest(match.homeTeamId));
            matchValues.put(AWAY_CREST_COL, getTeamCrest(match.awayTeamId));
            matchValues.put(MATCH_DAY_COL, match.matchDay);
            matchList.add(matchValues);
        }

        // insert matches
        ContentValues[] matchesValues = matchList.toArray(new ContentValues[matchList.size()]);
        getContentResolver().bulkInsert(DatabaseContract.BASE_CONTENT_URI, matchesValues);
    }
}

