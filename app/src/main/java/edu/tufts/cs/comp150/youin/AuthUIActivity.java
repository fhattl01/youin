package edu.tufts.cs.comp150.youin;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class AuthUIActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private Activity activity;
    private final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_ui);

        activity = this;

        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(activity, EventListActivity.class));
            return;
        }

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                        .setIsSmartLockEnabled(true, true)
                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                DatabaseManager databaseManager = new DatabaseManager(firebaseAuth.getCurrentUser().getUid());
                databaseManager.checkForProfileCreation(this);
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToast("Sign in cancelled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast("No internet connection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showToast("Unknown error");
                    return;
                }
            }

            showToast("Unknown Error");
        }
    }

    private void showToast(String message) {
        //TODO figure out snackbar alerts
        //Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG).show();
    }

    public void handleProfileCreation(boolean needToCreate) {
        if (needToCreate) {
            startActivity(new Intent(this, CreateProfileActivity.class));
        } else {
            startActivity(new Intent(this, EventListActivity.class));
        }
    }
}
