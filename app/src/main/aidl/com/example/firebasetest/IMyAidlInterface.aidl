// IMyAidlInterface.aidl
package com.example.firebasetest;

// Declare any non-default types here with import statements
interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    int getProcessNumber();

    void getRect(in Bundle bundle);

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
