package in.nullify.mobielomart.Activity.HomeActivity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 26-05-2018.
 */

public class CategoryFragent extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.categories_fragment, container, false);

        return rootView;
    }
}
