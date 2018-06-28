package in.nullify.mobielomart.Adapter.GetUser;

import java.util.List;

import in.nullify.mobielomart.Adapter.HomeCarousel.Carousel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public interface User {
    String BASE_URL = "https://www.nullify.in/";

    @FormUrlEncoded
    @POST("mobielo_mart/php/homepage/getUser.php")
    Call<List<Users>> getUser(@Field("email") String email);
}
