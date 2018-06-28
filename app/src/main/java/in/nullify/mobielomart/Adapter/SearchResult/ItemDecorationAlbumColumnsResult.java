package in.nullify.mobielomart.Adapter.SearchResult;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Abhishekpalodath on 20-05-2018.
 */

public class ItemDecorationAlbumColumnsResult extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemDecorationAlbumColumnsResult(int offset) {
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