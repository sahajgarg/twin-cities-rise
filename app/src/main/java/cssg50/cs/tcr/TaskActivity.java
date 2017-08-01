package cssg50.cs.tcr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Activity that allows the users to update the status of a task.
public class TaskActivity extends AppCompatActivity {

    private String taskStatus = null;
    PersistentSaver saver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        saver = PersistentSaver.getInstance();
        Task currtask = saver.currtask;
        loadTask(currtask);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.status_buttons);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = (RadioButton) findViewById(checkedId);
                taskStatus = checkedButton.getText().toString();

            }
        });
    }

    /**
     * Private helper function that initializes the view based on different fields in the task object
     * @param task-  task to be loaded
     */
    private void loadTask(Task task){
        if (task != null) {
            //initializes description text field in view
            TextView taskText = (TextView) findViewById(R.id.task_text);
            taskText.setText(task.initial_setup);

            //initializes due date field in view
            TextView dueDate = (TextView) findViewById(R.id.date_text);
            dueDate.setText(task.timeline_date);

        }

    }


    /**
     * OnClick listener for the 'submit' button that updates the status of a particular task to the firebase
     * @param view- for use when a button is clicked
     */
    public void updateStatus(View view){
        if (taskStatus != null){ //the user has selected an option
            String taskpath = saver.currtask.path;
            DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
            DatabaseReference child = fb.child(taskpath);
            //writes status to DB so coaches can see it
            if(saver.currtask.newtask) child.child("3").setValue(taskStatus);
            else child.child("4").setValue(taskStatus);
            Toast.makeText(this, "Task status has been updated", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, MainPage.class);
            this.finish();
        }

        else { //the user has not selected an option
            Toast.makeText(this, "Please tap an option, then submit", Toast.LENGTH_SHORT).show();
        }

    }
}
