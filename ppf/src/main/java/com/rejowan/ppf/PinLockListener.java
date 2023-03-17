package com.rejowan.ppf;


public interface PinLockListener {


    void onComplete(String pin);


    void onEmpty();


    void onPinChange(int pinLength, String intermediatePin);
}
