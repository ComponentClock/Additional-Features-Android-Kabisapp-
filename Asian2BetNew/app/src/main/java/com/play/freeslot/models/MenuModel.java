package slot.play.cuan88.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MenuModel extends RealmObject implements Serializable {

    @PrimaryKey
    @Expose
    @SerializedName("id_menu")
    private int idMenu;

    @Expose
    @SerializedName("nama_menu")
    private String nama_menu;

    @Expose
    @SerializedName("link")
    private long link;

    @Expose
    @SerializedName("icon")
    private long icon;

    @Expose
    @SerializedName("status")
    private long status;


    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public long getLink() {
        return link;
    }

    public void setLink(long link) {
        this.link = link;
    }

    public long getIcon() {
        return icon;
    }

    public void setIcon(long icon) {
        this.icon = icon;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}
