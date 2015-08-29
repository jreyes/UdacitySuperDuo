package barqsoft.footballscores.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.adapter.PagerAdapter;

public class PagerFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    private static final int POSITION_TODAY = 1;

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

// -------------------------- OTHER METHODS --------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPagerAdapter();
        initViewPager(view);
        initTabLayout(view);
    }

    private void initPagerAdapter() {
        mPagerAdapter = new PagerAdapter(getActivity());
    }

    private void initTabLayout(View view) {
        ((TabLayout) view.findViewById(R.id.tab_layout)).setupWithViewPager(mViewPager);
    }

    private void initViewPager(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(PagerAdapter.NUM_PAGES);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(POSITION_TODAY);
    }
}
