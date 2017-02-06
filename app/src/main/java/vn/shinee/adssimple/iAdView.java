package vn.shinee.adssimple;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class iAdView extends FrameLayout {
    private AdSize adSize;
    private String adUnitId;
    private final AdSize[] sAdSize = new AdSize[]{
            AdSize.BANNER,
            AdSize.FULL_BANNER,
            AdSize.LARGE_BANNER,
            AdSize.LEADERBOARD,
            AdSize.MEDIUM_RECTANGLE,
            AdSize.SMART_BANNER,
            AdSize.WIDE_SKYSCRAPER};
    private AdView adView;

    public iAdView(Context context) {
        this(context, null);
    }

    public iAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.iAdView, 0, 0);
        int adSize = a.getInt(R.styleable.iAdView_iAdSize, 0);
        String adUnitId = a.getString(R.styleable.iAdView_iAdUnitId);
        initAdView(0, adSize, adUnitId);
    }

    private void initAdView(int id, int adSize, String adUnitId) {
        initAdView(id, sAdSize[adSize], adUnitId);
    }
    public void initAdView(AdSize adSize, String adUnitId) {
        initAdView(0, adSize, adUnitId);
    }
    public void initAdView(AdSize adSize, int adUnitId) {
        initAdView( adSize, Apploader.context.getString(adUnitId));
    }
    private void initAdView(int id, AdSize adSize, String adUnitId) {
        if (adSize != null && adUnitId != null) {
            adView = new AdView(Apploader.context);
            this.adSize = adSize;
            this.adUnitId = adUnitId;
            if (id != 0) adView.setId(id);
            adView.setAdUnitId(adUnitId);
            adView.setAdSize(adSize);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(adView, params);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(BuildConfig.DEBUG ? Apploader.getAdmodDeviceId(Apploader.context) : null)
                    .build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    setVisibility(GONE);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    setVisibility(VISIBLE);
                }
            });
        }else{
            setVisibility(GONE);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (adView != null) {
            int id = adView.getId();
            removeAllViews();
            initAdView(id, adSize, adUnitId);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        if (adView != null) {
            adView.destroy();
            adView = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (adView == null) {
            initAdView(0, adSize, adUnitId);
        }
    }
}