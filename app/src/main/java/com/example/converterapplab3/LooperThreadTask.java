package com.example.converterapplab3;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.converterapplab3.tasks.BaseTask;

import java.util.concurrent.Callable;

public class LooperThreadTask<T> extends BaseTask<T> {
    private static Handler taskHandler;
    private static LooperThread looperThread;
    private Callable<T> callable;
    private Runnable action;

    public LooperThreadTask(Callable<T> callable) {
        this.callable = callable;

    }

    @Override
    protected void start() {
        looperThread = new LooperThread(handler -> taskHandler = handler);
        looperThread.start();

        action = () -> {
            T result = null;
            try {
                result = callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            publishSuccess(result);
        };
        taskHandler.post(action);
    }

    @Override
    protected void onCancelled() {
        if (action != null) {
            taskHandler.removeCallbacks(action);
        }
        action = null;
    }


    static class LooperThread extends Thread {
        private HandlerListener handlerListener;

        public LooperThread(HandlerListener handlerListener) {
            this.handlerListener = handlerListener;
        }

        @Override
        public void run() {
            Looper.prepare();
            Looper looper = Looper.myLooper();
            Handler handler = new Handler(looper);
            handlerListener.onHandler(handler);
            handlerListener = null;
            Looper.loop();
        }
    }

    private interface HandlerListener {
        void onHandler(Handler handler);
    }
}
