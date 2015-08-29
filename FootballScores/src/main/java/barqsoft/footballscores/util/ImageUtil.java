package barqsoft.footballscores.util;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ImageView;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.widget.FootballWidgetProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.caverock.androidsvg.SVG;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ImageUtil {
// ------------------------------ FIELDS ------------------------------

    private static final String SVG_EXTENSION = ".svg";

// -------------------------- STATIC METHODS --------------------------

    private static ImageVideoWrapperToBitmapDecoder decoder(Context context) {
        return new ImageVideoWrapperToBitmapDecoder(Glide.get(context).getBitmapPool());
    }

    private static int getIconSize(Context context) {
        return (int) context.getResources().getDimension(R.dimen.navigation_icon_size);
    }

    public static void load(Context context, String url, ImageView imageView) {
        // because of a redirect to https, the image wasn't being cached
        url = url.replace("http:", "https:");
        if (url.endsWith(SVG_EXTENSION)) {
            Glide
                    .with(context)
                    .using(streamModelLoader(context))
                    .from(String.class)
                    .asBitmap()
                    .decoder(decoder(context))
                    .load(url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        } else {
            Glide
                    .with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }

    public static void load(Context context, String url, RemoteViews row, final int resourceId) {
        int iconSize = getIconSize(context);
        try {
            // because of a redirect to https, the image wasn't being cached
            url = url.replace("http:", "https:");
            Bitmap bitmap;
            if (url.endsWith(SVG_EXTENSION)) {
                bitmap = Glide
                        .with(context)
                        .using(streamModelLoader(context))
                        .from(String.class)
                        .asBitmap()
                        .decoder(decoder(context))
                        .load(url)
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(iconSize, iconSize)
                        .get();
            } else {
                bitmap = Glide
                        .with(context)
                        .load(url)
                        .asBitmap()
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(iconSize, iconSize)
                        .get();
            }
            row.setImageViewBitmap(resourceId, bitmap);
        } catch (Exception ignored) {
            row.setImageViewResource(resourceId, R.drawable.ic_launcher);
        }
    }

    private static StreamModelLoader<String> streamModelLoader(Context context) {
        return (StreamModelLoader<String>) Glide.buildModelLoader(String.class, InputStream.class, context);
    }

// -------------------------- INNER CLASSES --------------------------

    static class ImageVideoWrapperToBitmapDecoder implements ResourceDecoder<ImageVideoWrapper, Bitmap> {
        private final BitmapPool bitmapPool;

        public ImageVideoWrapperToBitmapDecoder(BitmapPool bitmapPool) {
            this.bitmapPool = bitmapPool;
        }

        @Override
        public Resource<Bitmap> decode(ImageVideoWrapper source, int width, int height) throws IOException {
            try {
                SVG svg = SVG.getFromInputStream(source.getStream());
                Bitmap bitmap = findBitmap(Math.round(svg.getDocumentWidth()), Math.round(svg.getDocumentHeight()));
                svg.renderToCanvas(new Canvas(bitmap));
                return BitmapResource.obtain(bitmap, bitmapPool);
            } catch (Exception ex) {
                throw new IOException("Cannot load SVG from stream", ex);
            }
        }

        @Override
        public String getId() {
            return "ImageUtil.ImageVideoWrapperToBitmapDecoder";
        }

        private Bitmap findBitmap(int width, int height) {
            Bitmap bitmap = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            return bitmap;
        }
    }
}
