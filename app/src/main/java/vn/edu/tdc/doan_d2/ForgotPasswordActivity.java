//package vn.edu.tdc.doan_d2;
//
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthInvalidUserException;
//
//
//import vn.edu.tdc.doan_d2.databinding.ActivityForgotPasswordBinding;
//
//
//public class ForgotPasswordActivity extends AppCompatActivity {
//    private EditText emailText;
//    private ActivityForgotPasswordBinding binding;
//    private  FirebaseAuth myAuth;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
//        emailText = findViewById(R.id.inputEmail);
//
//        Log.d("zxczxcxzczc1231321xz", emailText.getText().toString());
//        String emailAddress =  binding.inputEmail.getText().toString();
//        Log.d("zxczxcxzczcxz", emailAddress);
//        binding.btnResister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myAuth = FirebaseAuth.getInstance();
//
//                myAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("1Tag123131", "Email sent.");
//                        } else {
//                            // Xử lý lỗi nếu có
//                            Exception e = task.getException();
//                            if (e instanceof FirebaseAuthInvalidUserException) {
//                                // Email không tồn tại
//                                Log.e("1Tag123131", "Email không tồn tại", e);
//                            } else {
//                                // Lỗi khác
//                                Log.e("1Tag123131", "Lỗi khi gửi email đặt lại mật khẩu", e);
//                            }
//                        }
//                    }
//                });
//            }
//        });
//
//    }
//}