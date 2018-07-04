package in.nullify.mobielomart.Activity.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.nullify.mobielomart.Activity.AddressActivity.AddressActivity;
import in.nullify.mobielomart.Activity.BestOffers.BestOfferActivity;
import in.nullify.mobielomart.Activity.EditAccountActivity.EditAccountActivity;
import in.nullify.mobielomart.Activity.FeaturedProducts.FeaturedProducts;
import in.nullify.mobielomart.Activity.InterestActivity.InterestActivity;
import in.nullify.mobielomart.Activity.NewArrivalActivity.NewArivalActivity;
import in.nullify.mobielomart.Activity.OrdersActivity.OrdersActivity;
import in.nullify.mobielomart.Activity.ProductActivity.ProductActivity;
import in.nullify.mobielomart.Activity.RecentView.RecentlyViewedActivity;
import in.nullify.mobielomart.Activity.SearchResult.SearchResultActivity;
import in.nullify.mobielomart.Activity.SigninActivity.SignInActivity;
import in.nullify.mobielomart.Activity.TrackActivity.TrackActivity;
import in.nullify.mobielomart.Activity.WishListActivity.WishlistActivity;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedApi;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedHome;
import in.nullify.mobielomart.Adapter.FeaturedProduct.HomeFeaturedAdapter;
import in.nullify.mobielomart.Adapter.GetUser.User;
import in.nullify.mobielomart.Adapter.GetUser.Users;
import in.nullify.mobielomart.Adapter.HomeCarousel.CardFragmentPagerAdapter;
import in.nullify.mobielomart.Adapter.HomeCarousel.Carousel;
import in.nullify.mobielomart.Adapter.HomeCarousel.CarouselApi;
import in.nullify.mobielomart.Adapter.HomeOffer.HomeOfferAdapter;
import in.nullify.mobielomart.Adapter.HomeOffer.OfferApi;
import in.nullify.mobielomart.Adapter.HomeOffer.OffersHome;
import in.nullify.mobielomart.Adapter.HomeRecent.RecentView;
import in.nullify.mobielomart.Adapter.HomeRecent.RecentViewAdapter;
import in.nullify.mobielomart.Adapter.InterestedGride.SuggestionGrideAdapter;
import in.nullify.mobielomart.Adapter.NewProduct.ItemDecorationAlbumColumns;
import in.nullify.mobielomart.Adapter.NewProduct.NewProduct;
import in.nullify.mobielomart.Adapter.NewProduct.NewProductAdapter;
import in.nullify.mobielomart.Adapter.NewProduct.NewProductApi;
import in.nullify.mobielomart.Adapter.NonScrollListView;
import in.nullify.mobielomart.Adapter.SearchResult.ItemDecorationAlbumColumnsResult;
import in.nullify.mobielomart.Adapter.ServerFetch.FetchServer;
import in.nullify.mobielomart.Adapter.SugListAdapter;
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

public class HomeActivity extends AppCompatActivity {


    private int cacheSize = 10*1024*1024;

    private SearchView seach_prod;
    private Toolbar toolbar;
    private AppBarLayout home_appbar;

    private TextView user_name,user_email;
    private ImageView user_img;

    private MenuItem searchItem;
    private Button searchOpen;
    private String searchString;
    private ArrayList<String> suggestions = new ArrayList<>();
    private ArrayList<String> type = new ArrayList<>();
    private String user_id=new String();


    private ListView suggestion_list, cat_list, suggestion_searched;
    private View footerView;

    private ViewPager offer_carousel;
    private List<Carousel> carousels = new ArrayList<>();
    private AppCompatActivity activity;

    private SugListAdapter adapter, adapter1, adapter2;
    private ArrayList<String> cats = new ArrayList<>();
    private ArrayList<String> ctype = new ArrayList<>();

    private ArrayList<String> searched = new ArrayList<>();
    private ArrayList<String> searchedtype = new ArrayList<>();

    private DachshundTabLayout home_tabbar;
    private ViewPager home_viewpager;
    private SharedPreferences prefs;
    private RecyclerView new_products_grid;
    private NewProductAdapter newProductAdapter;
    private List<NewProduct> newProducts = new ArrayList<>();

    private RecyclerView interested_gride;
    private RecyclerView interested_gride2;
    private SuggestionGrideAdapter interestedGrideAddapter;
    private SuggestionGrideAdapter interestedGrideAddapter2;
    private List<NewProduct> interestedProduct = new ArrayList<>();
    private List<NewProduct> interestedProduct2 = new ArrayList<>();

