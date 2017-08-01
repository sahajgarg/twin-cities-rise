package cssg50.cs.tcr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {

    EditText password;
    EditText username;
    EditText confirm;
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        password = (EditText) findViewById(R.id.password_field);
        username = (EditText) findViewById(R.id.username_field);
        confirm =  (EditText) findViewById(R.id.confirm_field);
        mAuth = FirebaseAuth.getInstance();


    }

    //Makes sure the user can sign in and there are no authentication problems.
    private void signInUser(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Authentication Succeded.",
                                    Toast.LENGTH_SHORT).show();
                            //registerUserInFirebase(participant_name);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Mehod to create a user and then return to main activity.
    //Takes the fields the user enters and stores them in firebase
    //for future authenticaiton.
    public void toMainActivity(View view){
        final StringHash hashGenerator = new StringHash();
        final String participant_email = username.getText().toString();
        final String password_str = password.getText().toString();
        final String confirm_str = confirm.getText().toString();

        if(!confirm_str.equals(password_str)){
            Toast.makeText(getApplicationContext(),"Your passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }
        if(participant_email.equals("")||password_str.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill out all the fields", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(participant_email, password_str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Account created.",
                                    Toast.LENGTH_SHORT).show();
                            signInUser(participant_email, password_str);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Username in use or password was too short.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    public void toReset(View view){
        Intent intent = new Intent(getApplicationContext(),ResetPassword.class);
        startActivity(intent);
    }
}

