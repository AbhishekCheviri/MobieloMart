package in.nullify.mobielomart.Adapter.HomeCarousel;

import android.support.v7.widget.CardView;

import java.util.List;

import in.nullify.mobielomart.Adapter.HomeOffer.OffersHome;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public interface CardAdapter {

    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();


}