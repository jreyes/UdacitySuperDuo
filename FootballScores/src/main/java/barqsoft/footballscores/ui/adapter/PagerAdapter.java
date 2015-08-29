package barqsoft.footballscores.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.fragment.MatchFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PagerAdapter extends FragmentStatePagerAdapter {
// ------------------------------ FIELDS ------------------------------

    public static final int NUM_PAGES = 5;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormat.forPattern("EEEE");

    private Context mContext;
    private DateTime mDate;

// --------------------------- CONSTRUCTORS ---------------------------

    public PagerAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mDate = DateTime.now().minusDays(1);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        return MatchFragment.newInstance(DATE_FORMATTER.print(mDate.plusDays(position)));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.yesterday);
            case 1:
                return mContext.getString(R.string.today);
            case 2:
                return mContext.getString(R.string.tomorrow);
            default:
                return DAY_FORMATTER.print(mDate.plusDays(position));
        }
    }
}
