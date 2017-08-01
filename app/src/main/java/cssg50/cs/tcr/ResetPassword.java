package cssg50.cs.tcr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

//Activity that allows the user to reset their password using Firebase's
//forgot password system.
public class ResetPassword extends AppCompatActivity {

    EditText emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailAddress = (EditText) findViewById(R.id.username_field);
    }

    //Sends an email to the user's firebase account for them to reset
    //their password
    public void sendForgotPasswordEmail(View view){
        final String passNotification = "Email Sent.";
        final String passFailed = "Incorrect Email Address.";
        String email = emailAddress.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), passNotification, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), passFailed, Toast.LENGTH_LONG).show();

                    }
                    }
                });
    }

}
