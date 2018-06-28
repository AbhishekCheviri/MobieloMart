package in.nullify.mobielomart.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 12-05-2018.
 */

public class SugListAdapter extends ArrayAdapter<String> {

        private Activity context;
        private ArrayList<String> sug=new ArrayList<>();
        private ArrayList<String> type=new ArrayList<>();


        public SugListAdapter(Activity context, ArrayList<String> sug, ArrayList<String> type) {
            super(context, R.layout.sug_list_item_style, sug);
            this.context = context;
            this.sug = sug;
            this.type = type;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.sug_list_item_style, null, true);
            TextView sugestions = (TextView) rowView.findViewById(R.id.suggestions);
            ImageView img = (ImageView) rowView.findViewById(R.id.sug_type_img);
            sugestions.setText(sug.get(position));
            if (!type.isEmpty()) {
                if (type.get(position).equals("1")) {
                    img.setImageResource(R.drawable.ic_history_black_24dp);

                } else if (type.get(position).equals("2")) {
                    img.setImageResource(R.drawable.ic_trending_up_black_24dp);
                }
            }
            return rowView;
        }
    }
