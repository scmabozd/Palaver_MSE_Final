package com.example.ude.palaver_mse;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.ude.palaver_mse.R.id.tablayout;

public class chatOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_overview);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(new AdapterFragmentCustomer(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(tablayout);
        //tabLayout.addTab(tablayout.newTab().setText("Tab 1"));
        //tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Kontakte"));

    }

    // TabLayout tabLayout = R.layout.class.;
    // tabLayout = (TabLayout) find
//   private TabLayout tablayout;
//   private ViewPager viewPager;

   /* public class chatFragment extends chatOverviewActivity {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_chat_window, container, false);
        }
    }*/

    /*ChatFragment chatFragment;
    ContactsFragment contactsFragment;

    tablayout = (TabLayout) findViewById(R.id.tablayout);
    tabLayout.setupWithViewPager(viewPager);
*/




}
