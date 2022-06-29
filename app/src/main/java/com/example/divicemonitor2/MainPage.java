package com.example.divicemonitor2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.divicemonitor2.Threads.Thread_main;
import com.example.divicemonitor2.tools.DataDeal;
import com.example.divicemonitor2.tools.FragmentIndexAdapter;
import com.example.divicemonitor2.tools.MyViewPager;
import com.example.divicemonitor2.tools.ServerLink;
import com.example.divicemonitor2.tools.UDPsocket;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    private MyViewPager mainviewpager;
    private ArrayList<Fragment> mfragments;
    private FragmentIndexAdapter mfragmentIndexAdapter;

    //线程
    public static Thread_main threadmain=new Thread_main();

    //底部图
    private ImageView image_divice;
    private ImageView image_connect;
    public static ImageView image_link;
    private ImageView image_find;
    private ImageView image_user;
    //底部布局
    private LinearLayout ll_divice;
    private LinearLayout ll_connect;
    private LinearLayout ll_link;
    private LinearLayout ll_find;
    private LinearLayout ll_user;
    private FrameLayout fl_bottom;
    private RelativeLayout rl_mainview;
    //底部text
    public static TextView linktext;

    //APP状态
    public static boolean isclicklink=false;   //是否点击连接
    public static boolean isAPPstart=false;//APP是否已打开
    public static boolean islastdivicestore=false; //是否存有上次连接设备
    public static boolean ismonitorstart=false;  //监控是否开始
    public static boolean isthreadstart=false;  //线程是否已启动
    public static int islinksuccess = 0;//判断设备连接情况，1为连接成功,0为失败
    public static int testservercon = 0;//测试服务器是否连接，1为连接成功，0为失败
    public static int connecttype=2;    //连接方式，0表示连接设备请求配置信息，1表示连接服务器请求配置信息，2表示连接设备获取IO字节流
    public static int isreceivewrong = 0; //判断套接字是否接收错误,0错误1正确

    //服务器端口IP和本地端口设置
    public static String serverip="192.168.0.57";
    public static int serverport1=2223;
    public static int homeport=3023;

    //toolbar选项
    public static MenuItem connectsight;//连接成功与否指示
    public static MenuItem linktype;//连接方式菜单图标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottommenuandtoolbar);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toptoolbar);
        setSupportActionBar(toolbar);
        initview();
        initdata();
        initevent();
    }

    private void initview(){
        mainviewpager=(MyViewPager)findViewById(R.id.mainview);
        image_divice=(ImageView)findViewById(R.id.iv_tab_one);
        image_connect=(ImageView)findViewById(R.id.iv_tab_two);
        image_link=(ImageView)findViewById(R.id.iv_tab_three);
        image_find=(ImageView)findViewById(R.id.iv_tab_four);
        image_user=(ImageView)findViewById(R.id.iv_tab_five);

        ll_divice=(LinearLayout)findViewById(R.id.ll_tab_one);
        ll_connect=(LinearLayout)findViewById(R.id.ll_tab_two);
        ll_link=(LinearLayout)findViewById(R.id.ll_tab_three);
        ll_find=(LinearLayout)findViewById(R.id.ll_tab_four);
        ll_user=(LinearLayout)findViewById(R.id.ll_tab_five);
        fl_bottom=(FrameLayout)findViewById(R.id.bottomview);
        rl_mainview=(RelativeLayout)findViewById(R.id.mainview2);

        linktext=(TextView)findViewById(R.id.tv_tab_three);

        connectsight=(MenuItem)findViewById(R.id.linkcondition1);
        linktype=(MenuItem)findViewById(R.id.linktype1);
    }


    private void initdata(){
        mfragments=new ArrayList<Fragment>();
        mfragments.add(DiviceinformActivity.newInstance(getResources().getString(R.string.diviceinform)));//设备界面
        mfragments.add(ConnectActivity.newInstance(getResources().getString(R.string.connectactivity)));   //通讯界面
        mfragments.add(FindActivity.newInstance(getResources().getString(R.string.findactivity)));      //发现界面
        mfragments.add(UserActivity.newInstance(getResources().getString(R.string.useractivity)));      //用户界面
        initIndexFragmentAdapter();
    }

    //初始化点击事件
    private void initevent(){
        ll_divice.setOnClickListener(new TabOnClickListener(0));
        ll_connect.setOnClickListener(new TabOnClickListener(1));
        ll_link.setOnClickListener(new TabOnClickListener(2));
        ll_find.setOnClickListener(new TabOnClickListener(3));
        ll_user.setOnClickListener(new TabOnClickListener(4));
    }

    //点击事件
    public class TabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TabOnClickListener(int i) {
            index = i;
        }
        @SuppressLint({"ResourceAsColor", "SetTextI18n"})
        public void onClick(View v) {
            if (index == 2) {
                if(isclicklink==false){  //点击了连接
                    DiviceinformActivity.getscreeninform();
                    if(DiviceinformActivity.diviceID.length()>2){//有ID信息
                        if(DiviceinformActivity.diviceIP.length()>4&&DiviceinformActivity.diviceport>0){      //有IP信息和端口信息
                            if(DiviceinformActivity.divicettcount>0&&DiviceinformActivity.diviceIDinf[0]!=null){   //信息完整直连设备
                                System.out.println("直连监控开始");
                                connecttype=2;
                                if(!isthreadstart && !threadmain.isSleep) { //进程首次开始
                                    ismonitorstart=true;
                                    isthreadstart=true;
                                    threadmain.start();
                                    threadmain.isStop=false;
                                }
                                else {
                                    ismonitorstart=true;
                                    threadmain.setSleep(false);
                                }
                                DiviceinformActivity.save(DiviceinformActivity.diviceID);
                                DiviceinformActivity.errtext.setText("");
                                image_link.setColorFilter(getResources().getColor(R.color.blue));
                                linktext.setText("断开");
                                isclicklink=true;
                            }

                            else {      //连接设备获取配置信息
                                System.out.println("连接设备请求配置信息");
                                ServerLink.serverport=DiviceinformActivity.diviceport;
                                ServerLink.serverIP=DiviceinformActivity.diviceIP;
                                connecttype=0;
                                if(DiviceinformActivity.diviceID.charAt(0)=='A'){
                                    ServerLink.diviceid=DiviceinformActivity.diviceID.substring(0,5).getBytes();
                                }
                                else  ServerLink.diviceid=DiviceinformActivity.diviceID.substring(0,6).getBytes();
                                ServerLink.divicexulie=  DataDeal.reverse_array(ByteBuffer.allocate(4).putInt(Integer.parseInt(DiviceinformActivity.diviceID.substring(8))).array());
                                if(!isthreadstart && !threadmain.isSleep) { //进程首次开始
                                    ismonitorstart=true;
                                    isthreadstart=true;
                                    threadmain.start();
                                    threadmain.isStop=false;

                                }
                                else {
                                   ismonitorstart=true;
                                    threadmain.setSleep(false);
                                }

                                image_link.setColorFilter(getResources().getColor(R.color.blue));
                                linktext.setText("断开");
                                isclicklink=true;

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(islinksuccess==1){
                                    if(DiviceinformActivity.divicettcount>0&&DiviceinformActivity.diviceIDinf[0]!=null){
                                        DiviceinformActivity.divicetotalcount_int.setText(""+DiviceinformActivity.divicettcount);
                                        System.out.println("直连监控设备开始");
                                        connecttype=2;
                                        DiviceinformActivity.save(DiviceinformActivity.diviceID);
                                        DiviceinformActivity.errtext.setText("");

                                    }
                                    else{
                                        DiviceinformActivity.errtext.setText("获取配置信息失败");
                                    }
                                }
                                else{
                                        DiviceinformActivity.errtext.setText("连接失败");
                                }
                            }
                        }
                        else{      //只有设备ID，则连接服务器获取配置信息
                            ServerLink.serverIP=serverip;
                            ServerLink.serverport=serverport1;
                            connecttype=1;
                            if(DiviceinformActivity.diviceID.charAt(0)=='A'){
                                ServerLink.diviceid=DiviceinformActivity.diviceID.substring(0,5).getBytes();
                            }
                            else  ServerLink.diviceid=DiviceinformActivity.diviceID.substring(0,6).getBytes();
                            ServerLink.divicexulie=  DataDeal.reverse_array(ByteBuffer.allocate(4).putInt(Integer.parseInt(DiviceinformActivity.diviceID.substring(8))).array());
                            System.out.println("连接服务器开始");
                            if(!isthreadstart && !threadmain.isSleep) { //进程首次开始
                                ismonitorstart=true;
                                isthreadstart=true;
                                threadmain.start();
                                threadmain.isSleep=false;
                                threadmain.isStop=false;
                            }
                            else {
                                ismonitorstart=true;
                                threadmain.setSleep(false);
                            }
                            image_link.setColorFilter(getResources().getColor(R.color.blue));
                            linktext.setText("断开");
                            isclicklink=true;
                            try {                    //
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(islinksuccess==1){
                                if(DiviceinformActivity.diviceIP.length()>0&&DiviceinformActivity.diviceport>0&&DiviceinformActivity.diviceIDinf[0]!=null){    //有返回信息
                                    DiviceinformActivity.tianxie();
                                    System.out.println("直连监控设备开始");
                                    connecttype=2;
                                    DiviceinformActivity.save(DiviceinformActivity.diviceID);
                                    DiviceinformActivity.errtext.setText("");
                                }
                                else{
                                    DiviceinformActivity.errtext.setText("获取配置信息失败");
                                }
                            }
                            else{
                                DiviceinformActivity.errtext.setText("连接失败");
                            }

                        }

                    }
                    else{    //ID错误
                        DiviceinformActivity.errtext.setText("ID错误");
                    }

                }
                else if(isclicklink==true){  //点击了断开
                    image_link.clearColorFilter();
                    linktext.setText("连接");
                    isclicklink=false;
                    ismonitorstart=false;
                    threadmain.setSleep(true);
                    //threadmain.isStop=true;
                }
            }
            else {
                //选择某一页
                if(index==0) {
                    mainviewpager.setCurrentItem(0, false);
                }
                if(index==1) {
                    mainviewpager.setCurrentItem(1, false);
                }
                if(index==3) {
                    mainviewpager.setCurrentItem(2, false);
                }
                if(index==4) {
                    mainviewpager.setCurrentItem(3, false);
                }

            }
        }

    }


    private void initIndexFragmentAdapter(){
        mfragmentIndexAdapter=new FragmentIndexAdapter(this.getSupportFragmentManager(),mfragments);
        mainviewpager.setAdapter(mfragmentIndexAdapter);
        mainviewpager.setCurrentItem(0);
        mainviewpager.setOffscreenPageLimit(3);
        mainviewpager.addOnPageChangeListener(new TabOnPageChangeListener());
        ll_divice.setSelected(true);
        resetbottomcolor();
    }


    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {
        //当滑动状态改变时调用，即滑动时调用和下面冲突。只通过滑动界面才调用
        public void onPageScrollStateChanged(int state) {
            //Toast.makeText(MainPage.this, "滑动状态改变", Toast.LENGTH_SHORT).show();

        }

        //当前页面被滑动时调用，太敏感没用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Toast.makeText(MainPage.this, "界面滑动", Toast.LENGTH_SHORT).show();

        }

        //当新的页面被选中时调用
        //滑到或选择某界面时调用
        public void onPageSelected(int position) {
            resetTextView();
            resetbottomcolor();
            switch (position) {
                case 0:
                    ll_divice.setSelected(true);
                    System.out.println("选择界面1");
                    if(ll_divice.isSelected())ll_divice.setBackgroundColor(Color.WHITE);
                    break;
                case 1:
                    ll_connect.setSelected(true);
                    System.out.println("选择界面2");
                    if(ll_connect.isSelected())ll_connect.setBackgroundColor(Color.WHITE);
                    break;
                case 2:
                    ll_find.setSelected(true);
                    System.out.println("选择界面3");
                    if(ll_find.isSelected())ll_find.setBackgroundColor(Color.WHITE);
                    break;
                case 3:
                    ll_user.setSelected(true);
                    System.out.println("选择界面4");
                    if(ll_user.isSelected())ll_user.setBackgroundColor(Color.WHITE);
                    break;
            }
        }
    }




    //重置
    private void resetTextView() {
        ll_divice.setSelected(false);
        ll_connect.setSelected(false);
        ll_find.setSelected(false);
        ll_user.setSelected(false);
    }
    //颜色变化
    private void resetbottomcolor(){
        if(ll_divice.isSelected())ll_divice.setBackgroundColor(Color.WHITE);
        else ll_divice.setBackgroundColor(getResources().getColor(R.color.ivory));
        if(ll_connect.isSelected())ll_connect.setBackgroundColor(Color.WHITE);
        else ll_connect.setBackgroundColor(getResources().getColor(R.color.ivory));
        if(ll_find.isSelected())ll_find.setBackgroundColor(Color.WHITE);
        else ll_find.setBackgroundColor(getResources().getColor(R.color.ivory));
        if(ll_user.isSelected())ll_user.setBackgroundColor(Color.WHITE);
        else ll_user.setBackgroundColor(getResources().getColor(R.color.ivory));
    }
}