package com.adndigitalbd.sslpaymentintegration;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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


    private String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Mandatory Field*/
        MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("testbox","qwerty","10", "1012", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST);
/*Mandatory Field For Specific Bank Page*/
     //   MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("testbox","qwerty","10", "1012", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_PAGE, BankName.DBBL_VISA);


        /*Optional Fields*/
        CustomerFieldModel customerFieldModel = new CustomerFieldModel("Customer Name","Customer Email Address", "Customer Address 1", "Customer Address 2", "Customer City", "Customer State", "Customer Post Code", "Customer Country", " Customer Phone", "Customer Fax");
        ShippingFieldModel shippingFieldModel = new ShippingFieldModel("Shipping Name", "Shipping Address 1","Shipping Address 2","Shipping City", "Shipping State", "Shipping Post Code", "Shipping Country" );
        AdditionalFieldModel additionalFieldModel = new AdditionalFieldModel();
        additionalFieldModel.setValueA("Value Option 1");
        additionalFieldModel.setValueB("Value Option 1");
        additionalFieldModel.setValueC("Value Option 1");
        additionalFieldModel.setValueD("Value Option 1");





        PayUsingSSLCommerz.getInstance().setData(this,mandatoryFieldModel,customerFieldModel,shippingFieldModel,additionalFieldModel,new OnPaymentResultListener() {
            @Override
            public void transactionSuccess(TransactionInfo transactionInfo) {
// If payment is success and risk label is 0.
                if(transactionInfo.getRiskLevel().equals("0")) {
                    Log.d(TAG, "Transaction Successfully completed");
                }
// Payment is success but payment is not complete yet. Card on hold now.
                else{
                    Log.d(TAG, "Transaction in risk. Risk Title : "+transactionInfo.getRiskTitle().toString());
                }
            }

            @Override
            public void transactionFail(TransactionInfo transactionInfo) {
// Transaction failed
                Log.e(TAG, "Transaction Fail");
            }

            @Override
            public void error(int errorCode) {
                switch (errorCode){
// Your provides information is not valid.
                    case ErrorKeys.USER_INPUT_ERROR :
                        Log.e(TAG, "User Input Error" );break;
// Internet is not connected.
                    case ErrorKeys.INTERNET_CONNECTION_ERROR :
                        Log.e(TAG, "Internet Connection Error" );break;
// Server is not giving valid data.
                    case ErrorKeys.DATA_PARSING_ERROR :
                        Log.e(TAG, "Data Parsing Error" );break;
// User press back button or canceled the transaction.
                    case ErrorKeys.CANCEL_TRANSACTION_ERROR :
                        Log.e(TAG, "User Cancel The Transaction" );break;
// Server is not responding.
                    case ErrorKeys.SERVER_ERROR :
                        Log.e(TAG, "Server Error" );break;
// For some reason network is not responding
                    case ErrorKeys.NETWORK_ERROR :
                        Log.e(TAG, "Network Error" );break;
                }
            }
        });

    }
}
