package com.example.ex12;

import static com.example.ex12.RemoteService.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    ArrayList<String> arrayDelete=new ArrayList<>();
 


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder box=new AlertDialog.Builder(this);
        switch (item.getItemId()){
            case R.id.delete:
                if(arrayDelete.size()==0){
                    box.setMessage("삭제할 항목을 선택하세요");
                    box.setPositiveButton("닫기",null);
                    box.show();
                }else {
                    box.setMessage(arrayDelete.size()+"개를 삭제하시겠습니까?");
                    box.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(String id:arrayDelete){
                                UserVO vo=new UserVO();
                                vo.setId(id);
                                Call<Void> call=service.delete(vo);
                                System.out.println("ididididididididididid............."+vo);
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        onRestart();

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        System.out.println("...qwdqwdqwd........."+t.toString());
                                    }
                                });
                            }

                        }

                    });
                    box.setNegativeButton("no",null);
                    box.show();
                }

        }
        return super.onOptionsItemSelected(item);

    }

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

        FloatingActionButton add= findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,InsertActivity.class);
                startActivity(intent);
            }
        });

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
                arrayDelete.clear();
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
            holder.itme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,ReadActivity.class);
                    intent.putExtra("id",vo.getId());
                    startActivity(intent);
                }
            });
            holder.chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.chk.isChecked()){
                        arrayDelete.add(vo.getId());
                    }else {
                        arrayDelete.remove(vo.getId());
                    }
//                    System.out.println(",......"+arrayDelete.size());
                }
            });

                holder.chk.setChecked(false);

        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView id,name;
            RelativeLayout itme;
            CheckBox chk;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image=itemView.findViewById(R.id.image);
                id=itemView.findViewById(R.id.id);
                name=itemView.findViewById(R.id.name);
                itme=itemView.findViewById(R.id.itme);
                chk=itemView.findViewById(R.id.chk);

            }
        }
    }
}