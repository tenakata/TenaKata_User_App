package com.tenakata.fingerprint;

import android.hardware.fingerprint.FingerprintManager;

/**
 * author @Hafijur
 */

public interface FingerprintCallBacks {

    //device doesn't support fingerprint authentication
    void onFingerprintHardwareNotDetected();

    //No fingerprint configured. Please register at least one fingerprint in your device's Settings
    void onFingerprintNotConfigured();

    //onAuthenticationError is called when a fatal error has occurred.
    // It provides the error code and error message as its parameters//
    void onAuthenticationError(int errorCode, CharSequence errString);

    //onAuthenticationFailed is called when the fingerprint
    // does not match with any of the fingerprints registered on the device//
    void onAuthenticationFailed();

    //onAuthenticationHelp is called when a non-fatal error has occurred.
    // This method provides additional information about the error,
    void onAuthenticationHelp(int helpCode, CharSequence helpString);

    //onAuthenticationSucceeded is called when a fingerprint
    // has been successfully matched to one of the fingerprints stored on the user’s device//
    void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result);

}
