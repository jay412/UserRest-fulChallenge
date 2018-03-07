package chau.jordan.userrestfulchallenge.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.MalformedURLException;
import java.util.Scanner;

/**
 * Created by Jordan on 3/6/2018.
 */

public class NetworkUtility {

    private static final String TAG = NetworkUtility.class.getSimpleName();
    private static final String URL = "http://fake-button.herokuapp.com/user";

    final static String CAN_PARAM = "candidate";

    public static URL buildURL(String candidateQuery) {
        Uri uri = Uri.parse(URL).buildUpon().appendQueryParameter(CAN_PARAM, candidateQuery).build();

        URL builtURL = null;
        try {
            builtURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + builtURL);

        return builtURL;
    }

    public static String getHttpUrlResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static void postHttpUrlResponse(URL url, String data) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");

        try {
            //Write
            OutputStream os = urlConnection.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();

            os.close();
        } finally {
            urlConnection.disconnect();
        }
    }
}
