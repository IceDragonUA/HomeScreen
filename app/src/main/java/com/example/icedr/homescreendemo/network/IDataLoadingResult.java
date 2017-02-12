package com.example.icedr.homescreendemo.network;


public interface IDataLoadingResult<T> {

    void onResult(T result);

    void onFailure(Throwable ex);

}