    private RecyclerView offers_gride;
    private HomeOfferAdapter homeOfferAdapter;
    private List<OffersHome> offersHomes = new ArrayList<>();

    private RecyclerView featured_gride;
    private HomeFeaturedAdapter homeFeaturedAdapter;
    private List<FeaturedHome> featuredHomes = new ArrayList<>();

    private RecyclerView recentlyViewd;
    private ArrayList<RecentView> recentList = new ArrayList<>();
    private ArrayList<String> catName = new ArrayList<>();
    private RecentViewAdapter recentListAdapter;
    private FirebaseAuth mAuth;

    private String c1,c2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        home_appbar = (AppBarLayout) findViewById(R.id.home_appbar);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        LinearLayout llsignin=(LinearLayout)findViewById(R.id.ll_signin);
        ListView lv_account=(ListView) findViewById(R.id.lv_account);
        Button btn_acc_signinup=(Button) findViewById(R.id.btn_acc_signinup);
        user_img = (ImageView) findViewById(R.id.user_img);
        user_name = (TextView) findViewById(R.id.user_name);
        user_email = (TextView) findViewById(R.id.user_email);

        View footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sug_footer, null, false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!prefs.getBoolean("SKIP", false)){
            user_id = "0";
            llsignin.setVisibility(LinearLayout.GONE);
            findViewById(R.id.user_info).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.user_info).setVisibility(View.GONE);
            llsignin.setVisibility(LinearLayout.VISIBLE);
        }

        btn_acc_signinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefs.edit().remove("SKIP").commit();
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
            }
        });
        lv_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!prefs.getBoolean("SIGNEDIN", false)) {
                    Toast.makeText(getApplicationContext(),"Sign in/Sign up to continue",Toast.LENGTH_SHORT).show();
                }
                else{
                    switch (position){
                        case 0:Intent intent=new Intent(getApplicationContext(),OrdersActivity.class);
                            startActivity(intent);
                            break;
                        case 1:intent=new Intent(getApplicationContext(),WishlistActivity.class);
                            startActivity(intent);
                            break;
                        case 2:intent=new Intent(getApplicationContext(),TrackActivity.class);
                            startActivity(intent);
                            break;
                        case 3:intent=new Intent(getApplicationContext(),EditAccountActivity.class);
                            startActivity(intent);
                            break;
                        case 4:intent=new Intent(getApplicationContext(), AddressActivity.class);
                            startActivity(intent);
                            break;
                        case 5://Logout Code
                            break;
                    }
                }

            }
        });
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        toolbar1.inflateMenu(R.menu.menu_product);
        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_search:findViewById(R.id.home_cont).setVisibility(View.GONE);
                    searchItem.expandActionView();
                    findViewById(R.id.sugg_list_cont).setVisibility(View.VISIBLE);break;
                }
                return true;
            }
        });

        loadSearched();
        suggestion_list = (ListView) findViewById(R.id.suggestion_list_search);
        suggestion_searched = (ListView) findViewById(R.id.suggestion_searched);
        cat_list =(ListView) findViewById(R.id.cat_list);
        adapter = new SugListAdapter(HomeActivity.this,suggestions,type);
        suggestion_list.setAdapter(adapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawer.setDrawerElevation(0f);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                float moveFactor = 0;
                moveFactor = (drawerView.getWidth() * slideOffset);

                findViewById(R.id.content).setTranslationX(moveFactor);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Typeface geomatos = Typeface.createFromAsset(getAssets(),"Geometos.ttf");
        TextView home_title = findViewById(R.id.home_title);
        TextView phone_text = findViewById(R.id.phones_text);
        TextView acc_text = findViewById(R.id.acc_text);
        TextView spare_text = findViewById(R.id.spare_text);
        TextView tool_text = findViewById(R.id.tool_text);
        home_title.setTypeface(geomatos);
        phone_text.setTypeface(geomatos);
        acc_text.setTypeface(geomatos);
        spare_text.setTypeface(geomatos);
        tool_text.setTypeface(geomatos);
        //((TextView) findViewById(R.id.gride_head_1)).setTypeface(geomatos);
        //.setTypeface(geomatos);
        //((TextView) findViewById(R.id.gride_head_3)).setTypeface(geomatos);
       // ((TextView) findViewById(R.id.shead2)).setTypeface(geomatos);
       // ((TextView) findViewById(R.id.shead3)).setTypeface(geomatos);
       // ((TextView) findViewById(R.id.shead)).setTypeface(geomatos);

        Button newArrival = (Button) findViewById(R.id.newarrival_view_all);
        //newArrival.setTypeface(geomatos);
        newArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewArivalActivity.class));
            }
        });

        Button bestOffers = (Button) findViewById(R.id.best_offers);
       // bestOffers.setTypeface(geomatos);
        bestOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BestOfferActivity.class));
            }
        });
        Button featured = (Button)findViewById(R.id.featured);
       // featured.setTypeface(geomatos);
        featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FeaturedProducts.class));
            }
        });
        //((Button) findViewById(R.id.interest_view)).setTypeface(geomatos);
        findViewById(R.id.interest_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InterestActivity.class);
                intent.putExtra("catid",c1);
                startActivity(intent);
            }
        });

        //((Button) findViewById(R.id.interest_view2)).setTypeface(geomatos);
        findViewById(R.id.interest_view2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InterestActivity.class);
                intent.putExtra("catid",c2);
                startActivity(intent);
            }
        });
        //((Button) findViewById(R.id.recentview)).setTypeface(geomatos);
        findViewById(R.id.recentview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecentlyViewedActivity.class));
            }
        });

        offer_carousel = (ViewPager) findViewById(R.id.home_carousal);
        offer_carousel.requestFocus();
        offer_carousel.setPageMargin(0);
        offer_carousel.setPadding(0, 0, 0, 0);

        new_products_grid = (RecyclerView) findViewById(R.id.new_products_grid);
        new_products_grid.setHasFixedSize(true);
        new_products_grid.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newProductAdapter = new NewProductAdapter(HomeActivity.this, null, newProducts);
        new_products_grid.setAdapter(newProductAdapter);

        interested_gride = (RecyclerView) findViewById(R.id.suggestion_list);
        interested_gride.setHasFixedSize(true);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        interested_gride.addItemDecoration(new ItemDecorationAlbumColumns(1));
        interested_gride.setLayoutManager(layoutManager1);
        interestedGrideAddapter = new SuggestionGrideAdapter(HomeActivity.this, null, interestedProduct);
        interested_gride.setAdapter(interestedGrideAddapter);

        interested_gride2 = (RecyclerView) findViewById(R.id.suggestion_list2);
        interested_gride2.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager2.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        interested_gride2.addItemDecoration(new ItemDecorationAlbumColumns(1));
        interested_gride2.setLayoutManager(layoutManager2);
        interestedGrideAddapter2 = new SuggestionGrideAdapter(HomeActivity.this, null, interestedProduct2);
        interested_gride2.setAdapter(interestedGrideAddapter2);

        offers_gride = (RecyclerView)findViewById(R.id.offers_gride);
        offers_gride.setHasFixedSize(true);
        offers_gride.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        homeOfferAdapter = new HomeOfferAdapter(HomeActivity.this, null, offersHomes);
        offers_gride.setAdapter(homeOfferAdapter);

        featured_gride = (RecyclerView) findViewById(R.id.featured_gride);
        featured_gride.setHasFixedSize(true);
        featured_gride.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        homeFeaturedAdapter = new HomeFeaturedAdapter(HomeActivity.this, null, featuredHomes);
        featured_gride.setAdapter(homeFeaturedAdapter);

        recentlyViewd = (RecyclerView) findViewById(R.id.recently_viewed);
        recentlyViewd.setHasFixedSize(true);
        RecyclerView.LayoutManager offerlayoumanager1 = new GridLayoutManager(getApplicationContext(), 1);
        recentlyViewd.addItemDecoration(new ItemDecorationAlbumColumnsResult(1));
        recentlyViewd.setLayoutManager(offerlayoumanager1);
        recentListAdapter = new RecentViewAdapter(HomeActivity.this, recentList,null);
        recentlyViewd.setAdapter(recentListAdapter);
        if (mAuth.getCurrentUser() == null)
            loaddatas();
        else
            getUsers();
    }

    private void getUsers() {

        if (!isNetworkAvailable(getApplicationContext())) {
            findViewById(R.id.noconnection).setVisibility(View.VISIBLE);
            findViewById(R.id.reconnect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUsers();
        }
            });
            findViewById(R.id.home_progress).setVisibility(View.GONE);
        } else {
            findViewById(R.id.home_progress).setVisibility(View.VISIBLE);
            findViewById(R.id.noconnection).setVisibility(View.GONE);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CarouselApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        User api = retrofit.create(User.class);
        Call<List<Users>> call = api.getUser(mAuth.getCurrentUser().getEmail(),"0");
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                List<Users> list = response.body();
                user_id = list.get(0).getId();
                user_name.setText(list.get(0).getName());
                user_email.setText(list.get(0).getEmail());
                Glide.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(user_img);
                loaddatas();
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                getUsers();
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loaddatas() {
        carousels.clear();
        newProducts.clear();
        featuredHomes.clear();
        offersHomes.clear();
        interestedProduct.clear();
        recentList.clear();
        if (!isNetworkAvailable(getApplicationContext())) {
            findViewById(R.id.noconnection).setVisibility(View.VISIBLE);
            findViewById(R.id.reconnect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loaddatas();
                }
            });
            findViewById(R.id.home_progress).setVisibility(View.GONE);
        } else {
            findViewById(R.id.home_progress).setVisibility(View.VISIBLE);
            findViewById(R.id.noconnection).setVisibility(View.GONE);
            if (!(user_id.equals("0"))) {
                Thread t5 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        interestedProduct.clear();
                        getInterested();
                    }
                });
                t5.start();
                Thread t6 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recentList.clear();getRecent();
                    }
                });
                t6.start();
            }
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    newProducts.clear();getNewProduct();
                }
            });
            t1.start();
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    offersHomes.clear();getNewOffers();
                }
            });
            t2.start();
            Thread t3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    carousels.clear();
                    getCarousels();
                }
            });
            t3.start();
            Thread t4 = new Thread(new Runnable() {
                @Override
                public void run() {
                    featuredHomes.clear();
                    getFeatured();
                }
            });
            t4.start();

        }
    }

    private void getCarousels() {
        carousels.clear();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getApplicationContext().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
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
                .baseUrl(CarouselApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CarouselApi api = retrofit.create(CarouselApi.class);
        Call<List<Carousel>> call = api.getCarousel();
        call.enqueue(new Callback<List<Carousel>>() {
            @Override
            public void onResponse(Call<List<Carousel>> call, Response<List<Carousel>> response) {
                carousels = response.body();
                CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(HomeActivity.this.getSupportFragmentManager(), dpToPx(0), carousels);


                offer_carousel.setAdapter(pagerAdapter);
                //offer_carousel.setPageTransformer(false, fragmentCardShadowTransformer);
                offer_carousel.setOffscreenPageLimit(10);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.dot_carousel);
                tabLayout.setupWithViewPager(offer_carousel, true);
                findViewById(R.id.home_progress).setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<Carousel>> call, Throwable t) {
                loaddatas();
            }
        });
    }
    private void getNewProduct() {
        newProducts.clear();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getApplicationContext().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
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

        NewProductApi api = retrofit.create(NewProductApi.class);
        Call<List<NewProduct>> call = api.getNewProduct();
        call.enqueue(new Callback<List<NewProduct>>() {
            @Override
            public void onResponse(Call<List<NewProduct>> call, Response<List<NewProduct>> response) {
                newProducts.addAll(response.body());
                newProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<NewProduct>> call, Throwable t) {
                loaddatas();
            }
        });

    }
    private void getInterested() {
        interestedProduct.clear();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://www.nullify.in/mobielo_mart/php/homepage/getInterested.php";

        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!(response.equals("0"))) {
                            findViewById(R.id.interest_cont).setVisibility(View.VISIBLE);
                            findViewById(R.id.interest_cont2).setVisibility(View.VISIBLE);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray array = jsonObj.getJSONArray("result");
                                JSONObject first = array.getJSONObject(0);
                                c1=first.keys().next();
                                JSONArray array1 = first.getJSONArray(c1);
                                for (int i=0;i<array1.length();i++) {
                                    JSONObject c = array1.getJSONObject(i);
                                    NewProduct searchResult = new NewProduct(c.getString("p_id"),
                                            c.getString("cat_id"), c.getString("p_name"), c.getString("p_price")
                                            , c.getString("p_img"), c.getString("off_id"), c.getString("off_price"));
                                    interestedProduct.add(searchResult);
                                }
                                interestedGrideAddapter.notifyDataSetChanged();

                                JSONObject second = array.getJSONObject(1);
                                c2=second.keys().next();
                                JSONArray array2 = second.getJSONArray(c2);
                                for (int i=0;i<array2.length();i++) {
                                    JSONObject c = array2.getJSONObject(i);
                                    NewProduct searchResult = new NewProduct(c.getString("p_id"),
                                            c.getString("cat_id"), c.getString("p_name"), c.getString("p_price")
                                            , c.getString("p_img"), c.getString("off_id"), c.getString("off_price"));
                                    interestedProduct2.add(searchResult);
                                }
                                interestedGrideAddapter2.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        loaddatas();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void getNewOffers() {
        offersHomes.clear();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getApplicationContext().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
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

        OfferApi api = retrofit.create(OfferApi.class);
        Call<List<OffersHome>> call = api.getNewOffer();
        call.enqueue(new Callback<List<OffersHome>>() {
            @Override
            public void onResponse(Call<List<OffersHome>> call, Response<List<OffersHome>> response) {
                offersHomes.addAll(response.body());
                homeOfferAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<OffersHome>> call, Throwable t) {
                loaddatas();
            }
        });

    }
    private void getFeatured() {
        featuredHomes.clear();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getApplicationContext().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
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
                .baseUrl(FeaturedApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FeaturedApi api = retrofit.create(FeaturedApi.class);
        Call<List<FeaturedHome>> call = api.getFeatured();
        call.enqueue(new Callback<List<FeaturedHome>>() {
            @Override
            public void onResponse(Call<List<FeaturedHome>> call, Response<List<FeaturedHome>> response) {
                featuredHomes.addAll(response.body());
                homeFeaturedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<FeaturedHome>> call, Throwable t) {
                loaddatas();
            }
        });

    }
    private void getRecent() {

        recentList.clear();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://www.nullify.in/mobielo_mart/php/homepage/getRecent.php";

        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("0")) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray array = jsonObj.getJSONArray("result");

                                for (int i = 0; i < array.length()&&i<3; i++) {
                                    JSONObject c = array.getJSONObject(i);
                                    catName.add(c.getString("p_id"));
                                    RecentView searchResult = new RecentView();
                                    searchResult.setP_id(c.getString("p_id"));
                                    searchResult.setP_name(c.getString("p_name"));
                                    searchResult.setP_price(c.getString("p_price"));
                                    if (!(c.get("off_id").equals("0"))) {

                                        searchResult.setOff_id(c.getString("off_id"));
                                        searchResult.setOff_price(c.getString("off_price"));
                                    } else {
                                        searchResult.setOff_id("0");
                                        searchResult.setOff_price("none");
                                    }
                                    String url = c.getString("p_img").replace("\\/", "/");
                                    searchResult.setP_img(url);
                                    recentList.add(searchResult);
                                }


                                findViewById(R.id.recent_cont).setVisibility(View.VISIBLE);
                                recentListAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (!recentList.isEmpty()) {
                                    findViewById(R.id.recent_cont).setVisibility(View.VISIBLE);
                                    recentListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        Log.d("Response", response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        loaddatas();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }
        };
        queue.add(postRequest);


    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        searchItem = menu.findItem(R.id.menu_search);
        seach_prod = (SearchView) MenuItemCompat.getActionView(searchItem);
        seach_prod.setQueryHint("Search for Product Brands and More");
        seach_prod.setMaxWidth(Integer.MAX_VALUE);

        footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sug_footer, null, false);
        suggestion_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("search", suggestions.get(position));
                suggestion_searched.removeFooterView(footerView);
                suggestion_searched.addFooterView(footerView);
                if (!(searched.contains(suggestions.get(position)))) {
                    searched.add(suggestions.get(position));
                    searchedtype.add("1");
                    adapter2.notifyDataSetChanged();
                }
                addSearched();
                startActivity(intent);
            }
        });
        cat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("search", cats.get(position));
                suggestion_searched.removeFooterView(footerView);
                suggestion_searched.addFooterView(footerView);
                if (!(searched.contains(cats.get(position)))) {
                    searched.add(cats.get(position));
                    searchedtype.add("1");
                    adapter2.notifyDataSetChanged();
                }
                addSearched();
                startActivity(intent);
            }
        });
        suggestion_searched.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("search", cats.get(position));
                startActivity(intent);
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (searched.isEmpty())
                    suggestion_searched.removeFooterView(footerView);
                else {
                    suggestion_searched.removeFooterView(footerView);
                    suggestion_searched.addFooterView(footerView);
                }
                adapter2 = new SugListAdapter(HomeActivity.this, searched, searchedtype);
                suggestion_searched.setAdapter(adapter2);
                FetchServer getCats = new FetchServer(HomeActivity.this);
                getCats.setUrl("http://nullify.in/mobielo_mart/php/homepage/getCats.php");
                getCats.setOnFetchListener(new FetchServer.OnFetchListener() {
                    @Override
                    public void onPreExecute() {
                        cats.clear();
                        ctype.clear();
                    }

                    @Override
                    public void onPostExecute(String result) {
                        try {
                            JSONObject jsonObj = new JSONObject(result);
                            JSONArray array = jsonObj.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject c = array.getJSONObject(i);
                                cats.add(c.getString("res"));
                                ctype.add(c.getString("type"));
                            }
                            adapter1 = new SugListAdapter(HomeActivity.this, cats, ctype);
                            cat_list.setAdapter(adapter1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                getCats.execute();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                suggestion_list.setAdapter(null);
                cat_list.setAdapter(null);
                findViewById(R.id.home_cont).setVisibility(View.VISIBLE);
                findViewById(R.id.sugg_list_cont).setVisibility(View.GONE);
                return true;
            }
        });


        seach_prod.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("query",query);
                final Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("search", query);
                suggestion_searched.removeFooterView(footerView);
                suggestion_searched.addFooterView(footerView);
                if (!(searched.contains(query))) {
                    searched.add(query);
                    searchedtype.add("1");
                    adapter2.notifyDataSetChanged();
                }
                addSearched();
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    cat_list.setVisibility(View.GONE);
                    suggestion_searched.setVisibility(View.GONE);
                    suggestion_list.setVisibility(View.VISIBLE);
                    getSuggestion(newText);

                } else {
                    cat_list.setVisibility(View.VISIBLE);
                    suggestion_list.setVisibility(View.GONE);
                    suggestion_searched.setVisibility(View.VISIBLE);
                    suggestions.clear();
                    type.clear();
                    try {
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
                return true;
            }
        });
        return true;
    }
    public void addSearched() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.putInt("size", searchedtype.size());

        for (int i = 0; i < searchedtype.size(); i++) {
            mEdit1.remove("search_" + i);
            mEdit1.remove("searcht_" + i);
            mEdit1.putString("search_" + i, searched.get(i));
            mEdit1.putString("searcht_" + i, searchedtype.get(i));
        }

        mEdit1.commit();
    }

    public void removeHistory() {

        FetchServer deleteHis = new FetchServer(HomeActivity.this);
        deleteHis.setUrl("https://nullify.in/mobielo_mart/php/homepage/clearHistories.php");
        deleteHis.setOnFetchListener(new FetchServer.OnFetchListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(String result) {

            }
        });

        try {
            deleteHis.setPostDataParams("p_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        deleteHis.execute();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEdit1 = sp.edit();

        mEdit1.remove("size");

        for (int i = 0; i < searchedtype.size(); i++) {
            mEdit1.remove("search_" + i);
            mEdit1.remove("searcht_" + i);
        }

        mEdit1.commit();
    }

    public void loadSearched() {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        searchedtype.clear();
        searched.clear();

        if (mSharedPreference1.contains("size")) {

            int size = mSharedPreference1.getInt("size", 0);

            for (int i = 0; i < size; i++) {
                searched.add(mSharedPreference1.getString("search_" + i, null));
                searchedtype.add(mSharedPreference1.getString("searcht_" + i, null));
            }
            //Collections.reverse(searchedtype);
            //Collections.reverse(searched);
        }

    }

    private void getSuggestion(final String skey) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://www.nullify.in/mobielo_mart/php/homepage/autoComplete.php";

        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!response.equals("0")) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);

                                JSONArray array = jsonObj.getJSONArray("result");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject c = array.getJSONObject(i);
                                    if (!(suggestions.contains(c.getString("res")))) {
                                        suggestions.add(c.getString("res"));
                                        type.add(c.getString("type"));
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("Response", response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("skey", skey);
                params.put("user_id",user_id);
                return params;
            }
        };
        queue.add(postRequest);


    }

    public void activityStart(String productid) {
        Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
        intent.putExtra("pid", Integer.parseInt(productid));
        startActivity(intent);
    }
}
