package com.fullertonfinnovatica.Inventory;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fullertonfinnovatica.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class InventoryAdd extends AppCompatActivity implements Callback<InventoryModel> {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    EditText ed_product_name, ed_product_qty, ed_product_cost;
    Button add;
    String product_name, product_qty, product_cost;
    InventoryAPI apiInterface;
    JSONObject paramObject;
    View rootLayout;
    int revealX;
    int revealY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_add);
        final Intent intent = getIntent();
        rootLayout = findViewById(R.id.add);
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }


        ed_product_cost = findViewById(R.id.product_cost);
        ed_product_qty = findViewById(R.id.product_qty);
        ed_product_name = findViewById(R.id.product_name);
        add = findViewById(R.id.add_product);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InventoryAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(InventoryAPI.class);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product_cost = ed_product_cost.getText().toString();
                product_qty = ed_product_qty.getText().toString();
                product_name = ed_product_name.getText().toString();

            }
        });

        try {
            paramObject = new JSONObject();
            paramObject.put("mobile_no", "8558855896");
            paramObject.put("inventory_name", "GoodDay");
            paramObject.put("inventory_category", "Food");
            paramObject.put("inventory_cost", "15");
            paramObject.put("inventory_qty", "100");
            TextView t = findViewById(R.id.abcc);
            t.setText(paramObject.toString());
            Call<InventoryModel> userCall = apiInterface.getUser(paramObject.toString());
            userCall.enqueue(this);
            Toast.makeText(getBaseContext(), "Sent data", Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();

        }
    }


    @Override
    public void onResponse(Call<InventoryModel> call, Response<InventoryModel> response) {
    }

    @Override
    public void onFailure(Call<InventoryModel> call, Throwable t) {
    }


    void apiCall(JSONObject j){

        Call<InventoryModel> userCall = apiInterface.getUser(j.toString());
        userCall.enqueue(this);
        Toast.makeText(getBaseContext(), "Sent data", Toast.LENGTH_LONG).show();

    }

}
