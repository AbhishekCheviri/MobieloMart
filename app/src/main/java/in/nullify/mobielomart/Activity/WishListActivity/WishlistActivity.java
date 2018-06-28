package in.nullify.mobielomart.Activity.WishListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import in.nullify.mobielomart.Activity.ProductActivity.WishProductActivity;
import in.nullify.mobielomart.Adapter.SigninActivity.PostMan;
import in.nullify.mobielomart.R;

public class WishlistActivity extends AppCompatActivity {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> wids = new ArrayList<>();
    private ArrayList<String> pids = new ArrayList<>();
    private ArrayList<String> pimgs = new ArrayList<>();
    private ArrayList<String> pcolor = new ArrayList<>();
    private ArrayList<String> offid = new ArrayList<>();
    private ArrayList<String> offprice = new ArrayList<>();
    private WishlistAdapter adapter;
    private ListView lv_wishlist;
    private LinearLayout ll_wishlist_progress;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lv_wishlist = findViewById(R.id.lv_wishlist);
        lv_wishlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WishProductActivity.class);
                intent.putExtra("pid",Integer.parseInt(pids.get(position)));
                intent.putExtra("color",pcolor.get(position));
                startActivity(intent);
            }
        });
        adapter = new WishlistAdapter(WishlistActivity.this, wids, names, prices, pids, pimgs,offid,offprice);
        lv_wishlist.setAdapter(adapter);
        Button btn_wishlist_clearall = (Button) findViewById(R.id.btn_wishlist_clearall);
        btn_wishlist_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostMan(getApplicationContext()).execute("https://www.nullify.in/mobielo_mart/php/Wishlist/deleteAllWishlist.php", "user_id", "1");
                names.clear();
                prices.clear();
                wids.clear();
                pimgs.clear();
                pids.clear();
                adapter.notifyDataSetChanged();
            }
        });
        //ll_wishlist_progress = findViewById(R.id.ll_wishlist_progress);

        new GetWishlist().execute("user_id", "1");

    }


    public class GetWishlist extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            //ll_wishlist_progress.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://www.nullify.in/mobielo_mart/php/Wishlist/getWishlist.php");

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
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray contacts = jsonObj.getJSONArray("result");
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    wids.add(c.getString("wid"));
                    names.add(c.getString("name"));
                    prices.add(c.getString("price"));
                    pids.add(c.getString("pid"));
                    pimgs.add(c.getString("pimg"));
                    pcolor.add(c.getString("color"));
                    offid.add(c.getString("off_id"));
                    offprice.add(c.getString("off_price"));

                }
                lv_wishlist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //ll_wishlist_progress.setVisibility(View.GONE);

            } catch (final JSONException e) {

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
        Log.e("Tag", result.toString());
        return result.toString();
    }

    public class WishlistAdapter extends ArrayAdapter<String> {
        private Activity context;
        private ArrayList<String> names;
        private ArrayList<String> prices;
        private ArrayList<String> wids;
        private ArrayList<String> pids;
        private ArrayList<String> pimgs;
        private ArrayList<String> offid;
        private ArrayList<String> offprice;

        public WishlistAdapter(Activity context, ArrayList<String> wids, ArrayList<String> names, ArrayList<String> prices,ArrayList<String> pids,ArrayList<String> pimgs,ArrayList<String> offid,ArrayList<String> offprice) {
            super(context, R.layout.address_list, prices);
            this.context = context;
            this.names = names;
            this.prices = prices;
            this.pids = pids;
            this.pimgs = pimgs;
            this.wids = wids;
            this.offid = offid;
            this.offprice = offprice;

        }

        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            final View rowView = inflater.inflate(R.layout.wishlist_item, null, true);
            TextView name = (TextView) rowView.findViewById(R.id.tv_wishlist_name);
            TextView ofprice = (TextView) rowView.findViewById(R.id.tv_wishlist_offprice);
            TextView price = (TextView) rowView.findViewById(R.id.tv_wishlist_price);
            ImageView iv_wishlist_template = (ImageView) rowView.findViewById(R.id.iv_wishlist_template);
            ImageView iv_wishlist_delete = (ImageView) rowView.findViewById(R.id.iv_wishlist_delete);

            iv_wishlist_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int position=(Integer)v.getTag();
                    new PostMan(getContext()).execute("https://www.nullify.in/mobielo_mart/php/Wishlist/deleteWishlist.php", "wid", wids.get(position));
                    names.remove(position);
                    prices.remove(position);
                    wids.remove(position);
                    pimgs.remove(position);
                    pids.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            name.setText(names.get(position));
            if (offid.get(position).equals("0")) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                int rs = Integer.parseInt(prices.get(position));
                ofprice.setText(format.format(rs));
                price.setText("");
            } else {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                int rs = Integer.parseInt(prices.get(position));
                price.setText((Html.fromHtml("<strike>" + format.format(rs) + "</strike>")));
                price.setTextColor(context.getResources().getColor(R.color.offer_original_text_color));

                rs = Integer.parseInt(offprice.get(position));
                ofprice.setText(format.format(rs));
            }
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(pimgs.get(position)).apply(options).into(iv_wishlist_template);
            return rowView;
        }
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