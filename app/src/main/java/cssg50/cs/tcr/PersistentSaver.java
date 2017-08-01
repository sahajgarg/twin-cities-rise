package cssg50.cs.tcr;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ruiaguiar1 on 4/16/17.
 * This is the singleton for Firebase methods and persistent data saving across multiple activities
 */


//Singleton for saving data between activites
//We should also store all of our SQL and firebase querying in this class.

class PersistentSaver {
    private static PersistentSaver ourInstance = null;
    private static ArrayList<Task> userTasks = new ArrayList<>();
    //private Context context;
    Task currtask = new Task();

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private String userEmail;

    static PersistentSaver getInstance() {
        if (ourInstance == null){
            ourInstance = new PersistentSaver();
        }
        return ourInstance;
    }


    //Reads in all tasks with a recursive search through the firebase JSON database.
    //loads tasks conditionally based on formatting and type.
     void getAllTasks(final String currpath, final boolean first, final Context context){
            if(currpath.contains("layout")) return;
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(currpath).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        boolean last = false;
                        if (!child.hasChildren()) {
                            if(child.getKey().equalsIgnoreCase("Coach Name") || child.getKey().equalsIgnoreCase("Email") || child.getKey().equalsIgnoreCase("Participant Name")) continue;
                            last = true;
                        }
                        if (last) {
                            int idx = 0;
                            String category="", objective = "", initial="", date="", goal = "&*^&**&^*(*^&*()*(&*";
                            boolean newtask = false;
                            for (DataSnapshot child2 : dataSnapshot.getChildren()) {

                                if(idx==0) category = child2.getValue().toString();
                                if(idx==1) objective = child2.getValue().toString();
                                if(idx==2) initial = child2.getValue().toString();
                                if(idx==3) date = child2.getValue().toString();
                                if(idx==4) goal = child2.getValue().toString();
                                idx++;
                            }

                            //if it's a new task, shift fields back
                            if (goal.equals("&*^&**&^*(*^&*()*(&*")) {
                                newtask = true;
                                String new_initial =initial;
                                String new_date = date;
                                initial = objective;
                                date = new_initial;
                                goal = new_date;
                            }


                            //ignore completed or invalid tasks
                           if(!date.toLowerCase().contains("ongoing") && !date.matches(".*/.*/.*") ||goal.equalsIgnoreCase("done")) continue;
                            Task task = new Task(category, initial, date, goal, currpath, context, newtask);
                                            if(!userTasks.contains(task)) {
                                                userTasks.add(task);
                                            }
                        } else {

                            getAllTasks(currpath + "/" + child.getKey(), false, context);
                        }
                    }
                    if(first) {

                        //change after it has read all of the data in
                        Intent intent = new Intent(context, IntermediateLoading.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        System.out.println("STARTING TASK");
                        context.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            }



            );


    }
    public static ArrayList<Task> getUserTasks() {
        return userTasks;
    }

     static void setUserTasks(ArrayList<Task> userTasks) {
        PersistentSaver.userTasks = userTasks;
    }

    //sorts task by due date.
      static void sortTasks(){
         Collections.sort(userTasks, new Comparator<Task>() {
             @Override
             public int compare(Task task1, Task task2) {
                 //dosent matter what the order is if either is ongoing.
                 if(task1.timeline_date.toLowerCase().contains("ongoing")||task2.timeline_date.toLowerCase().contains("ongoing")){
                     return -1;
                 }

                 SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy", Locale.US);

                 try {
                     Date date1 = dateFormat.parse(task1.timeline_date);
                     Date date2 = dateFormat.parse(task2.timeline_date);
                     return date1.compareTo(date2);
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
                 //shouldn't get here
                 return -1;
             }
         });

    }

    //gets all upcoming tasks for user by date
     static ArrayList<Task> getUpcomingTasks(){

        ArrayList<Task> upcomingTasks = new ArrayList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date date = new Date();
        for(Task task: userTasks){
            //only returns size five so it is not too overwhelming
            if(upcomingTasks.size()>=5) return upcomingTasks;
            Date taskdate = new Date();
            taskdate.setHours(0); //same for minutes and seconds

            if(task.timeline_date.toLowerCase().contains("ongoing")){
                continue;
            }
            try{
                taskdate = dateFormat.parse(task.timeline_date);
                if(taskdate.after(date)){
                    upcomingTasks.add(task);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        return upcomingTasks;
    }

    //gets all overdue tasks for user by date
     static ArrayList<Task> getOverdueTasks(){
        ArrayList<Task> overdueTasks = new ArrayList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date date = new Date();
        for(Task task: userTasks){
            Date taskdate = new Date();
            taskdate.setHours(0); //same for minutes and seconds
            if (task.timeline_date.toLowerCase().contains("ongoing")){
                overdueTasks.add(task);
                continue;
            }
                try {
                     taskdate = dateFormat.parse(task.timeline_date);
                    if(taskdate.before(date)){
                        overdueTasks.add(task);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
        return overdueTasks;
    }


}
