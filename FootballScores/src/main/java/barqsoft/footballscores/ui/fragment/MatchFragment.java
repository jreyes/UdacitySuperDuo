package barqsoft.footballscores.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import barqsoft.footballscores.R;
import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.model.Match;
import barqsoft.footballscores.ui.adapter.MatchAdapter;
import barqsoft.footballscores.util.MatchUtil;

public class MatchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
// ------------------------------ FIELDS ------------------------------

    private static final String KEY_DATE = "KEY_DATE";
    private static final String KEY_MATCHES = "KEY_MATCHES";
    private static final int SCORES_LOADER = 0;

    private MatchAdapter mAdapter;
    private String mDate;
    private ArrayList<Match> mMatches;

// -------------------------- STATIC METHODS --------------------------

    public static MatchFragment newInstance(String date) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_DATE, date);

        MatchFragment fragment = new MatchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface LoaderCallbacks ---------------------

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getActivity(),
                DatabaseContract.ScoresTable.buildScoreWithDate(),
                null,
                null,
                new String[]{mDate},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }

        // now set the items in the adapter
        mMatches = MatchUtil.getMatches(cursor);
        mAdapter.setItems(mMatches);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mDate = getArguments().getString(KEY_DATE);
            mMatches = new ArrayList<>();
            getLoaderManager().initLoader(SCORES_LOADER, null, this);
        } else {
            mDate = savedInstanceState.getString(KEY_DATE);
            mMatches = savedInstanceState.getParcelableArrayList(KEY_MATCHES);
        }
        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_DATE, mDate);
        outState.putParcelableArrayList(KEY_MATCHES, mMatches);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new MatchAdapter(getActivity(), mMatches);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }
}
