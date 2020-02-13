package com.example.msq.quizzy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;


public class registerPg extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private TextView regPgQuizzy, loginWith, loginQuizzy;
    private EditText QuizzerNameR, EmailR, PasswordR, PasswordL, QuizzerNameL;
    private Button createAccountButton, gotoLoginPg, loginButton, faceboook, googleBtn, createAcc, twitter;
    private CallbackManager callbackManager;
    private LoginButton fb_login_btn;
    private TwitterLoginButton twitter_login_btn;
    private RequestQueue queue;
    private GoogleApiClient googleApiClient;
    public static final int REQ_CODE = 9001;


    public boolean isValidEmail(CharSequence target) {
        return (!isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_register_pg);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AppEventsLogger.activateApp(this);

        regPgQuizzy = findViewById(R.id.regPgQuizzy);

        QuizzerNameR = findViewById(R.id.quizzerName);
        EmailR = findViewById(R.id.email);
        PasswordR = findViewById(R.id.password);
        createAccountButton = findViewById(R.id.createAcc);
        loginWith = findViewById(R.id.orLoginWith);
        gotoLoginPg = findViewById(R.id.gotoLoginPg);
        faceboook = findViewById(R.id.facebook);
        fb_login_btn = findViewById(R.id.fb_login_button);
        googleBtn = findViewById(R.id.gmail);
        createAcc = findViewById(R.id.createAcc);
        twitter = findViewById(R.id.twitter);

        View login_popup = getLayoutInflater().inflate(R.layout.login_alert_dialog, null);

        loginButton = login_popup.findViewById(R.id.loginButton);
        QuizzerNameL = findViewById(R.id.QuizzerNameL);
        PasswordL = login_popup.findViewById(R.id.LoginPassword);


        Typeface optima = Typeface.createFromAsset(getAssets(), "fonts/optima.ttf");
        Typeface proxima = Typeface.createFromAsset(getAssets(), "fonts/proxima.ttf");
        Typeface copperplate = Typeface.createFromAsset(getAssets(), "fonts/copperplate.ttf");
        Typeface noteworthy = Typeface.createFromAsset(getAssets(), "fonts/noteworthy.ttf");

        regPgQuizzy.setTypeface(optima);
        QuizzerNameR.setTypeface(proxima);
        EmailR.setTypeface(proxima);
        PasswordR.setTypeface(proxima);
        createAccountButton.setTypeface(copperplate);
        loginWith.setTypeface(noteworthy);
        gotoLoginPg.setTypeface(copperplate);
        gotoLoginPg.setTypeface(null, Typeface.BOLD);

        gotoLoginPg.setOnClickListener(this);

        ///////////////////////////////////////////////////////////////////// Facebook Authentication/Login /////////////////////////////////////////////////////////////////////

        fb_login_btn.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        fb_login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){

            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        displayUserInfo(object);

                        String name = null;

                        try {
                            name = response.getJSONObject().getString("first_name") + " " + response.getJSONObject().getString("last_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        editor.putString("username", name);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id");

                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                Intent intent = new Intent(registerPg.this, homePg.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(registerPg.this, "Failed to login, try again !", Toast.LENGTH_SHORT).show();
            }
        });

        faceboook.setOnClickListener(this);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        /////////////////////////////////////////////////////////////////////// Twitter Authentication ///////////////////////////////////////////////////////////////////////////

        twitter.setOnClickListener(this);
        twitter_login_btn = findViewById(R.id.twitter_login_btn);
        twitter_login_btn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                final String username = session.getUserName();

                editor.putString("username", username);
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        // Do something with the result, which provides the email address
                        Log.i("username", username);
                        Log.i("Result", String.valueOf(result));

                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        Log.i("Twitter Error", exception.getMessage());
                    }
                });

                Intent intent = new Intent(registerPg.this, homePg.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.i("Twitter Error", exception.getMessage());
            }
        });


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        /////////////////////////////////////////////////////////////////////// Google Authentication/Login ///////////////////////////////////////////////////////////////////////

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        //googleSignInBtn.setOnClickListener(this);
        googleBtn.setOnClickListener(this);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        ////////////////////////////////////////////////////////////////////////// API registration /////////////////////////////////////////////////////////////////////////

        queue = Volley.newRequestQueue(this);
        createAcc.setOnClickListener(this);

        //loginButton and login implemented in animate function

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    }

    public void animate(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(registerPg.this, R.style.MyCustomTheme);
        View mView = getLayoutInflater().inflate(R.layout.login_alert_dialog, null);

        QuizzerNameL = mView.findViewById(R.id.QuizzerNameL);
        PasswordL = mView.findViewById(R.id.LoginPassword);
        loginButton = mView.findViewById(R.id.loginButton);
        loginQuizzy = mView.findViewById(R.id.loginQuizzy);

        Typeface optima = Typeface.createFromAsset(getAssets(), "fonts/optima.ttf");
        Typeface proxima = Typeface.createFromAsset(getAssets(), "fonts/proxima.ttf");
        Typeface copperplate = Typeface.createFromAsset(getAssets(), "fonts/copperplate.ttf");

        loginQuizzy.setTypeface(optima);
        QuizzerNameL.setTypeface(proxima);
        PasswordL.setTypeface(proxima);
        loginButton.setTypeface(copperplate);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAPI();
            }
        });

        mBuilder.setView(mView);

        AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = dialog.getWindow();
        if(window != null){
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
            window.setDimAmount(0.8f); //0 for no dim to 1 for full dim
        }

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case (R.id.gotoLoginPg):
                animate();
                break;

            case (R.id.facebook):
                fb_login_btn.performClick();
                break;

            case (R.id.gmail):
                signIn();
                break;

            case (R.id.createAcc):
                registerUserAPI();
                break;

            case (R.id.twitter):
                twitter_login_btn.performClick();
                break;

        }
    }

    private void loginAPI() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        //Toast.makeText(this, "login button clicked !", Toast.LENGTH_SHORT).show();

        final String password = PasswordL.getText().toString().trim();
        final String username = QuizzerNameL.getText().toString().trim();

        if(password.isEmpty() || username.isEmpty()){
            Toast.makeText(this, "Please enter all the credentials !", Toast.LENGTH_SHORT).show();
            return;
        }

        else{

            Log.i("username", username);
            Log.i("password", password);

            String url = "http://quizzy-api.herokuapp.com/login";

            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Signing in...");
            progressDialog.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response recieved", response);
                            Toast.makeText(registerPg.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("username", QuizzerNameL.getText().toString());
                            editor.apply();

                            Intent intent = new Intent(registerPg.this, homePg.class);
                            startActivity(intent);
                            finish();


                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response recieved", error.toString());
                            progressDialog.dismiss();
                            Toast.makeText(registerPg.this, "Login Failed, Try again... ", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);

                    return params;
                }
            };
            queue.add(postRequest);

        }
    }

    private void registerUserAPI() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        final String username = QuizzerNameR.getText().toString().trim();
        final String email = EmailR.getText().toString().trim();
        final String password = PasswordR.getText().toString().trim();

        if(!isValidEmail(email)){
            Toast.makeText(this, "Enter a valid email !", Toast.LENGTH_SHORT).show();
        }

        else if(password.isEmpty() || username.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all the credentials !", Toast.LENGTH_SHORT).show();
            return;
        }

        else{

            String url = "http://quizzy-api.herokuapp.com/signup";

            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Registering...");
            progressDialog.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Toast.makeText(registerPg.this, "Registration Successful !", Toast.LENGTH_SHORT).show();
                            Log.d("Response recieved", response);
                            progressDialog.dismiss();

                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("username", QuizzerNameR.getText().toString());
                            editor.apply();

                            Intent intent = new Intent(registerPg.this, homePg.class);
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response recieved", error.toString());
                            progressDialog.dismiss();
                            Toast.makeText(registerPg.this, "Registration Failed, Try again... ", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);

                    return params;
                }
            };
            queue.add(postRequest);

        }

    }

    public void displayUserInfo(JSONObject object){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String first_name, last_name, email, id;
        first_name = "";
        last_name = "";
        email = "";
        id = "";
        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");

        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        /*String name = first_name + " " + last_name;
        editor.putString("username", name);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();*/

        //profile pic
        //Bitmap bitmap = getFacebookProfilePicture(id);

        Log.i("Name ", first_name + " " + last_name);
        Log.i("Email ", email);
        Log.i("ID ", id);

    }

    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);

        }

        twitter_login_btn.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);

    }

    private void handleResult(GoogleSignInResult result) {

        if(result.isSuccess()){

            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String imgURL = account.getPhotoUrl().toString();


            //to update profile pic
            //Glide.with(this).load(imgURL).into(ImageView);

            Log.i("name:- ", name);
            Log.i("Email:- ", email);
            Log.i("Img URL:- ", imgURL);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("username", name);
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent intent = new Intent(registerPg.this, homePg.class);
            startActivity(intent);
            finish();

        }

    }


}
