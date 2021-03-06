package com.example.mauriciogodinez.splashtest.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mauriciogodinez.splashtest.R;

public class DetallePromocionActivity extends AppCompatActivity implements View.OnClickListener {

    Toast mToast;

    FloatingActionButton fab_share;
    FloatingActionButton fab_face, fab_twitter, fab_whats, fab_mail;

    CardView cv_face, cv_twitter, cv_whats, cv_mail;

    View fuse_view;

    Animation show_button, hide_button;
    Animation show_card, hide_card;
    Animation rotate_view, rotate2_view;
    Animation show_fab, erase_fab;

    private static final int UI_ANIMATION_DELAY = 120;
    private Context mContext;

    private final Handler mShowHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_promocion);

        mContext = DetallePromocionActivity.this;

        Intent getIntent = getIntent();
        Integer drawable = getIntent.getIntExtra("imagen", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        setSupportActionBar(toolbar);

        ImageView iv = (ImageView) findViewById(R.id.app_bar_image_view);
        iv.setImageResource(drawable);

        fuse_view = findViewById(R.id.fuse_view);

        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);

        fab_face = (FloatingActionButton) findViewById(R.id.fab_facebook);
        fab_twitter = (FloatingActionButton) findViewById(R.id.fab_twitter);
        fab_whats = (FloatingActionButton) findViewById(R.id.fab_whats_app);
        fab_mail = (FloatingActionButton) findViewById(R.id.fab_mail);

        cv_face = (CardView) findViewById(R.id.cv_facebook);
        cv_twitter = (CardView) findViewById(R.id.cv_twitter);
        cv_whats = (CardView) findViewById(R.id.cv_whats);
        cv_mail = (CardView) findViewById(R.id.cv_mail);

        setVisibility(View.GONE);

        show_button = AnimationUtils.loadAnimation(mContext, R.anim.show_button);
        hide_button = AnimationUtils.loadAnimation(mContext, R.anim.hide_button);

        show_card = AnimationUtils.loadAnimation(mContext, R.anim.show_card);
        hide_card = AnimationUtils.loadAnimation(mContext, R.anim.hide_card);

        rotate_view = AnimationUtils.loadAnimation(mContext, R.anim.rotate_view);
        rotate2_view = AnimationUtils.loadAnimation(mContext, R.anim.rotate_2_view);

        erase_fab = AnimationUtils.loadAnimation(mContext, R.anim.erase_fab);
        show_fab = AnimationUtils.loadAnimation(mContext, R.anim.show_fab);

        fab_face.setOnClickListener(this);
        fab_twitter.setOnClickListener(this);
        fab_whats.setOnClickListener(this);
        fab_mail.setOnClickListener(this);
        cv_face.setOnClickListener(this);
        cv_twitter.setOnClickListener(this);
        cv_whats.setOnClickListener(this);
        cv_mail.setOnClickListener(this);

        fuse_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
                hideAnim();
            }
        });

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab_face.getVisibility() == View.VISIBLE) {
                    setVisibility(View.GONE);
                    hideAnim();
                } else {
                    setVisibility(View.VISIBLE);
                    showAnim();
                }
            }
        });

        fab_share.startAnimation(show_fab);
    }

    private void setVisibility(int visibility){
        fab_face.setVisibility(visibility);
        fab_twitter.setVisibility(visibility);
        fab_whats.setVisibility(visibility);
        fab_mail.setVisibility(visibility);

        fuse_view.setVisibility(visibility);

        cv_face.setVisibility(visibility);
        cv_twitter.setVisibility(visibility);
        cv_whats.setVisibility(visibility);
        cv_mail.setVisibility(visibility);
    }

    private void showAnim() {
        fab_face.startAnimation(show_button);
        fab_twitter.startAnimation(show_button);
        fab_whats.startAnimation(show_button);
        fab_mail.startAnimation(show_button);

        cv_face.startAnimation(show_card);
        cv_twitter.startAnimation(show_card);
        cv_whats.startAnimation(show_card);
        cv_mail.startAnimation(show_card);

        fab_share.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_add_white));
        fab_share.startAnimation(rotate_view);
    }

    private void hideAnim() {
        fab_face.startAnimation(hide_button);
        fab_twitter.startAnimation(hide_button);
        fab_whats.startAnimation(hide_button);
        fab_mail.startAnimation(hide_button);
        fab_share.startAnimation(rotate2_view);

        cv_face.startAnimation(hide_card);
        cv_twitter.startAnimation(hide_card);
        cv_whats.startAnimation(hide_card);
        cv_mail.startAnimation(hide_card);

        mShowHandler.removeCallbacks(mIntentRunnable);
        mShowHandler.postDelayed(mIntentRunnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mIntentRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            fab_share.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.iconocompartir));
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_promocion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(mToast != null){
            mToast.cancel();
        }
        Integer id = v.getId();
        switch (id) {
            case R.id.cv_facebook:
                mToast = Toast.makeText(mContext, "face", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            case R.id.fab_facebook:
                mToast = Toast.makeText(mContext, "face", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            case R.id.cv_twitter:
                mToast = Toast.makeText(mContext, "tuit", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            case R.id.fab_twitter:
                mToast = Toast.makeText(mContext, "tuit", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            case R.id.cv_whats:
                mToast = Toast.makeText(mContext, "whats", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            case R.id.fab_whats_app:
                mToast = Toast.makeText(mContext, "whats", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            case R.id.cv_mail:
                mToast = Toast.makeText(mContext, "mail", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            case R.id.fab_mail:
                mToast = Toast.makeText(mContext, "mail", Toast.LENGTH_SHORT);
                mToast.show();
                break;
            default:
                break;
        }
    }
}