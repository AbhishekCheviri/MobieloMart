package in.nullify.mobielomart.Adapter.Simular_products;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public class SimilarProducts {
    private String p_id;
    private String cat_id;
    private String p_name;
    private String p_price;
    private String p_img;
    private String off_id;
    private String off_price;



    public SimilarProducts(String p_id, String cat_id, String p_name, String p_price, String p_img, String off_id, String off_price)
    {

        this.p_id = p_id;
        this.cat_id = cat_id;
        this.p_name = p_name;
        this.p_price = p_price;
        this.p_img = p_img;
        this.off_id = off_id;
        this.off_price = off_price;

    }

    public String getP_id() {
        return p_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getP_name() {
        return p_name;
    }

    public String getP_price() {
        return p_price;
    }

    public String getP_img() {
        return p_img;
    }

    public String getOff_id() {
        return off_id;
    }

    public String getOff_price() {
        return off_price;
    }
}
