package com.example.converterapplab3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.converterapplab3.services.ConversionType;
import com.example.converterapplab3.tasks.Task;
import com.example.converterapplab3.tasks.TaskListener;

public class RetainFragment extends Fragment {
    public static final String TAG = RetainFragment.class.getSimpleName();

    private ActivityState activityState = new ActivityState();
    private ActivityListener activityListener;
    private Task<String> currentTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void convert(ConversionType conversionType, Double input, String fromType, String toType, SharedPreferences sharedPreferences) {
        currentTask = createConvertTask(conversionType, input, fromType, toType,sharedPreferences);
        currentTask.execute(new TaskListener<String>() {
            @Override
            public void onSuccess(String result) {
                activityState.result = result;
                updateState();
            }

            @Override
            public void onError() {
                updateState();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentTask != null)
            currentTask.cancel();
    }

    private void updateState() {
        if (activityListener != null)
            activityListener.onNewState(activityState);
    }

    private Task<String> createConvertTask(ConversionType conversionType,
                                           Double input,
                                           String fromType,
                                           String toType,
                                           SharedPreferences sharedPreferences) {
        return new LooperThreadTask<>(new ConvertorCallable(conversionType, input, fromType, toType, sharedPreferences));
    }

    public void setListener(ActivityListener activityListener) {
        this.activityListener = activityListener;
        if (activityListener != null)
            activityListener.onNewState(activityState);
    }

    public interface ActivityListener {
        void onNewState(ActivityState activityState);
    }

}
