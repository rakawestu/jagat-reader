package com.github.rakawestu.jagatreader.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.webkit.WebView;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.model.Article;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.inject(this);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        article = (Article) getIntent().getSerializableExtra("article");
        final String data = article.getContent()
                .replaceAll("width=\"[0-9]{1,}\"", "width=\"100%\"")
                .replaceAll("width: [0-9]{1,}px", "width: 100%")
                .replaceAll("height=\"[0-9]{1,}\"","height=auto")
                .replaceAll("width: [0-9]{1,}px", "height: auto");
        toolbar.setTitle(article.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDetails(data);
            }
        }, 300);
    }

    private void loadDetails(String data){
        webView.loadData(data, null, "utf-8");
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
        }
        return super.onOptionsItemSelected(item);
    }
}
