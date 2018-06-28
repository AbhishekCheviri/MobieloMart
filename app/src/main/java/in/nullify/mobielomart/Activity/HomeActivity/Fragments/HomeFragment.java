package in.nullify.mobielomart.Activity.HomeActivity.Fragments;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.nullify.mobielomart.Activity.BestOffers.BestOfferActivity;
import in.nullify.mobielomart.Activity.FeaturedProducts.FeaturedProducts;
import in.nullify.mobielomart.Activity.HomeActivity.HomeActivity;
import in.nullify.mobielomart.Activity.InterestActivity.InterestActivity;
import in.nullify.mobielomart.Activity.NewArrivalActivity.NewArivalActivity;
import in.nullify.mobielomart.Activity.ProductActivity.ProductActivity;
import in.nullify.mobielomart.Activity.RecentView.RecentlyViewedActivity;
import in.nullify.mobielomart.Activity.SigninActivity.SignInActivity;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedApi;
import in.nullify.mobielomart.Adapter.FeaturedProduct.FeaturedHome;
import in.nullify.mobielomart.Adapter.FeaturedProduct.HomeFeaturedAdapter;
import in.nullify.mobielomart.Adapter.GetUser.User;
import in.nullify.mobielomart.Adapter.GetUser.Users;
import in.nullify.mobielomart.Adapter.HomeCarousel.CardFragmentPagerAdapter;
import in.nullify.mobielomart.Adapter.HomeCarousel.Carousel;
import in.nullify.mobielomart.Adapter.HomeCarousel.CarouselApi;
import in.nullify.mobielomart.Adapter.HomeOffer.HomeOfferAdapter;
import in.nullify.mobielomart.Adapter.HomeOffer.ItemDecorationAlbumColumnsOffers;
import in.nullify.mobielomart.Adapter.HomeOffer.OfferApi;
import in.nullify.mobielomart.Adapter.HomeOffer.OffersHome;
import in.nullify.mobielomart.Adapter.HomeRecent.RecentView;
import in.nullify.mobielomart.Adapter.HomeRecent.RecentViewAdapter;
import in.nullify.mobielomart.Adapter.NewProduct.ItemDecorationAlbumColumns;
import in.nullify.mobielomart.Adapter.NewProduct.NewProduct;
import in.nullify.mobielomart.Adapter.NewProduct.NewProductAdapter;
import in.nullify.mobielomart.Adapter.NewProduct.NewProductApi;
import in.nullify.mobielomart.Adapter.SearchResult.ItemDecorationAlbumColumnsResult;
import in.nullify.mobielomart.Adapter.SearchResult.SearchResult;
import in.nullify.mobielomart.Adapter.SearchResult.SearchResultAdapter;
import in.nullify.mobielomart.Adapter.SearchResult.SearchResultApi;
import in.nullify.mobielomart.Adapter.SugListAdapter;
import in.nullify.mobielomart.Adapter.InterestedGride.SuggestionGrideAdapter;
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

/**
 * Created by Abhishekpalodath on 26-05-2018.
 */

public class HomeFragment extends Fragment {

    private int cacheSize = 10 * 1024 * 1024;

    private SearchView seach_prod;
    private Toolbar toolbar;
    private AppBarLayout home_appbar;

    private MenuItem searchItem;
    private Button searchOpen;
    private String searchString;
    private ArrayList<String> suggestions = new ArrayList<>();
    private ArrayList<String> type = new ArrayList<>();
    private String user_id = "0";

    View footerView;

    private SugListAdapter adapter, adapter1, adapter2;
    private ViewPager offer_carousel;
    private List<Carousel> carousels = new ArrayList<>();
    private AppCompatActivity activity;

    private RecyclerView new_products_grid;
    private NewProductAdapter newProductAdapter;
    private List<NewProduct> newProducts = new ArrayList<>();

    private RecyclerView offers_gride;
    private HomeOfferAdapter homeOfferAdapter;
    private List<OffersHome> offersHomes = new ArrayList<>();

