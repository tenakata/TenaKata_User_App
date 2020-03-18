package com.tenakata.fingerprint;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tenakata.BuildConfig;
import com.tenakata.fingerprint.FingerprintCallBacks;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;

/**
 * author @Hafijur
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintAuthManager implements PermissionListener {

    private static final String KEY_NAME = BuildConfig.APPLICATION_ID;
    private Context context;
    private FingerprintManager fingerprintManager;
    private FingerprintCallBacks callBacks;
    private KeyStore keyStore;
    private Cipher cipher;

    public FingerprintAuthManager(Context context) {
        this.context = context;
        if(context instanceof FingerprintCallBacks)
            callBacks = (FingerprintCallBacks)context;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public static boolean isFingerprintSupported(Context context){
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
        return fingerprintManager!=null && fingerprintManager.isHardwareDetected();
    }

    public void setCallBacks(FingerprintCallBacks callBacks){
        this.callBacks = callBacks;
    }

    public void initialize(){

        fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

        //Check whether the device has a fingerprint sensor
        if (fingerprintManager==null || !fingerprintManager.isHardwareDetected()) {

            // If a fingerprint sensor isn’t available, then inform the application.
            if(callBacks!=null)callBacks.onFingerprintHardwareNotDetected();

        }else { //request permissions to use fingerprint

            TedPermission.with(context)
                    .setPermissionListener(this)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                            "Please turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.USE_FINGERPRINT)
                    .check();
        }
    }

    @Override
    public void onPermissionGranted() {
        //user allowed the app to use fingerprint authentication.
        //check for saved fingerprint(s)
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            //if user have not saved any fingerprint, notify app.
            if(callBacks!=null)callBacks.onFingerprintNotConfigured();
        }else {
            //generate key and handle authentication
            try{
                generateKey();
            } catch (FingerprintException e) {
                e.printStackTrace();
            }

            if (initCipher()) {
                //If the cipher is initialized successfully, then create a CryptoObject instance//
                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);

                // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                FingerprintHandler helper = new FingerprintHandler(context);
                helper.setCallBacks(callBacks);
                helper.startAuth(fingerprintManager, cryptoObject);
            }
        }
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        //user doesn't allowed the app to use fingerprint authentication.
    }

    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    private boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        FingerprintException(Exception e) {
            super(e);
        }
    }
}

