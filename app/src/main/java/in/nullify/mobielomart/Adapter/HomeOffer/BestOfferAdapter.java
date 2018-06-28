package in.nullify.mobielomart.Adapter.HomeOffer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import in.nullify.mobielomart.Activity.HomeActivity.Fragments.HomeFragment;
import in.nullify.mobielomart.Activity.ProductActivity.ProductActivity;
import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 03-06-2018.
 */

public class BestOfferAdapter extends RecyclerView.Adapter<BestOfferAdapter.ViewHolder> {
    private List<OffersHome> products;
    private Context context;
    private int width=180,height=180;
    public BestOfferAdapter(Context context, List<OffersHome> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public BestOfferAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_offer_grid_style, viewGroup, false);
        width = viewGroup.getMeasuredWidth()/2;
        return new BestOfferAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BestOfferAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.newproduct_parent.setMinimumWidth(width);

        String pname = products.get(i).getP_name();
        if (pname.contains("("))
            pname = pname.substring(0,pname.indexOf("("));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("pid",Integer.parseInt(products.get(i).getP_id()));
                context.startActivity(intent);
            }
        });

        viewHolder.product_name.setText(pname);

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(products.get(i).getP_img().replace("\\/", "/"))
                .apply(options)
                .into(viewHolder.product_img);
        viewHolder.product_offer_perc.setText(products.get(i).getOff_perc()+"% Off");

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView product_name;
        private TextView product_offer_perc;
        private ImageView product_img;
        private LinearLayout newproduct_parent;
        public ViewHolder(View view) {
            super(view);

            product_name = (TextView)view.findViewById(R.id.product_name);
            product_img = (ImageView) view.findViewById(R.id.product_image);
            product_offer_perc = (TextView) view.findViewById(R.id.product_off_perc);
            newproduct_parent = (LinearLayout) view.findViewById(R.id.new_cont);
        }
    }

}