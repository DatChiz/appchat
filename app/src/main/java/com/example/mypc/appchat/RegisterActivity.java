package com.example.mypc.appchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
//    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private Socket mSocket = MyApplication.getInstance().getSocket();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mSocket.connect();

        inputFullName = (EditText) findViewById(R.id.name);
//        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mSocket.on("register", onRegister);



        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
//                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !password.isEmpty()) {
                    registerUser(name, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void registerUser(final String name, final String password) {
        mSocket.emit("register", name, password);

        pDialog.setMessage("Registering ...");
        showDialog();
    }

    private Emitter.Listener onRegister = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String data =  args[0].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(data == "true"){

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,
                                "Tài khoản này đã tồn tại rồi nhé!!", Toast.LENGTH_LONG).show();
                        Log.d("error", "cant register");
                    }

                    hideDialog();
                }
            });
        }
    };

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }
    }
}
