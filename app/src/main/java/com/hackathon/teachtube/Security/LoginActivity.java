package com.hackathon.teachtube.Security;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import com.google.android.material.snackbar.Snackbar;
import com.hackathon.teachtube.Classes.MainLoader;
import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.ImpMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private static final String TAG = "LoginActivity";

    private TextView et_phone_no, register_link;
    private MaterialCardView btn_submit_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        et_phone_no = findViewById(R.id.et_phone_no);
        register_link = findViewById(R.id.register_link);
        btn_submit_login = findViewById(R.id.btn_submit_login);

        btn_submit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_no = et_phone_no.getText().toString().trim();

                if (!ImpMethods.isMobileNoValid(phone_no)) {
                    Snackbar.make(view, "Invalid Mobile No. !!", Snackbar.LENGTH_SHORT).show();
                } else {
                    MainLoader.Loader(true , findViewById(R.id.LL_loader));
                    //SignIn(phone_no , view);
                }
            }
        });

        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    /*private void SignIn(String phone_no , View view)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AllKeyUrls.getGetSigninMobileNo(phone_no),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("1"))
                            {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                String userStatus = dataObject.getString("user_status");
                                //String fpStatus = dataObject.getString("fp_status");
                                //String mpinStatus = dataObject.getString("mpin_status");

                                if (userStatus.equals("1"))
                                {
                                    TinyDB tinyDB = new TinyDB(context);
                                    tinyDB.putString(DataSharedManager.USER_STATUS , userStatus);

                                    String mMobileNo = "+91"+phone_no;
                                    Intent intent = new Intent(context, VerificationActivity.class);
                                    intent.putExtra("phoneNo", mMobileNo);
                                    startActivity(intent);

                                }else
                                {
                                    Toast.makeText(context, "Your Account is Inactive !!", Toast.LENGTH_SHORT).show();
                                }
                                MainLoader.Loader(false , findViewById(R.id.LL_loader));
                            }else
                            {
                                MainLoader.Loader(false , findViewById(R.id.LL_loader));
                                ImpMethods.showErrorSnackbar(context , view , "Mobile No. not Registered !!\nPlease Register via the below link");
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            MainLoader.Loader(false , findViewById(R.id.LL_loader));
                            Log.d(TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        MainLoader.Loader(false , findViewById(R.id.LL_loader));
                        Log.d(TAG, "onErrorResponse: "+error.toString());
                        ImpMethods.showErrorSnackbar(context , view , "Network Problem !!\nPlease try again later");
                    }
                }
        );
        requestQueue.add(stringRequest);
    }*/
}
