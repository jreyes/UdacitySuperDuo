package it.jaschke.alexandria.util;

import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import it.jaschke.alexandria.R;

public class ViewUtil {
// -------------------------- STATIC METHODS --------------------------

    public static void setImage(View view, int resourceId, String value) {
        if (value != null && Patterns.WEB_URL.matcher(value).matches()) {
            ImageView cover = (ImageView) view.findViewById(resourceId);
            Picasso.with(view.getContext()).load(value).error(R.drawable.ic_launcher).into(cover);
            cover.setVisibility(View.VISIBLE);
        }
    }

    public static void setLines(View view, int resourceId, String value) {
        value = value == null ? "" : value.trim();
        ((TextView) view.findViewById(resourceId)).setLines(value.split(",").length);
        ((TextView) view.findViewById(resourceId)).setText(value.replace(",", "\n"));
    }

    public static void setText(View view, int resourceId, String value) {
        value = value == null ? "" : value.trim();
        ((TextView) view.findViewById(resourceId)).setText(value);
    }
}
