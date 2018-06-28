package in.nullify.mobielomart.Adapter.ProductViewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 22-05-2018.
 */

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> Images;
    LayoutInflater layoutInflater;


    public MyCustomPagerAdapter(Context context, ArrayList<String> Images) {
        this.context = context;
        this.Images = Images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.product_viewpager_style, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.product_images);

        RequestOptions options = new RequestOptions();
        options.fitCenter();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(Images.get(position))
                .apply(options)
                .into(imageView);


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
