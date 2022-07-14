package com.triking.driver.utils.api.service;

import com.triking.driver.json.AcceptRequestJson;
import com.triking.driver.json.AcceptResponseJson;
import com.triking.driver.json.BankResponseJson;
import com.triking.driver.json.ChangePassRequestJson;
import com.triking.driver.json.GetOnRequestJson;
import com.triking.driver.json.JobResponseJson;
import com.triking.driver.json.StripeRequestJson;
import com.triking.driver.json.UpdateLocationRequestJson;
import com.triking.driver.json.AllTransResponseJson;
import com.triking.driver.json.DetailRequestJson;
import com.triking.driver.json.DetailTransResponseJson;
import com.triking.driver.json.EditVehicleRequestJson;
import com.triking.driver.json.EditprofileRequestJson;
import com.triking.driver.json.GetHomeRequestJson;
import com.triking.driver.json.GetHomeResponseJson;
import com.triking.driver.json.LoginRequestJson;
import com.triking.driver.json.LoginResponseJson;
import com.triking.driver.json.PrivacyRequestJson;
import com.triking.driver.json.PrivacyResponseJson;
import com.triking.driver.json.RegisterRequestJson;
import com.triking.driver.json.RegisterResponseJson;
import com.triking.driver.json.ResponseJson;
import com.triking.driver.json.TopupRequestJson;
import com.triking.driver.json.TopupResponseJson;
import com.triking.driver.json.VerifyRequestJson;
import com.triking.driver.json.WalletRequestJson;
import com.triking.driver.json.WalletResponseJson;
import com.triking.driver.json.WithdrawRequestJson;
import com.triking.driver.json.WithdrawResponseJson;

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
    Call<LoginResponseJson> editKendaraan(@Body EditVehicleRequestJson param);

    @POST("driver/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("driver/history_progress")
    Call<AllTransResponseJson> history(@Body DetailRequestJson param);

    @POST("driver/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("driver/register_driver")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);

    @POST("customerapi/list_bank")
    Call<BankResponseJson> listbank(@Body WithdrawRequestJson param);

    @POST("driver/detail_transaksi")
    Call<DetailTransResponseJson> detailtrans(@Body DetailRequestJson param);

    @POST("driver/job")
    Call<JobResponseJson> job();


    @POST("customerapi/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

    @POST("customerapi/topupstripe")
    Call<TopupResponseJson> topup(@Body TopupRequestJson param);

    @POST("driver/withdraw")
    Call<WithdrawResponseJson> withdraw(@Body WithdrawRequestJson param);

    @POST("customerapi/wallet")
    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);

    @POST("driver/topuppaypal")
    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("driver/verifycode")
    Call<ResponseJson> verifycode(@Body VerifyRequestJson param);

    @POST("driver/stripeaction")
    Call<ResponseJson> actionstripe(@Body StripeRequestJson param);

    @POST("driver/intentstripe")
    Call<ResponseJson> intentstripe(@Body StripeRequestJson param);


}
