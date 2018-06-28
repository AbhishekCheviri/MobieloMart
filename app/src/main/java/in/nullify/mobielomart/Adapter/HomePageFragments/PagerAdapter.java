package in.nullify.mobielomart.Adapter.HomePageFragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.nullify.mobielomart.Activity.HomeActivity.Fragments.AccountFragment;
import in.nullify.mobielomart.Activity.HomeActivity.Fragments.CategoryFragent;
import in.nullify.mobielomart.Activity.HomeActivity.Fragments.HomeFragment;

/**
 * Created by Abhishekpalodath on 26-05-2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private String[] tabTitles = new String[]{"CATEGORIES", "HOME", "ACCOUNT"};
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CategoryFragent tab1 = new CategoryFragent();
                return tab1;
            case 1:
                HomeFragment tab2 = new HomeFragment();
                return tab2;
            case 2:
                AccountFragment tab3 = new AccountFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}