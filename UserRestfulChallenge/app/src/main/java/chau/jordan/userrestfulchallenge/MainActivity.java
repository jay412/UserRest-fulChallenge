package chau.jordan.userrestfulchallenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import chau.jordan.userrestfulchallenge.utilities.NetworkUtility;

/**
 * <h1>Main Activity</h1>
 * This activity displays a list of all users by making an API call with a specified candidate parameter
 * After the user data is retrieved with a GET request, the data is formatted to be displayed in a viewable fashion
 * If there no user data, an error message will show up asking to refresh in order to make another API call
 *
 * @author Jordan Chau
 * @since 2018-03-07
 */
public class MainActivity extends AppCompatActivity {

    private TextView mUserTextView, mErrorMessageDisplay;
    private EditText mCandidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserTextView = findViewById(R.id.tv_user_data);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mCandidate = findViewById(R.id.et_candidate);

        loadUserData();
    }

    /**
     * This method calls the AsyncTask with the specified candidate parameter to send the GET request
     */
    private void loadUserData() {
        if (TextUtils.isEmpty(mCandidate.getText().toString())) {
            Toast.makeText(MainActivity.this, "Please provide a candidate parameter.", Toast.LENGTH_LONG).show();
        } else {
            showUserDataView();
            new GetOperation().execute(mCandidate.getText().toString());
        }
    }

    /**
     * This method displays the user data
     */
    private void showUserDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mUserTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method displays the error message
     */
    private void showErrorMessage(){
        mUserTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * <h1>Get Operation</h1>
     * <p> This is an AsyncTask class to perform a GET operation in the background
     * Takes in a String parameter to display all users with the specified candidate parameter
     * @return String[] - Returns an array of user data to be formatted
     */
    private class GetOperation extends AsyncTask<String, Void, String[]> {

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
                Log.v("MainActivity.java", "HTTP URL RESPONSE: " + jsonUserResponse);

                String[] jsonUserData = getUserStringsFromJson(jsonUserResponse);

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

    /**
     * handles what happens when REFRESH or ADD is tapped
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //refresh the text view with information retrieved from a new GET request API call
        if (id == R.id.action_refresh) {
            mUserTextView.setText("");
            loadUserData();
            return true;
            //switch to the create new user activity
        } else if(id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, NewUser.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method creates a String array that is formatted to be displayed in the user data text view
     * By using the user JSON String that is retrieved from a GET request
     * @param userJsonStr - String parameter to create the String array with
     * @return String[] - Returns a String array that is formatted and represents all user info
     */
    private String[] getUserStringsFromJson(String userJsonStr) throws JSONException{

        final String FB_ID  = "id";
        final String FB_NAME  = "name";
        final String FB_EMAIL  = "email";
        final String FB_CANDIDATE  = "candidate";

        String[] parsedUserData;

        JSONArray userArray = new JSONArray(userJsonStr);

        parsedUserData = new String[userArray.length()];

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
