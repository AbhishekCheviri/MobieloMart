package in.nullify.mobielomart.Activity.SearchResult.BottomSheet;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 02-06-2018.
 */

@SuppressLint("ValidFragment")
public class BottomSheetFragment extends BottomSheetDialogFragment {
    private onCheckedListener callback;
    private int id;
    @SuppressLint("ValidFragment")
    public BottomSheetFragment(onCheckedListener callback,int id) {
            this.callback=callback;
            this.id=id;
    }

    public interface onCheckedListener {

        public void onChecked(int id);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.sort_bottom_sheet, null);
        RadioGroup rs = (RadioGroup) contentView.findViewById(R.id.radio_sort);

        if (id!=0) {
            RadioButton b = (RadioButton) contentView.findViewById(id);
            b.setChecked(true);
        }
        rs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                callback.onChecked(checkedId);
            }
        });

        dialog.setContentView(contentView);
    }
}