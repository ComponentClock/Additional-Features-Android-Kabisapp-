package slot.play.cuan88.constanst;


import java.text.SimpleDateFormat;
import java.util.Locale;


public class Constants {

//    private static final String BASE_URL = "https://admin.gohebat.com/";
    private static final String BASE_URL = "http://192.168.43.120/";
    public static final String CONNECTION = BASE_URL + "api/";
    public static final String IMAGESGAME = BASE_URL + "images/game/";


    public static SimpleDateFormat df =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static String versionname = "1.0.20";


}
