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

import java.net.URL;

import chau.jordan.userrestfulchallenge.utilities.NetworkUtility;

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

    //checks fields to see if user provided input
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

            //String user = params[0];
            URL userRequestUrl = NetworkUtility.buildURL("");

            try {
                NetworkUtility.postHttpUrlResponse(userRequestUrl, createUserJSONString());
                Log.d("NewUser.java", "POST HTTP URL RESPONSE: " + createUserJSONString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    /*private void createNewUser(){


        HttpURLConnection httpcon;
        String result;
        try {
            //Connect
            httpcon = (HttpURLConnection) NetworkUtility.buildURL(mCandidate.getText().toString()).openConnection();
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(createUserJSONString());
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            Log.d("NewUser.java", "Read Result: " + result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private String createUserJSONString() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", mName.getText().toString());
            object.put("email", mEmail.getText().toString());
            object.put("candidate", mCandidate.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
