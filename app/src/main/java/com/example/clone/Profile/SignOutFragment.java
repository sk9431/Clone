package com.example.clone.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.clone.Login.LoginActivity;
import com.example.clone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignOutFragment extends Fragment {
    private static final String TAG = "SignOutFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar mProgressBar;
    private TextView tvSignout,tvSigningOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_signout,container,false);

       tvSignout=(TextView)view.findViewById(R.id.tvconfirmedsignout);
       mProgressBar=(ProgressBar)view.findViewById(R.id.progressBar);
       tvSigningOut=(TextView)view.findViewById(R.id.tvSigningOut);
       Button btnConfirmSignout=(Button)view.findViewById(R.id.btnconfirmedsignout);

       mProgressBar.setVisibility(View.GONE);
       tvSigningOut.setVisibility(View.GONE);

       setupFirebaseAuth();

       btnConfirmSignout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.d(TAG, "onClick: attempting to signout");
               mProgressBar.setVisibility(View.VISIBLE);
               tvSigningOut.setVisibility(View.VISIBLE);

               mAuth.signOut();
               getActivity().finish();
           }
       });

       return view;
    }
    //---------------------------FIREBASE-------------------------------------------------

    // checked to see if the @param 'user' is logged in


    // setup the firebase auth object
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if(user!=null){
                    Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                }else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");

                    Log.d(TAG, "onAuthStateChanged: navigating back to login screen");
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
