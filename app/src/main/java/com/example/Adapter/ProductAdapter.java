package com.example.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.example.Interface.IProductClickListener;
import com.example.biling.MainActivity;
import com.example.biling.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    MainActivity mainActivity;
    List<SkuDetails> skuDetailsList;
    BillingClient billingClient;

    public ProductAdapter(MainActivity mainActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.mainActivity = mainActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mainActivity.getBaseContext())
                .inflate(R.layout.product_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtProduct.setText(skuDetailsList.get(position).getTitle());

        holder.setıProductClickListener(new IProductClickListener() {
            @Override
            public void onProductClickListener(View view, int position) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(position)).build();

                billingClient.launchBillingFlow(mainActivity, billingFlowParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtProduct;

        IProductClickListener ıProductClickListener;

        public void setıProductClickListener(IProductClickListener ıProductClickListener) {
            this.ıProductClickListener = ıProductClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProduct = itemView.findViewById(R.id.txtProductName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ıProductClickListener.onProductClickListener(itemView, getAdapterPosition());
        }
    }
}
