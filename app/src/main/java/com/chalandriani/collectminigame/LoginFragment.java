package com.chalandriani.collectminigame;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    //Username and password login
    TextView txt_login, txt_register, txt_title, txt_or;
    EditText etxt_mail, etxt_pass, etxt_pass_conf;
    Button confirm;
    //google sign in
    final int RC_SIGN_IN = 0;
    SignInButton mGoogleLoginButton;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions mGoogleOptions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API,mGoogleOptions)
                .build();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        setupReferences(v);
        return v;
    }

    private void setupReferences(View v) {
        txt_login =  v.findViewById(R.id.login);
        txt_register =  v.findViewById(R.id.register);
        txt_title =  v.findViewById(R.id.title);
        txt_or =  v.findViewById(R.id.or);
        etxt_mail =  v.findViewById(R.id.email);
        etxt_pass =  v.findViewById(R.id.password);
        etxt_pass_conf =  v.findViewById(R.id.passwordconfirm);
        confirm =  v.findViewById(R.id.send);
        mGoogleLoginButton = v.findViewById(R.id.login_with_google);

        txt_login.setOnClickListener(changeListener);
        txt_register.setOnClickListener(changeListener);
        confirm.setOnClickListener(loginListener);
        mGoogleLoginButton.setOnClickListener(googleSignListener);
    }


    View.OnClickListener googleSignListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    };

    View.OnClickListener changeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getTag().toString().equalsIgnoreCase("Login")){
                txt_title.setText("Login");
                txt_login.setVisibility(View.GONE);
                txt_register.setVisibility(View.VISIBLE);
                txt_or.setVisibility(View.VISIBLE);
                mGoogleLoginButton.setVisibility(View.VISIBLE);
                etxt_pass_conf.setVisibility(View.GONE);
                confirm.setText("Login");
            }
            else{
                txt_title.setText("Register");
                txt_login.setVisibility(View.VISIBLE);
                txt_register.setVisibility(View.GONE);
                txt_or.setVisibility(View.GONE);
                mGoogleLoginButton.setVisibility(View.GONE);
                etxt_pass_conf.setVisibility(View.VISIBLE);
                confirm.setText("Register");
            }
        }
    };

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String mail = etxt_mail.getText().toString();
            String pass = etxt_pass.getText().toString();
            String passconfirm = etxt_pass_conf.getText().toString();

            if(txt_register.getVisibility() == View.VISIBLE){
                FirebaseHandler.authentication.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(getActivity(),
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseHandler.authentication.getCurrentUser();
                                    Toast.makeText(getActivity(),"User logged in",Toast.LENGTH_LONG).show();
                                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                    FragmentHandler.
                                            switchFragment(R.id.fragment_container,new CharacterFragment());
                                }
                                else {
                                    Toast.makeText(getActivity(), "Blade",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {
                if(!pass.equals(passconfirm)){
                    etxt_pass_conf.setError("Passwords don't match");
                    return;
                }
                FirebaseHandler.authentication.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(getActivity(),
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseHandler.authentication.getCurrentUser();
                                    Toast.makeText(getActivity(),"User created successfully",Toast.LENGTH_LONG).show();
                                    FragmentHandler.
                                            switchFragment(R.id.fragment_container,new CharacterFragment());
                                }
                                else {
                                    Toast.makeText(getActivity(), "User creation failed"
                                            + task.getException().toString()
                                            ,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    };

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseHandler.authentication.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(),"User logged in",Toast.LENGTH_LONG).show();
                    FirebaseUser user = FirebaseHandler.authentication.getCurrentUser();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    FragmentHandler.
                            switchFragment(R.id.fragment_container,new CharacterFragment());
                } else {
                    Toast.makeText(getActivity(),"Failed to integrate with Google account",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }
}
