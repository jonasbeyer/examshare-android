package de.twisted.examshare.ui.shared.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.List;

import de.twisted.examshare.util.interfaces.ExamModel;

public abstract class ExamListAdapter extends BaseAdapter {

    protected List<ExamModel> itemList;

    public ExamListAdapter(List<ExamModel> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    public int getLastItemId() {
        return ((ExamModel) getItem(getCount() - 1)).getId();
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public abstract AdapterView.OnItemClickListener getClickListener();

    public abstract void setItemList(List<ExamModel> items);
}
