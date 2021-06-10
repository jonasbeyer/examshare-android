package de.twisted.examshare.ui.shared.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import de.twisted.examshare.data.models.ExFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class ExamPagerAdapter extends PagerAdapter {

    private List<ExFragment> fragmentList;

    public ExamPagerAdapter() {
        this.fragmentList = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = instantiateView(container, position);
        container.addView(itemView);
        this.fragmentList.add(position, new ExFragment(itemView));
        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void onSelected(int position) {
        ExFragment fragment = fragmentList.get(position);
        View itemView = fragment.getItemView();
        if (fragment.isInitialized())
            return;

        fragment.setInitialized(true);
        this.fragmentList.set(position, fragment);
        this.onView(itemView, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {}

    public abstract void onView(View itemView, int position);

    public abstract View instantiateView(ViewGroup container, int position);
}
