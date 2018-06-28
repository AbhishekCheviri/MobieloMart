package in.nullify.mobielomart.Adapter.ProductColor;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import in.nullify.mobielomart.Activity.ProductActivity.ProductActivity;
import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public class ProductColorAdapter extends RecyclerView.Adapter<ProductColorAdapter.ViewHolder> {
    private ArrayList<ProductColor> products;
    private Context context;
    private ProductActivity activity = new ProductActivity();
    private int width=180,height=180;
    int selectedpos = 0;
    public ProductColorAdapter(Activity context, ArrayList<ProductColor> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public ProductColorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_color_gride_style, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductColorAdapter.ViewHolder viewHolder, final int i) {



        if(selectedpos==i)
            viewHolder.itemView.setBackgroundResource(R.drawable.color_clicked);
        else
            viewHolder.itemView.setBackgroundResource(R.drawable.color_unclicked);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedpos=i;
                notifyDataSetChanged();
                ((ProductActivity) context).setImages(products.get(i).getProduct_color());
            }
        });
        viewHolder.product_color.setText(products.get(i).getProduct_color());

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(products.get(i).getProduct_image())
                .apply(options)
                .into(viewHolder.product_img);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView product_color;
        private ImageView product_img;
        private LinearLayout newproduct_parent;
        public ViewHolder(View view) {
            super(view);

            product_color = (TextView)view.findViewById(R.id.color_name);
            product_img = (ImageView) view.findViewById(R.id.color_product_preview);
            newproduct_parent = (LinearLayout) view.findViewById(R.id.item_cont);
        }
    }

}
