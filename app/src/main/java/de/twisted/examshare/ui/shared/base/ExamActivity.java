package de.twisted.examshare.ui.shared.base;

import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dagger.android.support.DaggerAppCompatActivity;
import de.twisted.examshare.R;
import de.twisted.examshare.util.ActivityHolder;

public abstract class ExamActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bundle data = getSavedInstanceState(getIntent().getExtras(), savedInstanceState);
        // if (ActivityHolder.redirectIfResumed(this, savedInstanceState, getIntent().getData())) return;
        // if (getContentView() != -1)
        //     setContentView(getContentView());

        this.setTaskDescription();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActivityHolder.setOptionsListener(this, item);
        return super.onOptionsItemSelected(item);
    }

    public ArrayAdapter getAutoCompleteAdapter(int ressourceId) {
        return new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(ressourceId));
    }

    public void finishStack() {
        Intent intent = new Intent();
        intent.putExtra("finish", true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void setTaskError(View view, int count, boolean error) {
        if (count > 0) {
            view.setVisibility(View.GONE);
            return;
        }
        int title = error ? R.string.no_internet_connection : R.string.no_results;
        int message = error ? R.string.connection_error_retry : R.string.no_results_hint;
        int drawable = error ? R.drawable.ic_error_outline : R.drawable.ic_search_grey;
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.message)).setText(message);
        ((ImageView) view.findViewById(R.id.imageView)).setImageResource(drawable);
        ((LinearLayout) view).setVisibility(View.VISIBLE);
    }

    protected void setTaskDescription() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logo_without_text);
        TaskDescription taskDesc = new TaskDescription(getString(R.string.app_name), bm, getResources().getColor(R.color.tabColor));
        setTaskDescription(taskDesc);
    }
}