package in.nullify.mobielomart.Adapter.SearchResult;

import java.util.List;

import in.nullify.mobielomart.Adapter.GetUser.Users;
import in.nullify.mobielomart.Adapter.NewProduct.NewProduct;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 26-05-2018.
 */

public interface SearchResultApi {
    String BASE_URL = "https://www.nullify.in/";

    @FormUrlEncoded
    @POST("mobielo_mart/php/homepage/getRecent.php")
    Call<List<SearchResult>> getProduct(@Field("user_id") String uid);
}
