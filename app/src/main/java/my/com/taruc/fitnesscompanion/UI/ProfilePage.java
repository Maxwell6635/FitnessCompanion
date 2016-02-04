package my.com.taruc.fitnesscompanion.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.R;

public class ProfilePage extends ActionBarActivity {

    private static final int NUM_PAGES = 2;
    @Bind(R.id.textViewUserProfile)
    TextView textViewUserProfile;
    @Bind(R.id.textViewHealthProfile)
    TextView textViewHealthProfile;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    textViewUserProfile.setBackgroundColor(Color.parseColor("#D95763"));
                    textViewHealthProfile.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    textViewHealthProfile.setBackgroundColor(Color.parseColor("#D95763"));
                    textViewUserProfile.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        textViewUserProfile.setBackgroundColor(Color.parseColor("#D95763"));
        textViewHealthProfile.setBackgroundColor(Color.TRANSPARENT);
    }
/*
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
*/
    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new UserProfilePage();
            } else {
                return new HealthProfilePage();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void GoHealthProfile(View view) {
        mPager.setCurrentItem(1);
        textViewHealthProfile.setBackgroundColor(Color.parseColor("#D95763"));
        textViewUserProfile.setBackgroundColor(Color.TRANSPARENT);
    }

    public void GoUserProfile(View view) {
        mPager.setCurrentItem(0);
        textViewUserProfile.setBackgroundColor(Color.parseColor("#D95763"));
        textViewHealthProfile.setBackgroundColor(Color.TRANSPARENT);
    }

    public void BackAction(View view) {
        this.finish();
    }


}
