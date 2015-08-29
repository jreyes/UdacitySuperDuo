package barqsoft.footballscores.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.joda.time.LocalDate;

import barqsoft.footballscores.service.FootballService;

public class SchedulerReceiver extends BroadcastReceiver {
// ------------------------------ FIELDS ------------------------------

    private static final int ALARM_INTERVAL = 1000 * 60 * 60 * 4; // 4 hours

    private boolean mRunning;

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mRunning) {
            return;
        }
        mRunning = true;

        // start the service 
        context.startService(new Intent(context, FootballService.class));

        // now schedule a refresh
        Intent receiverIntent = new Intent(context, FootballServiceReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                LocalDate.now().toDate().getTime(),
                ALARM_INTERVAL, PendingIntent.getBroadcast(context, 0, receiverIntent, 0)
        );
    }
}
