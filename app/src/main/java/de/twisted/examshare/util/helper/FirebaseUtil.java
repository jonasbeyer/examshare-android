package de.twisted.examshare.util.helper;

import androidx.core.util.Consumer;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseUtil {

    private final static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void getInstanceId(Consumer<String> consumer) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            String token = task.isSuccessful() ? task.getResult().getToken() : null;
            consumer.accept(token);
        });
    }

    public static void deleteInstance() {
       executorService.execute(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
