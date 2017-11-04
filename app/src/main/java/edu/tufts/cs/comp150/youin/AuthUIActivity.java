package edu.tufts.cs.comp150.youin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;

import io.fabric.sdk.android.Fabric;
import java.util.Arrays;

public class AuthUIActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private Activity activity;
    private final int RC_SIGN_IN = 123;

    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_auth_ui);

        activity = this;

        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(activity, EventListActivity.class));
            return;
        } else {
            Button retry = findViewById(R.id.retrySignIn);
            retry.setVisibility(View.VISIBLE);
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
                    showToast(R.string.signInCancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast(R.string.noNetworkConnection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showToast(R.string.noNetworkConnection);
                    return;
                }
            }

            showToast(R.string.unknownError);
        }
    }

    private void showToast(@StringRes int errorMessageRes) {
        mRootView = findViewById(R.id.authRoot);
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    public void retrySignIn(View v) {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                        .setIsSmartLockEnabled(true, true)
                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN);
    }

    public void handleProfileCreation(boolean needToCreate) {
        if (needToCreate) {
            startActivity(new Intent(this, CreateProfileActivity.class));
        } else {
            startActivity(new Intent(this, EventListActivity.class));
        }
    }
}
