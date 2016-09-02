package com.example.jayz.mydemo;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipe ;
    private RecyclerView recyclerView ;
    private ArrayList<String> mList ;
    private MyAdapter mAdapter ;
    private AVLoadingIndicatorView avLoadingIndicatorView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addData();
        mAdapter = new MyAdapter()  ;
        swipe = (SwipeRefreshLayout)findViewById(R.id.swipe) ;
        swipe.setColorSchemeColors(R.color.colorPrimary,R.color.colorPrimaryDark);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final HeaderViewRecyclerAdapter stringAdapter = new HeaderViewRecyclerAdapter(mAdapter) ;
        recyclerView.setAdapter(stringAdapter);
        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.loadmoreview,
                recyclerView,false) ;
        avLoadingIndicatorView = (AVLoadingIndicatorView)view1.findViewById(R.id.avi) ;
        stringAdapter.addFooterView(view1);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        stringAdapter.notifyDataSetChanged();
                    }
                },1500) ;
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        swipe.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                    }
                },1500) ;
            }
        });
    }
    private void loadData(){
        List<String> list = new ArrayList<>() ;
        for(int i=0;i<3;i++){
            list.add("加载的数据");
        }
        mList.addAll(list) ;
    }
    public void refreshData(){
        mList.add(0,"刷新的数据");
    }
    private void addData(){
        mList = new ArrayList<>() ;
        for(int i=0 ;i<15;i++){
            mList.add("这是第："+i+"个");
        }

    }
    //View.inflate(MainActivity.this,R.layout.activity_main_item,null
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder mv = new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_item,
                    parent,false)) ;
            Log.d("TAG", "LOG TEST ");
            return mv;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            holder.tv.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv  ;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv_item) ;
            }
        }
    }
}
