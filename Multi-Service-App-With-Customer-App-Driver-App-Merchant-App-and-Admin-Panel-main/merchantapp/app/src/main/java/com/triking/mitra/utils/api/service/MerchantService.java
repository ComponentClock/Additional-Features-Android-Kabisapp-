package com.triking.mitra.utils.api.service;

import com.triking.mitra.json.ActiveCatRequestJson;
import com.triking.mitra.json.AddEditItemRequestJson;
import com.triking.mitra.json.AddEditKategoriRequestJson;
import com.triking.mitra.json.BankResponseJson;
import com.triking.mitra.json.ChangePassRequestJson;
import com.triking.mitra.json.DetailRequestJson;
import com.triking.mitra.json.DetailTransResponseJson;
import com.triking.mitra.json.EditMerchantRequestJson;
import com.triking.mitra.json.EditProfileRequestJson;
import com.triking.mitra.json.GetServiceResponseJson;
import com.triking.mitra.json.GetOnRequestJson;
import com.triking.mitra.json.HistoryRequestJson;
import com.triking.mitra.json.HistoryResponseJson;
import com.triking.mitra.json.HomeRequestJson;
import com.triking.mitra.json.HomeResponseJson;
import com.triking.mitra.json.ItemRequestJson;
import com.triking.mitra.json.ItemResponseJson;
import com.triking.mitra.json.CategoryRequestJson;
import com.triking.mitra.json.CategoryResponseJson;
import com.triking.mitra.json.LoginRequestJson;
import com.triking.mitra.json.LoginResponseJson;
import com.triking.mitra.json.PrivacyRequestJson;
import com.triking.mitra.json.PrivacyResponseJson;
import com.triking.mitra.json.RegisterRequestJson;
import com.triking.mitra.json.RegisterResponseJson;
import com.triking.mitra.json.ResponseJson;
import com.triking.mitra.json.StripeRequestJson;
import com.triking.mitra.json.TopupRequestJson;
import com.triking.mitra.json.TopupResponseJson;
import com.triking.mitra.json.WalletRequestJson;
import com.triking.mitra.json.WalletResponseJson;
import com.triking.mitra.json.WithdrawRequestJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public interface MerchantService {

    @GET("partnerapi/kategorimerchant")
    Call<GetServiceResponseJson> getFitur();

    @POST("customerapi/list_bank")
    Call<BankResponseJson> listbank(@Body WithdrawRequestJson param);

    @POST("partnerapi/kategorimerchantbyfitur")
    Call<GetServiceResponseJson> getKategori(@Body HistoryRequestJson param);

    @POST("partnerapi/onoff")
    Call<ResponseJson> turnon(@Body GetOnRequestJson param);

    @POST("partnerapi/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);

    @POST("partnerapi/register_merchant")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);

    @POST("partnerapi/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("customerapi/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

    @POST("partnerapi/edit_profile")
    Call<LoginResponseJson> editprofile(@Body EditProfileRequestJson param);

    @POST("partnerapi/edit_merchant")
    Call<LoginResponseJson> editmerchant(@Body EditMerchantRequestJson param);

    @POST("partnerapi/home")
    Call<HomeResponseJson> home(@Body HomeRequestJson param);

    @POST("partnerapi/history")
    Call<HistoryResponseJson> history(@Body HistoryRequestJson param);

    @POST("partnerapi/detail_transaksi")
    Call<DetailTransResponseJson> detailtrans(@Body DetailRequestJson param);

    @POST("partnerapi/category")
    Call<CategoryResponseJson> category(@Body CategoryRequestJson param);

    @POST("partnerapi/item")
    Call<ItemResponseJson> itemlist(@Body ItemRequestJson param);

    @POST("partnerapi/active_kategori")
    Call<ResponseJson> activekategori(@Body ActiveCatRequestJson param);

    @POST("partnerapi/active_item")
    Call<ResponseJson> activeitem(@Body ActiveCatRequestJson param);

    @POST("partnerapi/add_kategori")
    Call<ResponseJson> addkategori(@Body AddEditKategoriRequestJson param);

    @POST("partnerapi/edit_kategori")
    Call<ResponseJson> editkategori(@Body AddEditKategoriRequestJson param);

    @POST("partnerapi/delete_kategori")
    Call<ResponseJson> deletekategori(@Body AddEditKategoriRequestJson param);

    @POST("partnerapi/add_item")
    Call<ResponseJson> additem(@Body AddEditItemRequestJson param);

    @POST("partnerapi/edit_item")
    Call<ResponseJson> edititem(@Body AddEditItemRequestJson param);

    @POST("partnerapi/delete_item")
    Call<ResponseJson> deleteitem(@Body AddEditItemRequestJson param);

    @POST("customerapi/topupstripe")
    Call<TopupResponseJson> topup(@Body TopupRequestJson param);

    @POST("partnerapi/withdraw")
    Call<ResponseJson> withdraw(@Body WithdrawRequestJson param);

    @POST("customerapi/wallet")
    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);

    @POST("partnerapi/topuppaypal")
    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("partnerapi/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("partnerapi/stripeaction")
    Call<ResponseJson> actionstripe(@Body StripeRequestJson param);

    @POST("partnerapi/intentstripe")
    Call<ResponseJson> intentstripe(@Body StripeRequestJson param);

}
