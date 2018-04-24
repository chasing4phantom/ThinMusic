package com.example.zhang.thinmusic.executor;

/**
 * Created by zhang on 2018/4/23.
 */

public interface IExecutor<T> {
    void execute();

    void onPrepare();

    void onExecuteSuccess(T t);

    void onExecuteFail(Exception e);
}
