package com.example.a16022774.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> al;
    ArrayAdapter<String> aa;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);
        al = new ArrayList<String>();
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean loggedIn = prefs.getBoolean("loggedIn", false);
        Log.i("Logged In", loggedIn + "");
        if(loggedIn == false){
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout accessCode =
                    (LinearLayout) inflater.inflate(R.layout.accesscode, null);
            final EditText etAccessCode = (EditText) accessCode
                    .findViewById(R.id.etAccessCode);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please login")
                    .setView(accessCode)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String code = etAccessCode.getText().toString();
                            if (code.equalsIgnoreCase("738964")) {
                                Toast.makeText(MainActivity.this, "Correct code", Toast.LENGTH_SHORT).show();

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor prefEdit = prefs.edit();
                                prefEdit.putBoolean("loggedIn", true);
                                prefEdit.commit();
                            } else {
                                Toast.makeText(MainActivity.this, "Incorrect code", Toast.LENGTH_SHORT).show();
                                //finish();
                                recreate();
                            }
                        }
                    }).setNegativeButton("NO ACCESS CODE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Access code is 738964", Toast.LENGTH_SHORT).show();
                    recreate();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }else{

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sendToFriend) {
            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jason_lim@rp.edu.sg"});
                                email.putExtra(Intent.EXTRA_TEXT, "");
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email, "Choose an Email Client : "));

                                Snackbar.make(getWindow().getDecorView().getRootView(), "Email Sent", Snackbar.LENGTH_SHORT).show();

                            } else {
                                try {
                                    String phoneNo = "";
                                    String msg = "";
                                    SmsManager smsManager = SmsManager.getDefault();
                                    //smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                                    Snackbar.make(getWindow().getDecorView().getRootView(), "SMS Sent", Snackbar.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage().toString(),
                                            Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.quiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            final RadioGroup rg1 = (RadioGroup) quiz.findViewById(R.id.rgNationalDay);

            final RadioGroup rg2 = (RadioGroup) quiz.findViewById(R.id.rgSGAge);

            final RadioGroup rg3 = (RadioGroup) quiz.findViewById(R.id.rgTheme);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(quiz)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            if(rg1.getCheckedRadioButtonId() == R.id.rbNo){
                                score++;
                            }

                            if(rg2.getCheckedRadioButtonId() == R.id.rbYes2){
                                score++;
                            }

                            if(rg3.getCheckedRadioButtonId() == R.id.rbYes3){
                                score++;
                            }

                            Toast.makeText(MainActivity.this, "Your score is " + score, Toast.LENGTH_LONG).show();

                            score = 0;

                            al.clear();

                            al.add("Singapore National Day is on 9 Aug");
                            al.add("Singapore is 53 years old");
                            al.add("Theme is 'We Are Singapore'");

                            aa.notifyDataSetChanged();
                        }
                    }).setNegativeButton("DON'T KNOW LAH", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")// Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            System.exit(0);
                        }
                    }).setNegativeButton("NOT REALLY", null);

            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
