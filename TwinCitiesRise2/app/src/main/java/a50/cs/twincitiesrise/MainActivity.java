package a50.cs.twincitiesrise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Calendar;

//This is a test comment! please remove in future commits
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //This is the code to create a new notification for a pending time in the future and
    //send it at the specified time on the calendar.
    //TODO: Add button that sends notification and make sure that this is persistent when phone is reset
    public void createNotification(View view) {
        Calendar calendar = Calendar.getInstance();
//        int min = Integer.parseInt(minNum.getText().toString());
//        int hour = Integer.parseInt(hourNum.getText().toString());
//        int day = Integer.parseInt(dayNum.getText().toString());
//        boolean ispm = pm.isChecked();

//        calendar.set(Calendar.SECOND,0);
//        calendar.set(Calendar.MINUTE,min);
//        calendar.set(Calendar.HOUR,hour);
//        if(ispm) calendar.set(Calendar.AM_PM,Calendar.PM);
//        else calendar.set(Calendar.AM_PM,Calendar.AM);
//        System.out.println(Calendar.DATE);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        calendar.add(Calendar.SECOND,5);
//        Intent intent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
//        PendingIntent broadcast = PendingIntent.getBroadcast(this,100,intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
//        //TODO: ACCESS SAVER AND SAVE TASK

    }
}
