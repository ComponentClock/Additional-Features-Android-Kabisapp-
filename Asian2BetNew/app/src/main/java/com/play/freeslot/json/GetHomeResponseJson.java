package slot.play.cuan88.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import slot.play.cuan88.models.MenuModel;

import java.util.ArrayList;
import java.util.List;


public class GetHomeResponseJson {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("allmenu")
    @Expose
    private List<MenuModel> allmenu = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MenuModel> getAllmenu() {
        return allmenu;
    }

    public void setAllmenu(List<MenuModel> allmenu) {
        this.allmenu = allmenu;
    }


   }
