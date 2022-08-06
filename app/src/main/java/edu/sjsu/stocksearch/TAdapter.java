package edu.sjsu.stocksearch;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;

public class TAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public TAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }

    public static class CustomAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
        public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected CharSequence convertSelectionToString(Object selectedItem) {
            String str = selectedItem.toString();
            String[] tmp = str.split(" ");
            str = tmp[0];
            return str;
        }
    }
}
