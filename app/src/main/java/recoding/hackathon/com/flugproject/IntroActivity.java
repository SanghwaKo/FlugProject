package recoding.hackathon.com.flugproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by KSH on 2017-06-16.
 */

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        AirApplication.getInstance().setCurrentActivity(this);
    }

    public void onLoaded(View view){
        setContentView(R.layout.tutorial_pages);
        setViewPager();
    }

    private void setViewPager(){
        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
    }

    public void onSkip(View view){
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter{
        public pagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position){
            android.support.v4.app.Fragment fragments = new TutorialFragments();
            ((TutorialFragments)fragments).setPage(position);
            ((TutorialFragments)fragments).setContext(IntroActivity.this);

            return fragments;
        }

        @Override
        public int getCount(){
            return 4;
        }
    }
}
