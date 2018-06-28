package in.nullify.mobielomart.Adapter.NewProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public interface NewProductApi {
    String BASE_URL = "https://www.nullify.in/";

    @POST("mobielo_mart/php/homepage/getNewProducts.php")
    Call<List<NewProduct>> getNewProduct();
}
