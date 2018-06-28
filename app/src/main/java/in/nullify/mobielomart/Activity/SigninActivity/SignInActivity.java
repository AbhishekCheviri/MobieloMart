package in.nullify.mobielomart.Activity.SigninActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import in.nullify.mobielomart.Activity.HomeActivity.HomeActivity;
import in.nullify.mobielomart.Adapter.GetUser.User;
import in.nullify.mobielomart.Adapter.GetUser.Users;
import in.nullify.mobielomart.Adapter.HomeCarousel.CarouselApi;
import in.nullify.mobielomart.Adapter.SigninActivity.PostMan;
import in.nullify.mobielomart.MainActivity;
import in.nullify.mobielomart.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aswin on 18-05-2018.
 */

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private String name;
    private ArrayAdapter adapter;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private EditText SignupEmail,SignupPassword;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private Button btnSignIn,Skip;

    private SharedPreferences prefs;

    private String userid="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (prefs.contains("SIGNEDIN"))
        {
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            SignInActivity.this.finish();
        }

        btnSignIn = findViewById(R.id.btn_signin_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestId().requestProfile()
                .build();

        Skip = (Button) findViewById(R.id.tv_skip);
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putString("USERID",userid).commit();
                prefs.edit().putBoolean("SIGNEDIN",false).commit();
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                SignInActivity.this.finish();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        signIn();
            }
        });


        SignupEmail = (EditText) findViewById(R.id.et_signin_email);
        SignupPassword = (EditText) findViewById(R.id.et_signin_pwd);
        Button SignupButton = (Button) findViewById(R.id.btn_signin);

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SignupEmail.getText().toString().contains("@")) {
                    new Signup().execute(SignupEmail.getText().toString(), SignupPassword.getText().toString(), null, "Result");
                }
                else {
                    Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void getUsers(final String email) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CarouselApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        User api = retrofit.create(User.class);
        Call<List<Users>> call = api.getUser(email);
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                List<Users> list = response.body();
                userid = list.get(0).getP_id();
                prefs.edit().putString("USERID",userid).commit();
                if (!(userid.equals("0")))
                    updateUI(true);
                else
                    Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            //new SignUp().execute(personName, email, "password", null, "Result");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("CallerPM", "signinactivity");
            editor.commit();
            Log.e("Post : ",new PostMan(getApplicationContext()).execute("https://www.nullify.in/mobielo_mart/php/signup" +
                    ".php","name",personName,"email", email,"pass", "password").toString());
            getUsers(email);
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);


        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    public void SignUpResult(String string){
        Log.e("Tag",string);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
        hideProgressDialog();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(Object o) {
        hideProgressDialog();
        findViewById(R.id.progress).setVisibility(View.GONE);
        if(o.equals(true)){

            prefs.edit().putBoolean("SIGNEDIN",true).commit();
            Intent intent=new Intent(this.getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }


    public class Signup extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://www.nullify.in/mobielo_mart/php/signin.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_email", arg0[0]);//post cheyyanda values ex: post..("email","a@.com")
                postDataParams.put("user_password", arg0[1]);//post cheyyanda values ex: post..("email","a@.com")
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("0")){
                Toast.makeText(getApplicationContext(), "Email id is not registered", Toast.LENGTH_LONG).show();
            }else if (result.equals("1")){
                Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }
            else {
                getUsers(SignupEmail.getText().toString());
            }

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
