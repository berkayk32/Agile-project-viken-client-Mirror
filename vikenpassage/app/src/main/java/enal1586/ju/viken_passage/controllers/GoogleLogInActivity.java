package enal1586.ju.viken_passage.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.CurrentUser;

public class GoogleLogInActivity extends AppCompatActivity {
    
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 1000;
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_log_in);
    
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        
        mAuth = FirebaseAuth.getInstance();
        signIn();
        
    }
    
    @Override
    public void onStart() {
        super.onStart();
        initiateUser();
    }
    
    
    private void initiateUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            CurrentUser.logIn(currentUser);
        }
    }
    
    private void deInitiateUser() {
        CurrentUser.logOut();
    }
    
    private void signOut() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(this,
                "Successfully logged out: " + currentUser.getEmail(),
                Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        CurrentUser.logOut();
    }
    
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
        //finish();
    }
    
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // If sign in success, do stuff
                    initiateUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(GoogleLogInActivity.this,
                            "Authentication Failed.",
                            Toast.LENGTH_SHORT).show();
                
                }
                // ...
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
}
