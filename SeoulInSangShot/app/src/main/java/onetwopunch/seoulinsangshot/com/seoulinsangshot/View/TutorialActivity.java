package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

public class TutorialActivity extends AppCompatActivity {

    private Button btn_tuto_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        btn_tuto_next = (Button) findViewById(R.id.btn_tuto_next);
        btn_tuto_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent loading = new Intent(getApplicationContext(), LoadingActivity.class);
                startActivity(loading);
                finish();

            }
        });

    }
}
