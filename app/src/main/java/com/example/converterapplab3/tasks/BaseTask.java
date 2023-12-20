package com.example.converterapplab3.tasks;

import android.os.Handler;
import android.os.Looper;


public abstract class BaseTask<T> implements Task<T> {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private TaskListener<T> taskListener;
    private boolean executed;
    private boolean cancelled;

    @Override
    public void execute(TaskListener<T> taskListener)
    {
        if (executed) throw new RuntimeException("");
        if (cancelled) return;
        executed = true;
        this.taskListener = taskListener;
        start();
    }

    @Override
    public void cancel() {
        if (!cancelled)
            cancelled = true;
        taskListener = null;
        onCancelled();
    }

    protected abstract void start();

    protected abstract void onCancelled();

    protected final void publishSuccess(T result) {
        runOnMainThread(() -> {
            if (taskListener != null)
                taskListener.onSuccess(result);
            taskListener = null;
        });

    }

    private void runOnMainThread(Runnable action) {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
        } else {
            HANDLER.post(action);
        }
    }
}
