package slot.play.cuan88.utils;

import slot.play.cuan88.json.GetHomeRequestJson;
import slot.play.cuan88.json.GetHomeResponseJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {

    @POST("pelanggan/home")
    Call<GetHomeResponseJson> home(@Body GetHomeRequestJson param);
}
