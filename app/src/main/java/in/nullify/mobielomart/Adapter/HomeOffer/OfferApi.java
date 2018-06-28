package in.nullify.mobielomart.Adapter.HomeOffer;

import java.util.List;

import in.nullify.mobielomart.Adapter.NewProduct.NewProduct;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public interface OfferApi {
    String BASE_URL = "https://www.nullify.in/";

    @POST("mobielo_mart/php/homepage/getOffers.php")
    Call<List<OffersHome>> getNewOffer();
}
