package com.sockett.common;

public interface Observable<T extends Object> {
    void registerObserver(T o);
    void removeObserver(T o);
    void notifyObserver();
}

