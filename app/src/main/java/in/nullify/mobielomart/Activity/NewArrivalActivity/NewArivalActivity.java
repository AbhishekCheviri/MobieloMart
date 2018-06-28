package in.nullify.mobielomart.Activity.NewArrivalActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.nullify.mobielomart.Activity.HomeActivity.Fragments.HomeFragment;
import in.nullify.mobielomart.Adapter.NewProduct.ItemDecorationAlbumColumns;
import in.nullify.mobielomart.Adapter.NewProduct.NewArrivalAdapter;
import in.nullify.mobielomart.Adapter.NewProduct.NewArrivalApi;
import in.nullify.mobielomart.Adapter.NewProduct.NewProduct;
import in.nullify.mobielomart.Adapter.NewProduct.NewProductAdapter;
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

public class NewArivalActivity extends AppCompatActivity {

    private RecyclerView new_products_grid;
    private NewArrivalAdapter newArrivalAdaptere;
    private List<NewProduct> newProducts = new ArrayList<>();

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_arival);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new_products_grid = (RecyclerView) findViewById(R.id.newarrivals);
        new_products_grid.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        new_products_grid.addItemDecoration(new ItemDecorationAlbumColumns(1));
        new_products_grid.setLayoutManager(layoutManager);
        newArrivalAdaptere = new NewArrivalAdapter(NewArivalActivity.this, newProducts);
        new_products_grid.setAdapter(newArrivalAdaptere);

        getNewProduct();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void getNewProduct() {
        newProducts.clear();
        if (!isNetworkAvailable(getApplicationContext()))
        {
            findViewById(R.id.progress).setVisibility(View.GONE);
            findViewById(R.id.noconnection).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.noconnection).setVisibility(View.GONE);
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
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
                    .baseUrl(NewProductApi.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            NewArrivalApi api = retrofit.create(NewArrivalApi.class);
            Call<List<NewProduct>> call = api.getNewProduct();
            call.enqueue(new Callback<List<NewProduct>>() {
                @Override
                public void onResponse(Call<List<NewProduct>> call, Response<List<NewProduct>> response) {
                    newProducts.addAll(response.body());
                    newArrivalAdaptere.notifyDataSetChanged();
                    findViewById(R.id.progress).setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<NewProduct>> call, Throwable t) {
                        getNewProduct();
                }
            });
        }
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
