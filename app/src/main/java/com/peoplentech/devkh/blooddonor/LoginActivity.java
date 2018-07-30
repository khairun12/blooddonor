package com.peoplentech.devkh.blooddonor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.peoplentech.devkh.blooddonor.model.ResObj;
import com.peoplentech.devkh.blooddonor.model.User;
import com.peoplentech.devkh.blooddonor.remote.ApiUtils;
import com.peoplentech.devkh.blooddonor.remote.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    //A tag for facebook key generation
    private static final String TAG = "mykeytool";

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    UserService userService;
    TextView back_reg, forgot_pass;

    //FB Login
    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FB integration
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        edtUsername = (EditText) findViewById(R.id.userEmail);
        edtPassword = (EditText) findViewById(R.id.userPassword);
        btnLogin = (Button) findViewById(R.id.login);
        //fb login button
        loginButton = (LoginButton)findViewById(R.id.login_button);

        back_reg = (TextView) findViewById(R.id.backToReg);
        forgot_pass = (TextView) findViewById(R.id.forgotPass);
        userService = ApiUtils.getUserService();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                //validate form
                if(validateLogin(email, password)){
                    //do login
                    doLogin(email, password);
                }
            }
        });

        back_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_forgot_pass,null);
                final EditText reset_email = (EditText) mView.findViewById(R.id.resetEmail);
                final Button send_pass = (Button) mView.findViewById(R.id.resetPass);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                send_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!reset_email.getText().toString().isEmpty()){

                            Toast.makeText(LoginActivity.this,

                                    "Well Done",

                                    Toast.LENGTH_SHORT).show();

                            dialog.dismiss();

                        }else{

                            Toast.makeText(LoginActivity.this,

                                    "Failed",

                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });

        /**
         * Generate hash key for fb integration
         * We had some problem trying to do this by using keytool command
         * This method generates a SHA hash key required for fb integration
         * key is displayed on logcat
         */

        /*try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }*/

        //FB login onclick
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(LoginActivity.this, SearchDonorActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
        });


    }

    //FB login result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Normal login method

    private boolean validateLogin(String email, String password){
        if(email == null || email.trim().length() == 0){
            Toast.makeText(this, "Valid Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String email,final String password){
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In");
        progressDialog.show();

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        UserService service = retrofit.create(UserService.class);

        //Defining the user object as we need to pass it with the call
        User user = new User(email, password);

        //defining the call
        Call<ResObj> call = service.getResponse(

                user.getEmail(),
                user.getPassword()

        );

        ConnectivityManager conntivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conntivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
                conntivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED) {

            //Connected ti internet
            //calling the api
            call.enqueue(new Callback<ResObj>() {
                @Override
                public void onResponse(Call<ResObj> call, Response<ResObj> response) {
                    //hiding progress dialog
                    progressDialog.dismiss();

                    //displaying the message from the response as toast
                    //Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    if (!response.body().getError()) {

                        startActivity(new Intent(getApplicationContext(), SearchDonorActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResObj> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
}