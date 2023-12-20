package com.example.converterapplab3.tasks;

public interface TaskListener<T> {
    void onSuccess(T result);
    void onError();
}