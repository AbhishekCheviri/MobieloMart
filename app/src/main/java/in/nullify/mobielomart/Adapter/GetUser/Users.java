package in.nullify.mobielomart.Adapter.GetUser;

/**
 * Created by Abhishekpalodath on 25-05-2018.
 */

public class Users {

    private String id;
    private String name;

    public Users (String p_id,String p_name)
    {
        this.id = p_id;
        this.name = p_name;
    }
    public String getP_id() {
        return id;
    }

    public String getP_name() {
        return name;
    }
}
