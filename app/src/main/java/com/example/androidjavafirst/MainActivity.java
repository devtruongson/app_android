package com.example.androidjavafirst;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidjavafirst.databinding.ActivityMainBinding;
import com.example.androidjavafirst.diff.DiffFragment;
import com.example.androidjavafirst.point.PointFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navDiff:
                        binding.viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navPoint:
                        binding.viewPager.setCurrentItem(1);
                        return true;
                }
                return false;
            }
        });
    }

    private void setupViewPager() {
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 1:
                        return new PointFragment();
                    default:
                        return new DiffFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navDiff);
                        break;
                    case 1:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navPoint);
                        break;
                }
            }
        });
    }
}
