package in.nullify.mobielomart.Activity.InterestActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedApi;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedHome;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedProductAdapter;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedProductApi;
import in.nullify.mobielomart.Adapter.FeaturedProduct.InterestedApi;
import in.nullify.mobielomart.Adapter.HomeOffer.ItemDecorationAlbumColumnsOffers;
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

public class InterestActivity extends AppCompatActivity {

    private String catid;
    private RecyclerView featured_gride;
    private FeaturedProductAdapter featuredProductAdapter;
    private List<FeaturedHome> featuredHomes = new ArrayList<>();

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catid = getIntent().getStringExtra("catid");

        featured_gride = (RecyclerView) findViewById(R.id.featuredProducts);
        featured_gride.setHasFixedSize(true);
        RecyclerView.LayoutManager fetured = new GridLayoutManager(getApplicationContext(), 2);
        featured_gride.addItemDecoration(new ItemDecorationAlbumColumnsOffers(1));
        featured_gride.setLayoutManager(fetured);
        featuredProductAdapter = new FeaturedProductAdapter(getApplicationContext(), featuredHomes);
        featured_gride.setAdapter(featuredProductAdapter);

        getFeatured();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void getFeatured() {

        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable(getApplicationContext())) {
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        }
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterestedApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterestedApi api = retrofit.create(InterestedApi.class);
        Call<List<FeaturedHome>> call = api.getFeatured(catid);
        call.enqueue(new Callback<List<FeaturedHome>>() {
            @Override
            public void onResponse(Call<List<FeaturedHome>> call, Response<List<FeaturedHome>> response) {
                featuredHomes.addAll(response.body());
                featuredProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<FeaturedHome>> call, Throwable t) {
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product,menu);
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