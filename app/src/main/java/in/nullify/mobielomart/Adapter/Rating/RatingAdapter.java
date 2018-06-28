package in.nullify.mobielomart.Adapter.Rating;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.nullify.mobielomart.Adapter.NewProduct.NewProduct;
import in.nullify.mobielomart.Adapter.NewProduct.NewProductAdapter;
import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 10-06-2018.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private List<Rating> products;
    private Context context;

    public RatingAdapter(List<Rating> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_review_style, parent, false);
        return new RatingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        holder.username.setText(products.get(position).getName());
        holder.product_rating_person.setText(products.get(position).getRating());
        holder.review.setText(products.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView product_rating_person;
        private TextView review;
        private TextView username;
        public ViewHolder(View view) {
            super(view);

            product_rating_person = (TextView)view.findViewById(R.id.product_rating_person);
            review = (TextView) view.findViewById(R.id.review);
            username = (TextView) view.findViewById(R.id.username);
        }
    }
}
