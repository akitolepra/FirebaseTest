// TestAidlInterface.aidl
package com.example.firebasetest;

// Declare any non-default types here with import statements

interface TestAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    String getHello();

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
