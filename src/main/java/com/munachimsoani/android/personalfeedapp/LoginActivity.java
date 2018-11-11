package com.munachimsoani.android.personalfeedapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import static com.munachimsoani.android.personalfeedapp.R.menu.register_menu;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private SignInButton signInButton;
      private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private  final String TAG = "Personal Feed App";

    // Firebase Instance Variable

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailTextInputLayout = findViewById(R.id.text_input_email);
        mPasswordTextInputLayout = findViewById(R.id.text_input_password);

        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign In");

        // Set the dimensions of the sign-in button.
         signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Get hold of the firebase instance variable
        mAuth = FirebaseAuth.getInstance();


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseUser user =mAuth.getCurrentUser();

        // Check ig firebase user exits and redirect to the MainActivity

        if(user != null){
            Intent mainActivitityIntent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(mainActivitityIntent);
            this.finish();
        }




        // Click the google sing button to sign in

//        signInButton.setSize(8);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



    }


//    Override the menu to add your own menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(register_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

//    Add click listeners to the menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.user_register:
                Intent intent = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // Sign into with google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Executed when Sign In button is pressed.
    public void signIn(View v) {
        attemptLogin();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
       // Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();

                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

//
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            showErrorDialog("Authentication Failed");
                        }


                    }
                });
    }




    // attempt to sign in
    public void attemptLogin(){
        if(!validateEmail() | !validatePassword()){
            return;
        } else {
            String email = mEmailTextInputLayout.getEditText().getText().toString();
            String password = mPasswordTextInputLayout.getEditText().getText().toString();

            Toast.makeText(LoginActivity.this,"Login Loading", Toast.LENGTH_SHORT).show();


            // Use FirebaseAuth to sign in with email & password

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful()){
                        showErrorDialog("There was a problem signing in!. Please check your login details");
                    } else {
//
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

//                        updateUI();
                    }
                }
            });
        }
    }




    // Alert Dialog if Login fails

    private void showErrorDialog(String message){

        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //    Validate Email

    private boolean validateEmail(){
        String emailInput = mEmailTextInputLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()){
            mEmailTextInputLayout.setError("Email cannot be empty");
            mEmailTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            return  false;
        } else if(!emailInput.contains("@")){

            mEmailTextInputLayout.setError("Email is invalid");
            mEmailTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            return  false;

        } else {

            mEmailTextInputLayout.setError(null);
            return true;

        }

    }

//    Validate Password

    private boolean validatePassword(){
        String passwordInput = mPasswordTextInputLayout.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()){
            mPasswordTextInputLayout.setError("Password  cannot be empty");
            mPasswordTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));

            return  false;
        } else if(passwordInput.length() < 8){

            mPasswordTextInputLayout.setError("Password cannot be less than 8 characters");
            mPasswordTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));

            return  false;


        } else {

            mPasswordTextInputLayout.setError(null);
            return true;

        }
    }

}


