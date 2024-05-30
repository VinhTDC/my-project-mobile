package vn.edu.tdc.doan_d2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class HomeActivity extends AppCompatActivity {

    private ImageView imageView2;
    private ImageView imageView5;
    private Button button3;
    private Button button4;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        imageView2 = findViewById(R.id.imageView2);
        imageView5 = findViewById(R.id.imageView5);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button2 = findViewById(R.id.button2);

        //Di tới trang category
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImage(imageView2);
                // Đặt lại hình ảnh về kích thước ban đầu cho imageView5
                resetImage(imageView5);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImage(imageView5);
                // Đặt lại hình ảnh về kích thước ban đầu cho imageView2
                resetImage(imageView2);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đặt lại hình ảnh về kích thước ban đầu cho cả imageView2 và imageView5
                resetImage(imageView2);
                resetImage(imageView5);
                showExitConfirmationDialog();
            }
        });

        // Xác định layout gốc
        ConstraintLayout rootLayout = findViewById(R.id.main);

        // Thêm sự kiện click cho layout gốc
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí click
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float clickX = location[0];
                float clickY = location[1];

                // Lấy vị trí của các nút
                int[] button3Location = new int[2];
                button3.getLocationOnScreen(button3Location);
                int[] button4Location = new int[2];
                button4.getLocationOnScreen(button4Location);
                int[] button2Location = new int[2];
                button2.getLocationOnScreen(button2Location);

                // Kiểm tra xem vị trí click có trùng với vị trí của các nút không
                if (clickX >= button3Location[0] && clickX <= button3Location[0] + button3.getWidth()
                        && clickY >= button3Location[1] && clickY <= button3Location[1] + button3.getHeight()) {
                    // Nếu trùng với button3, không làm gì
                } else if (clickX >= button4Location[0] && clickX <= button4Location[0] + button4.getWidth()
                        && clickY >= button4Location[1] && clickY <= button4Location[1] + button4.getHeight()) {
                    // Nếu trùng với button4, không làm gì
                } else if (clickX >= button2Location[0] && clickX <= button2Location[0] + button2.getWidth()
                        && clickY >= button2Location[1] && clickY <= button2Location[1] + button2.getHeight()) {
                    // Nếu trùng với button2, không làm gì
                } else {
                    // Nếu không trùng với cả các nút, đặt lại hình ảnh về kích thước ban đầu
                    resetImage(imageView2);
                    resetImage(imageView5);
                }
            }
        });
    }

    private void zoomImage(ImageView imageView) {
        imageView.setBackgroundResource(R.drawable.image_hover);
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("You want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void resetImage(ImageView imageView) {
        // Xóa nền và animator của imageView
        imageView.setBackgroundResource(0);
        imageView.setStateListAnimator(null);
    }
}
