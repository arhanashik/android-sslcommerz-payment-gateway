package com.adndigitalbd.sslpaymentintegration;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.adndigitalbd.sslpaymentintegration.databinding.ActivityMainBinding;
import com.sslcommerz.library.payment.Classes.PayUsingSSLCommerz;
import com.sslcommerz.library.payment.Listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.Util.ConstantData.BankName;
import com.sslcommerz.library.payment.Util.ConstantData.CurrencyType;
import com.sslcommerz.library.payment.Util.ConstantData.ErrorKeys;
import com.sslcommerz.library.payment.Util.ConstantData.SdkCategory;
import com.sslcommerz.library.payment.Util.ConstantData.SdkType;
import com.sslcommerz.library.payment.Util.JsonModel.TransactionInfo;
import com.sslcommerz.library.payment.Util.Model.AdditionalFieldModel;
import com.sslcommerz.library.payment.Util.Model.CustomerFieldModel;
import com.sslcommerz.library.payment.Util.Model.MandatoryFieldModel;
import com.sslcommerz.library.payment.Util.Model.ShippingFieldModel;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getName();

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        mBinding.content.btnRequestPayment.setOnClickListener(v -> {
            if(mBinding.content.etAmount.getText() == null) return;
            String amountStr = mBinding.content.etAmount.getText().toString();
            if(TextUtils.isEmpty(amountStr)) return;

            if(!TextUtils.isDigitsOnly(amountStr)) {
                Toast.makeText(
                        MainActivity.this,
                        "Please insert a valid amount",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            int amount = Integer.parseInt(amountStr);
            requestPayment(amount, "1012");
        });
    }

    private void requestPayment(int amount, String transactionId) {
        // Mandatory Field
        MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel(
                getString(R.string.store_id),
                getString(R.string.store_password),
                String.valueOf(amount),
                transactionId,
                CurrencyType.BDT,
                SdkType.TESTBOX,
                SdkCategory.BANK_LIST);

        //Mandatory Field For Specific Bank Page
//        MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel(
//                "styli5c8f270aeecec",
//                "styli5c8f270aeecec@ssl",
//                String.valueOf(amount),
//                "1012",
//                CurrencyType.BDT,
//                SdkType.TESTBOX,
//                SdkCategory.BANK_PAGE,
//                BankName.DBBL_VISA);

        //Optional Fields
        CustomerFieldModel customerFieldModel = new CustomerFieldModel(
                "Customer Name",
                "Customer Email Address",
                "Customer Address 1",
                "Customer Address 2",
                "Customer City",
                "Customer State",
                "Customer Post Code",
                "Customer Country",
                " Customer Phone",
                "Customer Fax");

        ShippingFieldModel shippingFieldModel = new ShippingFieldModel(
                "Shipping Name",
                "Shipping Address 1",
                "Shipping Address 2",
                "Shipping City",
                "Shipping State",
                "Shipping Post Code",
                "Shipping Country" );

        AdditionalFieldModel additionalFieldModel = new AdditionalFieldModel();
        additionalFieldModel.setValueA("Additional Field Value A");
        additionalFieldModel.setValueB("Additional Field Value B");
        additionalFieldModel.setValueC("Additional Field Value C");
        additionalFieldModel.setValueD("Additional Field Value D");

        //Request payment
        PayUsingSSLCommerz.getInstance().setData(
                this,
                mandatoryFieldModel,
                customerFieldModel,
                shippingFieldModel,
                additionalFieldModel,
                new OnPaymentResultListener() {
                    @Override
                    public void transactionSuccess(TransactionInfo transactionInfo) {
                        String message = prepareTransactionInfoMessage(transactionInfo);

                        if(transactionInfo.getRiskLevel().equals("0")) {
                            // If payment is success and risk label is 0.
                            message = "Transaction Successfully completed.\n" + message;
                            Log.d(TAG, message);
                            mBinding.content.tvResponse.setText(message);

                        } else{
                            // Payment is success but payment is not complete yet. Card on hold now.
                            message = "Transaction in risk.\n" + message;
                            Log.d(TAG, message);
                        }
                    }

                    @Override
                    public void transactionFail(TransactionInfo transactionInfo) {
                        // Transaction failed
                        String error = "Transaction Failed";
                        Log.e(TAG, error);
                        mBinding.content.tvResponse.setText(error);
                    }

                    @Override
                    public void error(int errorCode) {
                        String error = "Unknown error";
                        switch (errorCode){
                            // Provided information is not valid.
                            case ErrorKeys.USER_INPUT_ERROR :
                                error = "User Input Error";
                                break;

                            // Internet is not connected.
                            case ErrorKeys.INTERNET_CONNECTION_ERROR :
                                error = "Data Parsing Error";
                                break;

                            // Server is not giving valid data.
                            case ErrorKeys.DATA_PARSING_ERROR :
                                error = "Internet Connection Error";
                                break;

                            // User press back button or canceled the transaction.
                            case ErrorKeys.CANCEL_TRANSACTION_ERROR :
                                error = "User Cancel The Transaction";
                                break;

                            // Server is not responding.
                            case ErrorKeys.SERVER_ERROR :
                                error = "Server Error";
                                break;

                            // For some reason network is not responding
                            case ErrorKeys.NETWORK_ERROR :
                                error = "Network Error";
                                break;
                        }

                        Log.e(TAG, error);
                        mBinding.content.tvResponse.setText(error);
                    }
                }
        );
    }

    private String prepareTransactionInfoMessage(TransactionInfo info) {
        return "Status: " + info.getStatus()
                + "\nAmount: " + info.getAmount()
                + "\nCurrency Type: " + info.getCurrencyType()
                + "\nCurrency Amount: " + info.getCurrencyAmount()
                + "\nCurrency Rate: " + info.getCurrencyRate()
                + "\nSession Key: " + info.getSessionkey()
                + "\nTransfer ID: " + info.getTranId()
                + "\nTransfer Date: " + info.getTranDate()
                + "\nValid ID: " + info.getValId()
                + "\nStore Amount: " + info.getStoreAmount()
                + "\nBank Transfer Id: " + info.getBankTranId()
                + "\nCard Type: " + info.getCardType()
                + "\nCard No: " + info.getCardNo()
                + "\nCard Issuer: " + info.getCardIssuer()
                + "\nCard Brand: " + info.getCardBrand()
                + "\nCard Issuer Country: " + info.getCardIssuerCountry()
                + "\nCard Issuer Country Code: " + info.getCardIssuerCountryCode()
                + "\nBase Fair: " + info.getBaseFair()
                + "\nValue A: " + info.getValueA()
                + "\nValue B: " + info.getValueB()
                + "\nValue C: " + info.getValueC()
                + "\nValue D: " + info.getValueD()
                + "\nRisk Title: " + info.getRiskTitle()
                + "\nRisk Level: " + info.getRiskLevel()
                + "\nAPI Connect: " + info.getAPIConnect()
                + "\nValidate On: " + info.getValidatedOn()
                + "\nGw Version: " + info.getGwVersion();
    }
}
