package in.nullify.mobielomart.Adapter.GetUser;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public class Users {

    private String id;
    private String name;
    private String email;
    private String img;

    public Users(String id, String name, String email, String img) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImg() {
        return img;
    }
}
