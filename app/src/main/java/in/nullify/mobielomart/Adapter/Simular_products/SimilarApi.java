package in.nullify.mobielomart.Adapter.Simular_products;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public interface SimilarApi {
    String BASE_URL = "https://www.nullify.in/";

    @FormUrlEncoded
    @POST("mobielo_mart/php/Products/getSimilarProducts.php")
    Call<List<SimilarProducts>> getFeatured(@Field("pid") String pid);
}
