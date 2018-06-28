package in.nullify.mobielomart.Adapter.FeaturedProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public interface FeaturedApi {
    String BASE_URL = "https://www.nullify.in/";

    @POST("mobielo_mart/php/homepage/getFeatured.php")
    Call<List<FeaturedHome>> getFeatured();
}
