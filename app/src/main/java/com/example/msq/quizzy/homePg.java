package com.example.msq.quizzy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class homePg extends AppCompatActivity implements View.OnClickListener {

    private TextView swiftText, cppText, cLangText, javaText, pythonText, javascriptText, phpText, rubyText, toolbartitle, welcomeNote, confirmationText;
    private Button signOut, yesSignOut, noSignOut, swift, cpp, cLang, java, ruby, python, javascript, php;

    private RequestQueue requestQueue;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pg);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        swift = findViewById(R.id.swiftIcon);
        cpp = findViewById(R.id.cppIcon);
        cLang = findViewById(R.id.cIcon);
        java = findViewById(R.id.javaIcon);
        ruby = findViewById(R.id.rubyIcon);
        python = findViewById(R.id.pythonIcon);
        javascript = findViewById(R.id.jsIcon);
        php = findViewById(R.id.phpIcon);

        swiftText = findViewById(R.id.swiftText);
        cppText = findViewById(R.id.cppText);
        cLangText = findViewById(R.id.cText);
        javaText = findViewById(R.id.javaText);
        pythonText = findViewById(R.id.pythonText);
        javascriptText = findViewById(R.id.javascriptText);
        phpText = findViewById(R.id.phpText);
        rubyText = findViewById(R.id.rubyText);

        signOut = findViewById(R.id.signOutBtn);
        welcomeNote = findViewById(R.id.welcomeNote);
        toolbartitle = findViewById(R.id.toolbar_title);

        String username = pref.getString("username", null);

        if(username == null || username == "coder") {
            startActivity(getIntent());
        }

        welcomeNote.setText(new StringBuilder().append("Hi, ").append(username).append(" !").toString());

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/feltmark.ttf");
        swiftText.setTypeface(customFont);
        cppText.setTypeface(customFont);
        cLangText.setTypeface(customFont);
        javaText.setTypeface(customFont);
        phpText.setTypeface(customFont);
        javascriptText.setTypeface(customFont);
        phpText.setTypeface(customFont);
        rubyText.setTypeface(customFont);

        Typeface customFont1 = Typeface.createFromAsset(getAssets(), "fonts/proxima.ttf");
        toolbartitle.setTypeface(customFont1);


        signOut.setOnClickListener(this);

        swift.setOnClickListener(this);
        cLang.setOnClickListener(this);
        cpp.setOnClickListener(this);
        java.setOnClickListener(this);
        javascript.setOnClickListener(this);
        php.setOnClickListener(this);
        python.setOnClickListener(this);
        ruby.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onClick(View view) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        switch(view.getId()){

            case R.id.signOutBtn:
                editor.putBoolean("isLoggedIn", false);
                editor.putString("username", "Coder");
                editor.apply();

                animate();
                break;

            case R.id.swiftIcon:
                questions("Swift");
                break;

            case R.id.cppIcon:
                questions("C++");
                break;

            case R.id.cIcon:
                questions("C");
                break;

            case R.id.phpIcon:
                questions("PHP");
                break;

            case R.id.pythonIcon:
                questions("Python");
                break;

            case R.id.rubyIcon:
                questions("Ruby");
                break;

            case R.id.javaIcon:
                questions("Java");
                break;

            case R.id.jsIcon:
                questions("Javascript");
                break;

        }

    }

    private void questions(final String language) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Starting Quiz...");
        progressDialog.show();

        final Intent intent = new Intent(homePg.this, quizPg.class);
        final ArrayList<JsonObj> jsonObj = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final String url = "http://quizzy-api.herokuapp.com/api/quizzyqa";

        //prepare the request
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.i("Initial API Response", response.toString());
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject jsonObject = response.getJSONObject(i);
                                Log.i("Question " + String.valueOf(i+1), jsonObject.toString());

                                JSONArray JsonOptions = jsonObject.getJSONArray("options");
                                String options[] = new String[JsonOptions.length()];

                                for(int j=0;j<JsonOptions.length();j++)
                                    options[j] = JsonOptions.getString(j);

                                Log.i("options:- ", options[0] + " " + options[1] + " " + options[2] + " " + options[3]);

                                String question = jsonObject.getString("question");
                                String questionNo = jsonObject.getString("qno");
                                String QID = jsonObject.getString("_id");
                                String answer = jsonObject.getString("answer");

                                Log.i("Topic", jsonObject.getString("topic") +  " & " + language);

                                // Get the current jsonObject data
                                if(language.equals(jsonObject.getString("topic"))){

                                    JsonObj obj = new JsonObj();
                                    obj.setAnswer(answer);
                                    obj.setOptions(options);
                                    obj.setQID(QID);
                                    obj.setqNo(questionNo);
                                    obj.setQuestion(question);

                                    jsonObj.add(obj);

                                    Log.i("Question", obj.getQID());

                                }

                            }
                            JsonObj test = jsonObj.get(1);
                            Log.i("QID: ", test.getQID());

                            Log.i("Total Questions: ", String.valueOf(jsonObj.size()));

                            progressDialog.dismiss();
                            intent.putParcelableArrayListExtra("jsonObj", jsonObj);
                            intent.putExtra("language", language);
                            startActivity(intent);
                            finish();//remove this line

                        }
                        catch (JSONException e){
                            progressDialog.dismiss();
                            e.printStackTrace();
                            //Toast.makeText(homePg.this, "Response successful but, " + String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                        progressDialog.dismiss();
                        Log.d("Error.Response", error.toString());
                        //Toast.makeText(homePg.this, "Response unsuccessful volley error:-  " + String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // add it to the RequestQueue
        requestQueue.add(jsonArrayRequest);

    }

    private void animate() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(homePg.this, R.style.MyCustomTheme);
        View mView = getLayoutInflater().inflate(R.layout.sign_out_confirmation_dialog, null);

        yesSignOut = mView.findViewById(R.id.yes);
        noSignOut = mView.findViewById(R.id.no);
        confirmationText = mView.findViewById(R.id.confirmation_text);

        Typeface optima = Typeface.createFromAsset(getAssets(), "fonts/optima.ttf");
        Typeface copperplate = Typeface.createFromAsset(getAssets(), "fonts/copperplate.ttf");

        yesSignOut.setTypeface(copperplate);
        noSignOut.setTypeface(copperplate);
        confirmationText.setTypeface(optima);

        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = dialog.getWindow();
        if(window != null){
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
            window.setDimAmount(0.8f); //0 for no dim to 1 for full dim
        }

        dialog.show();

        yesSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });

        noSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void SignOut() {

        final ProgressDialog progressDialog = new ProgressDialog(homePg.this);
        progressDialog.setMessage("Signing off...");
        progressDialog.show();

        //Google Logout
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...

                        progressDialog.dismiss();

                        Intent intent = new Intent(homePg.this, registerPg.class);
                        startActivity(intent);
                        finish();
                    }
                });

        //Twitter Logout
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {

            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookie();
            TwitterCore.getInstance().getSessionManager().clearActiveSession();

            progressDialog.dismiss();

            Intent intent = new Intent(homePg.this, registerPg.class);
            startActivity(intent);
            finish();
        }

        //Facebook Logout
        LoginManager.getInstance().logOut();

        progressDialog.dismiss();

        Intent intent = new Intent(homePg.this, registerPg.class);
        startActivity(intent);
        finish();

    }

}
