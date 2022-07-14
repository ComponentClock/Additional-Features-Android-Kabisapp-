package com.radjago.drivergo.utils.api.service;

import com.radjago.drivergo.json.AreaResponse;
import com.radjago.drivergo.json.DriverResponse;
import com.radjago.drivergo.json.ItemRespon;
import com.radjago.drivergo.json.JobResponse;
import com.radjago.drivergo.json.KomisiResponse;
import com.radjago.drivergo.json.LayananRespon;
import com.radjago.drivergo.json.LokasiResponse;
import com.radjago.drivergo.json.MidtransResponse;
import com.radjago.drivergo.json.PointRespon;
import com.radjago.drivergo.json.SaldoResponse;
import com.radjago.drivergo.json.TransaksiRespon;
import com.radjago.drivergo.json.UpdateItemRespon;
import com.radjago.drivergo.json.WalletRespon;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MaswendApi {

    //  @GET("viewpoint.php")
    // Call<PointRespon> getPoint();
    @FormUrlEncoded
    @POST("topup.php")
    Call<MidtransResponse> midtrans(@Field("id_user") String id,
                                       @Field("jumlah") String jumlah,
                                       @Field("bank") String bank,
                                       @Field("nama_pemilik") String nama,
                                       @Field("rekening") String rekening,
                                        @Field("type") String type,
                                       @Field("status") String status);

    @FormUrlEncoded
    @POST("viewpoint.php")
    Call<PointRespon> getPoint(@Field("com") String id);

    @FormUrlEncoded
    @POST("cekpoint.php")
    Call<PointRespon> cekPoint(@Field("com") String id);

    @FormUrlEncoded
    @POST("cekkomisi.php")
    Call<KomisiResponse> cekKomisi(@Field("com") String id);

    @FormUrlEncoded
    @POST("loaditem.php")
    Call<ItemRespon> cekItemMenu(@Field("com") String id);

    @FormUrlEncoded
    @POST("tukarpoint.php")
    Call<PointRespon> tukarPoint(@Field("com") String id,
                                 @Field("point") String point);

    @FormUrlEncoded
    @POST("updatesaldo.php")
    Call<PointRespon> updateSaldo(@Field("id_user") String id,
                                  @Field("saldo") String point);

    @FormUrlEncoded
    @POST("updatesaldo.php")
    Call<SaldoResponse> updateSaldoUser(@Field("id_user") String id,
                                        @Field("saldo") String point);

    @FormUrlEncoded
    @POST("updatelokasi.php")
    Call<LokasiResponse> updatelokasi(@Field("id_driver") String id,
                                      @Field("latitude") String latitude,
                                      @Field("longitude") String longitude,
                                      @Field("bearing") String bearing);

    @FormUrlEncoded
    @POST("updateitem.php")
    Call<UpdateItemRespon> updateItem(@Field("com") String id,
                                      @Field("idtrans") String idtrans,
                                      @Field("jumlah") String jumlah,
                                      @Field("biaya") String biaya);

    @FormUrlEncoded
    @POST("updateharga.php")
    Call<LayananRespon> updateLayanan(@Field("com") String id,
                                      @Field("biaya") String biaya);

    @FormUrlEncoded
    @POST("updatetotal.php")
    Call<TransaksiRespon> updateBiaya(@Field("com") String id,
                                      @Field("biaya") String biaya);

    @FormUrlEncoded
    @POST("cekarea.php")
    Call<AreaResponse> cekArea(@Field("kota") String id);

    @FormUrlEncoded
    @POST("hapusmenu.php")
    Call<UpdateItemRespon> hapusItem(@Field("com") String id,
                                     @Field("idtrans") String idtrans);

    @FormUrlEncoded
    @POST("ceksaldo.php")
    Call<SaldoResponse> cekSaldo(@Field("id_user") String id);

    @FormUrlEncoded
    @POST("cekjob.php")
    Call<JobResponse> cekJob(@Field("com") String id);

    @FormUrlEncoded
    @POST("viewdriver.php")
    Call<DriverResponse> cekDriver(@Field("com") String id);

    @FormUrlEncoded
    @POST("wallet.php")
    Call<WalletRespon> sendwallet(@Field("id_user") String id_user,
                                   @Field("jumlah") String jumlah,
                                   @Field("bank") String bank,
                                   @Field("nama_pemilik") String nama_pemilik,
                                   @Field("rekening") String rekening,
                                   @Field("tujuan") String tujuan,
                                   @Field("type") String type,
                                   @Field("status") String status);

}
