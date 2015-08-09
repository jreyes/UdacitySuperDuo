package it.jaschke.alexandria.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Patterns;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.model.Book;
import it.jaschke.alexandria.services.BookService;

public class BookDetail extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
// ------------------------------ FIELDS ------------------------------

    public static final int LOADER_ID = 10;
    public static final String TAG = BookDetail.class.getSimpleName();

    private Book mBook;
    private ShareActionProvider mShareActionProvider;
    private View rootView;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface LoaderCallbacks ---------------------

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(args.getLong(BookService.EAN)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        mBook = new Book();
        mBook.ean = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry._ID));
        mBook.bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        mBook.bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        mBook.description = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.DESC));
        mBook.authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        mBook.imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        mBook.categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));

        // close cursor to stop leaks
        data.close();

        // display the book details
        displayBook();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            getLoaderManager().restartLoader(LOADER_ID, arguments, this);
        }

        rootView = inflater.inflate(R.layout.fragment_full_book, container, false);
        rootView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(getActivity(), BookService.class);
                bookIntent.putExtra(BookService.EAN, mBook.ean);
                bookIntent.setAction(BookService.DELETE_BOOK);
                getActivity().startService(bookIntent);
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return true;
    }

    private void displayBook() {
        // Fix share intent crashing the application on orientation change
        if (mShareActionProvider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            // if this is atleast lollipop use the new document flag
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            }
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + mBook.bookTitle);
            mShareActionProvider.setShareIntent(shareIntent);
        }

        // populate the view
        ((TextView) rootView.findViewById(R.id.fullBookTitle)).setText(mBook.bookTitle);
        ((TextView) rootView.findViewById(R.id.fullBookSubTitle)).setText(mBook.bookSubTitle);
        ((TextView) rootView.findViewById(R.id.fullBookDesc)).setText(mBook.description);
        ((TextView) rootView.findViewById(R.id.authors)).setLines(mBook.authors.split(",").length);
        ((TextView) rootView.findViewById(R.id.authors)).setText(mBook.authors.replace(",", "\n"));
        ((TextView) rootView.findViewById(R.id.categories)).setText(mBook.categories);
        if (Patterns.WEB_URL.matcher(mBook.imgUrl).matches()) {
            ImageView cover = (ImageView) rootView.findViewById(R.id.fullBookCover);
            Picasso.with(getActivity()).load(mBook.imgUrl).error(R.drawable.ic_launcher).into(cover);
            cover.setVisibility(View.VISIBLE);
        }
    }
}