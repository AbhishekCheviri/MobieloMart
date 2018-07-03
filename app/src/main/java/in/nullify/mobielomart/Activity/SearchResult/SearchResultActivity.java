package in.nullify.mobielomart.Activity.SearchResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.nullify.mobielomart.Activity.ProductActivity.ProductActivity;
import in.nullify.mobielomart.Activity.SearchResult.BottomSheet.BottomSheetFragment;
import in.nullify.mobielomart.Adapter.GetUser.User;
import in.nullify.mobielomart.Adapter.GetUser.Users;
import in.nullify.mobielomart.Adapter.HomeCarousel.CarouselApi;
import in.nullify.mobielomart.Adapter.RecyclerItemClickListener;
import in.nullify.mobielomart.Adapter.SearchResult.ItemDecorationAlbumColumnsResult;
import in.nullify.mobielomart.Adapter.SearchResult.SearchResult;
import in.nullify.mobielomart.Adapter.SearchResult.SearchResultAdapter;
import in.nullify.mobielomart.Adapter.ServerFetch.FetchServer;
import in.nullify.mobielomart.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.volley.Response.success;

public class SearchResultActivity extends AppCompatActivity implements BottomSheetFragment.onCheckedListener {

    private Toolbar toolbar;
    private ImageButton cart;
    private TextView search_key;

    private SearchView seach_prod;
    private MenuItem searchItem;
    private LinearLayout search_search;
    private String searchkey;

    private GoogleSignInAccount account;
    private String username,userid;

    private BottomSheetFragment.onCheckedListener onCheckedListener;

    private RecyclerView search_result;
    private ArrayList<SearchResult> searchResults = new ArrayList<>();
    private ArrayList<String> catName = new ArrayList<>();
    private SearchResultAdapter searchResultAdapter;
    private Button sort;

    BottomSheetDialogFragment bottomSheetDialogFragment;
    private int checked=0;
    private SharedPreferences prefs;
    public SearchResultActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        searchkey = getIntent().getStringExtra("search");

        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean("SIGNEDIN", false))
            userid = prefs.getString("USERID", "0");


        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sort = (Button) findViewById(R.id.sort_button);

        onCheckedListener=this;

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogFragment = new BottomSheetFragment(onCheckedListener,checked);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });
        cart = (ImageButton) findViewById(R.id.search_cart_button);
        search_key = (TextView) findViewById(R.id.search_key);
        search_key.setText(searchkey);

        search_result = (RecyclerView) findViewById(R.id.search_result);
        search_result.setHasFixedSize(true);
        RecyclerView.LayoutManager offerlayoumanager = new GridLayoutManager(getApplicationContext(),1);
        search_result.addItemDecoration(new ItemDecorationAlbumColumnsResult(1));
        search_result.setLayoutManager(offerlayoumanager);
        searchResultAdapter = new SearchResultAdapter(getApplicationContext(),searchResults);
        search_result.setAdapter(searchResultAdapter);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                getSearchResult();
            }
        });
        t1.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product,menu);
        searchItem = menu.findItem(R.id.menu_search);
        return true;
    }

    private void getSearchResult() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://www.nullify.in/mobielo_mart/php/SearchResult/searchresult.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray array = jsonObj.getJSONArray("result");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject c = array.getJSONObject(i);
                                catName.add(c.getString("cat_name"));
                                JSONArray product = c.getJSONArray("products");
                                for (int j = 0; j < product.length(); j++) {
                                    JSONObject pr = product.getJSONObject(j);
                                    SearchResult searchResult = new SearchResult();
                                    searchResult.setP_id(pr.getString("p_id"));
                                    searchResult.setP_name(pr.getString("p_name"));
                                    searchResult.setP_price(pr.getString("p_price"));
                                    searchResult.setP_rating(pr.getString("rating"));
                                    searchResult.setRelevance(pr.getString("relevance"));
                                  if (pr.has("off_id")) {

                                        searchResult.setOff_id(pr.getString("off_id"));
                                        searchResult.setOff_price(pr.getString("off_price"));
                                    }else {
                                        searchResult.setOff_id("0");
                                        searchResult.setOff_price("none");
                                    }
                                    String url = pr.getString("p_img").replace("\\/","/");
                                    searchResult.setP_img(url);
                                    searchResults.add(searchResult);
                                }
                            }

                            searchResultAdapter.notifyDataSetChanged();
                            findViewById(R.id.search_progress).setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            Log.d("Error.Response", error.getMessage());
                        }catch (Exception e){

                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("skey", searchkey);
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void addSearched() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://www.nullify.in/mobielo_mart/php/SearchResult/addSearches.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("skey", searchkey);
                params.put("p_id", userid);
                return params;
            }
        };
        queue.add(postRequest);

    }

    private void getUsers() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CarouselApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        User api = retrofit.create(User.class);
        Call<List<Users>> call = api.getUser(account.getEmail(),"0");
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                List<Users> list = response.body();
                userid = list.get(0).getId();
                addSearched();
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                String TAG ="error";
            }
        });

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onChecked(int id) {
        checked = id;
        switch (checked)
        {
            case R.id.sort_new:
                sort.setText("Sort: Newest Firts");
                Collections.sort(searchResults, new Comparator<SearchResult>() {
                    @Override
                    public int compare(SearchResult lhs, SearchResult rhs) {
                        return (rhs.getP_id().compareTo(lhs.getP_id()));
                    }
                });
                searchResultAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_rel:
                sort.setText("Sort: By Relevance");
                Collections.sort(searchResults, new Comparator<SearchResult>() {
                    @Override
                    public int compare(SearchResult lhs, SearchResult rhs) {
                        return (rhs.getRelevance().compareTo(lhs.getRelevance()));
                    }
                });
                searchResultAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_lh:
                sort.setText("Sort: Price -- Low to High");
                Collections.sort(searchResults, new Comparator<SearchResult>() {
                    @Override
                    public int compare(SearchResult lhs, SearchResult rhs) {
                        return (lhs.getP_price().compareTo(rhs.getP_price()));
                    }
                });
                searchResultAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_hl:
                sort.setText("Sort: Price -- High to Low");
                Collections.sort(searchResults, new Comparator<SearchResult>() {
                    @Override
                    public int compare(SearchResult lhs, SearchResult rhs) {
                        return (rhs.getP_price().compareTo(lhs.getP_price()));
                    }
                });
                searchResultAdapter.notifyDataSetChanged();
                break;
        }
    }
}
