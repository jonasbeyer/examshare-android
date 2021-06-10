package de.twisted.imagepicker.utility;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.MimeTypeMap;

import java.io.File;

public class ExMediaScannerConnectionClient implements MediaScannerConnectionClient {

    private File file;
    private Runnable runnable;
    private MediaScannerConnection mediaScannerConnection;

    @Override
    public void onMediaScannerConnected() {
        mediaScannerConnection.scanFile(file.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(final String path, Uri uri) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (path.equals(file.getAbsolutePath())) {
                mediaScannerConnection.disconnect();
                runnable.run();
            }
        });
    }

    public void setScanner(MediaScannerConnection mediaScannerConnection, File file, Runnable runnable) {
        this.mediaScannerConnection = mediaScannerConnection;
        this.runnable = runnable;
        this.file = file;
    }
}
