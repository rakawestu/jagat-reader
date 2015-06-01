package com.github.rakawestu.jagatreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.model.Article;
import com.github.rakawestu.jagatreader.ui.adapter.ArticleAdapter;
import com.github.rakawestu.jagatreader.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author rakawm
 */
public class MainActivity extends AppCompatActivity{

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigationView)
    NavigationView navigationView;
    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Handler handler = new Handler();
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.title_jagat_review);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(navigationView);

        setupRecyclerView();
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(!menuItem.isChecked()) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setJagatReview();
                                }
                            }, 300);
                            break;
                        case R.id.nav_play:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setJagatPlay();
                                }
                            }, 300);
                            break;
                        case R.id.nav_oc:
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setJagatOc();
                                }
                            }, 300);
                            break;
                    }
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void setupRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new ArticleAdapter(this);
        recyclerView.setLayoutManager(manager);
        adapter.setArticles(createDummyJagatReviewData());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        startActivity(intent);
                    }
                }, 300);
            }
        }));
    }

    private void setJagatReview() {
        toolbar.setTitle(R.string.title_jagat_review);
        adapter.setArticles(createDummyJagatReviewData());
        adapter.notifyDataSetChanged();
    }

    private void setJagatPlay() {
        toolbar.setTitle(R.string.title_jagat_play);
        adapter.setArticles(createDummyJagatPlayData());
        adapter.notifyDataSetChanged();
    }

    private void setJagatOc() {
        toolbar.setTitle(R.string.title_jagat_oc);
        adapter.setArticles(createDummyJagatOcData());
        adapter.notifyDataSetChanged();
    }

    private List<Article> createDummyJagatReviewData(){
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("DOTA 2 Akan Dibagi Jadi 2 Versi?", "", "1 Juni 2015", "http://jagatplay.com/wp-content/uploads/2015/06/luna-calling-the-moon-wallp-600x338.jpg"));
        articles.add(new Article("Pendiri OnePlus: Tunggu Waktu yang Tepat untuk Meluncurkan OnePlus 2", "", "1 Juni 2015", "http://www.jagatreview.com/wp-content/uploads/2015/05/cyanogen-oneplus-500x333.jpg"));
        articles.add(new Article("Microsoft Bantah Akan Beli Silent Hills", "", "1 Juni 2015", "http://jagatplay.com/wp-content/uploads/2015/04/silent-hills1-600x338.jpg"));
        articles.add(new Article("Computex 2015 Opening: Ini Era IoT!", "", "1 Juni 2015", "http://www.jagatreview.com/wp-content/uploads/2015/06/BHS_1460-500x285.jpg"));
        articles.add(new Article("First Look Motherboard MSI 990FXA Gaming", "", "1 Juni 2015", "http://www.jagatreview.com/wp-content/uploads/2015/05/MSI_990FA_Gaming_02.jpg"));
        articles.add(new Article("Penampakan ZTE Nubia X8 dengan Layar Tanpa Bezel Muncul di Internet", "", "1 Juni 2015", "http://www.jagatreview.com/wp-content/uploads/2015/06/x8-500x284.png"));
        articles.add(new Article("Hands-on Overclocking NVIDIA GeForce GTX 980 Ti", "", "1 Juni 2015", "http://www.jagatreview.com/wp-content/uploads/2015/05/DSCF9510.jpg"));
        articles.add(new Article("Review NVIDIA GTX 980 Ti: Alternatif Kelas Atas yang Super Kencang!", "", "1 Juni 2015", "http://www.jagatreview.com/wp-content/uploads/2015/05/DSCF9506.jpg"));
        return articles;
    }

    private List<Article> createDummyJagatOcData(){
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Extreme Overclocking DDR3 di ASUS Maximus VII Impact Z97", "", "12 Mei 2015", "http://www.jagatoc.com/wp-content/uploads/2015/05/ASUS_M7i_logo-500x335.jpg"));
        articles.add(new Article("Easy OC dengan EZ Tuning Wizard pada ASUS Maximus VII Gene", "", "22 Mei 2015", "http://www.jagatoc.com/wp-content/uploads/2015/05/M7G_s2s.jpg"));
        articles.add(new Article("Extreme Overclocking MSI GTX 960 Gaming 2G: 1.8Ghz GPU Clock!", "", "5 Mei 2015", "http://www.jagatreview.com/wp-content/uploads/2015/01/DSC09520-500x293.jpg"));
        articles.add(new Article("Easy OC dengan Fitur CPU Upgrade pada Gigabyte GA-Z97X-SOC Force", "", "21 Mei 2015", "http://www.jagatoc.com/wp-content/uploads/2015/05/Result_DSCF1602-500x333.jpg"));
        articles.add(new Article("Extreme Overclocking Galax GTX 980 Hall-of-Fame (HOF)", "", "31 Desember 2014", "http://www.jagatreview.com/wp-content/uploads/2014/12/HOF_Box-500x369.jpg"));
        articles.add(new Article("Easy Overclocking Guide AMD A10-7850K Kaveri dengan Gigabyte F2A88XN-WiFi", "", "14 April 2014", "http://www.jagatreview.com/wp-content/uploads/2014/01/6ss.jpg"));
        return articles;
    }

    private List<Article> createDummyJagatPlayData() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Capcom Umumkan Monster Hunter X", "", "1 Juni 2015", "http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x-600x391.jpg"));
        articles.add(new Article("DOTA 2 Akan Dibagi Jadi 2 Versi?", "", "1 Juni 2015", "http://jagatplay.com/wp-content/uploads/2015/06/luna-calling-the-moon-wallp-600x338.jpg"));
        articles.add(new Article("Microsoft Bantah Akan Beli Silent Hills", "", "1 Juni 2015", "http://jagatplay.com/wp-content/uploads/2015/04/silent-hills1-600x338.jpg"));
        articles.add(new Article("Bloodstained Kini di Bawah Bendera Deep Silver", "", "29 Mei 2015", "http://jagatplay.com/wp-content/uploads/2015/05/bloostained-ritual-of-the-night1-600x338.jpg"));
        articles.add(new Article("Steam Summer Sale Dimulai 2 Minggu Lagi?", "", "29 Mei 2015", "http://jagatplay.com/wp-content/uploads/2015/05/summer-sale-600x336.png"));
        articles.add(new Article("Nenek Jin Kazama Bergabung di Tekken 7", "", "29 Mei 2015", "http://jagatplay.com/wp-content/uploads/2015/05/kazumi-mishima-tekken-7-1-600x338.jpg"));
        return articles;
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
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
