package com.example.ex12;

import static com.example.ex12.RemoteService.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;
    RemoteService service;
    List<UserVO> array=new ArrayList<>();
    UserAdapter userAdapter=new UserAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("사용자관리");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_icecream_24);

        //node mysql과 연결하는것
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service=retrofit.create(RemoteService.class);
        onRestart();
        RecyclerView list= findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(userAdapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Call<List<UserVO>> call=service.list();

        call.enqueue(new Callback<List<UserVO>>() {
            @Override
            public void onResponse(Call<List<UserVO>> call, Response<List<UserVO>> response) {
                array=response.body();
                System.out.println("......"+array.size());
                userAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<UserVO>> call, Throwable t) {
                System.out.println("오류:......................."+t.toString());
            }
        });
    }
    //어댑터 정의
    class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

        @NonNull
        @Override
        public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.item_user,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
            UserVO vo=array.get(position);
            holder.id.setText(vo.getId());
            holder.name.setText(vo.getName());
            String image=vo.getImage();
            if(image != null){
                Picasso.with(MainActivity.this).load(BASE_URL+image).into(holder.image);
            }else {
                holder.image.setImageResource(R.drawable.ic_fl);
            }

        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView id,name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image=itemView.findViewById(R.id.image);
                id=itemView.findViewById(R.id.id);
                name=itemView.findViewById(R.id.name);

            }
        }
    }
}