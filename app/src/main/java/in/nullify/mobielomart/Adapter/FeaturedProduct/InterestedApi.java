package in.nullify.mobielomart.Adapter.FeaturedProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 05-06-2018.
 */

public interface InterestedApi {
    String BASE_URL = "https://www.nullify.in/";

    @FormUrlEncoded
    @POST("mobielo_mart/php/SuggessionActivity/getSuggestion.php")
    Call<List<FeaturedHome>> getFeatured(@Field("catid") String catid);
}
