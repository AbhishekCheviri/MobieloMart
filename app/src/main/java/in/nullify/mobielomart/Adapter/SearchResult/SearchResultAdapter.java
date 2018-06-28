package in.nullify.mobielomart.Adapter.SearchResult;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import in.nullify.mobielomart.Activity.ProductActivity.ProductActivity;
import in.nullify.mobielomart.Activity.SearchResult.SearchResultActivity;
import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private ArrayList<SearchResult> products;
    private Context context;

    public SearchResultAdapter(Context context, ArrayList<SearchResult> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_product_style, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.product_name.setText(products.get(i).getP_name());

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("pid",Integer.parseInt(products.get(i).getP_id()));
                context.startActivity(intent);
            }
        });

        float rating = Float.parseFloat(products.get(i).getP_rating());

        if (rating > 3.5) {
            viewHolder.product_rating.setText(String.valueOf(rating));
            viewHolder.product_rating.setBackgroundColor(context.getResources().getColor(R.color.rating_good));
        } else if (rating > 2.5) {
            viewHolder.product_rating.setText(String.valueOf(rating));
            viewHolder.product_rating.setBackgroundColor(context.getResources().getColor(R.color.rating_avg));
        } else {
            viewHolder.product_rating.setText(String.valueOf(rating));
            viewHolder.product_rating.setBackgroundColor(context.getResources().getColor(R.color.rating_bad));
        }
        Glide.with(context)
                .load(products.get(i).getP_img())
                .apply(options)
                .into(viewHolder.product_img);

        if (products.get(i).getOff_id().equals("0")) {
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            int rs = Integer.parseInt(products.get(i).getP_price());
            viewHolder.product_offer_price.setText(format.format(rs));
            viewHolder.product_price.setVisibility(View.GONE);
        } else {
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            int rs = Integer.parseInt(products.get(i).getP_price());
            viewHolder.product_price.setText((Html.fromHtml("<strike>" + format.format(rs) + "</strike>")));
            viewHolder.product_price.setTextColor(context.getResources().getColor(R.color.offer_original_text_color));

            rs = Integer.parseInt(products.get(i).getOff_price());
            viewHolder.product_offer_price.setText(format.format(rs));
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView product_name;
        private TextView product_price;
        private TextView product_offer_price;
        private TextView product_rating;
        private ImageView product_img;
        private RelativeLayout newproduct_parent;

        public ViewHolder(View view) {
            super(view);

            product_name = (TextView) view.findViewById(R.id.product_name);
            product_img = (ImageView) view.findViewById(R.id.product_image);
            product_price = (TextView) view.findViewById(R.id.product_price);
            product_rating = (TextView) view.findViewById(R.id.product_rating);
            product_offer_price = (TextView) view.findViewById(R.id.product_offer_price);
            newproduct_parent = (RelativeLayout) view.findViewById(R.id.new_cont);
        }
    }




}
