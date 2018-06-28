package in.nullify.mobielomart.Activity.RecentView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedApi;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedHome;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedProductApi;
import in.nullify.mobielomart.Adapter.HomeRecent.RecentViewAdapter;
import in.nullify.mobielomart.Adapter.SearchResult.ItemDecorationAlbumColumnsResult;
import in.nullify.mobielomart.Adapter.SearchResult.SearchResult;
import in.nullify.mobielomart.Adapter.SearchResult.SearchResultAdapter;
import in.nullify.mobielomart.R;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecentlyViewedActivity extends AppCompatActivity {

    private RecyclerView recently_viewed;
    private ArrayList<SearchResult> recentView = new ArrayList<>();
    private SearchResultAdapter RecentlyViewAdapter;

    private Toolbar toolbar;
    private String userid;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean("SIGNEDIN", false))
            userid = prefs.getString("USERID", "0");

        recently_viewed = (RecyclerView) findViewById(R.id.recently_viewed);
        recently_viewed.setHasFixedSize(true);
        RecyclerView.LayoutManager offerlayoumanager = new GridLayoutManager(getApplicationContext(), 1);
        recently_viewed.addItemDecoration(new ItemDecorationAlbumColumnsResult(1));
        recently_viewed.setLayoutManager(offerlayoumanager);
        RecentlyViewAdapter = new SearchResultAdapter(getApplicationContext(), recentView);
        recently_viewed.setAdapter(RecentlyViewAdapter);

        getRecent();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void getRecent() {
        recentView.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://nullify.in/mobielo_mart/php/RecentView/getRecentView.php";

        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray array = jsonObj.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject c = array.getJSONObject(i);
                                SearchResult searchResult = new SearchResult();
                                searchResult.setP_id(c.getString("p_id"));
                                searchResult.setP_name(c.getString("p_name"));
                                searchResult.setP_price(c.getString("p_price"));
                                searchResult.setP_rating(c.getString("rating"));
                                searchResult.setRelevance(c.getString("relevance"));
                                if (c.has("off_id")) {

                                    searchResult.setOff_id(c.getString("off_id"));
                                    searchResult.setOff_price(c.getString("off_price"));
                                } else {
                                    searchResult.setOff_id("0");
                                    searchResult.setOff_price("none");
                                }
                                String url = c.getString("p_img").replace("\\/", "/");
                                searchResult.setP_img(url);
                                recentView.add(searchResult);
                            }

                            RecentlyViewAdapter.notifyDataSetChanged();
                        } catch (
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new com.android.volley.Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Error.Response", error.getMessage());
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                return params;
            }
        };
        queue.add(postRequest);

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
}
