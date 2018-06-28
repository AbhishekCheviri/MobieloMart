package in.nullify.mobielomart.Adapter.SearchResult;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public class SearchResult {
    private String cat_id;
    private String cat_name;
    private String p_id;
    private String p_name;
    private String p_price;
    private String p_img;
    private String off_id;
    private String off_price;
    private String p_rating;
    private String relevance;

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public void setP_rating(String p_rating) {
        this.p_rating = p_rating;
    }

    public String getP_rating() {
        return p_rating;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public void setP_img(String p_img) {
        this.p_img = p_img;
    }

    public void setOff_id(String off_id) {
        this.off_id = off_id;
    }

    public void setOff_price(String off_price) {
        this.off_price = off_price;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getP_id() {
        return p_id;
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

