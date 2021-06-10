package de.twisted.imagepicker.imageeditengine.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import de.twisted.imagepicker.imageeditengine.utils.TaskCallback;
import de.twisted.imagepicker.imageeditengine.utils.Utility;

public final class ProcessingImage extends AsyncTask<Void, Void, String> {
  private final Bitmap srcBitmap;
  private final TaskCallback<String> callback;
  private final String imagePath;

  public ProcessingImage(Bitmap srcBitmap, String imagePath, TaskCallback<String> taskCallback) {
    this.srcBitmap = srcBitmap;
    this.callback = taskCallback;
    this.imagePath = imagePath;
  }

  @Override protected String doInBackground(Void... voids) {
    return Utility.saveBitmap(srcBitmap,imagePath);
  }

  @Override protected void onPreExecute() {
    super.onPreExecute();
  }

  @Override protected void onPostExecute(String s) {
    super.onPostExecute(s);
    if(callback!=null){
      callback.onTaskDone(s);
    }
  }
}// end inner class