package cssg50.cs.tcr;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class MainPage extends AppCompatActivity {
// ...
private PersistentSaver saver;
    private static final int WEEK_BEFORE = -7;
    private static final int DAYS_BEFORE = -2;
    private static final int STARTING_ID = 100;
    private static final int UNUSED = -100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        saver = PersistentSaver.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PersistentSaver.sortTasks();

        ArrayList<Task> upcomingTasks= PersistentSaver.getUpcomingTasks();
        ArrayAdapter<Task> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, upcomingTasks
        );
        //Creates and adds the alarms for future notifications.
        //only shows these alarms for upcoming tasks and those that are ongoing
        int i=STARTING_ID;
        for(Task task: upcomingTasks){
            addAlarm(WEEK_BEFORE,task,i, true);
            i++;
            addAlarm(DAYS_BEFORE,task,i, false);
            i++;
        }

        ArrayList<Task> overdueTasks= PersistentSaver.getOverdueTasks();
        ArrayAdapter<Task> overdueAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, overdueTasks
        );

        for(Task task: overdueTasks) {
            //schedules all notifications for ongoing tasks
            if (task.timeline_date.toLowerCase().contains("ongoing")){
            addAlarm(UNUSED, task, i, false);
            i++;
            }

        }
        ListView list = (ListView) findViewById(R.id.upcoming_list);
        list.setAdapter(adapter);
        ListView overdueList = (ListView) findViewById(R.id.overdue_list);
        overdueList.setAdapter(overdueAdapter);
        setAdapter(list);
        setAdapter(overdueList);
    }
    //sets it such that when you click on a cell, goes to the update page.
    private void setAdapter(ListView list){
        list.setOnItemClickListener( //sets onClickListener for Upcoming tasks list
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> list,
                                            View row, int index, long rowID) {
                        Task clickedTask = (Task) list.getItemAtPosition(index);
                        Intent intent = new Intent(MainPage.this, TaskActivity.class);
                        //System.out.println(clickedTask);
                        saver.currtask = clickedTask;
                        startActivity(intent);
                    }
                }
        );
    }

    //Adds an alarm for a notification to the user.
    private void addAlarm(int days, Task task, int i, boolean cancel){
        Calendar calendar = task.getCalendar(days);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        PendingIntent broadcast = PendingIntent.getBroadcast(this,i,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //cancels previously scheduled alarms
        if(cancel){
            alarmManager.cancel(broadcast);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);

    }

    //sets it so that when you click on a bitmap, returns.
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

}

