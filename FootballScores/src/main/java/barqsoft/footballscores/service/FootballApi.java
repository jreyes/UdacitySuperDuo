package barqsoft.footballscores.service;

import barqsoft.footballscores.model.MatchResponse;
import barqsoft.footballscores.model.Match;
import barqsoft.footballscores.model.League;
import barqsoft.footballscores.model.Team;
import barqsoft.footballscores.model.TeamResponse;
import barqsoft.footballscores.util.JsonUtil;

import com.google.gson.*;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

import java.lang.reflect.Type;
import java.util.List;

public class FootballApi {
// ------------------------------ FIELDS ------------------------------

    public static final String API_ENDPOINT = "http://api.football-data.org/alpha";
    public static final String LEAGUE_URL_PATH = API_ENDPOINT + "/soccerseasons/";
    public static final String MATCH_URL_PATH = API_ENDPOINT + "/fixtures/";
    public static final String TEAM_URL_PATH = API_ENDPOINT + "/teams/";
    private static final String API_KEY = "88d436deae424722902d0f6ff38bd207";

    private Api mApi;

// --------------------------- CONSTRUCTORS ---------------------------

    public FootballApi(OkHttpClient okHttpClient) {
        this.mApi = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(API_ENDPOINT)
                .setConverter(gsonConverter())
                .build()
                .create(Api.class);
    }

// -------------------------- OTHER METHODS --------------------------

    public List<League> getLeagues() {
        return mApi.getLeagues();
    }

    public List<Match> getMatches(final String timeFrame) {
        return mApi.getMatches(timeFrame).matches;
    }

    public List<Team> getTeams(final String leagueId) {
        return mApi.getTeams(leagueId).teams;
    }

    private GsonConverter gsonConverter() {
        return new GsonConverter(new GsonBuilder()
                .registerTypeAdapter(League.class, new LeagueDeserializer())
                .registerTypeAdapter(Match.class, new MatchDeserializer())
                .registerTypeAdapter(Team.class, new TeamDeserializer())
                .create());
    }

// -------------------------- INNER CLASSES --------------------------

    interface Api {
        @GET("/fixtures")
        @Headers("X-Auth-Token: " + API_KEY)
        MatchResponse getMatches(@Query("timeFrame") String timeFrame);

        @GET("/soccerseasons/{id}/teams")
        @Headers("X-Auth-Token: " + API_KEY)
        TeamResponse getTeams(@Path("id") String leagueId);

        @GET("/soccerseasons")
        @Headers("X-Auth-Token: " + API_KEY)
        List<League> getLeagues();
    }

    static class LeagueDeserializer implements JsonDeserializer<League> {
        @Override
        public League deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            League league = new League();
            league.leagueId = JsonUtil.toString(element, "_links/self/href").replace(LEAGUE_URL_PATH, "");
            league.name = JsonUtil.toString(element, "caption");
            return league;
        }
    }

    static class MatchDeserializer implements JsonDeserializer<Match> {
        @Override
        public Match deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            Match match = new Match();
            match.matchId = JsonUtil.toString(element, "_links/self/href").replace(MATCH_URL_PATH, "");
            match.leagueId = JsonUtil.toString(element, "_links/soccerseason/href").replace(LEAGUE_URL_PATH, "");
            match.homeTeamId = JsonUtil.toString(element, "_links/homeTeam/href").replace(TEAM_URL_PATH, "");
            match.homeTeamName = JsonUtil.toString(element, "homeTeamName");
            match.homeTeamGoals = JsonUtil.toInteger(element, "result/goalsHomeTeam");
            match.awayTeamId = JsonUtil.toString(element, "_links/awayTeam/href").replace(TEAM_URL_PATH, "");
            match.awayTeamName = JsonUtil.toString(element, "awayTeamName");
            match.awayTeamGoals = JsonUtil.toInteger(element, "result/goalsAwayTeam");
            match.matchDay = JsonUtil.toString(element, "matchday");
            match.date = JsonUtil.toDate(element, "date");
            match.time = JsonUtil.toTime(element, "date");
            return match;
        }
    }

    static class TeamDeserializer implements JsonDeserializer<Team> {
        @Override
        public Team deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            Team team = new Team();
            team.teamId = JsonUtil.toString(element, "_links/self/href").replace(TEAM_URL_PATH, "");
            team.crestUrl = JsonUtil.toString(element, "crestUrl");
            return team;
        }
    }
}
