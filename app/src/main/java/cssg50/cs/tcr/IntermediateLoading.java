package cssg50.cs.tcr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

//Intermediate loading screen to see the users tasks.
//Only contains a method to go to the task list.
public class IntermediateLoading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate_loading);
    }

    public void toTaskList(View view){
        Intent intent = new Intent(getApplicationContext(), MainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);

    }
}
