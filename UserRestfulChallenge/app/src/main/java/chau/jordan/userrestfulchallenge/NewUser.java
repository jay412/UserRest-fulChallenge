package chau.jordan.userrestfulchallenge;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import chau.jordan.userrestfulchallenge.utilities.NetworkUtility;

/**
 * <h1>New User</h1>
 * This activity takes in user input from the text fields to create a new user
 * and calls the API to POST the new user based on its candidate parameter
 *
 * @author Jordan Chau
 * @since 2018-03-07
 */
public class NewUser extends AppCompatActivity {
    private EditText mName, mEmail, mCandidate;
    private Button mCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create a New User"); */

        //Creates the back arrow on the top left corner to return to MainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mName = findViewById(R.id.et_name);
        mEmail = findViewById(R.id.et_email);
        mCandidate = findViewById(R.id.et_candidate);
        mCreate = findViewById(R.id.btn_create_user);

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields())
                    new PostOperation().execute("");
            }
        });
    }

    /**
     * This method returns True or False depending if there are empty fields that the user needs to fill
     * @return boolean - This returns True or False depending on conditions fulfilled
     */
    private boolean checkFields() {
        if (TextUtils.isEmpty(mName.getText().toString())) {
            Toast.makeText(NewUser.this, "Please provide a name.", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mEmail.getText().toString())) {
            Toast.makeText(NewUser.this, "Please provide an email.", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mCandidate.getText().toString())) {
            Toast.makeText(NewUser.this, "Please provide a candidate parameter.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * <h1>Post Operation</h1>
     * <p> This is an AsyncTask class to perform a POST operation in the background
     * Takes in a String parameter to create a user with a specified candidate parameter
     */
    private class PostOperation extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(NewUser.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setTitle("Please wait ...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            /*String user = params[0];
            URL userRequestUrl = NetworkUtility.buildURL(user); */

            try {
                String cReq = NetworkUtility.curl(createUserJSONObject().toString());
                Log.v("NewUSER CURL: ", cReq);
            }
            catch(Exception e){
                Log.v("NewUser EXCEPTION: " , e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    /**
     * This method returns a JSONObject that is created based on user input
     * @return JSONObject - Returns a JSONObject with specified key-value pairs
     */
    private JSONObject createUserJSONObject() {
        JSONObject object = new JSONObject();
        try {

            object.put("name", mName.getText().toString());
            object.put("email", mEmail.getText().toString());
            object.put("candidate", mCandidate.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // handles back arrow presses
        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
