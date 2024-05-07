package vn.edu.tdc.doan_d2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static final String emailPattern = "[a-zA-Z0-9.-]+@[a-zA-Z]+[a-z]{2,}";
    //để private cho nó chỉ ánh xạ ở trong này được thoi
    private EditText textEmail;
    private EditText textPassWord;
    private Button btnLogin;
    private TextView textValidate;
    //Validation
    private Boolean isValidEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //ánh xạ
        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassWord = (EditText) findViewById(R.id.textPassword);
        textValidate = (TextView) findViewById(R.id.textValidate);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hiển thị j đó khi nhấn nút
                String email = textEmail.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "your email:", Toast.LENGTH_SHORT).show();
            }
      });
                if (!isValidEmail) {
                    Toast.makeText(getApplicationContext(), "Validate email failed!!", Toast.LENGTH_SHORT).show();
                }
                //validate
                textEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //
                        textValidate.setText("");
                        String email = textEmail.getText().toString().trim();
                        isValidEmail = (email.matches(emailPattern) && s.length() > 0);
                        if (!isValidEmail) {
                            textValidate.setTextColor(Color.rgb(255, 0, 0));
                            textValidate.setText("Invalidate email address!!");
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
