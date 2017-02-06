package vn.shinee.adssimple;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Apploader extends Application {
    public static Context context;
    private static Toast toast = null;
    private static InterstitialAd interstitialAd;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();
    }

    public static void showInterstitialAds() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
        requestNewInterstitial();
    }

    private static void requestNewInterstitial() {
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(BuildConfig.DEBUG ? getAdmodDeviceId(Apploader.context) : null)
                .build());
    }

    public static void Toast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    public static void Toast(int msgId) {
        Toast(context.getString(msgId));
    }
    public static String getAdmodDeviceId(Context context) {
        try {
            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(android_id.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
