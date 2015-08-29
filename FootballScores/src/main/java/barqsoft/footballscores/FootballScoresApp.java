package barqsoft.footballscores;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import barqsoft.footballscores.service.FootballApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FootballScoresApp extends Application {
// ------------------------------ FIELDS ------------------------------

    private static final long CACHE_SIZE = 25 * 1024 * 1024;

    private FootballApi mFootballApi;
    private OkHttpClient mOkHttpClient;

// -------------------------- STATIC METHODS --------------------------

    public static FootballScoresApp getApplication(@NonNull Context context) {
        return (FootballScoresApp) context.getApplicationContext();
    }

    public static FootballApi getFootballApi(@NonNull Context context) {
        return getApplication(context).mFootballApi;
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate() {
        super.onCreate();

        initOkClient();
        initFootballApi();
        initGlide();
    }

    private Cache cache() {
        try {
            return new Cache(new File(getApplicationContext().getCacheDir(), "http"), CACHE_SIZE);
        } catch (IOException e) {
            return null;
        }
    }

    private void initFootballApi() {
        mFootballApi = new FootballApi(mOkHttpClient);
    }

    private void initGlide() {
        Glide
                .get(getApplicationContext())
                .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(mOkHttpClient));
    }

    private void initOkClient() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCache(cache());
        mOkHttpClient.setConnectTimeout(10, SECONDS);
        mOkHttpClient.setReadTimeout(10, SECONDS);
        mOkHttpClient.setWriteTimeout(10, SECONDS);
    }
}
