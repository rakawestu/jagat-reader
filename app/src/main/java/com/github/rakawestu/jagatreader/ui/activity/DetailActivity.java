package com.github.rakawestu.jagatreader.ui.activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.app.Constant;
import com.github.rakawestu.jagatreader.app.JagatApp;
import com.github.rakawestu.jagatreader.model.Article;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * @author rakawm
 */
public class DetailActivity extends AppCompatActivity {

    private static final String NEWS_DETAIL = "News Detail";
    private static final String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
    private static final String EXTRA_CUSTOM_TABS_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.article_detail)
    WebView webView;
    private Handler handler = new Handler();
    private Article article;
    private Tracker mTracker;

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private String mPackageNameToBind;

    private static class NavigationCallback extends CustomTabsCallback {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            Timber.w("onNavigationEvent: Code = " + navigationEvent);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.inject(this);
        // Obtain the shared Tracker instance.
        JagatApp application = (JagatApp) getApplication();
        mTracker = application.getDefaultTracker();

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        article = (Article) getIntent().getSerializableExtra("article");
        final String data = article.getContent()
                .replaceAll("width=\"[0-9]{1,}\"", "width=\"100%\"")
                .replaceAll("width: [0-9]{1,}px", "width: 100%")
                .replaceAll("height=\"[0-9]{1,}\"","height=auto")
                .replaceAll("width: [0-9]{1,}px", "height: auto");
        toolbar.setTitle("");
        String css = "<html><head><style type=\"text/css\">@font-face {font-family: ubuntu;src: url(\"file:///android_asset/Ubuntu-R.ttf\")}body {font-family: ubuntu; font-size: larger; word-spacing: 2px; letter-spacing:1.125px}</style></head><body>";
        String header = String.format("<h6 style=\"color:#00BFFF\">%s | %s | oleh %s</h6><h2 style=\"color:#00BFFF\">%s</h2>", article.getCategory(), article.getFormattedDateTime(), article.getCreator(), article.getTitle());
        final String dataContent = css + header + data + "</body></html>";
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(Constant.CATEGORY_NEWS)
                        .setAction(Constant.ACTION_READ)
                        .setLabel(article.getTitle())
                        .build());
                loadDetails(dataContent);
            }
        }, 300);
    }

    private void loadDetails(String data){
        webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("Setting screen name: " + NEWS_DETAIL);
        mTracker.setScreenName(NEWS_DETAIL);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                return true;
            case R.id.action_show_web:
                openWeb();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        article = (Article) getIntent().getSerializableExtra("article");
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, article.getUrl())
                .putExtra(Intent.EXTRA_TITLE, article.getTitle())
                .putExtra(Intent.EXTRA_SUBJECT, article.getTitle())
                .setType("text/plain");
        shareAction.setShareIntent(shareIntent); //crashes here, shareAction is null
        shareAction.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(Constant.CATEGORY_NEWS)
                        .setAction(Constant.ACTION_SHARE)
                        .setLabel(article.getTitle())
                        .build());
                return false;
            }
        });
        return true;
    }

    private CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new NavigationCallback());
        }
        return mCustomTabsSession;
    }

    private void openWeb() {

        // Using a VIEW intent for compatibility with any other browsers on device.
        // Caller should not be setting FLAG_ACTIVITY_NEW_TASK or
        // FLAG_ACTIVITY_NEW_DOCUMENT.
        String url = article.getUrl();

        //  Must have. Extra used to match the session. Its value is an IBinder passed
        //  whilst creating a news session. See newSession() below. Even if the service is not
        //  used and there is no valid session id to be provided, this extra has to be present
        //  with a null value to launch a custom tab.

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());

        builder.setToolbarColor(getResources().getColor(android.R.color.black)).setShowTitle(true);
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_close_white_24dp));
        //builder.setStartAnimations(this, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
        //builder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
