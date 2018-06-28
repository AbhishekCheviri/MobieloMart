package in.nullify.mobielomart.Adapter.HomeCarousel;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public class Carousel {

    private String cats;
    private String id;
    private String img;


    public Carousel(String cats,String id,String img)
    {
        this.cats=cats;
        this.id=id;
        this.img=img;
    }

    public String getCats() {
        return cats;
    }

    public String getId() {
        return id;
    }

    public String getImg() {
        return img;
    }
}
