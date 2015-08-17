package com.github.rakawestu.jagatreader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.model.Article;

import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author rakawm
 */
public class DetailActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.article_detail)
    WebView webView;
    private Handler handler = new Handler();
    private Article article;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.inject(this);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        article = (Article) getIntent().getSerializableExtra("article");
        final String data = article.getContent()
                .replaceAll("width=\"[0-9]{1,}\"", "width=\"100%\"")
                .replaceAll("width: [0-9]{1,}px", "width: 100%")
                .replaceAll("height=\"[0-9]{1,}\"","height=auto")
                .replaceAll("width: [0-9]{1,}px", "height: auto");
        toolbar.setTitle("");
        String css = "<html><head><style type=\"text/css\">@font-face {font-family: ubuntu;src: url(\"file:///android_asset/Ubuntu-R.ttf\")}body {font-family: ubuntu;}</style></head><body>";
        String header = String.format("<h4 style=\"color:#00BFFF\">%s oleh %s</h4><h1 style=\"color:#00BFFF\">%s</h1>", article.getFormattedDateTime(), article.getCreator(), article.getTitle());
        final String dataContent = css + header + data + "</body></html>";
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDetails(dataContent);
            }
        }, 300);
    }

    private void loadDetails(String data){
        webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                //TODO: Share Article
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
        return true;
    }

    private boolean copyFile(Context context, String fileName) {
        boolean status = false;
        try {
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            InputStream in = context.getAssets().open(fileName);
            // Transfer bytes from the input file to the output file
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Close the streams
            out.close();
            in.close();
            status = true;
        } catch (Exception e) {
            System.out.println("Exception in copyFile:: " + e.getMessage());
            status = false;
        }
        System.out.println("copyFile Status:: " + status);
        return status;
    }

}
