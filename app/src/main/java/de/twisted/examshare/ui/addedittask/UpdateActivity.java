package de.twisted.examshare.ui.addedittask;

import de.twisted.examshare.ui.shared.base.ExamActivity;

public class UpdateActivity extends ExamActivity {

    // private Task task;
    // private EditText title;
    // private TagGroup keywords;
    // private EditText grade;
    // private EditText creator;
    // private DropDownInput schoolForm;
    // private DropDownInput federalState;
    //
    // private ProgressBar progressBar;
    // private ExamSpinner inputSpinner, subjectSpinner;
    // private LinkedHashMap<EditText, Integer> inputViews;
    //
    // @Override
    // protected void onLoad(Bundle bundle) {
    //     setSupportActionBar(findViewById(R.id.toolbar));
    //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    //     getSupportActionBar().setDisplayShowTitleEnabled(false);
    //
    //     this.task = bundle.getParcelable("task");
    //     this.setUpSpinner();
    //
    //     this.title = (EditText) findViewById(R.id.title);
    //     this.title.setText(task.getTitle());
    //     this.title.addTextChangedListener(TextUtil.getNotEmptyListener(title));
    //
    //     this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
    //     this.progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), Mode.SRC_IN);
    //
    //     this.grade = (EditText) findViewById(R.id.grade);
    //     this.grade.setText(task.getGrade() == 0 ? null : String.valueOf(task.getGrade()));
    //     this.grade.setFilters(new InputFilter[]{new InputFilterMinMax(1, 13)});
    //     this.grade.addTextChangedListener(TextUtil.getNotEmptyListener(grade));
    //     this.creator = (EditText) findViewById(R.id.creator);
    //     this.creator.setText(task.getCreator());
    //
    //     this.schoolForm = (DropDownInput) findViewById(R.id.schoolForm);
    //     this.schoolForm.setText(task.getSchoolForm());
    //     this.schoolForm.setAdapter(getAutoCompleteAdapter(R.array.school_forms));
    //     this.schoolForm.addTextChangedListener(TextUtil.getNotEmptyListener(schoolForm));
    //
    //     this.federalState = (DropDownInput) findViewById(R.id.federalState);
    //     this.federalState.setText(task.getFederalState());
    //     this.federalState.setAdapter(getAutoCompleteAdapter(R.array.federal_states));
    //     this.federalState.addTextChangedListener(TextUtil.getNotEmptyListener(federalState));
    //
    //     this.inputSpinner = (ExamSpinner) findViewById(R.id.spinner);
    //     this.inputSpinner.setAdapter(R.array.create_task_options, 18);
    //     // this.inputSpinner.setOnItemSelectedListener(ItemSelectedListener.get(position -> showInput(position)));
    //
    //     this.keywords = (TagGroup) findViewById(R.id.keywords);
    //     this.keywords.setMaxTags(Preferences.MAX_TAGS);
    //     this.keywords.setTags(task.getKeywords());
    //     this.keywords.setOnTagChangeListener((tagGroup, tag) -> {
    //         if (tagGroup.getTags().length >= Preferences.MIN_TAGS)
    //             keywords.setError(false);
    //     });
    //
    //     this.setUpdateListener();
    //     this.inputViews = new LinkedHashMap<>();
    //     this.inputViews.put(grade, 1);
    //     this.inputViews.put(schoolForm, 2);
    //     this.inputViews.put(federalState, 2);
    //
    //     findViewById(R.id.layout).setFocusableInTouchMode(true);
    //     findViewById(R.id.layout).requestFocus();
    // }
    //
    // @Override
    // protected int getContentView() {
    //     return R.layout.activity_update;
    // }
    //
    // private void setUpdateListener() {
    //     Button button = (Button) findViewById(R.id.update);
    //     button.setOnClickListener(new ExamClickListener(this) {
    //         @Override
    //         public void onClicked(View view, Runnable runnable) {
    //             task.setTitle(title.getText().toString());
    //             task.setKeywords(Arrays.asList(keywords.getTags()));
    //             task.setSubject(subjectSpinner.getSelectedItem().toString());
    //             task.setGrade(Integer.parseInt(grade.getText().toString()));
    //             task.setCreator(creator.getText().toString());
    //             task.setSchoolForm(schoolForm.getText().toString());
    //             task.setFederalState(federalState.getText().toString());
    //             task.setUpdated(System.currentTimeMillis());
    //
    //             progressBar.setVisibility(View.VISIBLE);
    //             ExamShareApplication.getInstance().getTaskManager().updateTask(task, (responseType) -> {
    //                 progressBar.setVisibility(View.GONE);
    //                 switch (responseType) {
    //                     case NOT_FOUND:
    //                         TextUtil.toast(getBaseContext(), R.string.task_not_exist);
    //                         notifyTaskChange(task, UpdateStatus.REMOVED);
    //                         onBackPressed();
    //                         break;
    //                     case SUCCESS:
    //                         notifyTaskChange(task, UpdateStatus.CHANGED);
    //                         onBackPressed();
    //                 }
    //                 runnable.run();
    //             });
    //         }
    //     });
    // }
    //
    // @Override
    // public boolean onInputCheck(boolean valid) {
    //     if (TextUtil.isEmpty(title)) return false;
    //     if (keywords.getTags().length < Preferences.MIN_TAGS) {
    //         keywords.setError(true);
    //         inputSpinner.setSelection(0);
    //         TextUtil.toast(this, R.string.two_keywords_required);
    //         return false;
    //     }
    //     for (EditText view : inputViews.keySet()) {
    //         int spinnerPos = inputViews.get(view);
    //         if (TextUtil.isEmpty(view)) {
    //             inputSpinner.setSelection(spinnerPos);
    //             return false;
    //         }
    //     }
    //     return true;
    // }
    //
    // @Override
    // public void onBackPressed() {
    //     if (progressBar.getVisibility() != View.VISIBLE)
    //         super.onBackPressed();
    // }
    //
    // private void showInput(int position) {
    //     keywords.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    //     grade.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
    //     creator.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
    //     schoolForm.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
    //     federalState.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
    // }
    //
    // private void setUpSpinner() {
    //     String[] subjects = ExamShareApplication.getInstance().getSubjectManager().getSubjectsArray();
    //     this.subjectSpinner = (ExamSpinner) findViewById(R.id.subjectSpinner);
    //     this.subjectSpinner.setAdapter(subjects, null);
    //     this.subjectSpinner.setSelection(getSelectedPosition(task.getSubject(), subjects));
    // }
    //
    // private int getSelectedPosition(String subject, String[] subjects) {
    //     for (int i = 0; i < subjects.length; i++) {
    //         if (subjects[i].equals(subject))
    //             return i;
    //     }
    //
    //     return 0;
    // }
}
