package in.nullify.mobielomart.Activity.HomeActivity.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import in.nullify.mobielomart.Activity.AddressActivity.AddressActivity;
import in.nullify.mobielomart.Activity.EditAccountActivity.EditAccountActivity;
import in.nullify.mobielomart.Activity.OrdersActivity.OrdersActivity;
import in.nullify.mobielomart.Activity.SigninActivity.SignInActivity;
import in.nullify.mobielomart.Activity.TrackActivity.TrackActivity;
import in.nullify.mobielomart.Activity.WishListActivity.WishlistActivity;

import in.nullify.mobielomart.R;

/**
 * Created by Abhishekpalodath on 26-05-2018.
 */

public class AccountFragment extends Fragment {

    private boolean SIGNEDIN;
    private SharedPreferences mShared;

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.account_fragment, container, false);
        LinearLayout llsignin=(LinearLayout)rootView.findViewById(R.id.ll_signin);
        ListView lv_account=(ListView)rootView.findViewById(R.id.lv_account);
        Button btn_acc_signinup=(Button)rootView.findViewById(R.id.btn_acc_signinup);

        mShared = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SIGNEDIN = (mShared.getBoolean("SIGNEDIN", false));
        if(!SIGNEDIN) {
            llsignin.setVisibility(LinearLayout.VISIBLE);
        }
        else
        {
            llsignin.setVisibility(LinearLayout.GONE);
        }
        btn_acc_signinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mShared.edit().remove("SIGNEDIN").commit();
                Intent intent=new Intent(getActivity(),SignInActivity.class);
                startActivity(intent);
            }
        });
        lv_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!SIGNEDIN) {
                    Toast.makeText(getActivity(),"Sign in/Sign up to continue",Toast.LENGTH_SHORT).show();
                }
                else{
                    switch (position){
                        case 0:Intent intent=new Intent(getActivity(),OrdersActivity.class);
                            startActivity(intent);
                            break;
                        case 1:intent=new Intent(getActivity(),WishlistActivity.class);
                            startActivity(intent);
                            break;
                        case 2:intent=new Intent(getActivity(),TrackActivity.class);
                            startActivity(intent);
                            break;
                        case 3:intent=new Intent(getActivity(),EditAccountActivity.class);
                            startActivity(intent);
                            break;
                        case 4:intent=new Intent(getActivity(), AddressActivity.class);
                            startActivity(intent);
                            break;
                        case 5://Logout Code
                            break;
                    }
                }

            }
        });
        return rootView;
    }
}