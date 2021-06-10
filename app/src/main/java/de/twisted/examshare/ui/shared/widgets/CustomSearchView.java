package de.twisted.examshare.ui.shared.widgets;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import de.twisted.examshare.R;
import de.twisted.examshare.util.helper.TextUtil;
import de.twisted.examshare.ui.shared.base.ExamActivity;
import de.twisted.examshare.util.listeners.TextChangedListener;
import de.twisted.examshare.data.models.QueryData;

public class CustomSearchView extends AppCompatEditText {

    private ImageView clearButton;
    private ImageView filterButton;
    private Runnable runnable;
    private ArrayAdapter<String> classesAdapter;

    private String schoolForm = "";
    private String federalState = "";
    private String grade = "";
    private String creator = "";
    private int sortOrder, uploadDate;

    private AutoCompleteTextView schoolFormInput;
    private AutoCompleteTextView federalStateInput;
    private AutoCompleteTextView gradeInput;
    private TextInputEditText creatorInput;
    private ExamSpinner sortOrderInput, uploadDateInput;

    public CustomSearchView(Context context) {
        this(context, null);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable filterDrawable = ContextCompat.getDrawable(context, R.drawable.ic_tune);
        Drawable searchDrawable = ContextCompat.getDrawable(context, R.drawable.ic_search_grey);

        filterDrawable.setBounds(0, 0, (int) getResources().getDimension(R.dimen.icon_padding), 0);
        searchDrawable.setBounds(0, 0, searchDrawable.getIntrinsicWidth(), searchDrawable.getIntrinsicHeight());
        setCompoundDrawables(searchDrawable, null, filterDrawable, null);
    }

    public void init(ExamActivity activity, Runnable runnable) {
        this.clearButton = activity.findViewById(R.id.clearButton);
        this.clearButton.setOnClickListener(v -> clearText());
        this.classesAdapter = activity.getAutoCompleteAdapter(R.array.classes);

        Dialog filterDialog = buildFilterDialog(activity);
        this.filterButton = activity.findViewById(R.id.filterButton);
        this.filterButton.setOnClickListener(v -> {
            this.schoolFormInput.setText(schoolForm, false);
            this.federalStateInput.setText(federalState, false);
            this.gradeInput.setText(grade, false);
            this.creatorInput.setText(creator);
            this.sortOrderInput.setSelection(sortOrder, false);
            this.uploadDateInput.setSelection(uploadDate, false);
            filterDialog.show();
        });
        this.runnable = runnable;
        addTextChangedListener(getIconWatcher());
        addTextChangedListener(TextChangedListener.get(text -> runnable.run()));
    }

    private Dialog buildFilterDialog(ExamActivity activity) {
        View filterView = activity.getLayoutInflater().inflate(R.layout.dialog_search_filter, null);
        this.schoolFormInput = filterView.findViewById(R.id.schoolForm);
        this.schoolFormInput.setAdapter(activity.getAutoCompleteAdapter(R.array.school_forms));
        this.federalStateInput = filterView.findViewById(R.id.federalState);
        this.federalStateInput.setAdapter(activity.getAutoCompleteAdapter(R.array.federal_states));

        this.gradeInput = filterView.findViewById(R.id.grade);
        this.gradeInput.setAdapter(classesAdapter);
        this.creatorInput = filterView.findViewById(R.id.creator);

        this.sortOrderInput = filterView.findViewById(R.id.sortOrder);
        this.uploadDateInput = filterView.findViewById(R.id.uploadData);
        this.sortOrderInput.setAdapter(R.array.sort_by, filterView.findViewById(R.id.sortHint));
        this.uploadDateInput.setAdapter(R.array.upload_time, filterView.findViewById(R.id.sortHint));

        Builder builder = new Builder(activity);
        builder.setTitle(R.string.search_filter);
        builder.setView(filterView);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            this.schoolForm = schoolFormInput.getText().toString().trim();
            this.federalState = federalStateInput.getText().toString().trim();
            this.grade = gradeInput.getText().toString();
            this.creator = creatorInput.getText().toString().trim();
            this.sortOrder = sortOrderInput.getSelectedItemPosition();
            this.uploadDate = uploadDateInput.getSelectedItemPosition();
            this.filterButton.setColorFilter(getResources().getColor(isDefaultState() ? R.color.unselected : R.color.colorAccent), Mode.SRC_IN);
            if (hasKeyword())
                runnable.run();
        });
        return builder.create();
    }

    public QueryData getQuery() {
        QueryData query = new QueryData(getKeyword(), uploadDate, sortOrder);
        query.set("schoolForm", schoolForm);
        query.set("federalState", federalState);
        query.set("grade", String.valueOf(classesAdapter.getPosition(grade)));
        query.set("creator", creator);
        return query;
    }

    public String getKeyword() {
        return getText().toString().trim();
    }

    public boolean hasKeyword() {
        return !getKeyword().isEmpty();
    }

    private boolean isDefaultState() {
        return schoolForm.isEmpty() && federalState.isEmpty() && grade.isEmpty() && creator.isEmpty() && uploadDate == 0 && sortOrder == 0;
    }

    private void clearText() {
        setText("");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
        clearFocus();
    }

    private TextWatcher getIconWatcher() {
        return TextChangedListener.get(text -> clearButton.setVisibility(TextUtil.isEmpty(text) ? View.GONE : View.VISIBLE));
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(null);
    }
}