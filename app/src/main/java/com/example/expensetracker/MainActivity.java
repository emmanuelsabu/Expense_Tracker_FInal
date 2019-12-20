package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button button;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    SignInButton signin;
    public static GoogleSignInOptions googleSignInOptions;
    public static GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private static int Sign_in_Value = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin = findViewById(R.id.signin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }

        });
        mAuth = FirebaseAuth.getInstance();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser CurrentUser = mAuth.getCurrentUser();
        updateUI(CurrentUser);
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Sign_in_Value);

        mSharedPreferences = getSharedPreferences("Login Completed", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putInt("Login", 1);
        mEditor.apply();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Sign_in_Value) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, "Sign In Failed" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null)
        {

            Intent intent = new Intent(MainActivity.this, HomePage.class);
            mSharedPreferences = getSharedPreferences("Login Completed", MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
            String Uid= user.getUid();
            mEditor.putString("uniqueId",Uid);
            String Names = user.getDisplayName();
            mEditor.putString("name",Names);

            String Mail = user.getEmail();
            mEditor.putString("mail",Mail);
            String photo = String.valueOf(user.getPhotoUrl());
            mEditor.putString("DP", photo);

            mEditor.apply();
            intent.putExtra("Name",Names);
            intent.putExtra("Mail",Mail);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {

            AlertDialog.Builder mBuilder;
            mBuilder = new AlertDialog.Builder(MainActivity.this);

            mBuilder.setMessage("Do you want to Exit ?")
                    .setTitle("Exit")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finishAffinity();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });

            AlertDialog alertDialog = mBuilder.create();
            alertDialog.setTitle("Exit");
            alertDialog.show();
        }

}


