package ru.haknazarovfarkhod.advertisementwebview;

import android.Manifest;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.haknazarovfarkhod.advertisementwebview.dao.PostRequest;
import ru.haknazarovfarkhod.advertisementwebview.dialogs.SimpleDialog;
import ru.haknazarovfarkhod.advertisementwebview.services.WebService;

public class MainActivity extends AppCompatActivity implements SimpleDialog.SimpleDialogListener {
    private WebView mWebView;
    private Context context;
    private Integer REQUEST_READ_PHONE_STATE = 1;
    private ProgressBar progressBar;
    private static final TimeInterpolator GAUGE_ANIMATION_INTERPOLATOR = new DecelerateInterpolator(2);
    private static final int MAX_LEVEL = 100;
    private static final long GAUGE_ANIMATION_DURATION = 5000;
    private MainAsyncTask mainAsyncTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        int permissionCheck = context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        mainAsyncTask = new MainAsyncTask();
        mainAsyncTask.execute(imsi);
    }

    private void showSimpleDialog(String message) {
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.setDialogHeader("Some header!");
        simpleDialog.setDialogMessage(message);
        simpleDialog.show(getFragmentManager(), "SimpleDialog");
    }

    @Override
    public void onDialogClosed() {
        this.finish();
    }

    class MainAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
            mWebView.setVisibility(ProgressBar.INVISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                TimeUnit.SECONDS.sleep(3);
                String imsi = strings[0];

                WebService webService = new WebService();

                webService.getApi().getData(imsi).enqueue(new Callback<PostRequest>() {
                    @Override
                    public void onResponse(Call<PostRequest> call, Response<PostRequest> response) {
                        if (!(response.body() == null)) {
                            String status = response.body().getStatus();
                            if (status.equals("OK")) {
                                String url = response.body().getUrl();
                                mWebView.loadUrl(url);
                            } else {
                                String message = response.body().getMessage();
                                showSimpleDialog(message);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostRequest> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Failue " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            mWebView.setVisibility(ProgressBar.VISIBLE);
        }
    }
}

