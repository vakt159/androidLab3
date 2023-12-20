package com.example.converterapplab3.tasks;

public interface Task<T> {

    void execute(TaskListener<T> taskListener);
    void cancel();
}
