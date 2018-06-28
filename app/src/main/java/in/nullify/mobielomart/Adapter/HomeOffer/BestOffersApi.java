package in.nullify.mobielomart.Adapter.HomeOffer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 03-06-2018.
 */

public interface BestOffersApi {
    String BASE_URL = "https://www.nullify.in/";

    @POST("mobielo_mart/php/BestOffers/getBestOffers.php")
    Call<List<OffersHome>> getNewOffer();
}
