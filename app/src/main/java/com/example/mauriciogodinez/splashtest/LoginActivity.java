package com.example.mauriciogodinez.splashtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<List<PagaTodo>> {

    private static final int APP_REQUEST_CODE = 1;
    CallbackManager callbackManager;
    boolean mo = false;
    int PAGATODO_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "https://agentemovil.pagatodo.com/AgenteMovil.svc/agMov/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        LoginButton facebookButton = (LoginButton) findViewById(R.id.button);

        TextInputEditText usuario = (TextInputEditText) findViewById(R.id.editTextUsuario);
        final TextInputEditText contraseña = (TextInputEditText) findViewById(R.id.editTextContraseña);

        final Button mostrar = (Button) findViewById(R.id.buttonMostrar);

        Button ingresar = (Button) findViewById(R.id.button2);
        Button ingresaTelefono = (Button) findViewById(R.id.button3);
        TextView ingresaMail = (TextView) findViewById(R.id.textViewIngresaMail);

        ingresaTelefono.setFocusable(true);
        ingresaTelefono.setFocusableInTouchMode(true);
        ingresaTelefono.requestFocus();

        ingresaTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppEventsLogger logger = AppEventsLogger.newLogger(LoginActivity.this);
                logger.logEvent("onPhoneLogin");

                onLogin(LoginType.PHONE);

            }
        });

        ingresaMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppEventsLogger logger = AppEventsLogger.newLogger(LoginActivity.this);
                logger.logEvent("onEmailLogin");

                onLogin(LoginType.EMAIL);

            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), loginResult.toString() + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
                launchPromocionesActivity();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMo();
                if (mo) {
                    contraseña.setTransformationMethod(null);
                    mostrar.setText(getString(R.string.ocultar));
                } else {
                    contraseña.setTransformationMethod(new PasswordTransformationMethod());
                    mostrar.setText(getString(R.string.mostrar));
                }
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPromocionesActivity();
            }
        });

        // check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();
        if (accessToken != null || loginToken != null) {
            // if previously logged in, proceed to the account activity
            launchPromocionesActivity();
        }

/*
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.mauriciogodinez.splashtest",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(PAGATODO_LOADER_ID, null, this);
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // For Account Kit, confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                // display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null) {
                // on successful login, proceed to the account activity
                launchPromocionesActivity();
            }
        }

    }

    private void launchPromocionesActivity() {
        Intent intent = new Intent(this, PromocionesActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLogin(final LoginType loginType) {
        // create intent for the Account Kit activity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        // configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );
        final AccountKitConfiguration configuration = configurationBuilder.build();

        // launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }


    private void changeMo() {
        mo = !mo;
    }


    @Override
    public android.content.Loader<List<PagaTodo>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new LoginLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<PagaTodo>> loader, List<PagaTodo> data) {
        if (data != null && !data.isEmpty()) {
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<PagaTodo>> loader) {

    }

}
