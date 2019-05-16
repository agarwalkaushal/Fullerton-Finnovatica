package com.fullertonfinnovatica.Accounts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.fullertonfinnovatica.Login;
import com.fullertonfinnovatica.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Ledger extends AppCompatActivity {

    Retrofit retrofit;
    AccountsAPI apiInterface;
    Call<LoginModel> loginCall;
    Call<JsonObject> ledgerCall;
    String account_name, balance_type, balance_amt;
    String[] debit_name, debit_amt, credit_name, credit_amt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);

        debit_amt = new String[10000];
        debit_name = new String[10000];
        credit_name = new String[10000];
        credit_amt = new String[10000];

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient cleint = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();

        retrofit = new Retrofit.Builder().baseUrl(AccountsAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cleint)
                .build();

        apiInterface = retrofit.create(AccountsAPI.class);

        loginCall = apiInterface.login("demouserid", "demo");

        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                ledgerCall = apiInterface.getLedger(getAuthToken("adhikanshmittalcool@gmail.com", "adhikansh/123"));

                ledgerCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        JsonObject bodyy = response.body();
                        JsonArray ledgerAray = bodyy.getAsJsonArray("ledger");
                        for (int i = 0; i<ledgerAray.size(); i++) {

                            JsonObject ledger = ledgerAray.get(i).getAsJsonObject();

                            JsonObject account = ledger.getAsJsonObject("account");
                            account_name = String.valueOf(account.get("name"));

                            JsonArray debitsArray = ledger.getAsJsonArray("debits");
                            JsonArray creditsArray = ledger.getAsJsonArray("credits");


//                            Log.e("blabla", ledger.getAsJsonArray("debits").toString() + " " + ledger.getAsJsonArray("debits").size());

                            for (int j = 0; j<debitsArray.size();j++){

                                JsonObject deb;
                                deb = debitsArray.get(j).getAsJsonObject();
                                debit_amt[j] = String.valueOf(deb.get("amount"));
                                JsonObject to = deb.getAsJsonObject("to");
                                debit_name[j] = String.valueOf(to.get("name"));
//                                Log.e("abcde", debit_amt[j] + " " + debit_name[j]);

                            }

                            for (int j = 0; j<creditsArray.size();j++){

                                JsonObject cred;
                                cred = creditsArray.get(j).getAsJsonObject();
                                credit_amt[j] = String.valueOf(cred.get("amount"));
                                JsonObject to = cred.getAsJsonObject("from");
                                credit_name[j] = String.valueOf(to.get("name"));
                            }

                            JsonObject balance = ledger.getAsJsonObject("balance");
                            balance_type = String.valueOf(balance.get("type"));
                            balance_amt = String.valueOf(balance.get("amount"));

//                            Log.e("blabla", balance_amt + balance_type);


                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

            }
        });

    }

    public static String getAuthToken(String userName, String password) {
        byte[] data = new byte[0];
        try {
            data = (userName + ":" + password).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("chekin2", "Basic " + Base64.encodeToString(data, Base64.NO_WRAP));
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }

}
