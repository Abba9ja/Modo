package com.abba9ja.modo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AppSuggestions extends AppCompatActivity {

    Button btnSubmit;
    EditText etDescription, etSubject;
    CheckBox chk1, chk2, chk3, chk4, chk5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_suggestions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(getString(R.string.app_suggestion));
        initializeVar();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = etSubject.getText().toString();
                String description = etDescription.getText().toString();

                String mailSubject = "Modo app suggestion on " + subject;
                String chkString, chkString2, chkString3, chkString4, chkString5;
                chkString = "";
                chkString2 = "";
                chkString3 = "";
                chkString4 = "";
                chkString5 = "";
                if (description.length() >0){
                    if (chk1.isChecked()){
                        chkString = getString(R.string.loading_slow);
                    }
                    if(chk3.isChecked()){
                        chkString3 = getString(R.string.need_more_cate);
                    }
                    if (chk4.isChecked()){
                        chkString4 = getString(R.string.problem_sharing_app_suggestion);
                    }
                    if (chk5.isChecked()) {
                        chkString5 += getString(R.string.app_crashing);
                    }

                    String emailAddress = getString(R.string.dev_email_address);
                    String message = chkString + chkString2 + chkString3 + chkString4 + chkString5 + description;
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "abba9ja@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(Intent.createChooser(intent, getString(R.string.send_app_mail_lbl)));
                    }else{
                        Toast.makeText(AppSuggestions.this, R.string.not_email_app, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AppSuggestions.this, R.string.descriptio_required, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void initializeVar() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etSubject = (EditText) findViewById(R.id.subject);
        chk1 = (CheckBox) findViewById(R.id.loadinSlow);
        chk3 = (CheckBox) findViewById(R.id.needMore);
        chk4 = (CheckBox) findViewById(R.id.prblmShare);
        chk5 = (CheckBox) findViewById(R.id.appshutdown);

    }
}
