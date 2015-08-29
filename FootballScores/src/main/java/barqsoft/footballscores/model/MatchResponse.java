
package barqsoft.footballscores.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatchResponse {
// ------------------------------ FIELDS ------------------------------

    @SerializedName("fixtures")
    public List<Match> matches;
}
