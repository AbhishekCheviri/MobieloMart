package in.nullify.mobielomart.Activity.BestOffers;

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

import in.nullify.mobielomart.Activity.HomeActivity.Fragments.HomeFragment;
import in.nullify.mobielomart.Activity.NewArrivalActivity.NewArivalActivity;
import in.nullify.mobielomart.Adapter.HomeOffer.BestOfferAdapter;
import in.nullify.mobielomart.Adapter.HomeOffer.BestOffersApi;
import in.nullify.mobielomart.Adapter.HomeOffer.HomeOfferAdapter;
import in.nullify.mobielomart.Adapter.HomeOffer.ItemDecorationAlbumColumnsOffers;
import in.nullify.mobielomart.Adapter.HomeOffer.OfferApi;
import in.nullify.mobielomart.Adapter.HomeOffer.OffersHome;
import in.nullify.mobielomart.Adapter.NewProduct.ItemDecorationAlbumColumns;
import in.nullify.mobielomart.Adapter.NewProduct.NewArrivalAdapter;
import in.nullify.mobielomart.Adapter.NewProduct.NewArrivalApi;
import in.nullify.mobielomart.Adapter.NewProduct.NewProduct;
import in.nullify.mobielomart.Adapter.NewProduct.NewProductApi;
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

public class BestOfferActivity extends AppCompatActivity {
    private RecyclerView offers_gride;
    private BestOfferAdapter bestOfferAdapter;
    private List<OffersHome> offersHomes = new ArrayList<>();

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_offer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        offers_gride = (RecyclerView) findViewById(R.id.bestOffers);
        offers_gride.setHasFixedSize(true);
        RecyclerView.LayoutManager offerlayoumanager = new GridLayoutManager(getApplicationContext(), 2);
        offers_gride.addItemDecoration(new ItemDecorationAlbumColumnsOffers(1));
        offers_gride.setLayoutManager(offerlayoumanager);
        bestOfferAdapter = new BestOfferAdapter(getApplicationContext(), offersHomes);
        offers_gride.setAdapter(bestOfferAdapter);

        getNewOffers();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void getNewOffers() {

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
                .baseUrl(OfferApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BestOffersApi api = retrofit.create(BestOffersApi.class);
        Call<List<OffersHome>> call = api.getNewOffer();
        call.enqueue(new Callback<List<OffersHome>>() {
            @Override
            public void onResponse(Call<List<OffersHome>> call, Response<List<OffersHome>> response) {
                offersHomes.addAll(response.body());
                bestOfferAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<OffersHome>> call, Throwable t){
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