    private RecyclerView featured_gride;
    private HomeFeaturedAdapter homeFeaturedAdapter;
    private List<FeaturedHome> featuredHomes = new ArrayList<>();

    private GoogleSignInAccount account;

    private ArrayList<String> cats = new ArrayList<>();
    private ArrayList<String> ctype = new ArrayList<>();

    private ArrayList<String> searched = new ArrayList<>();
    private ArrayList<String> searchedtype = new ArrayList<>();

    private RecyclerView interested_gride;
    private RecyclerView interested_gride2;
    private SuggestionGrideAdapter interestedGrideAddapter;
    private SuggestionGrideAdapter interestedGrideAddapter2;
    private List<NewProduct> interestedProduct = new ArrayList<>();
    private List<NewProduct> interestedProduct2 = new ArrayList<>();
    private View rootView;

    private RecyclerView recentlyViewd;
    private ArrayList<RecentView> recentList = new ArrayList<>();
    private ArrayList<String> catName = new ArrayList<>();
    private RecentViewAdapter recentListAdapter;


    private Button newArrival;
    private Button bestOffers;
    private Button featured;
    private SharedPreferences prefs;

    private String c1,c2;


    @Override
    public void onAttach(Activity activity) {
        this.activity = (AppCompatActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (prefs.getBoolean("SIGNEDIN", false))
            user_id = prefs.getString("USERID", "0");
        offer_carousel = (ViewPager) rootView.findViewById(R.id.home_carousal);
        offer_carousel.requestFocus();
        offer_carousel.setPageMargin(0);
        offer_carousel.setPadding(0, 0, 0, 0);

        newArrival = (Button) rootView.findViewById(R.id.newarrival_view_all);
        newArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewArivalActivity.class));
            }
        });

        bestOffers = (Button) rootView.findViewById(R.id.best_offers);
        bestOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BestOfferActivity.class));
            }
        });
        featured = (Button) rootView.findViewById(R.id.featured);
        featured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FeaturedProducts.class));
            }
        });
        rootView.findViewById(R.id.interest_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InterestActivity.class);
                intent.putExtra("catid",c1);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.interest_view2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InterestActivity.class);
                intent.putExtra("catid",c2);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.recentview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecentlyViewedActivity.class));
            }
        });


        recentlyViewd = (RecyclerView) rootView.findViewById(R.id.recently_viewed);
        recentlyViewd.setHasFixedSize(true);
        RecyclerView.LayoutManager offerlayoumanager1 = new GridLayoutManager(getActivity(), 1);
        recentlyViewd.addItemDecoration(new ItemDecorationAlbumColumnsResult(1));
        recentlyViewd.setLayoutManager(offerlayoumanager1);
        recentListAdapter = new RecentViewAdapter(getActivity(), recentList,HomeFragment.this);
        recentlyViewd.setAdapter(recentListAdapter);

        new_products_grid = (RecyclerView) rootView.findViewById(R.id.new_products_grid);
        new_products_grid.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        new_products_grid.addItemDecoration(new ItemDecorationAlbumColumns(1));
        new_products_grid.setLayoutManager(layoutManager);
        newProductAdapter = new NewProductAdapter(getActivity(), HomeFragment.this, newProducts);
        new_products_grid.setAdapter(newProductAdapter);

        interested_gride = (RecyclerView) rootView.findViewById(R.id.suggestion_list);
        interested_gride.setHasFixedSize(true);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 2);
        layoutManager1.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        interested_gride.addItemDecoration(new ItemDecorationAlbumColumns(1));
        interested_gride.setLayoutManager(layoutManager1);
        interestedGrideAddapter = new SuggestionGrideAdapter(getActivity(), HomeFragment.this, interestedProduct);
        interested_gride.setAdapter(interestedGrideAddapter);

        interested_gride2 = (RecyclerView) rootView.findViewById(R.id.suggestion_list2);
        interested_gride2.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 2);
        layoutManager2.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        interested_gride2.addItemDecoration(new ItemDecorationAlbumColumns(1));
        interested_gride2.setLayoutManager(layoutManager2);
        interestedGrideAddapter2 = new SuggestionGrideAdapter(getActivity(), HomeFragment.this, interestedProduct2);
        interested_gride2.setAdapter(interestedGrideAddapter2);

        offers_gride = (RecyclerView) rootView.findViewById(R.id.offers_gride);
        offers_gride.setHasFixedSize(true);
        RecyclerView.LayoutManager offerlayoumanager = new GridLayoutManager(getActivity(), 2);
        offers_gride.addItemDecoration(new ItemDecorationAlbumColumnsOffers(1));
        offers_gride.setLayoutManager(offerlayoumanager);
        homeOfferAdapter = new HomeOfferAdapter(getActivity(), HomeFragment.this, offersHomes);
        offers_gride.setAdapter(homeOfferAdapter);

        featured_gride = (RecyclerView) rootView.findViewById(R.id.featured_gride);
        featured_gride.setHasFixedSize(true);
        RecyclerView.LayoutManager fetured = new GridLayoutManager(getActivity(), 2);
        featured_gride.addItemDecoration(new ItemDecorationAlbumColumnsOffers(1));
        featured_gride.setLayoutManager(fetured);
        homeFeaturedAdapter = new HomeFeaturedAdapter(getActivity(), HomeFragment.this, featuredHomes);
        featured_gride.setAdapter(homeFeaturedAdapter);


        loaddatas();

        return rootView;
    }

    private void loaddatas() {
        carousels.clear();
        newProducts.clear();
        featuredHomes.clear();
        offersHomes.clear();
        interestedProduct.clear();
        recentList.clear();
        if (!isNetworkAvailable(getActivity())) {
            rootView.findViewById(R.id.noconnection).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.reconnect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loaddatas();
                }
            });
            rootView.findViewById(R.id.home_progress).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.home_progress).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.noconnection).setVisibility(View.GONE);
            if (!(user_id.equals("0"))) {
                Thread t5 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getInterested();
                    }
                });
                t5.start();
                Thread t6 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getRecent();
                    }
                });
                t6.start();
            }
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    getNewProduct();
                }
            });
            t1.start();
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    getNewOffers();
                }
            });
            t2.start();
            Thread t3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    getCarousels();
                }
            });
            t3.start();
            Thread t4 = new Thread(new Runnable() {
                @Override
                public void run() {
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
                .cache(new Cache(getActivity().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable(getActivity())) {
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
                CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(activity.getSupportFragmentManager(), dpToPx(0), carousels);


                offer_carousel.setAdapter(pagerAdapter);
                //offer_carousel.setPageTransformer(false, fragmentCardShadowTransformer);
                offer_carousel.setOffscreenPageLimit(10);
                TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.dot_carousel);
                tabLayout.setupWithViewPager(offer_carousel, true);


            }

            @Override
            public void onFailure(Call<List<Carousel>> call, Throwable t) {
                loaddatas();
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

    private void getNewOffers() {
        offersHomes.clear();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getActivity().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable(getActivity())) {
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
                .cache(new Cache(getActivity().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable(getActivity())) {
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
                rootView.findViewById(R.id.home_progress).setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<FeaturedHome>> call, Throwable t) {
                loaddatas();
            }
        });

    }

    private void getInterested() {
        interestedProduct.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String URL = "https://www.nullify.in/mobielo_mart/php/homepage/getInterested.php";

        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!(response.equals("0"))) {
                            rootView.findViewById(R.id.interest_cont).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.interest_cont2).setVisibility(View.VISIBLE);
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


    private void getNewProduct() {
        newProducts.clear();
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getActivity().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable(getActivity())) {
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

    private void getRecent() {

        recentList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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


                                rootView.findViewById(R.id.recent_cont).setVisibility(View.VISIBLE);
                                recentListAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (!recentList.isEmpty()) {
                                    rootView.findViewById(R.id.recent_cont).setVisibility(View.VISIBLE);
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
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void activityStart(String productid) {
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra("pid", Integer.parseInt(productid));
        startActivity(intent);
    }
}
