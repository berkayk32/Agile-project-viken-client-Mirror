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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.NetworkUtils;

public class GoogleLogInActivity extends AppCompatActivity {
    
    public static String LOGIN_ATTEMPT = "LOGIN_ATTEMPT";

    private final String NETWORK_INTERFACE_WIFI = "wlan0";
    private final String MAC_ADRESS = "Mac Addresses";
    private final String USERS = "Users";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    
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
    }
    
    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            signOut();
            finish();
        }
        else {
            signIn();
        }
    }
    private void initiateUser() {
        registerUserToMacAddress();
        registerUser();
    }
    private void registerUser() {

        final String userName = mAuth.getCurrentUser().getEmail();

        DocumentReference docRef = db.collection(USERS).document(userName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        createNewUser(userName);
                    }
                } /*else { } TODO handle if needed */
            }
        });
    }

    private void createNewUser(String userName) {

        Map<String, Object> user = new HashMap<>();
        user.put("balance", 500);
        user.put("freePass", Calendar.getInstance().getTime());

        db.collection(USERS).document(userName).set(user)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // TODO: Handle this if it's needed.
            }
        });
    }

    private void registerUserToMacAddress() {
        Map<String, Object> user = new HashMap<>();

        String macAddr = NetworkUtils.getMACAddress(NETWORK_INTERFACE_WIFI);
        String userName = mAuth.getCurrentUser().getEmail();
        user.put("User Name", userName);

        db.collection(MAC_ADRESS).document(macAddr).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO: Handle this if it's needed.
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    
    private void signOut() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(this,
                "Successfully logged out: " + currentUser.getEmail(),
                Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
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
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser loggedInUser = mAuth.getCurrentUser();
                    Toast.makeText(GoogleLogInActivity.this, "Welcome " + loggedInUser.getEmail(), Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    initiateUser();
                    finish();
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
        finish();
    }
    
}
