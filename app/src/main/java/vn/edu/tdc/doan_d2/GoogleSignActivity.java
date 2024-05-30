package vn.edu.tdc.doan_d2;

import android.content.Intent;
import android.os.Bundle;


public class GoogleSignActivity extends LoginActivity {

//    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_sign_layout);
//
//
//// Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        Intent signIntent = mGoogleSignInClient.getSignInIntent();
//        startActivity();
//    }
}}