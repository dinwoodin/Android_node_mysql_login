package com.example.ex12;

import static com.example.ex12.RemoteService.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsertActivity extends AppCompatActivity {
    EditText id,name,pass;
    Retrofit retrofit;
    RemoteService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setTitle("사용자등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button save=findViewById(R.id.save);
        id=findViewById(R.id.id);
        name=findViewById(R.id.name);
        pass=findViewById(R.id.pass);

        //node mysql과 연결하는것
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service=retrofit.create(RemoteService.class);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserVO vo=new UserVO();
                vo.setId(id.getText().toString());
                vo.setName(name.getText().toString());
                vo.setPass(pass.getText().toString());
                if(vo.getId().equals("")||vo.getName().equals("")||vo.getPass().equals("")){
                    Toast.makeText(InsertActivity.this,"내용을 확인 바랍니다.",Toast.LENGTH_SHORT).show();

                }else {
                    Call<Void> call=service.insert(vo);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            //성공
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //실패
                            System.out.println("insert 오류............"+t.toString());
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