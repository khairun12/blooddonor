package com.peoplentech.devkh.blooddonor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.peoplentech.devkh.blooddonor.model.ResObj;
import com.peoplentech.devkh.blooddonor.model.User;
import com.peoplentech.devkh.blooddonor.remote.ApiUtils;
import com.peoplentech.devkh.blooddonor.remote.UserService;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

        String[] bloodNames={"A+","A-","B+","B-","O+","O-","AB+","AB-"};
        private EditText editTextName, editTextEmail, editTextPassword,editTextAddress, editTextNumber;
        private EditText editTextBirth;
        private RadioGroup radioGender, radioStatus;

        private int mYear, mMonth, mDate;

        private String bloodChoose;

    @Override
        protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            Button buttonSignUp = (Button) findViewById(R.id.register);

            editTextName = (EditText) findViewById(R.id.userName);
            editTextEmail = (EditText) findViewById(R.id.userRegEmail);
            editTextPassword = (EditText) findViewById(R.id.userRegPassword);
            editTextBirth = (EditText) findViewById(R.id.userAge);
            editTextAddress = (EditText) findViewById(R.id.userArea);
            editTextNumber = (EditText) findViewById(R.id.userPhone);


            radioGender = (RadioGroup) findViewById(R.id.radioGender);
            radioStatus = (RadioGroup) findViewById(R.id.radioInterest);

            editTextBirth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDate = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    editTextBirth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                                }
                            }, mYear, mMonth, mDate);
                    datePickerDialog.show();
                }
            });

            Spinner spin = (Spinner) findViewById(R.id.bloodSpinner);
            spin.setOnItemSelectedListener(this);

//Creating the ArrayAdapter instance having the bank name list
            ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item, bloodNames);
            //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);
            //spin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSignUp();
                }
            });
        }

        private void userSignUp(){

                //defining a progress dialog to show while signing up
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signing Up...");
                progressDialog.show();

                //getting the user values
                final RadioButton radioMale = (RadioButton) findViewById(radioGender.getCheckedRadioButtonId());
                final RadioButton radioStatusYes = (RadioButton) findViewById(radioStatus.getCheckedRadioButtonId());


                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String birthDate = editTextBirth.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String number = editTextNumber.getText().toString().trim();
                String status;
                String bloodGroup = bloodChoose;

                String gender = radioMale.getText().toString();
                String Textstatus = radioStatusYes.getText().toString();

                if (Textstatus.equals("Yes")){
                     status = "1";
                } else {
                     status = "0";
                }


                //building retrofit object
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiUtils.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                //Defining retrofit api service
                UserService service = retrofit.create(UserService.class);

                //Defining the user object as we need to pass it with the call
                User user = new User(name, email, password, gender, birthDate, address, number, status, bloodGroup);

                //defining the call
                Call<ResObj> call = service.createUser(
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getGender(),
                        user.getBirth(),
                        user.getArea(),
                        user.getPhone(),
                        user.getStatus(),
                        user.getBlood()

                );

                //calling the api
                call.enqueue(new Callback<ResObj>() {
                    @Override
                    public void onResponse(Call<ResObj> call, Response<ResObj> response) {
                        //hiding progress dialog
                        progressDialog.dismiss();

                        //displaying the message from the response as toast
                        //Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                        if (!response.body().getError()){
                            Intent intent = new Intent(RegisterActivity.this,SearchDonorActivity.class);

                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResObj> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), bloodNames[position], Toast.LENGTH_LONG).show();
        bloodChoose = bloodNames[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub
        bloodChoose = "";

    }

}
