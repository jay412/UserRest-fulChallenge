package chau.jordan.userrestfulchallenge;

import android.app.ProgressDialog;
import android.net.Uri;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

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
                    new PostOperation().execute(mCandidate.getText().toString());
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

            /*String user = params[0];
            URL userRequestUrl = NetworkUtility.buildURL(user);

            try {
                /*Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", mName.getText().toString())
                        .appendQueryParameter("email", mEmail.getText().toString())
                        .appendQueryParameter("candidate", mCandidate.getText().toString());
                String query = builder.build().getEncodedQuery();
                JSONObject postDataParmas = createUserJSONObject();

                NetworkUtility.postHttpUrlResponse(userRequestUrl, getPostDataString(postDataParmas));
                Log.d("NewUser.java", "POST HTTP URL RESPONSE: " + getPostDataString(postDataParmas));

            } catch (Exception e) {
                e.printStackTrace();
            } */
            try {
            /*URL url = new URL("http://fake-button.herokuapp.com/user"); // here is your URL path

            JSONObject postDataParams = createUserJSONObject();
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds );
            conn.setConnectTimeout(15000 /* milliseconds );
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            //writer.write(getPostDataString(postDataParams));
                //writer.write();

                Log.e("POST DATA STRING: ", getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                //return sb.toString();
            }
            else {
                //return new String("false : "+responseCode);
                Log.d("NewUser RC: " , new String("false : "+responseCode));
            } */

                String c = curl(createUserJSONObject().toString(), "http://fake-button.herokuapp.com/user");
                Log.e("NewUSER CURL: ", c);
        }
            catch(Exception e){
                Log.d("NewUser EXCEPTION: " , e.getMessage());
            //return new String("Exception: " + e.getMessage());
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

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
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

    private static String curl(String minusD, String url) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type",  "application/x-www-form-urlencoded");
        con.getOutputStream().write(minusD.getBytes());
        con.getOutputStream().close();

        ByteArrayOutputStream rspBuff = new ByteArrayOutputStream();
        InputStream rspStream = con.getInputStream();

        int c;
        while ((c = rspStream.read()) > 0) {
            rspBuff.write(c);
        }
        rspStream.close();

        return new String(rspBuff.toByteArray());
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
