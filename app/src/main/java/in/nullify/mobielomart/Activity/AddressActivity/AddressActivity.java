package in.nullify.mobielomart.Activity.AddressActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import in.nullify.mobielomart.Adapter.ServerFetch.FetchServer;
import in.nullify.mobielomart.Adapter.SigninActivity.PostMan;
import in.nullify.mobielomart.R;

public class AddressActivity extends AppCompatActivity {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<String> aid = new ArrayList<>();
    private AddressAdapter adapter;
    private ListView lv_address;
    //private LinearLayout ll_progress;
    private TextView tv_addr_delete;
    private Toolbar toolbar;
    private SharedPreferences prefs;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean("SIGNEDIN", false))
            user_id = prefs.getString("USERID", "0");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv_address = findViewById(R.id.lv_address);
        adapter = new AddressAdapter(AddressActivity.this, aid, names, addresses);
        lv_address.setAdapter(adapter);
        adapter = new AddressAdapter(AddressActivity.this, aid, names, addresses);
        Button btn = (Button) findViewById(R.id.btn_address_add);
        TextView tv_addr_edit = (TextView) findViewById(R.id.tv_addr_edit);


        getAddress();
    }

    private void getAddress()

    {
        if (!isNetworkAvailable(getApplicationContext())) {
            findViewById(R.id.progress).setVisibility(View.GONE);
            findViewById(R.id.noconnection).setVisibility(View.VISIBLE);
            findViewById(R.id.reconnect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAddress();
                }
            });
        } else {
            findViewById(R.id.noconnection).setVisibility(View.GONE);
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
            new GetAddress().execute("user_id", user_id);
        }
    }

    public class GetAddress extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://www.nullify.in/mobielo_mart/php/Address/getAddress.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put(arg0[0], arg0[1]);
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
            if (result.equals("0")) {
                findViewById(R.id.noaddress).setVisibility(View.VISIBLE);
            } else {
                try {
                    findViewById(R.id.noaddress).setVisibility(View.GONE);
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray contacts = jsonObj.getJSONArray("result");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        aid.add(c.getString("aid"));
                        names.add(c.getString("name"));
                        addresses.add(c.getString("address"));

                    }
                    findViewById(R.id.progress).setVisibility(View.GONE);

                    lv_address.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //ll_progress.setVisibility(View.GONE);

                } catch (final JSONException e) {

                }
            }
        }

        @Override
        protected void onCancelled() {
            getAddress();
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
        Log.e("Tag", result.toString());
        return result.toString();
    }

    public class AddressAdapter extends ArrayAdapter<String> {
        private Activity context;
        private ArrayList<String> names;
        private ArrayList<String> addresses;
        private ArrayList<String> aid;

        public AddressAdapter(Activity context, ArrayList<String> aid, ArrayList<String> names, ArrayList<String> addresses) {
            super(context, R.layout.address_list, addresses);
            this.context = context;
            this.names = names;
            this.addresses = addresses;
            this.aid = aid;

        }

        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            final View rowView = inflater.inflate(R.layout.address_list, null, true);
            TextView name = (TextView) rowView.findViewById(R.id.tv_addr_name);
            TextView address = (TextView) rowView.findViewById(R.id.tv_addr_address);
            TextView tv_addr_edit = (TextView) rowView.findViewById(R.id.tv_addr_edit);
            TextView tv_addr_delete = (TextView) rowView.findViewById(R.id.tv_addr_delete);
            tv_addr_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int position=(Integer)v.getTag();
                    new PostMan(getContext()).execute("https://www.nullify.in/mobielo_mart/php/Address/deleteAddress.php", "aid", aid.get(position));
                    names.remove(position);
                    addresses.remove(position);
                    aid.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            tv_addr_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Edit Cheyyanda Activity aa Mairan thannittilla", Toast.LENGTH_SHORT).show();
                }
            });
            name.setText(names.get(position));
            address.setText(addresses.get(position));
            return rowView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}