package com.radjago.drivergo.utils.api.service;

import com.radjago.drivergo.json.AcceptRequestJson;
import com.radjago.drivergo.json.AcceptResponseJson;
import com.radjago.drivergo.json.AllTransResponseJson;
import com.radjago.drivergo.json.AreaRequestJson;
import com.radjago.drivergo.json.AreaResponseJson;
import com.radjago.drivergo.json.BankResponseJson;
import com.radjago.drivergo.json.ChangePassRequestJson;
import com.radjago.drivergo.json.DetailRequestJson;
import com.radjago.drivergo.json.DetailTransResponseJson;
import com.radjago.drivergo.json.EditKendaraanRequestJson;
import com.radjago.drivergo.json.EditprofileRequestJson;
import com.radjago.drivergo.json.FcmKeyResponse;
import com.radjago.drivergo.json.GetHomeRequestJson;
import com.radjago.drivergo.json.GetHomeResponseJson;
import com.radjago.drivergo.json.GetOnRequestJson;
import com.radjago.drivergo.json.JobResponseJson;
import com.radjago.drivergo.json.LoginRequestJson;
import com.radjago.drivergo.json.LoginResponseJson;
import com.radjago.drivergo.json.LokasiDriverRequest;
import com.radjago.drivergo.json.LokasiDriverResponse;
import com.radjago.drivergo.json.MainBGResponse;
import com.radjago.drivergo.json.MapKeyResponse;
import com.radjago.drivergo.json.PrivacyRequestJson;
import com.radjago.drivergo.json.PrivacyResponseJson;
import com.radjago.drivergo.json.RegisterRequestJson;
import com.radjago.drivergo.json.RegisterResponseJson;
import com.radjago.drivergo.json.ResponseJson;
import com.radjago.drivergo.json.TopupRequestJson;
import com.radjago.drivergo.json.TopupResponseJson;
import com.radjago.drivergo.json.UpdateLocationRequestJson;
import com.radjago.drivergo.json.UpdateStatusRequest;
import com.radjago.drivergo.json.UpdateTokenRequestJson;
import com.radjago.drivergo.json.VerifyRequestJson;
import com.radjago.drivergo.json.WalletRequestJson;
import com.radjago.drivergo.json.WalletResponseJson;
import com.radjago.drivergo.json.WithdrawRequestJson;
import com.radjago.drivergo.json.WithdrawResponseJson;
import com.radjago.drivergo.json.fcm.CancelBookRequestJson;
import com.radjago.drivergo.json.fcm.CancelBookResponseJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public interface DriverService {

    @POST("driver/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);

    @POST("driver/update_location")
    Call<ResponseJson> updatelocation(@Body UpdateLocationRequestJson param);

    @POST("driver/syncronizing_account")
    Call<GetHomeResponseJson> home(@Body GetHomeRequestJson param);

    @POST("driver/logout")
    Call<GetHomeResponseJson> logout(@Body GetHomeRequestJson param);

    @POST("driver/turning_on")
    Call<ResponseJson> turnon(@Body GetOnRequestJson param);

    @POST("driver/accept")
    Call<AcceptResponseJson> accept(@Body AcceptRequestJson param);

    @POST("driver/start")
    Call<AcceptResponseJson> startrequest(@Body AcceptRequestJson param);

    @POST("driver/finish")
    Call<AcceptResponseJson> finishrequest(@Body AcceptRequestJson param);

    @POST("driver/edit_profile")
    Call<LoginResponseJson> editProfile(@Body EditprofileRequestJson param);

    @POST("driver/edit_kendaraan")
    Call<LoginResponseJson> editKendaraan(@Body EditKendaraanRequestJson param);

    @POST("driver/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("driver/history_progress")
    Call<AllTransResponseJson> history(@Body DetailRequestJson param);


    @POST("driver/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("driver/register_driver")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);

    @POST("pelanggan/list_bank")
    Call<BankResponseJson> listbank(@Body WithdrawRequestJson param);


    @POST("driver/detail_transaksi")
    Call<DetailTransResponseJson> detailtrans(@Body DetailRequestJson param);

    @POST("driver/job")
    Call<JobResponseJson> job();

    @POST("driver/user_cancel")
    Call<CancelBookResponseJson> cancelOrder(@Body CancelBookRequestJson param);

    @POST("driver/mwapikey")
    Call<MapKeyResponse> mwapikey();

    @POST("driver/mwapikey")
    Call<FcmKeyResponse> fcmapikey();

    @POST("pelanggan/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

    @POST("pelanggan/topupstripe")
    Call<TopupResponseJson> topup(@Body TopupRequestJson param);

    @POST("driver/withdraw")
    Call<WithdrawResponseJson> withdraw(@Body WithdrawRequestJson param);

    @POST("driver/midtrans")
    Call<WithdrawResponseJson> midtranspay(@Body WithdrawRequestJson param);

    @POST("driver/midtransresult")
    Call<WithdrawResponseJson> midtransres(@Body WithdrawRequestJson param);

    @POST("pelanggan/wallet")
    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);


    @POST("driver/topuppaypal")
    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("driver/verifycode")
    Call<ResponseJson> verifycode(@Body VerifyRequestJson param);

    @POST("driver/liat_lokasi_driver")
    Call<LokasiDriverResponse> liatLokasiDriver(@Body LokasiDriverRequest param);

    @POST("pelanggan/MainBG")
    Call<MainBGResponse> mainbackground();

    @POST("driver/update_token")
    Call<ResponseJson> updateToken(@Body UpdateTokenRequestJson param);


    @POST("driver/liat_area_driver")
    Call<AreaResponseJson> listarea(@Body AreaRequestJson param);

    @POST("pelanggan/update_status")
    Call<ResponseJson> updateStatus(@Body UpdateStatusRequest param);
}
