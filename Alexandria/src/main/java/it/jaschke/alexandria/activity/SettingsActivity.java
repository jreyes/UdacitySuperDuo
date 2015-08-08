package it.jaschke.alexandria.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import it.jaschke.alexandria.R;

public class SettingsActivity extends PreferenceActivity {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
