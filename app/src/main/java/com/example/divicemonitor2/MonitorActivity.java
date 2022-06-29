package com.example.divicemonitor2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.divicemonitor2.tools.DataDeal;
import com.example.divicemonitor2.tools.MonitorAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MonitorActivity extends AppCompatActivity{
    //界面
    private ViewPager viewPager;
    private TextView presentmode_diji;
    private ConstraintLayout constraintLayout_bottom;
    private TextView err_show;
    private ArrayList<Fragment> modefragment;
    private MonitorAdapter monitoradapter;

    public static int pagenum;     //当前第几界面

    /**
     * 灯亮条件
     */
    public static int lightlightingbyte=0;   //byte型数据灯亮的条件
    public static int lightlightingshort=0;   //short型数据灯亮的条件
    public static int lightlightingint=0;   //int型数据灯亮的条件
    public static float lightlightingfloat=0.0f;  //float型数据灯亮的条件
    public static double lightlightingdouble=0;  //double型数据灯亮的条件

    /**
     * switch改变量
     */
    public static boolean isdataunsign=false;    //监控的字节流表示的数据是否是无符号数据
    public static boolean isphysicsdata=false;   //监控获得的数据是否需要转为物理量
    public static boolean ismodechange=true;    //模块是否更改/滑动

    //显示信息
    public static int modenum = 1;     //当前第几模块
    public static String modeltype="CPU"; //模块类型,默认为CPU作为首模块
    public static String datatype="bit";       //寄存器中的数据类型，默认为bit
    public static byte[] modeinform=new byte[1024];   //总的模块ID和序号信息
    public static int modexulie;    //模块序列号为4字节无符号数字，前4位表示时间，后表示编码序号如210700002
    public static String modecode="aaa";  //模块型号  //模块型号8字节字符串，和模块序列号即模块ID共12字节构成单个模块的信息
    public static String modebeizhu="";    //单个模块备注信息，由配置信息获得
    public static int moderoadnum=16;//模块通路数量，默认16路
    public static int data_address=1;      //当前模块数据地址
    public int lastdata_address=1;    //上一模块的数据地址

    //预加载下一界面信息
    public static int nextmodenum=2;   //预加载的下一模块,初始为第2模块
    public static String nextmodetype="next";
    public static String nextdatatype="bit";
    public static String nextmodecode="nextmode";
    public static int nextModexulie=110;
    public static String nextmodebeizhu="";
    public static int nextmoderoadnum=16;
    public static int nextdata_address=1;


    //读取和通讯的信息
    public static byte[] receBuf = new byte[256];   //连接设备监控数据时存储接收字节
    public static String[][] modeproperties=new String[15][10];       //从txt中读取的模块配置信息
    public static String[][] cpuproperties=new String[4][3];    //CPU的配置信息
    public static String zhushi;     //设备总体注释信息，由通讯获得
    public static int[] modelong=new int[30];  //保存各个模块的数据字节长度
    public static byte[] cpudata=new byte[16];
    public static int[] lightdata=new int[16];
    public static long[] lightdatalong=new long[16];
    public static float[] lightdata2=new float[16];
    public static double[] lightdata3=new double[32];


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divicemonitor);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toptoolbar2);
        setSupportActionBar(toolbar);//顶部toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        pagenum=1;            //每次打开都进入第一个模块

        presentmode_diji=(TextView)findViewById(R.id.presentmode_serialnumber);
        //nextmodenum=2;
        presentmode_diji.setText("当前模块"+pagenum);
        System.out.println("当前模块"+pagenum);

        /*
        String[] temp6=new String[10];
        String[] temp7=new String[10];
        modecode=DiviceinformActivity.diviceIDinf[pagenum-1];
        modecode=modecode.trim();
        nextmodecode=DiviceinformActivity.diviceIDinf[nextmodenum-1];
        nextmodecode=nextmodecode.trim();

        temp6=initAssets(modecode);  //读取配置文件
        temp7=initAssets(nextmodecode);
        if(temp6==null){
            err_show.setText("该模块不存在于配置文件中");
        }
        else{
            for(int i=0;i<temp6.length;i++){
                modeproperties[i]=temp6[i].split(":");
            }

            if(temp6[0].substring(1).equals("CPU")) {   //为CPU模块
                modeltype="CPU";
                modelong[pagenum-1]=Integer.parseInt(modeproperties[6][0].trim());
                modebeizhu=modeproperties[1][0].trim();
                for(int k=0;k<4;k++)
                    for(int l=0;l<3;l++){
                        cpuproperties[k][l]=modeproperties[2+k][l].trim();
                    }
            }
            else{                 //其他模块
                modeltype=modeproperties[0][0].substring(1);
                moderoadnum=Integer.parseInt(modeproperties[2][1].trim());
                modebeizhu=modeproperties[1][0].trim();
                datatype=modeproperties[2][2];
                modelong[pagenum-1]=Integer.parseInt(modeproperties[3][0].trim());
            }
        }
        System.out.println("当前模块"+modecode);
        System.out.println("下一模块"+nextmodecode);

        System.out.println("初始读取完成"+modecode+nextmodecode);
        */

        initfragment();
        initview();

        //网络请求较小时需要
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void initview(){
        viewPager=(ViewPager)findViewById(R.id.monitorview);
        err_show=(TextView)findViewById(R.id.err_text);
        constraintLayout_bottom=(ConstraintLayout)findViewById(R.id.constraintLayout1);
        monitoradapter=new MonitorAdapter(getSupportFragmentManager());
        monitoradapter.setFragments(modefragment);
        viewPager.setAdapter(monitoradapter);
        viewPager.addOnPageChangeListener(new TabOnPageChangeListener());
        //viewPager.setOffscreenPageLimit(1);
    }

    //模块界面初始化填充配置信息,fragment初始化
    private void initfragment() {
        modefragment=new ArrayList<Fragment>();
        String[] temp1=new String[10];
        for(int i=0;i<DiviceinformActivity.divicettcount;i++){  //将每个模块配置打包为bundle发送给fragment
            modecode=DiviceinformActivity.diviceIDinf[i];
            modecode=modecode.trim();
            temp1=initAssets(modecode);  //读取配置文件
            Bundle bundle=new Bundle();
            if(temp1==null){
                System.out.println(modecode+"不存在配置文件中");
                bundle.putString("modecode",modecode);
                bundle.putBoolean("isexist",false);
                bundle.putString("errtext","模块不存在");
            }
            else{
                bundle.putBoolean("isexist",true);
                for(int j=0;j<temp1.length;j++){
                    modeproperties[j]=temp1[j].split(":");
                }
                bundle.putString("modecode",modecode);
                if(temp1[0].substring(1).equals("CPU")){   //CPU模块
                    modeltype=temp1[0].substring(1);
                    modelong[i]=Integer.parseInt(modeproperties[6][0].trim());
                    modebeizhu=modeproperties[1][0].trim();
                    for(int k=0;k<4;k++)
                        for(int l=0;l<3;l++){
                            cpuproperties[k][l]=modeproperties[2+k][l].trim();
                        }
                    bundle.putString("modetype",modeltype);
                    bundle.putInt("modelong",modelong[i]);
                    bundle.putString("modebeizhu",modebeizhu);
                    bundle.putInt("modexulie",DiviceinformActivity.divicexulieinf[i]);


                }
                else{           //其他模块
                    modeltype=modeproperties[0][0].substring(1);
                    moderoadnum=Integer.parseInt(modeproperties[2][1].trim());
                    modebeizhu=modeproperties[1][0].trim();
                    datatype=modeproperties[2][2];
                    modelong[pagenum-1]=Integer.parseInt(modeproperties[3][0].trim());
                    bundle.putString("modetype",modeltype);
                    bundle.putInt("moderoadnum",moderoadnum);
                    bundle.putString("modebeizhu",modebeizhu);
                    bundle.putInt("modelong",modelong[i]);
                    bundle.putInt("modexulie",DiviceinformActivity.divicexulieinf[i]);

                }
                /*
                if(temp1[0].substring(1).equals("CPU")) {   //为CPU模块
                    modeltype="CPU";
                    modelong[pagenum-1]=Integer.parseInt(modeproperties[6][0].trim());
                    modebeizhu=modeproperties[1][0].trim();
                    for(int k=0;k<4;k++)
                        for(int l=0;l<3;l++){
                            cpuproperties[k][l]=modeproperties[2+k][l].trim();
                        }
                }
                else{                 //其他模块
                    modeltype=modeproperties[0][0].substring(1);
                    moderoadnum=Integer.parseInt(modeproperties[2][1].trim());
                    modebeizhu=modeproperties[1][0].trim();
                    datatype=modeproperties[2][2];
                    modelong[pagenum-1]=Integer.parseInt(modeproperties[3][0].trim());
                }
                */
            }

            Fragment fragment=DiviceModeFragment.newInstance(bundle);
            modefragment.add(fragment);
        }
        /*
        modefragment = new ArrayList<Fragment>();
        for(int i=0;i<DiviceinformActivity.divicettcount;i++){
            Fragment fragment=new DiviceModeFragment();
            modefragment.add(fragment);
        }
        */
    }


    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {
        //当滑动状态改变时调用，即滑动时调用。只通过滑动界面才调用
        public void onPageScrollStateChanged(int state) {
            //Toast.makeText(MonSystem.out.println("oncreatview");itorActivity.this, "滑动状态改变", Toast.LENGTH_SHORT).show();
            //System.out.println("滑动状态"+state);
        }

        //当前页面被滑动时调用，太敏感没用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Toast.makeText(MainPage.this, "界面滑动", Toast.LENGTH_SHORT).show();
            //System.out.println("滑动"+position);
        }


        //当新的页面被选中时调用
        //滑到或选择某界面时调用，显示界面
        @SuppressLint("SetTextI18n")
        public void onPageSelected(int position) {
            System.out.println("当前位置"+position);
            String[] temp5=new String[10];
            pagenum=position+1; //position+1号模块的界面。改变地址和配置信息
            nextmodenum=pagenum+1;
            presentmode_diji.setText("当前模块"+pagenum);
            System.out.println("模块"+pagenum);
            modecode=DiviceinformActivity.diviceIDinf[pagenum-1];
            modecode=modecode.trim();
            System.out.println(modecode);

                temp5=initAssets(modecode);  //读取配置文件
                if(temp5==null){
                    err_show.setText("该模块不存在于配置文件中");
                }
                else{
                    for(int i=0;i<temp5.length;i++){
                        modeproperties[i]=temp5[i].split(":");
                    }
                    if(temp5[0].substring(1).equals("CPU")) {   //为CPU模块
                        modeltype="CPU";
                        modelong[pagenum-1]=Integer.parseInt(modeproperties[6][0].trim());
                        modebeizhu=modeproperties[1][0].trim();
                        for(int k=0;k<4;k++)
                            for(int l=0;l<3;l++){
                                cpuproperties[k][l]=modeproperties[2+k][l].trim();
                            }
                    }
                    else{                 //其他模块
                        modeltype=modeproperties[0][0].substring(1);
                        moderoadnum=Integer.parseInt(modeproperties[2][1].trim());
                        modebeizhu=modeproperties[1][0].trim();
                        datatype=modeproperties[2][2];
                        modelong[pagenum-1]=Integer.parseInt(modeproperties[3][0].trim());
                    }
                }
            System.out.println("二次读取完成"+modecode);
        }
    }


    //模块ID和序号字节数组处理，存入dviceinformactivity的diviceIDinf和divicexulieinf中
    public static void modeinfdispose(byte[] modeinform,int modetotalcount){
        byte[] temp1=new byte[4];
        byte[] temp2=new byte[8];
        for(int i=0;i<modetotalcount;i++){
            System.arraycopy(modeinform,12*i,temp2,0,8);

            if(DataDeal.byteToString(temp2).charAt(0)=='A')
                DiviceinformActivity.diviceIDinf[i]= DataDeal.byteToString(temp2).substring(0,5);    //CPU
            else DiviceinformActivity.diviceIDinf[i]= DataDeal.byteToString(temp2).substring(0,6);   //其他模块
            System.arraycopy(modeinform,12*(i)+8,temp1,0,4);
            DiviceinformActivity.divicexulieinf[i]=(temp1[0]&0xff)+((temp1[1]&0xff)<<8)+((temp1[2]&0xff)<<16)+((temp1[3]&0xff)<<24);
        }
        System.out.println("ID处理结果"+DiviceinformActivity.diviceIDinf.toString());
        System.out.println("序号处理结果"+DiviceinformActivity.divicexulieinf.toString());
    }


    //根据模块ID名称读取assets配置文件,
    public String[] initAssets(String filename) {
        try {
            //获取输入流
            InputStream inputStream = getAssets().open(filename);//这里的名字是你的txt 文本文件名称
            String str = getString(inputStream);
            String[] arr = str.split("\n");
            return arr;
        } catch (IOException e1) {
            System.out.println("不存在该模块");
            e1.printStackTrace();
            return null;
        }
    }
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        //创建字符缓冲流
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                //添加到字符缓冲流中
                sb.append(line);
                //一条一行
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
