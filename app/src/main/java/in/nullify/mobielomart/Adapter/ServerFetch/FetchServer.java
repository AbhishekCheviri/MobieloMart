package in.nullify.mobielomart.Adapter.ServerFetch;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
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

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Abhishekpalodath on 10-03-2018.
 */

public class FetchServer {
    private JSONObject postDataParams = new JSONObject();
    private String loadurl = new String();
    private boolean loadRealtime = false;
    private Activity context;
    private int readTimeout = 15000;
    private int connectTimeout = 15000;
    private OnFetchListener onFetchListener;

    public FetchServer(Activity context) {
        this.context = context;
    }

    public void setOnFetchListener(OnFetchListener onFetchListener) {
        this.onFetchListener = onFetchListener;
    }

    public interface OnFetchListener {
        void onPreExecute();

        void onPostExecute(String result);
    }

    public void setPostDataParams(String key, String value) throws JSONException {
        postDataParams.put(key, value);
    }

    public void setUrl(String loadurl) {
        this.loadurl = loadurl;
    }

    public void execute() {
        new loadurl().execute();
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    private class loadurl extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            onFetchListener.onPreExecute();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(loadurl);

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(readTimeout);
                conn.setConnectTimeout(connectTimeout);
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
            if (!(onFetchListener == null)) {
                if (result != null) {
                    onFetchListener.onPostExecute(result);
                } else {
                    onFetchListener.onPostExecute("");
                }
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
