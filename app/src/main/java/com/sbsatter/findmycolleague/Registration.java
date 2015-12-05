package com.sbsatter.findmycolleague;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Registration extends AppCompatActivity {

    @Bind(R.id.loginButton)Button loginButton;
    @Bind(R.id.registerButton)Button registerButton;
 //   private Button loginButton;
    private CheckBox showPassword;
    private EditText username;
    private EditText pin;
    private EditText password;
    private EditText confirmPassword;
    private EditText yourEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
     //   loginButton=(Button) findViewById(R.id.loginButton);
        showPassword=(CheckBox) findViewById(R.id.content_registration_showPasswordCheckbox);
        password=(EditText) findViewById(R.id.content_registration_password);
        username=(EditText) findViewById(R.id.content_login_username);
        pin=(EditText) findViewById(R.id.content_login_password);
        confirmPassword=(EditText) findViewById(R.id.content_registration_confirmPassword);

//        username.addTextChangedListener(new GenericTextWatcher(username));
//        pin.addTextChangedListener(new GenericTextWatcher(pin));
//        password.addTextChangedListener(new GenericTextWatcher(password));
//        confirmPassword.addTextChangedListener(new GenericTextWatcher(confirmPassword));

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });

        pin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }else{
                    yourEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pin.length()!=8){
                    Toast.makeText(Registration.this, "Invalid PIN, must be 8 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                } else {
                    Toast.makeText(Registration.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmPassword.setText("");
                }
            }
        });




//        username.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, Login.class);
                startActivity(i);
            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    password.setInputType(129);
                    confirmPassword.setInputType(129);
                }
            }
        });






//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });




    }

    @OnClick(R.id.loginButton)
    public void submit(){
        Intent i = new Intent(Registration.this, Login.class);
        startActivity(i);
    }

    public void register(View view) {
        if(password.getText().toString().equals( confirmPassword.getText().toString())){

        }
        else{
            Toast.makeText(Registration.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
            password.setText("");
            confirmPassword.setText("");
        }
    }


//    private class GenericTextWatcher implements TextWatcher{
//
//        private View view;
//        private GenericTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//        public void afterTextChanged(Editable editable) {
//            String text = editable.toString();
//            switch(view.getId()){
//                case R.id.content_registration_username:
//                 //   model.setName(text);
//                    break;
//                case R.id.content_registration_pin:
//                //    model.setEmail(text);
//                    break;
//                case R.id.content_registration_password:
//                //    model.setPhone(text);
//
//                    break;
////                case R.id.content_registration_confirmPassword:
////                    //    model.setPhone(text);
////                 //   if((password.getText().toString()!=text)){
////                        Toast.makeText(Registration.this,"Password does not match",Toast.LENGTH_SHORT).show();
////                      //  password.setText("l");
////                     //   confirmPassword.setText("");
////               //     }
////                    break;
//            }
//        }
//    }

}
