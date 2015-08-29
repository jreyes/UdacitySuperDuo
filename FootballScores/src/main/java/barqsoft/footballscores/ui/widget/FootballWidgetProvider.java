package barqsoft.footballscores.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;

public class FootballWidgetProvider extends AppWidgetProvider {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // let's start the service to populate the widget
            Intent intent = new Intent(context, FootballWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // create the RemoteViews
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_match);
            views.setRemoteAdapter(R.id.widget_list_view, intent);
            views.setEmptyView(R.id.widget_list_view, R.id.widget_list_empty);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
