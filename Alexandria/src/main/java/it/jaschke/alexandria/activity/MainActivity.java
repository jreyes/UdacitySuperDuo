package it.jaschke.alexandria.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.fragment.*;
import it.jaschke.alexandria.services.BookService;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, Callback {
// ------------------------------ FIELDS ------------------------------

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";
    private NavigationDrawerFragment mDrawer;

    private BroadcastReceiver mMessageReceiver;
    private boolean mTwoPane;

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(MESSAGE_KEY) != null) {
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Callback ---------------------

    @Override
    public void onItemSelected(String ean) {
        Bundle args = new Bundle();
        args.putLong(BookService.EAN, Long.valueOf(ean));

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        if (mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.right_container, fragment, BookDetail.TAG)
                    .addToBackStack(BookDetail.TAG)
                    .commit();
        } else {
            mDrawer.setDrawerIndicatorEnabled(false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, BookDetail.TAG)
                    .addToBackStack(BookDetail.TAG)
                    .commit();
        }
    }

// --------------------- Interface NavigationDrawerCallbacks ---------------------

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // clear the back stack
        while (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        // now select the fragment to display
        Fragment nextFragment;
        switch (position) {
            default:
            case 0:
                nextFragment = new ListOfBooks();
                break;
            case 1:
                nextFragment = new AddBook();
                break;
            case 2:
                nextFragment = new About();
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, nextFragment)
                .commit();
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onBackPressed() {
        mDrawer.setDrawerIndicatorEnabled(true);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDrawer.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getTitle());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Here we remove R.layout.activity_main_tablet since is the same as R.layout.activity_main and
         * move and rename the tablet layout in /layout-land to /layout-large-land/activity_main.xml
         */
        setContentView(R.layout.activity_main);

        mMessageReceiver = new MessageReceiver();
        mTwoPane = findViewById(R.id.right_container) != null;

        // Set up the drawer.
        mDrawer = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mDrawer.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        onNavigationDrawerItemSelected(mDrawer.getCurrentSelectedPosition());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
        super.onResume();
    }
}