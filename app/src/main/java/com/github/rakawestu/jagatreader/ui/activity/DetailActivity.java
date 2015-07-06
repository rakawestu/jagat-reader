package com.github.rakawestu.jagatreader.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.webkit.WebView;

import com.github.rakawestu.jagatreader.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.inject(this);

        toolbar.setTitle("Capcom Umumkan Monster Hunter X");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDetails();
            }
        }, 300);
    }

    private void loadDetails(){
        String data = "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Capcom Umumkan Monster Hunter X</h1>\n" +
                        "<p>By Pladidus Santoso June 1, 2015</p>" +
                "<img class=\"attachment-thumbnail wp-post-image\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x7-150x150.jpg\" alt=\"monster hunter x7\" width=\"150\" height=\"150\" style='width:100%;'/>\n" +
                "<iframe src=\"https://www.youtube.com/embed/vz7Ge7SJOX8\" width=\"560\" height=\"315\" frameborder=\"0\" allowfullscreen=\"allowfullscreen\" style='width:100%;'></iframe>\n" +
                "Seperti halnya sebuah bisnis pada umumnya, publisher game juga akan selalu memaksimalkan potensi franchisenya yang memang sudah terbukti jelas, sukses di pasaran secara finansial. Tidak terkecuali <strong>Capcom</strong>. Selain mengkonfirmasikan lebih banyak proses reboot game-game klasik mereka, termasuk <strong>Resident Evil Zero HD Remaster</strong> yang belum lama diumumkan, Capcom juga berjanji akan merilis banyak game baru di masa depan. Tidak ada lagi pilihan lebih rasional selain meluncurkan lebih banyak seri Monster Hunter yang laris bak kacang goreng di beragam pasar, baik di Jepang ataupun di luar Jepang. Benar saja, Capcom akhirnya mengumumkan sang seri terbaru –<strong> Monster Hunter X!</strong>\n" +
                " \n" +
                " <strong>Monster Hunter X</strong> – dibaca dengan <strong>Monster Hunter Cross</strong> – akhirnya diperkenalkan lewat event Nintendo Direct Jepang belum lama ini. Seperti halnya Monster Hunter 4: Ultimate, Monster Hunter X akan dirilis secara eksklusif untuk Nintendo 3DS. Sebuah trailer perdana dirilis, memperlihatkan beragam hal baru yang ditawarkan, dari variasi senjata, monster, dan moveset yang kian keren. Dunia yang dibangun juga terlihat indah untuk ukuran sebuah game 3DS, dengan variasi aksi yang juga terasa lebih cepat dibandingkan seri Monster Hunter sebelumnya.\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49879\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x-600x391.jpg\" alt=\"monster hunter x\" width=\"600\" height=\"391\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49878\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x1-600x360.jpg\" alt=\"monster hunter x1\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49877\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x2-600x360.jpg\" alt=\"monster hunter x2\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49876\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x3-600x360.jpg\" alt=\"monster hunter x3\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49875\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x4-600x360.jpg\" alt=\"monster hunter x4\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49874\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x5-600x360.jpg\" alt=\"monster hunter x5\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49873\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x6-600x360.jpg\" alt=\"monster hunter x6\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49872\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x7-600x360.jpg\" alt=\"monster hunter x7\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49871\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x8-600x360.jpg\" alt=\"monster hunter x8\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49870\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x9-600x360.jpg\" alt=\"monster hunter x9\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49869\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x10-600x360.jpg\" alt=\"monster hunter x10\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49868\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x11-600x360.jpg\" alt=\"monster hunter x11\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49867\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x12-600x360.jpg\" alt=\"monster hunter x12\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <img class=\"aligncenter size-medium wp-image-49866\" src=\"http://jagatplay.com/wp-content/uploads/2015/06/monster-hunter-x13-600x360.jpg\" alt=\"monster hunter x13\" width=\"600\" height=\"360\" style='width:100%;'/>\n" +
                " \n" +
                " <strong>Monster Hunter X akan dirilis di pasar Jepang</strong> di musim dingin tahun 2015 ini. Belum ada kejelasan soal kapan rilis versi Barat, walaupun bisa dipastikan, potensi pasar akan selalu membawanya ke region tersebut. <em>Still no Wii U version, really?</em>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        webView.loadData(data, null, null);
    }
}
