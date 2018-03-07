package chau.jordan.userrestfulchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import chau.jordan.userrestfulchallenge.utilities.NetworkUtility;

public class MainActivity extends AppCompatActivity {

    private TextView mUserTextView, mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserTextView = findViewById(R.id.tv_user_data);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        loadUserData();
    }

    private void loadUserData() {
        showUserDataView();
        new RestOperation().execute("c2366");
    }

    private void showUserDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mUserTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mUserTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private class RestOperation extends AsyncTask<String, Void, String[]> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setTitle("Please wait ...");
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
               return null;
            }

            String user = params[0];
            URL userRequestUrl = NetworkUtility.buildURL(user);

            try {
                String jsonUserResponse = NetworkUtility.getHttpUrlResponse(userRequestUrl);
                Log.d("MainActivity.java", "HTTP URL RESPONSE: " + jsonUserResponse);

                String[] jsonUserData = getUserStringsFromJson(MainActivity.this, jsonUserResponse);

                return jsonUserData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] userData) {
            progressDialog.dismiss();

            if(userData != null) {
                showUserDataView();
                //do something

                for(String uData : userData) {
                mUserTextView.append((uData) + "\n\n\n");
                }
            } else {
                //show error msg
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mUserTextView.setText("");
            loadUserData();
            return true;
        } else if(id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, NewUser.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //add to new class
    private String[] getUserStringsFromJson(Context context, String userJsonStr) throws JSONException{

        final String FB_ID  = "id";
        final String FB_NAME  = "name";
        final String FB_EMAIL  = "email";
        final String FB_CANDIDATE  = "candidate";

        String[] parsedUserData = null;

        JSONArray userArray = new JSONArray(userJsonStr);

        parsedUserData = new String[userArray.length()];
        Log.d("MainActivity.java", "USER ARRAY LENGTH: " + userArray.length());

        for(int i = 0; i < userArray.length(); ++i){
            int id;
            String name;
            String email;
            String candidate;

            JSONObject currentUser = userArray.getJSONObject(i);

            id = currentUser.getInt(FB_ID);
            name = currentUser.getString(FB_NAME);
            email = currentUser.getString(FB_EMAIL);
            candidate = currentUser.getString(FB_CANDIDATE);

            parsedUserData[i] = "ID: " + id + " | Name: " + name + "\nEmail: " + email + "\nCandidate: " + candidate;
        }

        return parsedUserData;
    }
}
