package in.nullify.mobielomart.Adapter.NewProduct;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public class ItemDecorationAlbumColumns extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemDecorationAlbumColumns(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = offset;
        outRect.right = offset;
        outRect.bottom = offset;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = offset;
        }

    }
}