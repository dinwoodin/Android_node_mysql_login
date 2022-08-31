package com.example.ex11;

import static com.example.ex11.RemoteService.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsertActivity2 extends AppCompatActivity {
    EditText id,name,pass;
    Retrofit retrofit;
    RemoteService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        getSupportActionBar().setTitle("사용자등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id=findViewById(R.id.id);
        pass=findViewById(R.id.pass);
        name=findViewById(R.id.name);



        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service=retrofit.create(RemoteService.class);


        Button save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strId=id.getText().toString();
                String strPass=pass.getText().toString();
                String strName=name.getText().toString();
                if(strId.equals("")||strPass.equals("")||strName.equals("")){
                    Toast.makeText(InsertActivity2.this,"입력 정보를 확인바랍니다.",Toast.LENGTH_SHORT).show();
                }else {
                    UserVO vo = new UserVO();
                    vo.setId(strId);
                    vo.setPass(strPass);
                    vo.setName(strName);

                    Call<Void> call=service.insert(vo);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.println("ddddddd......."+t.toString());
                            Toast.makeText(InsertActivity2.this,"입력실패",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}