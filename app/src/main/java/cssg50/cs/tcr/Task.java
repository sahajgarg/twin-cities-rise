package cssg50.cs.tcr;

import android.content.Context;

import com.google.firebase.database.Exclude;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.description;

/**
 * Created by ruiaguiar1 on 4/28/17. Task class to help with modularity of information
 */

 class Task {

     private String category;
     private String goal_achived;
     String initial_setup;
     String timeline_date;
     String path;
    public Context context;
     boolean newtask;
     Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    //Task constructor.
     Task(String category, String initial_setup, String timeline_date , String goal_achived, String path, Context context, boolean newtask) {
        this.category = category;
        this.goal_achived = goal_achived;
        this.initial_setup = initial_setup;
        this.timeline_date = timeline_date;
        this.path = path;
        this.context=context;
        this.newtask = newtask;

    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("category", category);
        result.put("goal_achived", goal_achived);
        result.put("initial_setup", initial_setup);
        result.put("objective", description);
        result.put("timeline_date", timeline_date);
        return result;
    }

    //Code to create a new notification for a pending time in the future and
    //send it at the specified time on the calendar.
    Calendar getCalendar(int numDays) {
            String alarmdate = timeline_date;
            //If it's ongoing, then just alert 2 weeks from now.
            if(alarmdate.toLowerCase().contains("ongoing")){
                String frequency = alarmdate.split(" ")[1];
                Calendar ongoingcal = Calendar.getInstance();
                //weekly ongoing notivies once a week
                if(frequency.equalsIgnoreCase("weekly")) {
                    ongoingcal.add(Calendar.DAY_OF_MONTH, 7);
                    return ongoingcal;
                }
                //Monthly ongoing notifies every month.
                else{
                    ongoingcal.add(Calendar.MONTH,1);
                    return ongoingcal;
                }
            }
        Calendar calendar = Calendar.getInstance();
        int month = Integer.parseInt(alarmdate.substring(0, alarmdate.indexOf("/"))) - 1;
            alarmdate = alarmdate.substring(alarmdate.indexOf("/") + 1);
            int day = Integer.parseInt(alarmdate.substring(0, alarmdate.indexOf("/")));
            alarmdate = alarmdate.substring(alarmdate.indexOf("/") + 1);
            int year = Integer.parseInt(alarmdate);
            int hour = 12;
            int min = 0;
            calendar.set(year, month, day, hour, min);
            calendar.add(Calendar.DAY_OF_YEAR,numDays);
            return calendar;
        }

    //Checks if two tasks are equal based on date and initial step
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Task.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Task other = (Task) obj;
        return (this.initial_setup == null)?
                other.initial_setup==null:
                this.initial_setup.equals(other.initial_setup)
                        && this.timeline_date.equals(other.timeline_date);
    }


    //toString method for printing things and debugging.
    @Override
    public String toString(){
        return this.initial_setup +"    "+this.timeline_date;
    }
}
