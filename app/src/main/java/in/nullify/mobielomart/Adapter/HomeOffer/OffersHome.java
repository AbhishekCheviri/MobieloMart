package in.nullify.mobielomart.Adapter.HomeOffer;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public class OffersHome {
    private String p_img;
    private String p_name;
    private String p_price;
    private String off_perc;
    private String off_price;
    private String p_id;
    private String off_id;
    private String offercat_id;



    public OffersHome(String off_id,String offercat_id,String p_id,String off_price,String off_perc,String p_name,String p_price,String p_img)
    {

        this.off_id = off_id;
        this.offercat_id = offercat_id;
        this.p_id = p_id;
        this.off_price = off_price;
        this.off_perc = off_perc;
        this.p_name = p_name;
        this.p_price = p_price;
        this.p_img = p_img;

    }

    public String getP_img() {
        return p_img;
    }

    public String getP_name() {
        return p_name;
    }

    public String getP_price() {
        return p_price;
    }

    public String getOff_perc() {
        return off_perc;
    }

    public String getOff_price() {
        return off_price;
    }

    public String getP_id() {
        return p_id;
    }

    public String getOff_id() {
        return off_id;
    }

    public String getOffercat_id() {
        return offercat_id;
    }
}
