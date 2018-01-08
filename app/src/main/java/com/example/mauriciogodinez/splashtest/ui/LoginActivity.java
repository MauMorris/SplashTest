package com.example.mauriciogodinez.splashtest.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauriciogodinez.splashtest.R;
import com.example.mauriciogodinez.splashtest.utils.LoginLoader;
import com.example.mauriciogodinez.splashtest.utils.PagaTodo;
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

import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<List<PagaTodo>> {

    public static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private static final int APP_REQUEST_CODE = 1;
    int PAGATODO_LOADER_ID = 1;

    private CallbackManager callbackManager;

    private Context mContext;

    private Button ingresar, ingresaTelefono, mostrarPassword;
    TextInputEditText usuario, contraseña;
    TextView ingresaMail;

    boolean mo = false;

    private static final String USGS_REQUEST_URL = "https://agentemovil.pagatodo.com/AgenteMovil.svc/agMov/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = LoginActivity.this;

        callbackManager = CallbackManager.Factory.create();

        LoginButton facebookButton = (LoginButton) findViewById(R.id.button);

        //TODO crear respuesta al lanzar servicio web con el usuario y contraseña y agregar manejo de errores en el TextInputLayout
        usuario = (TextInputEditText) findViewById(R.id.editTextUsuario);
        contraseña = (TextInputEditText) findViewById(R.id.editTextContraseña);

        mostrarPassword = (Button) findViewById(R.id.buttonMostrar);
        ingresar = (Button) findViewById(R.id.button2);

        mostrarPassword.setOnClickListener(mostrarPasswordListener(contraseña, mostrarPassword));
        ingresar.setOnClickListener(launchIngresar);

        ingresaTelefono = (Button) findViewById(R.id.button3);
        ingresaMail = (TextView) findViewById(R.id.textViewIngresaMail);

        ingresaTelefono.setFocusable(true);
        ingresaTelefono.setFocusableInTouchMode(true);
        ingresaTelefono.requestFocus();

        ingresaTelefono.setOnClickListener(loginMailPhone(mContext, "onPhoneLogin", LoginType.PHONE));
        ingresaMail.setOnClickListener(loginMailPhone(mContext, "onEmailLogin", LoginType.EMAIL));

        LoginManager.getInstance().registerCallback(callbackManager, faceResult);
        // check for an existing access token
        AccessToken mToken = AccountKit.getCurrentAccessToken();
        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();

        if (mToken != null || loginToken != null) {
            // if previously logged in, proceed to the account activity
            launchPromocionesActivity();
        }

    }

    public View.OnClickListener loginMailPhone(final Context context, final String eventName, final LoginType loginType) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppEventsLogger logger = AppEventsLogger.newLogger(context);

                logger.logEvent(eventName);
                onLogin(loginType);
            }
        };
    }

    FacebookCallback<LoginResult> faceResult = new FacebookCallback<LoginResult>() {
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
    };

    public View.OnClickListener mostrarPasswordListener(final TextInputEditText contraseñaInput, final Button mostrarOcultar) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMo();
                if (mo) {
                    contraseñaInput.setTransformationMethod(null);
                    mostrarOcultar.setText(getString(R.string.ocultar));
                } else {
                    contraseñaInput.setTransformationMethod(new PasswordTransformationMethod());
                    mostrarOcultar.setText(getString(R.string.mostrar));
                }
            }
        };
    }

    View.OnClickListener launchIngresar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(1 == 0){
                initLoaderPagatodo();
            } else
                launchPromocionesActivity();
        }
    };

    private void initLoaderPagatodo(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

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
    public Loader<List<PagaTodo>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new LoginLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<PagaTodo>> loader, List<PagaTodo> data) {
        if (data != null && !data.isEmpty()) {
            launchPromocionesActivity();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<PagaTodo>> loader) {

    }
}