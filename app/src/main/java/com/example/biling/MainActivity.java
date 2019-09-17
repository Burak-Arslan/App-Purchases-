package com.example.biling;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.example.Adapter.ProductAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {


    BillingClient billingClient;
    Button btnLoadProduct;
    RecyclerView recyclerProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetupCillingClient();

        Init();
        Events();


    }

    private void Events() {
        btnLoadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (billingClient.isReady()) {

                    final SkuDetailsParams skuDetails = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("billing", "billing"))
                            .setType(BillingClient.SkuType.INAPP).build();

                    billingClient.querySkuDetailsAsync(skuDetails, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {

                            LoadProductToRecyclerView(skuDetailsList);
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Billing client hata", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoadProductToRecyclerView(List<SkuDetails> skuDetailsList) {

        ProductAdapter productAdapter = new ProductAdapter(this, skuDetailsList, billingClient);
        recyclerProduct.setAdapter(productAdapter);
    }

    private void Init() {
        btnLoadProduct = findViewById(R.id.btnLoaded);
        recyclerProduct = findViewById(R.id.recMain);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));

    }

    private void SetupCillingClient() {
        try {
            billingClient = BillingClient.newBuilder(this).setListener(this).build();

            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    Toast.makeText(MainActivity.this, billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBillingServiceDisconnected() {
                    Toast.makeText(MainActivity.this, "HATA", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        Toast.makeText(this, purchases.size(), Toast.LENGTH_SHORT).show();
    }
}
