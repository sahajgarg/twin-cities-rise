package cssg50.cs.tcr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private PersistentSaver saver;
    private EditText pass;
    private EditText username;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saver = PersistentSaver.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        username = (EditText) findViewById(R.id.username_field);
        pass = (EditText) findViewById(R.id.password_field);
        mAuth = FirebaseAuth.getInstance();


    }

    //Reads the users login, and then passes control to the persistent saver
    private void readLogin(final String participant_name){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //searches database for user
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // iterates through every user in the user collection in the database
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    // iterates through every field in an individual user's information
                    for(DataSnapshot participant: user.getChildren()) {
                        if(participant.getKey().equalsIgnoreCase("Email") &&
                                participant.getValue().toString().trim().equals(participant_name)) {
                            System.out.println("HERE IN SNAP");
                            //login here only if they're authenticated, so we don't need to check passwords
                            String userId = user.getKey();
                            //reset user tasks and read new ones in
                            PersistentSaver.setUserTasks(new ArrayList<cssg50.cs.tcr.Task>());
                            saver.getAllTasks("tasks/" + userId, true, getApplicationContext());
                            return;
                        }
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Authenticates the user with firebase and searches the DB for their
    //tasks to load in.
    public void loginUser(View view){
        final String participant_name = username.getText().toString();
        final String password = pass.getText().toString();
        mAuth.signInWithEmailAndPassword(participant_name, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Authentication Succeded.",
                                    Toast.LENGTH_SHORT).show();
                            readLogin(participant_name);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //guides user to the register view.
    public void registerAccount(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);

    }
}
