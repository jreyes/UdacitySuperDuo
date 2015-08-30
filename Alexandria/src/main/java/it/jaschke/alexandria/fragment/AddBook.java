package it.jaschke.alexandria.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.model.Book;
import it.jaschke.alexandria.services.BookService;

import java.util.Collections;

import static it.jaschke.alexandria.util.ViewUtil.setImage;
import static it.jaschke.alexandria.util.ViewUtil.setLines;
import static it.jaschke.alexandria.util.ViewUtil.setText;

public class AddBook extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
// ------------------------------ FIELDS ------------------------------

    private static final int LOADER_ID = 1;

    private Book mBook;
    private EditText mEan;
    private View mRootView;

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

        // close cursor
        data.close();

        // display the book details
        displayBook();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            mEan.setText(result.getContents());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.scan);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_add_book, container, false);

        mEan = (EditText) mRootView.findViewById(R.id.ean);
        mEan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                retrieveBook(s.toString());
            }
        });

        mRootView.findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator
                        .forSupportFragment(AddBook.this)
                        .setDesiredBarcodeFormats(Collections.singletonList("EAN_13"))
                        .setBeepEnabled(true)
                        .initiateScan();
            }
        });

        mRootView.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextBook();
            }
        });

        mRootView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook();
            }
        });

        return mRootView;
    }

    private void deleteBook() {
        startBookService(BookService.DELETE_BOOK, mBook.ean);
        mBook = null;
        displayBook();
    }

    private void displayBook() {
        if (mBook == null) {
            setText(mRootView, R.id.bookTitle, "");
            setText(mRootView, R.id.bookSubTitle, "");
            setText(mRootView, R.id.authors, "");
            setText(mRootView, R.id.categories, "");
            mRootView.findViewById(R.id.bookCover).setVisibility(View.INVISIBLE);
            mRootView.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
            mRootView.findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
        } else {
            setText(mRootView, R.id.bookTitle, mBook.bookTitle);
            setText(mRootView, R.id.bookSubTitle, mBook.bookSubTitle);
            setLines(mRootView, R.id.authors, mBook.authors);
            setText(mRootView, R.id.categories, mBook.categories);
            setImage(mRootView, R.id.bookCover, mBook.imgUrl);
            mRootView.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
        }
    }

    private void nextBook() {
        mEan.getText().clear();
        mBook = null;
        displayBook();
    }

    private void retrieveBook(String ean) {
        //catch isbn10 numbers
        if (ean.length() == 10 && !ean.startsWith("978")) {
            ean = "978" + ean;
        }

        // if the ean is not 13 characters don't do anything
        if (ean.length() != 13) {
            return;
        }

        //Once we have an ISBN, start a book intent
        startBookService(BookService.FETCH_BOOK, ean);

        // and restart the loader
        Bundle arguments = new Bundle();
        arguments.putLong(BookService.EAN, Long.valueOf(ean));
        getLoaderManager().restartLoader(LOADER_ID, arguments, this);
    }

    private void startBookService(String action, String ean) {
        Intent intent = new Intent(getActivity(), BookService.class);
        intent.putExtra(BookService.EAN, ean);
        intent.setAction(action);
        getActivity().startService(intent);
    }
}
