package barqsoft.footballscores.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import barqsoft.footballscores.service.FootballService;

public class FootballServiceReceiver extends BroadcastReceiver {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, FootballService.class));
    }
} 