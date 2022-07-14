package com.radjago.drivergo.models;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.Authentication;
import com.midtrans.sdk.corekit.models.snap.CreditCard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataCustomer {

    public static CustomerDetails customerDetails() {

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("maswend");
        customerDetails.setPhone("628991585001");
        customerDetails.setEmail("maswend.2020@gmail.com");

        return customerDetails;
    }

    public static TransactionRequest transactionRequest(String id, int price, int qty, String name) {

        TransactionRequest transactionRequest = new TransactionRequest(System.currentTimeMillis() + " ", price);
        transactionRequest.setCustomerDetails(customerDetails());

        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);

        transactionRequest.setItemDetails(itemDetails);

        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false);
        creditCard.setAuthentication(Authentication.AUTH_RBA);
        creditCard.setBank(BankType.MANDIRI);

        transactionRequest.setCreditCard(creditCard);

        return transactionRequest;
    }

    public void userDetails(String nama,
                            String email,
                            String nohp,
                            String address,
                            String city,
                            String codepos,
                            String country
    ) {
        UserDetail userDetail;
        userDetail = LocalDataHandler.readObject("user_details", UserDetail.class);

        if (userDetail == null) {
            userDetail = new UserDetail();

            userDetail.setUserFullName(nama);
            userDetail.setEmail(email);
            userDetail.setPhoneNumber(nohp);
            userDetail.setUserId(UUID.randomUUID().toString());

            List<UserAddress> userAddresses = new ArrayList<>();

            UserAddress userAddress = new UserAddress();
            userAddress.setAddress(address);
            userAddress.setCity(city);
            userAddress.setCountry(country);
            userAddress.setZipcode(codepos);
            userAddress.setAddressType(Constants.ADDRESS_TYPE_BOTH);
            userAddresses.add(userAddress);

            userDetail.setUserAddresses(new ArrayList<>(userAddresses));

            LocalDataHandler.saveObject("user_details", userDetail);
        }

    }

}