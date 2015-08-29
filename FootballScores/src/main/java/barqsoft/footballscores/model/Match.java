package barqsoft.footballscores.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {
// ------------------------------ FIELDS ------------------------------

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        public Match createFromParcel(Parcel source) {
            return new Match(source);
        }

        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public String awayTeamCrest;
    public int awayTeamGoals;
    public String awayTeamId;
    public String awayTeamName;
    public String date;
    public String homeTeamCrest;
    public int homeTeamGoals;
    public String homeTeamId;
    public String homeTeamName;
    public String leagueId;
    public String leagueName;
    public String matchDay;
    public String matchId;
    public String time;

// --------------------------- CONSTRUCTORS ---------------------------

    public Match() {
    }

    protected Match(Parcel in) {
        this.awayTeamCrest = in.readString();
        this.awayTeamGoals = in.readInt();
        this.awayTeamId = in.readString();
        this.awayTeamName = in.readString();
        this.date = in.readString();
        this.homeTeamCrest = in.readString();
        this.homeTeamGoals = in.readInt();
        this.homeTeamId = in.readString();
        this.homeTeamName = in.readString();
        this.leagueId = in.readString();
        this.leagueName = in.readString();
        this.matchDay = in.readString();
        this.matchId = in.readString();
        this.time = in.readString();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Parcelable ---------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.awayTeamCrest);
        dest.writeInt(this.awayTeamGoals);
        dest.writeString(this.awayTeamId);
        dest.writeString(this.awayTeamName);
        dest.writeString(this.date);
        dest.writeString(this.homeTeamCrest);
        dest.writeInt(this.homeTeamGoals);
        dest.writeString(this.homeTeamId);
        dest.writeString(this.homeTeamName);
        dest.writeString(this.leagueId);
        dest.writeString(this.leagueName);
        dest.writeString(this.matchDay);
        dest.writeString(this.matchId);
        dest.writeString(this.time);
    }
}
