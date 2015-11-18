package my.com.taruc.fitnesscompanion;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import my.com.taruc.fitnesscompanion.HRStripBLE.DeviceScanActivity;
import my.com.taruc.fitnesscompanion.HeartRateCamera.HeartRateMonitor;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learner_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View view_container;
    UserLocalStore userLocalStore;
    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if(savedInstanceState!= null){
            mFromSavedInstanceState =true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        userLocalStore = new UserLocalStore(view.getContext());
        FacebookSdk.sdkInitialize(view.getContext());
        Button buttonPairHR = (Button) view.findViewById(R.id.btnPairHR);
        buttonPairHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), DeviceScanActivity.class);
                startActivity(i);
            }
        });
        Button button = (Button) view.findViewById(R.id.btnCheckHR);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity().getApplicationContext(), HeartRateMonitor.class);
                startActivity(intent);
            }
        });
        Button buttonSignout = (Button) view.findViewById(R.id.btnSignOut);
        buttonSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(getActivity().getApplicationContext(), LoginPage.class);
                startActivity(loginIntent);
            }
        });

        return view;
    }


    public void setUp(int fragmentId, final DrawerLayout drawerLayout, final Toolbar toolbar) {
        view_container = getActivity().findViewById(fragmentId);
        mDrawLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawLayout.openDrawer(view_container);
        }
        mDrawLayout.setDrawerListener(mDrawerToggle);
        mDrawLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public void saveToPreferences(Context context,String preferenceName ,String preferenceValue){

        SharedPreferences sharedPreferences  = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context,String preferenceName ,String defaultValue){

        SharedPreferences sharedPreferences  = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }



}
