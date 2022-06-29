package com.example.divicemonitor2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.divicemonitor2.tools.DataDeal;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.divicemonitor2.MonitorActivity.lightlightingbyte;
import static com.example.divicemonitor2.MonitorActivity.lightlightingdouble;
import static com.example.divicemonitor2.MonitorActivity.lightlightingfloat;
import static com.example.divicemonitor2.MonitorActivity.lightlightingint;
import static com.example.divicemonitor2.MonitorActivity.lightlightingshort;
import static com.example.divicemonitor2.MonitorActivity.modexulie;
import static com.example.divicemonitor2.MonitorActivity.nextmodenum;

public class DiviceModeFragment extends Fragment {


    //界面text控件
    TextView moderoad;  //通道数
    TextView modetype;  //模块类型
    TextView recdatatype;   //数据类型
    TextView mode_add;      //当前模块数据地址
    TextView startaddress; //设备起始地址
    TextView diviceonline1; //是否在线
    TextView modecodetext;  //模块型号ID如A8601
    TextView modeintroduction;   //模块备注信息
    TextView xulietext;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch isdatasign;  //有无符号数开关
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch isphysics; //是否物理量开关
    //灯和模块图片
    ImageView connectlight;
    ImageView light1;
    ImageView light2;
    ImageView light3;
    ImageView light4;
    ImageView light5;
    ImageView light6;
    ImageView light7;
    ImageView light8;
    ImageView light9;
    ImageView light10;
    ImageView light11;
    ImageView light12;
    ImageView light13;
    ImageView light14;
    ImageView light15;
    ImageView light16;
    ImageView lightpower1;
    ImageView lightpower2;
    ImageView modeimage;

    //数据显示情况
    TextView CH1;
    TextView CH2;
    TextView CH3;
    TextView CH4;
    TextView CH5;
    TextView CH6;
    TextView CH7;
    TextView CH8;
    TextView CH9;
    TextView CH10;
    TextView CH11;
    TextView CH12;
    TextView CH13;
    TextView CH14;
    TextView CH15;
    TextView CH16;
    TextView T1;
    TextView T2;
    TextView T3;
    TextView T4;
    TextView T5;
    TextView T6;
    TextView T7;
    TextView T8;
    TextView T9;
    TextView T10;
    TextView T11;
    TextView T12;
    TextView T13;
    TextView T14;
    TextView T15;
    TextView T16;

    private Bundle modemessage;   //存储传递过来的模块信息
    private String currentmodetype;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modemessage=getArguments();
        System.out.println("oncreat该界面模块"+modemessage.getString("modecode"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_divicemode, container, false);
        initview(view);

        System.out.println("oncreatview监控主界面"+modemessage.getString("modetype")+modemessage.getString("modecode"));

        modeimageshow();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Mytesk(), 1, 1500);   //每1.5秒更新模块信息和灯的状态

        if(DiviceinformActivity.diviceonline==0)diviceonline1.setText("设备在线：否");
        else diviceonline1.setText("设备在线：是");


        //有无符号数开关事件
        isdatasign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    MonitorActivity.isdataunsign=true;
                    System.out.println("已更改为无符号数");
                }
                else{
                    MonitorActivity.isdataunsign=false;
                    System.out.println("已更改为有符号数");
                }
            }
        });

        //是否物理量开关事件
        isphysics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    MonitorActivity.isphysicsdata=true;
                    System.out.println("已更改为显示物理量");
                }
                else{
                    MonitorActivity.isphysicsdata=false;
                    System.out.println("已更改为显示实际数据值");
                }
            }
        });

        return view;

    }

    //界面初始化
    public void initview(View view){
        isdatasign=view.findViewById(R.id.switch2);
        isphysics=view.findViewById(R.id.switch1);
        modecodetext=view.findViewById(R.id.textView31);
        moderoad=view.findViewById(R.id.textView10);
        modetype=view.findViewById(R.id.textView11);
        diviceonline1=view.findViewById(R.id.textView32);
        recdatatype=view.findViewById(R.id.textView34);
        mode_add =view.findViewById(R.id.textView37);
        startaddress=view.findViewById(R.id.textView33);
        modeintroduction=view.findViewById(R.id.textView36);
        xulietext=view.findViewById(R.id.textView39);

        light1=view.findViewById(R.id.light1);
        light2=view.findViewById(R.id.light2);
        light3=view.findViewById(R.id.light3);
        light4=view.findViewById(R.id.light4);
        light5=view.findViewById(R.id.light5);
        light6=view.findViewById(R.id.light6);
        light7=view.findViewById(R.id.light7);
        light8=view.findViewById(R.id.light8);
        light9=view.findViewById(R.id.light9);
        light10=view.findViewById(R.id.light10);
        light11=view.findViewById(R.id.light11);
        light12=view.findViewById(R.id.light12);
        light13=view.findViewById(R.id.light13);
        light14=view.findViewById(R.id.light14);
        light15=view.findViewById(R.id.light15);
        light16=view.findViewById(R.id.light16);
        lightpower1=view.findViewById(R.id.lightpower1);
        lightpower2=view.findViewById(R.id.lightpower2);
        connectlight=view.findViewById(R.id.imageView2);
        modeimage=view.findViewById(R.id.imageView24);

        CH1=view.findViewById(R.id.CH1);
        CH2=view.findViewById(R.id.CH2);
        CH3=view.findViewById(R.id.CH3);
        CH4=view.findViewById(R.id.CH4);
        CH5=view.findViewById(R.id.CH5);
        CH6=view.findViewById(R.id.CH6);
        CH7=view.findViewById(R.id.CH7);
        CH8=view.findViewById(R.id.CH8);
        CH9=view.findViewById(R.id.CH9);
        CH10=view.findViewById(R.id.CH10);
        CH11=view.findViewById(R.id.CH11);
        CH12=view.findViewById(R.id.CH12);
        CH13=view.findViewById(R.id.CH13);
        CH14=view.findViewById(R.id.CH14);
        CH15=view.findViewById(R.id.CH15);
        CH16=view.findViewById(R.id.CH16);
        T1=view.findViewById(R.id.textView40);
        T2=view.findViewById(R.id.textView41);
        T3=view.findViewById(R.id.textView42);
        T4=view.findViewById(R.id.textView43);
        T5=view.findViewById(R.id.textView44);
        T6=view.findViewById(R.id.textView45);
        T7=view.findViewById(R.id.textView46);
        T8=view.findViewById(R.id.textView47);
        T9=view.findViewById(R.id.textView48);
        T10=view.findViewById(R.id.textView49);
        T11=view.findViewById(R.id.textView50);
        T12=view.findViewById(R.id.textView51);
        T13=view.findViewById(R.id.textView52);
        T14=view.findViewById(R.id.textView53);
        T15=view.findViewById(R.id.textView54);
        T16=view.findViewById(R.id.textView55);
    }

    //信息填写
    @SuppressLint("SetTextI18n")
    public void informtianxie(){
        modecodetext.setText("型号："+MonitorActivity.modecode);
        moderoad.setText("模块通路数"+MonitorActivity.moderoadnum);
        modetype.setText("模块类型：" +MonitorActivity.modeltype);
        xulietext.setText("模块序号：" +MonitorActivity.modexulie);
        startaddress.setText("设备起始地址：" + DiviceinformActivity.Starting_add);
        mode_add.setText("当前模块地址"+MonitorActivity.data_address);
        modeintroduction.setText(MonitorActivity.modebeizhu);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(MainPage.ismonitorstart&&MainPage.islinksuccess==1){
                        if(MonitorActivity.modeltype.equals("CPU")){
                            CHshowCPU(MonitorActivity.cpudata,MonitorActivity.cpuproperties);    //cpudata有16字节
                        }
                        else{
                            if (MonitorActivity.datatype.equals("bit")) {
                                Modeimage(MonitorActivity.lightdata, MonitorActivity.moderoadnum, 0);  //设备图片和灯
                                CHshow(MonitorActivity.lightdata);    //文本显示数据
                            } else if (MonitorActivity.datatype.equals("byte")) {
                                Modeimage(MonitorActivity.lightdata, MonitorActivity.moderoadnum, 1);
                                CHshow(MonitorActivity.lightdata);
                            } else if (MonitorActivity.datatype.equals("short")) {
                                Modeimage(MonitorActivity.lightdata, MonitorActivity.moderoadnum, 2);
                                CHshow(MonitorActivity.lightdata);
                            } else if (MonitorActivity.datatype.equals("int")) {
                                if (MonitorActivity.isdataunsign == false) {
                                    CHshow(MonitorActivity.lightdata);
                                    Modeimage(MonitorActivity.lightdata, MonitorActivity.moderoadnum, 3);
                                } else {
                                    modeimage4(MonitorActivity.lightdatalong, MonitorActivity.moderoadnum);
                                    CHshow4(MonitorActivity.lightdatalong);
                                }
                            } else if (MonitorActivity.datatype.equals("float")) {
                                modeimage2(MonitorActivity.lightdata2, MonitorActivity.moderoadnum);
                                CHshow2(MonitorActivity.lightdata2);
                            } else if (MonitorActivity.datatype.equals("double")) {
                                modeimage3(MonitorActivity.lightdata3, MonitorActivity.moderoadnum);
                                CHshow3(MonitorActivity.lightdata3);
                            }

                        }

                    }

                    break;
                    default:break;
            }
        }
    };


    private class Mytesk extends TimerTask {
        @Override
        public void run() {
            Message message=new Message();

            message.what=1;
            handler.sendMessage(message);

        }
    }

    //模块图片和配置信息显示
    @SuppressLint("SetTextI18n")
    public void modeimageshow(){
        int road=0;
        int []x=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        currentmodetype=modemessage.getString("modetype","err");
        if(currentmodetype.equals("CPU")){//显示CPU模块图和配置数据
            modeimage.setImageResource(R.drawable.cpumode);
            light1.setVisibility(View.INVISIBLE);
            light2.setVisibility(View.INVISIBLE);
            light3.setVisibility(View.INVISIBLE);
            light4.setVisibility(View.INVISIBLE);
            light5.setVisibility(View.INVISIBLE);
            light6.setVisibility(View.INVISIBLE);
            light7.setVisibility(View.INVISIBLE);
            light8.setVisibility(View.INVISIBLE);
            light9.setVisibility(View.INVISIBLE);
            light10.setVisibility(View.INVISIBLE);
            light11.setVisibility(View.INVISIBLE);
            light12.setVisibility(View.INVISIBLE);
            light13.setVisibility(View.INVISIBLE);
            light14.setVisibility(View.INVISIBLE);
            light15.setVisibility(View.INVISIBLE);
            light16.setVisibility(View.INVISIBLE);
            lightpower1.setVisibility(View.INVISIBLE);
            lightpower2.setVisibility(View.INVISIBLE);
            moderoad.setText("");
            recdatatype.setText("");
            modecodetext.setText("型号：" + modemessage.getString("modecode",""));
            modeintroduction.setText(modemessage.getString("modebeizhu",""));

            startaddress.setText("设备起始地址：" + modemessage.getInt("start_add",1));
            mode_add.setText("当前模块地址：" + modemessage.getInt("data_add",1));

            modetype.setText("模块类型：" + currentmodetype);
            xulietext.setText("模块序号：" + modemessage.getInt("modexulie",1));
            CHshowCPU(MonitorActivity.cpudata,MonitorActivity.cpuproperties);
        }
        else{   //其他模块类型
            modeimage.setImageResource(R.drawable.modetype3);
            light1.setVisibility(View.VISIBLE);
            light2.setVisibility(View.VISIBLE);
            light3.setVisibility(View.VISIBLE);
            light4.setVisibility(View.VISIBLE);
            light5.setVisibility(View.VISIBLE);
            light6.setVisibility(View.VISIBLE);
            light7.setVisibility(View.VISIBLE);
            light8.setVisibility(View.VISIBLE);
            light9.setVisibility(View.VISIBLE);
            light10.setVisibility(View.VISIBLE);
            light11.setVisibility(View.VISIBLE);
            light12.setVisibility(View.VISIBLE);
            light13.setVisibility(View.VISIBLE);
            light14.setVisibility(View.VISIBLE);
            light15.setVisibility(View.VISIBLE);
            light16.setVisibility(View.VISIBLE);
            lightpower1.setVisibility(View.VISIBLE);
            lightpower2.setVisibility(View.VISIBLE);
            road=modemessage.getInt("moderoadnum",0);
            switch (road){
                case 16:
                    light1.setColorFilter(Color.BLACK);
                    light2.setColorFilter(Color.BLACK);
                    light3.setColorFilter(Color.BLACK);
                    light4.setColorFilter(Color.BLACK);
                    light5.setColorFilter(Color.BLACK);
                    light6.setColorFilter(Color.BLACK);
                    light7.setColorFilter(Color.BLACK);
                    light8.setColorFilter(Color.BLACK);
                    light9.setColorFilter(Color.BLACK);
                    light10.setColorFilter(Color.BLACK);
                    light11.setColorFilter(Color.BLACK);
                    light12.setColorFilter(Color.BLACK);
                    light13.setColorFilter(Color.BLACK);
                    light14.setColorFilter(Color.BLACK);
                    light15.setColorFilter(Color.BLACK);
                    light16.setColorFilter(Color.BLACK);
                    System.out.println("通道数"+modemessage.getInt("moderoadnum"));
                    break;
                case 8:
                    System.out.println("通道数"+modemessage.getInt("moderoadnum"));
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    light1.setColorFilter(Color.BLACK);
                    light3.setColorFilter(Color.BLACK);
                    light5.setColorFilter(Color.BLACK);
                    light7.setColorFilter(Color.BLACK);
                    light9.setColorFilter(Color.BLACK);
                    light11.setColorFilter(Color.BLACK);
                    light13.setColorFilter(Color.BLACK);
                    light15.setColorFilter(Color.BLACK);
                    break;
                case 4:
                    System.out.println("通道数"+modemessage.getInt("moderoadnum"));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    light1.setColorFilter(Color.BLACK);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light5.setColorFilter(Color.BLACK);
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light9.setColorFilter(Color.BLACK);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light13.setColorFilter(Color.BLACK);
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 2:
                    System.out.println("通道数"+modemessage.getInt("moderoadnum"));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    light1.setColorFilter(Color.BLACK);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    light5.setColorFilter(getResources().getColor(R.color.gray1));
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light9.setColorFilter(Color.BLACK);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    light13.setColorFilter(getResources().getColor(R.color.gray1));
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                default:break;
            }
            modecodetext.setText("型号：" + modemessage.getString("modecode",""));
            moderoad.setText("模块通路数：" + modemessage.getInt("moderoadnum",16));
            modetype.setText("模块类型：" + currentmodetype);
            xulietext.setText("模块序号：" + modemessage.getInt("modexulie",1));
            startaddress.setText("设备起始地址：" + modemessage.getInt("start_add",1));
            mode_add.setText("当前模块地址：" + modemessage.getInt("data_add",1));
            modeintroduction.setText(modemessage.getString("modebeizhu",""));

            CHshow(x);
        }
    }

    //CPU模块的界面显示效果
    @SuppressLint("SetTextI18n")
    public void modeCPUimage(){
        modeimage.setImageResource(R.drawable.cpumode);
        light1.setVisibility(View.INVISIBLE);
        light2.setVisibility(View.INVISIBLE);
        light3.setVisibility(View.INVISIBLE);
        light4.setVisibility(View.INVISIBLE);
        light5.setVisibility(View.INVISIBLE);
        light6.setVisibility(View.INVISIBLE);
        light7.setVisibility(View.INVISIBLE);
        light8.setVisibility(View.INVISIBLE);
        light9.setVisibility(View.INVISIBLE);
        light10.setVisibility(View.INVISIBLE);
        light11.setVisibility(View.INVISIBLE);
        light12.setVisibility(View.INVISIBLE);
        light13.setVisibility(View.INVISIBLE);
        light14.setVisibility(View.INVISIBLE);
        light15.setVisibility(View.INVISIBLE);
        light16.setVisibility(View.INVISIBLE);
        lightpower1.setVisibility(View.INVISIBLE);
        lightpower2.setVisibility(View.INVISIBLE);

        /*if(MainPage.islinksuccess==1&&MainPage.ismonitorstart==true){
            connectlight.setColorFilter(Color.GREEN);
        }
        else if(MainPage.ismonitorstart==true&&MainPage.islinksuccess!=1) connectlight.setColorFilter(Color.RED);
        else connectlight.setColorFilter(Color.BLACK);*/

            moderoad.setText("");
            recdatatype.setText("");
            modecodetext.setText("型号：" + MonitorActivity.modecode);
            modeintroduction.setText(MonitorActivity.modebeizhu);
            startaddress.setText("设备起始地址：" + DiviceinformActivity.Starting_add);
            mode_add.setText("当前模块地址" + MonitorActivity.data_address);
            modetype.setText("模块类型：" + MonitorActivity.modeltype);
            xulietext.setText("模块序号：" + MonitorActivity.modexulie);
    }

    //其他界面的显示效果
    @SuppressLint("SetTextI18n")
    public void othermodeimage(int road){
        modeimage.setImageResource(R.drawable.modetype3);
        light1.setVisibility(View.VISIBLE);
        light2.setVisibility(View.VISIBLE);
        light3.setVisibility(View.VISIBLE);
        light4.setVisibility(View.VISIBLE);
        light5.setVisibility(View.VISIBLE);
        light6.setVisibility(View.VISIBLE);
        light7.setVisibility(View.VISIBLE);
        light8.setVisibility(View.VISIBLE);
        light9.setVisibility(View.VISIBLE);
        light10.setVisibility(View.VISIBLE);
        light11.setVisibility(View.VISIBLE);
        light12.setVisibility(View.VISIBLE);
        light13.setVisibility(View.VISIBLE);
        light14.setVisibility(View.VISIBLE);
        light15.setVisibility(View.VISIBLE);
        light16.setVisibility(View.VISIBLE);
        lightpower1.setVisibility(View.VISIBLE);
        lightpower2.setVisibility(View.VISIBLE);
        switch (road){
            case 16:
                break;
            case 8:
                light2.setColorFilter(getResources().getColor(R.color.gray1));
                light4.setColorFilter(getResources().getColor(R.color.gray1));
                light6.setColorFilter(getResources().getColor(R.color.gray1));
                light8.setColorFilter(getResources().getColor(R.color.gray1));
                light10.setColorFilter(getResources().getColor(R.color.gray1));
                light12.setColorFilter(getResources().getColor(R.color.gray1));
                light14.setColorFilter(getResources().getColor(R.color.gray1));
                light16.setColorFilter(getResources().getColor(R.color.gray1));
                break;
            case 4:
                light4.setColorFilter(getResources().getColor(R.color.gray1));
                light8.setColorFilter(getResources().getColor(R.color.gray1));
                light12.setColorFilter(getResources().getColor(R.color.gray1));
                light16.setColorFilter(getResources().getColor(R.color.gray1));
                break;
            case 2:
                light8.setColorFilter(getResources().getColor(R.color.gray1));
                light16.setColorFilter(getResources().getColor(R.color.gray1));
                break;
            default:break;
        }
            modecodetext.setText("型号：" + MonitorActivity.modecode);
            moderoad.setText("模块通路数" + MonitorActivity.moderoadnum);
            modetype.setText("模块类型：" + MonitorActivity.modeltype);
            xulietext.setText("模块序号：" + MonitorActivity.modexulie);
            startaddress.setText("设备起始地址：" + DiviceinformActivity.Starting_add);
            mode_add.setText("当前模块地址" + MonitorActivity.data_address);
            modeintroduction.setText(MonitorActivity.modebeizhu);


    }



    //CPU模块的文本数据显示
    public void CHshowCPU(byte[] x,String[][] cpupeizhi){
        byte[] temp1=new byte[2];
        int x1,x2,x3,x4;
        int[] temp2=new int[16];
        if(cpupeizhi!=null) {
            x1 = Integer.parseInt(cpupeizhi[0][1]);  //DI
            x2 = Integer.parseInt(cpupeizhi[1][1]);  //DO
            x3 = Integer.parseInt(cpupeizhi[2][1]);  //AI
            x4 = Integer.parseInt(cpupeizhi[3][1]);  //AO

            if(x1<=8&&x2<=8) {
                temp1[0] = x[0];
                temp1[1] = x[1];
                temp2 = DataDeal.bytetobinary(temp1);
                String s1 = "";
                for (int i = 0; i < temp2.length; i++) {
                    s1 = s1 + temp2[i] + ",";
                }
                CH1.setText(s1.substring(0, x1 * 2));
                CH5.setText(s1.substring(16, 16 + x2 * 2));
            }
            if(cpupeizhi[2][2].equals("byte")){
                String s2="";
                for(int i=0;i<x3;i++){
                    s2=s2+x[2+i]+",";
                }
                CH9.setText(s2);
            }
            if(cpupeizhi[3][2].equals("byte")){
                String s3="";
                for(int i=0;i<x4;i++){
                    s3=s3+x[2+x3+i]+",";
                    CH13.setText(s3);
                }

            }
        }
        T1.setText("DI");
        CH2.setText("");T2.setText("");
        CH3.setText("");T3.setText("");
        CH4.setText("");T4.setText("");

        T5.setText("DO");
        CH6.setText("");T6.setText("");
        CH7.setText("");T7.setText("");
        CH8.setText("");T8.setText("");

        T9.setText("AI");
        CH10.setText("");T10.setText("");
        CH11.setText("");T11.setText("");
        CH12.setText("");T12.setText("");

        T13.setText("AO");
        CH14.setText("");T14.setText("");
        CH15.setText("");T15.setText("");
        CH16.setText("");T16.setText("");


    }

    //根据设置的通路数和显示模式显示灯，红色为灯不亮，黄色为灯亮，灰色为灯不存在
    public void Modeimage(int [] x,int road,int datatype){     //图片显示,x代表数组数据。road代表通路数,datatype代表数据类型，0bit,1byte,2,short,3int  4种情况
        modeimage.setImageResource(R.drawable.modetype3);
        light1.setVisibility(View.VISIBLE);
        light2.setVisibility(View.VISIBLE);
        light3.setVisibility(View.VISIBLE);
        light4.setVisibility(View.VISIBLE);
        light5.setVisibility(View.VISIBLE);
        light6.setVisibility(View.VISIBLE);
        light7.setVisibility(View.VISIBLE);
        light8.setVisibility(View.VISIBLE);
        light9.setVisibility(View.VISIBLE);
        light10.setVisibility(View.VISIBLE);
        light11.setVisibility(View.VISIBLE);
        light12.setVisibility(View.VISIBLE);
        light13.setVisibility(View.VISIBLE);
        light14.setVisibility(View.VISIBLE);
        light15.setVisibility(View.VISIBLE);
        light16.setVisibility(View.VISIBLE);
        lightpower1.setVisibility(View.VISIBLE);
        lightpower2.setVisibility(View.VISIBLE);
        if(MainPage.islinksuccess==1&&MainPage.ismonitorstart==true){        //连接正常
            connectlight.setColorFilter(Color.GREEN);
            if(road==16) {
                switch (datatype) {
                    case 0:  //bit
                        if (x[0] == 1) light1.setColorFilter(Color.YELLOW);
                        else if (x[0] == 0) light1.setColorFilter(Color.RED);
                        if (x[1] == 1) light2.setColorFilter(Color.YELLOW);
                        else if (x[1] == 0) light2.setColorFilter(Color.RED);
                        if (x[2] == 1) light3.setColorFilter(Color.YELLOW);
                        else if (x[2] == 0) light3.setColorFilter(Color.RED);
                        if (x[3] == 1) light4.setColorFilter(Color.YELLOW);
                        else if (x[3] == 0) light4.setColorFilter(Color.RED);
                        if (x[4] == 1) light5.setColorFilter(Color.YELLOW);
                        else if (x[4] == 0) light5.setColorFilter(Color.RED);
                        if (x[5] == 1) light6.setColorFilter(Color.YELLOW);
                        else if (x[5] == 0) light6.setColorFilter(Color.RED);
                        if (x[6] == 1) light7.setColorFilter(Color.YELLOW);
                        else if (x[6] == 0) light7.setColorFilter(Color.RED);
                        if (x[7] == 1) light8.setColorFilter(Color.YELLOW);
                        else if (x[7] == 0) light8.setColorFilter(Color.RED);
                        if (x[8] == 1) light9.setColorFilter(Color.YELLOW);
                        else if (x[8] == 0) light9.setColorFilter(Color.RED);
                        if (x[9] == 1) light10.setColorFilter(Color.YELLOW);
                        else if (x[9] == 0) light10.setColorFilter(Color.RED);
                        if (x[10] == 1) light11.setColorFilter(Color.YELLOW);
                        else if (x[10] == 0) light11.setColorFilter(Color.RED);
                        if (x[11] == 1) light12.setColorFilter(Color.YELLOW);
                        else if (x[11] == 0) light12.setColorFilter(Color.RED);
                        if (x[12] == 1) light13.setColorFilter(Color.YELLOW);
                        else if (x[12] == 0) light13.setColorFilter(Color.RED);
                        if (x[13] == 1) light14.setColorFilter(Color.YELLOW);
                        else if (x[13] == 0) light14.setColorFilter(Color.RED);
                        if (x[14] == 1) light15.setColorFilter(Color.YELLOW);
                        else if (x[14] == 0) light15.setColorFilter(Color.RED);
                        if (x[15] == 1) light16.setColorFilter(Color.YELLOW);
                        else if (x[15] == 0) light16.setColorFilter(Color.RED);
                        break;
                    case 1:   //byte
                        if (x[0] >lightlightingbyte) light1.setColorFilter(Color.YELLOW);
                        else  light1.setColorFilter(Color.RED);
                        if (x[1] >lightlightingbyte) light2.setColorFilter(Color.YELLOW);
                        else  light2.setColorFilter(Color.RED);
                        if (x[2] >lightlightingbyte) light3.setColorFilter(Color.YELLOW);
                        else  light3.setColorFilter(Color.RED);
                        if (x[3] >lightlightingbyte) light4.setColorFilter(Color.YELLOW);
                        else  light4.setColorFilter(Color.RED);
                        if (x[4] >lightlightingbyte) light5.setColorFilter(Color.YELLOW);
                        else  light5.setColorFilter(Color.RED);
                        if (x[5] >lightlightingbyte) light6.setColorFilter(Color.YELLOW);
                        else  light6.setColorFilter(Color.RED);
                        if (x[6] >lightlightingbyte) light7.setColorFilter(Color.YELLOW);
                        else  light7.setColorFilter(Color.RED);
                        if (x[7] >lightlightingbyte) light8.setColorFilter(Color.YELLOW);
                        else  light8.setColorFilter(Color.RED);
                        if (x[8] >lightlightingbyte) light9.setColorFilter(Color.YELLOW);
                        else  light9.setColorFilter(Color.RED);
                        if (x[9] >lightlightingbyte) light10.setColorFilter(Color.YELLOW);
                        else  light10.setColorFilter(Color.RED);
                        if (x[10] >lightlightingbyte) light11.setColorFilter(Color.YELLOW);
                        else  light11.setColorFilter(Color.RED);
                        if (x[11] >lightlightingbyte) light12.setColorFilter(Color.YELLOW);
                        else  light12.setColorFilter(Color.RED);
                        if (x[12] >lightlightingbyte) light13.setColorFilter(Color.YELLOW);
                        else  light13.setColorFilter(Color.RED);
                        if (x[13] >lightlightingbyte) light14.setColorFilter(Color.YELLOW);
                        else  light14.setColorFilter(Color.RED);
                        if (x[14] >lightlightingbyte) light15.setColorFilter(Color.YELLOW);
                        else  light15.setColorFilter(Color.RED);
                        if (x[15] >lightlightingbyte) light16.setColorFilter(Color.YELLOW);
                        else  light16.setColorFilter(Color.RED);
                        break;

                    case 2:      //short
                        if (x[0] >lightlightingshort) light1.setColorFilter(Color.YELLOW);
                        else  light1.setColorFilter(Color.RED);
                        if (x[1] >lightlightingshort) light2.setColorFilter(Color.YELLOW);
                        else  light2.setColorFilter(Color.RED);
                        if (x[2] >lightlightingshort) light3.setColorFilter(Color.YELLOW);
                        else  light3.setColorFilter(Color.RED);
                        if (x[3] >lightlightingshort) light4.setColorFilter(Color.YELLOW);
                        else  light4.setColorFilter(Color.RED);
                        if (x[4] >lightlightingshort) light5.setColorFilter(Color.YELLOW);
                        else  light5.setColorFilter(Color.RED);
                        if (x[5] >lightlightingshort) light6.setColorFilter(Color.YELLOW);
                        else  light6.setColorFilter(Color.RED);
                        if (x[6] >lightlightingshort) light7.setColorFilter(Color.YELLOW);
                        else  light7.setColorFilter(Color.RED);
                        if (x[7] >lightlightingshort) light8.setColorFilter(Color.YELLOW);
                        else  light8.setColorFilter(Color.RED);
                        if (x[8] >lightlightingshort) light9.setColorFilter(Color.YELLOW);
                        else  light9.setColorFilter(Color.RED);
                        if (x[9] >lightlightingshort) light10.setColorFilter(Color.YELLOW);
                        else  light10.setColorFilter(Color.RED);
                        if (x[10] >lightlightingshort) light11.setColorFilter(Color.YELLOW);
                        else  light11.setColorFilter(Color.RED);
                        if (x[11] >lightlightingshort) light12.setColorFilter(Color.YELLOW);
                        else  light12.setColorFilter(Color.RED);
                        if (x[12] >lightlightingshort) light13.setColorFilter(Color.YELLOW);
                        else  light13.setColorFilter(Color.RED);
                        if (x[13] >lightlightingshort) light14.setColorFilter(Color.YELLOW);
                        else  light14.setColorFilter(Color.RED);
                        if (x[14] >lightlightingshort) light15.setColorFilter(Color.YELLOW);
                        else  light15.setColorFilter(Color.RED);
                        if (x[15] >lightlightingshort) light16.setColorFilter(Color.YELLOW);
                        else  light16.setColorFilter(Color.RED);
                        break;
                    case 3:    //int
                        if (x[0] >lightlightingint) light1.setColorFilter(Color.YELLOW);
                        else  light1.setColorFilter(Color.RED);
                        if (x[1] >lightlightingint) light2.setColorFilter(Color.YELLOW);
                        else  light2.setColorFilter(Color.RED);
                        if (x[2] >lightlightingint) light3.setColorFilter(Color.YELLOW);
                        else  light3.setColorFilter(Color.RED);
                        if (x[3] >lightlightingint) light4.setColorFilter(Color.YELLOW);
                        else  light4.setColorFilter(Color.RED);
                        if (x[4] >lightlightingint) light5.setColorFilter(Color.YELLOW);
                        else  light5.setColorFilter(Color.RED);
                        if (x[5] >lightlightingint) light6.setColorFilter(Color.YELLOW);
                        else  light6.setColorFilter(Color.RED);
                        if (x[6] >lightlightingint) light7.setColorFilter(Color.YELLOW);
                        else  light7.setColorFilter(Color.RED);
                        if (x[7] >lightlightingint) light8.setColorFilter(Color.YELLOW);
                        else  light8.setColorFilter(Color.RED);
                        if (x[8] >lightlightingint) light9.setColorFilter(Color.YELLOW);
                        else  light9.setColorFilter(Color.RED);
                        if (x[9] >lightlightingint) light10.setColorFilter(Color.YELLOW);
                        else  light10.setColorFilter(Color.RED);
                        if (x[10] >lightlightingint) light11.setColorFilter(Color.YELLOW);
                        else  light11.setColorFilter(Color.RED);
                        if (x[11] >lightlightingint) light12.setColorFilter(Color.YELLOW);
                        else  light12.setColorFilter(Color.RED);
                        if (x[12] >lightlightingint) light13.setColorFilter(Color.YELLOW);
                        else  light13.setColorFilter(Color.RED);
                        if (x[13] >lightlightingint) light14.setColorFilter(Color.YELLOW);
                        else  light14.setColorFilter(Color.RED);
                        if (x[14] >lightlightingint) light15.setColorFilter(Color.YELLOW);
                        else  light15.setColorFilter(Color.RED);
                        if (x[15] >lightlightingint) light16.setColorFilter(Color.YELLOW);
                        else  light16.setColorFilter(Color.RED);
                        break;
                    default:break;
                }
            }
            else if(road==8){
                switch (datatype) {
                    case 0:   //bit
                        if (x[0] ==1) light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[1] == 1) light3.setColorFilter(Color.YELLOW);
                        else light3.setColorFilter(Color.RED);
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[2] == 1) light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[3] == 1) light7.setColorFilter(Color.YELLOW);
                        else light7.setColorFilter(Color.RED);
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[4] == 1) light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[5] == 1) light11.setColorFilter(Color.YELLOW);
                        else light11.setColorFilter(Color.RED);
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[6] == 1) light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[7] == 1) light15.setColorFilter(Color.YELLOW);
                        else light15.setColorFilter(Color.RED);
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 1:    //byte
                        if (x[0] >lightlightingbyte) light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[1] >lightlightingbyte) light3.setColorFilter(Color.YELLOW);
                        else light3.setColorFilter(Color.RED);
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[2]>lightlightingbyte) light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[3] >lightlightingbyte) light7.setColorFilter(Color.YELLOW);
                        else light7.setColorFilter(Color.RED);
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[4] >lightlightingbyte) light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[5] >lightlightingbyte) light11.setColorFilter(Color.YELLOW);
                        else light11.setColorFilter(Color.RED);
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[6] >lightlightingbyte) light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[7] >lightlightingbyte) light15.setColorFilter(Color.YELLOW);
                        else light15.setColorFilter(Color.RED);
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 2:         //short
                        if (x[0] >lightlightingshort) light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[1] >lightlightingshort) light3.setColorFilter(Color.YELLOW);
                        else light3.setColorFilter(Color.RED);
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[2]>lightlightingshort) light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[3] >lightlightingshort) light7.setColorFilter(Color.YELLOW);
                        else light7.setColorFilter(Color.RED);
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[4] >lightlightingshort) light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[5] >lightlightingshort) light11.setColorFilter(Color.YELLOW);
                        else light11.setColorFilter(Color.RED);
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[6] >lightlightingshort) light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[7] >lightlightingshort) light15.setColorFilter(Color.YELLOW);
                        else light15.setColorFilter(Color.RED);
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 3:     //int
                        if (x[0] >lightlightingint) light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[1] >lightlightingint) light3.setColorFilter(Color.YELLOW);
                        else light3.setColorFilter(Color.RED);
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[2]>lightlightingint) light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[3] >lightlightingint) light7.setColorFilter(Color.YELLOW);
                        else light7.setColorFilter(Color.RED);
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[4] >lightlightingint) light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[5] >lightlightingint) light11.setColorFilter(Color.YELLOW);
                        else light11.setColorFilter(Color.RED);
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[6] >lightlightingint) light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        if (x[7] >lightlightingint) light15.setColorFilter(Color.YELLOW);
                        else light15.setColorFilter(Color.RED);
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;

                    default:break;
                }
            }
            else if(road==4){
                switch (datatype){
                    case 0:
                        if(x[0]==1)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]==1)light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[2]==1)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[3]==1)light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 1: //byte
                        if(x[0]>lightlightingbyte)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]>lightlightingbyte)light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[2]>lightlightingbyte)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[3]>lightlightingbyte)light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 2:    //short
                        if(x[0]>lightlightingshort)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]>lightlightingshort)light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[2]>lightlightingshort)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[3]>lightlightingshort)light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;

                    case 3:  //int
                        if(x[0]>lightlightingint)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]>lightlightingint)light5.setColorFilter(Color.YELLOW);
                        else light5.setColorFilter(Color.RED);
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[2]>lightlightingint)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[3]>lightlightingint)light13.setColorFilter(Color.YELLOW);
                        else light13.setColorFilter(Color.RED);
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    default:break;
                }

            }
            else if(road==2){
                switch (datatype){
                    case 0:  //bit
                        if(x[0]==1)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        light5.setColorFilter(getResources().getColor(R.color.gray1));
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]==1)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        light13.setColorFilter(getResources().getColor(R.color.gray1));
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 1:  //byte
                        if(x[0]>lightlightingbyte)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        light5.setColorFilter(getResources().getColor(R.color.gray1));
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]>lightlightingbyte)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        light13.setColorFilter(getResources().getColor(R.color.gray1));
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 2:
                        if(x[0]>lightlightingshort)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        light5.setColorFilter(getResources().getColor(R.color.gray1));
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]>lightlightingshort)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        light13.setColorFilter(getResources().getColor(R.color.gray1));
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    case 3:
                        if(x[0]>lightlightingint)light1.setColorFilter(Color.YELLOW);
                        else light1.setColorFilter(Color.RED);
                        light2.setColorFilter(getResources().getColor(R.color.gray1));
                        light3.setColorFilter(getResources().getColor(R.color.gray1));
                        light4.setColorFilter(getResources().getColor(R.color.gray1));
                        light5.setColorFilter(getResources().getColor(R.color.gray1));
                        light6.setColorFilter(getResources().getColor(R.color.gray1));
                        light7.setColorFilter(getResources().getColor(R.color.gray1));
                        light8.setColorFilter(getResources().getColor(R.color.gray1));
                        if(x[1]>lightlightingint)light9.setColorFilter(Color.YELLOW);
                        else light9.setColorFilter(Color.RED);
                        light10.setColorFilter(getResources().getColor(R.color.gray1));
                        light11.setColorFilter(getResources().getColor(R.color.gray1));
                        light12.setColorFilter(getResources().getColor(R.color.gray1));
                        light13.setColorFilter(getResources().getColor(R.color.gray1));
                        light14.setColorFilter(getResources().getColor(R.color.gray1));
                        light15.setColorFilter(getResources().getColor(R.color.gray1));
                        light16.setColorFilter(getResources().getColor(R.color.gray1));
                        break;
                    default:break;
                }
            }
        }
        else if(MainPage.ismonitorstart==true&&MainPage.islinksuccess!=1) connectlight.setColorFilter(Color.RED);
        else connectlight.setColorFilter(Color.BLACK);

    }

    public void modeimage2(float[] x,int road){
        modeimage.setImageResource(R.drawable.modetype3);
        light1.setVisibility(View.VISIBLE);
        light2.setVisibility(View.VISIBLE);
        light3.setVisibility(View.VISIBLE);
        light4.setVisibility(View.VISIBLE);
        light5.setVisibility(View.VISIBLE);
        light6.setVisibility(View.VISIBLE);
        light7.setVisibility(View.VISIBLE);
        light8.setVisibility(View.VISIBLE);
        light9.setVisibility(View.VISIBLE);
        light10.setVisibility(View.VISIBLE);
        light11.setVisibility(View.VISIBLE);
        light12.setVisibility(View.VISIBLE);
        light13.setVisibility(View.VISIBLE);
        light14.setVisibility(View.VISIBLE);
        light15.setVisibility(View.VISIBLE);
        light16.setVisibility(View.VISIBLE);
        lightpower1.setVisibility(View.VISIBLE);
        lightpower2.setVisibility(View.VISIBLE);
        if(MainPage.islinksuccess==1&&MainPage.ismonitorstart==true){
            connectlight.setColorFilter(Color.GREEN);
            switch (road){
                case 2:
                    if(x[0]>lightlightingfloat)light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    light5.setColorFilter(getResources().getColor(R.color.gray1));
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[1]>lightlightingfloat)light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    light13.setColorFilter(getResources().getColor(R.color.gray1));
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 4:
                    if(x[0]>lightlightingfloat)light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[1]>lightlightingfloat)light5.setColorFilter(Color.YELLOW);
                    else light5.setColorFilter(Color.RED);
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[2]>lightlightingfloat)light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[3]>lightlightingfloat)light13.setColorFilter(Color.YELLOW);
                    else light13.setColorFilter(Color.RED);
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 8:
                    if (x[0] >lightlightingfloat) light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[1] >lightlightingfloat) light3.setColorFilter(Color.YELLOW);
                    else light3.setColorFilter(Color.RED);
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[2]>lightlightingfloat) light5.setColorFilter(Color.YELLOW);
                    else light5.setColorFilter(Color.RED);
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[3] >lightlightingfloat) light7.setColorFilter(Color.YELLOW);
                    else light7.setColorFilter(Color.RED);
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[4] >lightlightingfloat) light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[5] >lightlightingfloat) light11.setColorFilter(Color.YELLOW);
                    else light11.setColorFilter(Color.RED);
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[6] >lightlightingfloat) light13.setColorFilter(Color.YELLOW);
                    else light13.setColorFilter(Color.RED);
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[7] >lightlightingfloat) light15.setColorFilter(Color.YELLOW);
                    else light15.setColorFilter(Color.RED);
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 16:
                    if (x[0] >lightlightingfloat) light1.setColorFilter(Color.YELLOW);
                    else  light1.setColorFilter(Color.RED);
                    if (x[1] >lightlightingfloat) light2.setColorFilter(Color.YELLOW);
                    else  light2.setColorFilter(Color.RED);
                    if (x[2] >lightlightingfloat) light3.setColorFilter(Color.YELLOW);
                    else  light3.setColorFilter(Color.RED);
                    if (x[3] >lightlightingfloat) light4.setColorFilter(Color.YELLOW);
                    else  light4.setColorFilter(Color.RED);
                    if (x[4] >lightlightingfloat) light5.setColorFilter(Color.YELLOW);
                    else  light5.setColorFilter(Color.RED);
                    if (x[5] >lightlightingfloat) light6.setColorFilter(Color.YELLOW);
                    else  light6.setColorFilter(Color.RED);
                    if (x[6] >lightlightingfloat) light7.setColorFilter(Color.YELLOW);
                    else  light7.setColorFilter(Color.RED);
                    if (x[7] >lightlightingfloat) light8.setColorFilter(Color.YELLOW);
                    else  light8.setColorFilter(Color.RED);
                    if (x[8] >lightlightingfloat) light9.setColorFilter(Color.YELLOW);
                    else  light9.setColorFilter(Color.RED);
                    if (x[9] >lightlightingfloat) light10.setColorFilter(Color.YELLOW);
                    else  light10.setColorFilter(Color.RED);
                    if (x[10] >lightlightingfloat) light11.setColorFilter(Color.YELLOW);
                    else  light11.setColorFilter(Color.RED);
                    if (x[11] >lightlightingfloat) light12.setColorFilter(Color.YELLOW);
                    else  light12.setColorFilter(Color.RED);
                    if (x[12] >lightlightingfloat) light13.setColorFilter(Color.YELLOW);
                    else  light13.setColorFilter(Color.RED);
                    if (x[13] >lightlightingfloat) light14.setColorFilter(Color.YELLOW);
                    else  light14.setColorFilter(Color.RED);
                    if (x[14] >lightlightingfloat) light15.setColorFilter(Color.YELLOW);
                    else  light15.setColorFilter(Color.RED);
                    if (x[15] >lightlightingfloat) light16.setColorFilter(Color.YELLOW);
                    else  light16.setColorFilter(Color.RED);
                    break;
                default:break;

            }
        }
        else if(MainPage.ismonitorstart==true&&MainPage.islinksuccess!=1) connectlight.setColorFilter(Color.RED);
        else connectlight.setColorFilter(Color.BLACK);
    }

    public void modeimage3(double[] x,int road){
        modeimage.setImageResource(R.drawable.modetype3);
        light1.setVisibility(View.VISIBLE);
        light2.setVisibility(View.VISIBLE);
        light3.setVisibility(View.VISIBLE);
        light4.setVisibility(View.VISIBLE);
        light5.setVisibility(View.VISIBLE);
        light6.setVisibility(View.VISIBLE);
        light7.setVisibility(View.VISIBLE);
        light8.setVisibility(View.VISIBLE);
        light9.setVisibility(View.VISIBLE);
        light10.setVisibility(View.VISIBLE);
        light11.setVisibility(View.VISIBLE);
        light12.setVisibility(View.VISIBLE);
        light13.setVisibility(View.VISIBLE);
        light14.setVisibility(View.VISIBLE);
        light15.setVisibility(View.VISIBLE);
        light16.setVisibility(View.VISIBLE);
        lightpower1.setVisibility(View.VISIBLE);
        lightpower2.setVisibility(View.VISIBLE);
        if(MainPage.islinksuccess==1&&MainPage.ismonitorstart==true){
            connectlight.setColorFilter(Color.GREEN);
            switch (road){
                case 2:
                    if(x[0]>lightlightingdouble)light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    light5.setColorFilter(getResources().getColor(R.color.gray1));
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[1]>lightlightingdouble)light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    light13.setColorFilter(getResources().getColor(R.color.gray1));
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 4:
                    if(x[0]>lightlightingdouble)light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[1]>lightlightingdouble)light5.setColorFilter(Color.YELLOW);
                    else light5.setColorFilter(Color.RED);
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[2]>lightlightingdouble)light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[3]>lightlightingdouble)light13.setColorFilter(Color.YELLOW);
                    else light13.setColorFilter(Color.RED);
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 8:
                    if (x[0] >lightlightingdouble) light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[1] >lightlightingdouble) light3.setColorFilter(Color.YELLOW);
                    else light3.setColorFilter(Color.RED);
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[2]>lightlightingdouble) light5.setColorFilter(Color.YELLOW);
                    else light5.setColorFilter(Color.RED);
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[3] >lightlightingdouble) light7.setColorFilter(Color.YELLOW);
                    else light7.setColorFilter(Color.RED);
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[4] >lightlightingdouble) light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[5] >lightlightingdouble) light11.setColorFilter(Color.YELLOW);
                    else light11.setColorFilter(Color.RED);
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[6] >lightlightingdouble) light13.setColorFilter(Color.YELLOW);
                    else light13.setColorFilter(Color.RED);
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[7] >lightlightingdouble) light15.setColorFilter(Color.YELLOW);
                    else light15.setColorFilter(Color.RED);
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 16:
                    if (x[0] >lightlightingdouble) light1.setColorFilter(Color.YELLOW);
                    else  light1.setColorFilter(Color.RED);
                    if (x[1] >lightlightingdouble) light2.setColorFilter(Color.YELLOW);
                    else  light2.setColorFilter(Color.RED);
                    if (x[2] >lightlightingdouble) light3.setColorFilter(Color.YELLOW);
                    else  light3.setColorFilter(Color.RED);
                    if (x[3] >lightlightingdouble) light4.setColorFilter(Color.YELLOW);
                    else  light4.setColorFilter(Color.RED);
                    if (x[4] >lightlightingdouble) light5.setColorFilter(Color.YELLOW);
                    else  light5.setColorFilter(Color.RED);
                    if (x[5] >lightlightingdouble) light6.setColorFilter(Color.YELLOW);
                    else  light6.setColorFilter(Color.RED);
                    if (x[6] >lightlightingdouble) light7.setColorFilter(Color.YELLOW);
                    else  light7.setColorFilter(Color.RED);
                    if (x[7] >lightlightingdouble) light8.setColorFilter(Color.YELLOW);
                    else  light8.setColorFilter(Color.RED);
                    if (x[8] >lightlightingdouble) light9.setColorFilter(Color.YELLOW);
                    else  light9.setColorFilter(Color.RED);
                    if (x[9] >lightlightingdouble) light10.setColorFilter(Color.YELLOW);
                    else  light10.setColorFilter(Color.RED);
                    if (x[10] >lightlightingdouble) light11.setColorFilter(Color.YELLOW);
                    else  light11.setColorFilter(Color.RED);
                    if (x[11] >lightlightingdouble) light12.setColorFilter(Color.YELLOW);
                    else  light12.setColorFilter(Color.RED);
                    if (x[12] >lightlightingdouble) light13.setColorFilter(Color.YELLOW);
                    else  light13.setColorFilter(Color.RED);
                    if (x[13] >lightlightingdouble) light14.setColorFilter(Color.YELLOW);
                    else  light14.setColorFilter(Color.RED);
                    if (x[14] >lightlightingdouble) light15.setColorFilter(Color.YELLOW);
                    else  light15.setColorFilter(Color.RED);
                    if (x[15] >lightlightingdouble) light16.setColorFilter(Color.YELLOW);
                    else  light16.setColorFilter(Color.RED);
                    break;
                default:break;

            }

        }
        else if(MainPage.ismonitorstart==true&&MainPage.islinksuccess!=1) connectlight.setColorFilter(Color.RED);
        else connectlight.setColorFilter(Color.BLACK);
    }

    public void modeimage4(long[] x,int road){
        modeimage.setImageResource(R.drawable.modetype3);
        light1.setVisibility(View.VISIBLE);
        light2.setVisibility(View.VISIBLE);
        light3.setVisibility(View.VISIBLE);
        light4.setVisibility(View.VISIBLE);
        light5.setVisibility(View.VISIBLE);
        light6.setVisibility(View.VISIBLE);
        light7.setVisibility(View.VISIBLE);
        light8.setVisibility(View.VISIBLE);
        light9.setVisibility(View.VISIBLE);
        light10.setVisibility(View.VISIBLE);
        light11.setVisibility(View.VISIBLE);
        light12.setVisibility(View.VISIBLE);
        light13.setVisibility(View.VISIBLE);
        light14.setVisibility(View.VISIBLE);
        light15.setVisibility(View.VISIBLE);
        light16.setVisibility(View.VISIBLE);
        lightpower1.setVisibility(View.VISIBLE);
        lightpower2.setVisibility(View.VISIBLE);
        if(MainPage.islinksuccess==1&&MainPage.ismonitorstart==true){
            connectlight.setColorFilter(Color.GREEN);
            switch (road){
                case 2:
                    if(x[0]>lightlightingint)light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    light5.setColorFilter(getResources().getColor(R.color.gray1));
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[1]>lightlightingint)light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    light13.setColorFilter(getResources().getColor(R.color.gray1));
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 4:
                    if(x[0]>lightlightingint)light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    light3.setColorFilter(getResources().getColor(R.color.gray1));
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[1]>lightlightingint)light5.setColorFilter(Color.YELLOW);
                    else light5.setColorFilter(Color.RED);
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    light7.setColorFilter(getResources().getColor(R.color.gray1));
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[2]>lightlightingint)light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    light11.setColorFilter(getResources().getColor(R.color.gray1));
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    if(x[3]>lightlightingint)light13.setColorFilter(Color.YELLOW);
                    else light13.setColorFilter(Color.RED);
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    light15.setColorFilter(getResources().getColor(R.color.gray1));
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 8:
                    if (x[0] >lightlightingint) light1.setColorFilter(Color.YELLOW);
                    else light1.setColorFilter(Color.RED);
                    light2.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[1] >lightlightingint) light3.setColorFilter(Color.YELLOW);
                    else light3.setColorFilter(Color.RED);
                    light4.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[2]>lightlightingint) light5.setColorFilter(Color.YELLOW);
                    else light5.setColorFilter(Color.RED);
                    light6.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[3] >lightlightingint) light7.setColorFilter(Color.YELLOW);
                    else light7.setColorFilter(Color.RED);
                    light8.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[4] >lightlightingint) light9.setColorFilter(Color.YELLOW);
                    else light9.setColorFilter(Color.RED);
                    light10.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[5] >lightlightingint) light11.setColorFilter(Color.YELLOW);
                    else light11.setColorFilter(Color.RED);
                    light12.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[6] >lightlightingint) light13.setColorFilter(Color.YELLOW);
                    else light13.setColorFilter(Color.RED);
                    light14.setColorFilter(getResources().getColor(R.color.gray1));
                    if (x[7] >lightlightingint) light15.setColorFilter(Color.YELLOW);
                    else light15.setColorFilter(Color.RED);
                    light16.setColorFilter(getResources().getColor(R.color.gray1));
                    break;
                case 16:
                    if (x[0] >lightlightingint) light1.setColorFilter(Color.YELLOW);
                    else  light1.setColorFilter(Color.RED);
                    if (x[1] >lightlightingint) light2.setColorFilter(Color.YELLOW);
                    else  light2.setColorFilter(Color.RED);
                    if (x[2] >lightlightingint) light3.setColorFilter(Color.YELLOW);
                    else  light3.setColorFilter(Color.RED);
                    if (x[3] >lightlightingint) light4.setColorFilter(Color.YELLOW);
                    else  light4.setColorFilter(Color.RED);
                    if (x[4] >lightlightingint) light5.setColorFilter(Color.YELLOW);
                    else  light5.setColorFilter(Color.RED);
                    if (x[5] >lightlightingint) light6.setColorFilter(Color.YELLOW);
                    else  light6.setColorFilter(Color.RED);
                    if (x[6] >lightlightingint) light7.setColorFilter(Color.YELLOW);
                    else  light7.setColorFilter(Color.RED);
                    if (x[7] >lightlightingint) light8.setColorFilter(Color.YELLOW);
                    else  light8.setColorFilter(Color.RED);
                    if (x[8] >lightlightingint) light9.setColorFilter(Color.YELLOW);
                    else  light9.setColorFilter(Color.RED);
                    if (x[9] >lightlightingint) light10.setColorFilter(Color.YELLOW);
                    else  light10.setColorFilter(Color.RED);
                    if (x[10] >lightlightingint) light11.setColorFilter(Color.YELLOW);
                    else  light11.setColorFilter(Color.RED);
                    if (x[11] >lightlightingint) light12.setColorFilter(Color.YELLOW);
                    else  light12.setColorFilter(Color.RED);
                    if (x[12] >lightlightingint) light13.setColorFilter(Color.YELLOW);
                    else  light13.setColorFilter(Color.RED);
                    if (x[13] >lightlightingint) light14.setColorFilter(Color.YELLOW);
                    else  light14.setColorFilter(Color.RED);
                    if (x[14] >lightlightingint) light15.setColorFilter(Color.YELLOW);
                    else  light15.setColorFilter(Color.RED);
                    if (x[15] >lightlightingint) light16.setColorFilter(Color.YELLOW);
                    else  light16.setColorFilter(Color.RED);
                    break;
                default:break;
            }
        }
        else if(MainPage.ismonitorstart==true&&MainPage.islinksuccess!=1) connectlight.setColorFilter(Color.RED);
        else connectlight.setColorFilter(Color.BLACK);
    }

    //数据显示结果在设备图像右侧
    public void CHshow(int []x){
        int num1=modemessage.getInt("moderoadnum",0);
        if(num1==16) {
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            if(x[1]<0)CH2.setText(x[1]+"");else CH2.setText(" "+x[1]);
            if(x[2]<0)CH3.setText(x[2]+"");else CH3.setText(" "+x[2]);
            if(x[3]<0)CH4.setText(x[3]+"");else CH4.setText(" "+x[3]);
            if(x[4]<0)CH5.setText(x[4]+"");else CH5.setText(" "+x[4]);
            if(x[5]<0)CH6.setText(x[5]+"");else CH6.setText(" "+x[5]);
            if(x[6]<0)CH7.setText(x[6]+"");else CH7.setText(" "+x[6]);
            if(x[7]<0)CH8.setText(x[7]+"");else CH8.setText(" "+x[7]);
            if(x[8]<0)CH9.setText(x[8]+"");else CH9.setText(" "+x[8]);
            if(x[9]<0)CH10.setText(x[9]+"");else CH10.setText(" "+x[9]);
            if(x[10]<0)CH11.setText(x[10]+"");else CH11.setText(" "+x[10]);
            if(x[11]<0)CH12.setText(x[11]+"");else CH12.setText(" "+x[11]);
            if(x[12]<0)CH13.setText(x[12]+"");else CH13.setText(" "+x[12]);
            if(x[13]<0)CH14.setText(x[13]+"");else CH14.setText(" "+x[13]);
            if(x[14]<0)CH15.setText(x[14]+"");else CH15.setText(" "+x[14]);
            if(x[15]<0)CH16.setText(x[15]+"");else CH16.setText(" "+x[15]);
            T1.setText("CH1");
            T2.setText("CH2");
            T3.setText("CH3");
            T4.setText("CH4");
            T5.setText("CH5");
            T6.setText("CH6");
            T7.setText("CH7");
            T8.setText("CH8");
            T9.setText("CH9");
            T10.setText("CH10");
            T11.setText("CH11");
            T12.setText("CH12");
            T13.setText("CH13");
            T14.setText("CH14");
            T15.setText("CH15");
            T16.setText("CH16");
        }
        else if(num1==8){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            if(x[1]<0)CH3.setText(x[1]+"");else CH3.setText(" "+x[1]);
            T3.setText("CH2");
            CH4.setText("");T4.setText("");
            if(x[2]<0)CH5.setText(x[2]+"");else CH5.setText(" "+x[2]);
            T5.setText("CH3");
            CH6.setText("");T6.setText("");
            if(x[3]<0)CH7.setText(x[3]+"");else CH7.setText(" "+x[3]);
            T7.setText("CH4");
            CH8.setText("");T8.setText("");
            if(x[4]<0)CH9.setText(x[4]+"");else CH9.setText(" "+x[4]);
            T9.setText("CH5");
            CH10.setText("");T10.setText("");
            if(x[5]<0)CH11.setText(x[5]+"");else CH11.setText(" "+x[5]);
            T11.setText("CH6");
            CH12.setText("");T12.setText("");
            if(x[6]<0)CH13.setText(x[6]+"");else CH13.setText(" "+x[6]);
            T13.setText("CH7");
            CH14.setText("");T14.setText("");
            if(x[7]<0)CH15.setText(x[7]+"");else CH15.setText(" "+x[7]);
            T15.setText("CH8");
            CH16.setText("");T16.setText("");
        }
        else if(num1==4){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            if(x[1]<0)CH5.setText(x[1]+"");else CH5.setText(" "+x[1]);
            T5.setText("CH2");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[2]<0)CH9.setText(x[2]+"");else CH9.setText(" "+x[2]);
            T9.setText("CH3");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            if(x[3]<0)CH13.setText(x[3]+"");else CH13.setText(" "+x[3]);
            T13.setText("CH4");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
        else if(num1==2){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            CH5.setText("");T5.setText("");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[1]<0)CH9.setText(x[1]+"");else CH9.setText(" "+x[1]);
            T9.setText("CH2");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            CH13.setText("");T13.setText("");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
    }

    //数据显示结果在设备图像右侧 ,float型
    public void CHshow2(float []x){
        int num2=modemessage.getInt("moderoadnum",0);
        if(num2==16) {
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            if(x[1]<0)CH2.setText(x[1]+"");else CH2.setText(" "+x[1]);
            if(x[2]<0)CH3.setText(x[2]+"");else CH3.setText(" "+x[2]);
            if(x[3]<0)CH4.setText(x[3]+"");else CH4.setText(" "+x[3]);
            if(x[4]<0)CH5.setText(x[4]+"");else CH5.setText(" "+x[4]);
            if(x[5]<0)CH6.setText(x[5]+"");else CH6.setText(" "+x[5]);
            if(x[6]<0)CH7.setText(x[6]+"");else CH7.setText(" "+x[6]);
            if(x[7]<0)CH8.setText(x[7]+"");else CH8.setText(" "+x[7]);
            if(x[8]<0)CH9.setText(x[8]+"");else CH9.setText(" "+x[8]);
            if(x[9]<0)CH10.setText(x[9]+"");else CH10.setText(" "+x[9]);
            if(x[10]<0)CH11.setText(x[10]+"");else CH11.setText(" "+x[10]);
            if(x[11]<0)CH12.setText(x[11]+"");else CH12.setText(" "+x[11]);
            if(x[12]<0)CH13.setText(x[12]+"");else CH13.setText(" "+x[12]);
            if(x[13]<0)CH14.setText(x[13]+"");else CH14.setText(" "+x[13]);
            if(x[14]<0)CH15.setText(x[14]+"");else CH15.setText(" "+x[14]);
            if(x[15]<0)CH16.setText(x[15]+"");else CH16.setText(" "+x[15]);
            T1.setText("CH1");
            T2.setText("CH2");
            T3.setText("CH3");
            T4.setText("CH4");
            T5.setText("CH5");
            T6.setText("CH6");
            T7.setText("CH7");
            T8.setText("CH8");
            T9.setText("CH9");
            T10.setText("CH10");
            T11.setText("CH11");
            T12.setText("CH12");
            T13.setText("CH13");
            T14.setText("CH14");
            T15.setText("CH15");
            T16.setText("CH16");

        }
        else if(num2==8){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            if(x[1]<0)CH3.setText(x[1]+"");else CH3.setText(" "+x[1]);
            T3.setText("CH2");
            CH4.setText("");T4.setText("");
            if(x[2]<0)CH5.setText(x[2]+"");else CH5.setText(" "+x[2]);
            T5.setText("CH3");
            CH6.setText("");T6.setText("");
            if(x[3]<0)CH7.setText(x[3]+"");else CH7.setText(" "+x[3]);
            T7.setText("CH4");
            CH8.setText("");T8.setText("");
            if(x[4]<0)CH9.setText(x[4]+"");else CH9.setText(" "+x[4]);
            T9.setText("CH5");
            CH10.setText("");T10.setText("");
            if(x[5]<0)CH11.setText(x[5]+"");else CH11.setText(" "+x[5]);
            T11.setText("CH6");
            CH12.setText("");T12.setText("");
            if(x[6]<0)CH13.setText(x[6]+"");else CH13.setText(" "+x[6]);
            T13.setText("CH7");
            CH14.setText("");T14.setText("");
            if(x[7]<0)CH15.setText(x[7]+"");else CH15.setText(" "+x[7]);
            T15.setText("CH8");
            CH16.setText("");T16.setText("");
        }
        else if(num2==4){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            if(x[1]<0)CH5.setText(x[1]+"");else CH5.setText(" "+x[1]);
            T5.setText("CH2");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[2]<0)CH9.setText(x[2]+"");else CH9.setText(" "+x[2]);
            T9.setText("CH3");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            if(x[3]<0)CH13.setText(x[3]+"");else CH13.setText(" "+x[3]);
            T13.setText("CH4");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
        else if(num2==2){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            CH5.setText("");T5.setText("");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[1]<0)CH9.setText(x[1]+"");else CH9.setText(" "+x[1]);
            T9.setText("CH2");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            CH13.setText("");T13.setText("");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
    }

    //double型
    public void CHshow3(double []x){
        int num3=modemessage.getInt("moderoadnum",0);
        if(num3==16) {
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            if(x[1]<0)CH2.setText(x[1]+"");else CH2.setText(" "+x[1]);
            if(x[2]<0)CH3.setText(x[2]+"");else CH3.setText(" "+x[2]);
            if(x[3]<0)CH4.setText(x[3]+"");else CH4.setText(" "+x[3]);
            if(x[4]<0)CH5.setText(x[4]+"");else CH5.setText(" "+x[4]);
            if(x[5]<0)CH6.setText(x[5]+"");else CH6.setText(" "+x[5]);
            if(x[6]<0)CH7.setText(x[6]+"");else CH7.setText(" "+x[6]);
            if(x[7]<0)CH8.setText(x[7]+"");else CH8.setText(" "+x[7]);
            if(x[8]<0)CH9.setText(x[8]+"");else CH9.setText(" "+x[8]);
            if(x[9]<0)CH10.setText(x[9]+"");else CH10.setText(" "+x[9]);
            if(x[10]<0)CH11.setText(x[10]+"");else CH11.setText(" "+x[10]);
            if(x[11]<0)CH12.setText(x[11]+"");else CH12.setText(" "+x[11]);
            if(x[12]<0)CH13.setText(x[12]+"");else CH13.setText(" "+x[12]);
            if(x[13]<0)CH14.setText(x[13]+"");else CH14.setText(" "+x[13]);
            if(x[14]<0)CH15.setText(x[14]+"");else CH15.setText(" "+x[14]);
            if(x[15]<0)CH16.setText(x[15]+"");else CH16.setText(" "+x[15]);
            T1.setText("CH1");
            T2.setText("CH2");
            T3.setText("CH3");
            T4.setText("CH4");
            T5.setText("CH5");
            T6.setText("CH6");
            T7.setText("CH7");
            T8.setText("CH8");
            T9.setText("CH9");
            T10.setText("CH10");
            T11.setText("CH11");
            T12.setText("CH12");
            T13.setText("CH13");
            T14.setText("CH14");
            T15.setText("CH15");
            T16.setText("CH16");

        }
        else if(num3==8){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            if(x[1]<0)CH3.setText(x[1]+"");else CH3.setText(" "+x[1]);
            T3.setText("CH2");
            CH4.setText("");T4.setText("");
            if(x[2]<0)CH5.setText(x[2]+"");else CH5.setText(" "+x[2]);
            T5.setText("CH3");
            CH6.setText("");T6.setText("");
            if(x[3]<0)CH7.setText(x[3]+"");else CH7.setText(" "+x[3]);
            T7.setText("CH4");
            CH8.setText("");T8.setText("");
            if(x[4]<0)CH9.setText(x[4]+"");else CH9.setText(" "+x[4]);
            T9.setText("CH5");
            CH10.setText("");T10.setText("");
            if(x[5]<0)CH11.setText(x[5]+"");else CH11.setText(" "+x[5]);
            T11.setText("CH6");
            CH12.setText("");T12.setText("");
            if(x[6]<0)CH13.setText(x[6]+"");else CH13.setText(" "+x[6]);
            T13.setText("CH7");
            CH14.setText("");T14.setText("");
            if(x[7]<0)CH15.setText(x[7]+"");else CH15.setText(" "+x[7]);
            T15.setText("CH8");
            CH16.setText("");T16.setText("");
        }
        else if(num3==4){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            if(x[1]<0)CH5.setText(x[1]+"");else CH5.setText(" "+x[1]);
            T5.setText("CH2");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[2]<0)CH9.setText(x[2]+"");else CH9.setText(" "+x[2]);
            T9.setText("CH3");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            if(x[3]<0)CH13.setText(x[3]+"");else CH13.setText(" "+x[3]);
            T13.setText("CH4");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
        else if(num3==2){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            CH5.setText("");T5.setText("");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[1]<0)CH9.setText(x[1]+"");else CH9.setText(" "+x[1]);
            T9.setText("CH2");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            CH13.setText("");T13.setText("");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
    }

    //无符号int显示需要的long型
    public void CHshow4(long []x){
        int num4=modemessage.getInt("moderoadnum",0);
        if(num4==16) {
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            if(x[1]<0)CH2.setText(x[1]+"");else CH2.setText(" "+x[1]);
            if(x[2]<0)CH3.setText(x[2]+"");else CH3.setText(" "+x[2]);
            if(x[3]<0)CH4.setText(x[3]+"");else CH4.setText(" "+x[3]);
            if(x[4]<0)CH5.setText(x[4]+"");else CH5.setText(" "+x[4]);
            if(x[5]<0)CH6.setText(x[5]+"");else CH6.setText(" "+x[5]);
            if(x[6]<0)CH7.setText(x[6]+"");else CH7.setText(" "+x[6]);
            if(x[7]<0)CH8.setText(x[7]+"");else CH8.setText(" "+x[7]);
            if(x[8]<0)CH9.setText(x[8]+"");else CH9.setText(" "+x[8]);
            if(x[9]<0)CH10.setText(x[9]+"");else CH10.setText(" "+x[9]);
            if(x[10]<0)CH11.setText(x[10]+"");else CH11.setText(" "+x[10]);
            if(x[11]<0)CH12.setText(x[11]+"");else CH12.setText(" "+x[11]);
            if(x[12]<0)CH13.setText(x[12]+"");else CH13.setText(" "+x[12]);
            if(x[13]<0)CH14.setText(x[13]+"");else CH14.setText(" "+x[13]);
            if(x[14]<0)CH15.setText(x[14]+"");else CH15.setText(" "+x[14]);
            if(x[15]<0)CH16.setText(x[15]+"");else CH16.setText(" "+x[15]);
            T1.setText("CH1");
            T2.setText("CH2");
            T3.setText("CH3");
            T4.setText("CH4");
            T5.setText("CH5");
            T6.setText("CH6");
            T7.setText("CH7");
            T8.setText("CH8");
            T9.setText("CH9");
            T10.setText("CH10");
            T11.setText("CH11");
            T12.setText("CH12");
            T13.setText("CH13");
            T14.setText("CH14");
            T15.setText("CH15");
            T16.setText("CH16");

        }
        else if(num4==8){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            if(x[1]<0)CH3.setText(x[1]+"");else CH3.setText(" "+x[1]);
            T3.setText("CH2");
            CH4.setText("");T4.setText("");
            if(x[2]<0)CH5.setText(x[2]+"");else CH5.setText(" "+x[2]);
            T5.setText("CH3");
            CH6.setText("");T6.setText("");
            if(x[3]<0)CH7.setText(x[3]+"");else CH7.setText(" "+x[3]);
            T7.setText("CH4");
            CH8.setText("");T8.setText("");
            if(x[4]<0)CH9.setText(x[4]+"");else CH9.setText(" "+x[4]);
            T9.setText("CH5");
            CH10.setText("");T10.setText("");
            if(x[5]<0)CH11.setText(x[5]+"");else CH11.setText(" "+x[5]);
            T11.setText("CH6");
            CH12.setText("");T12.setText("");
            if(x[6]<0)CH13.setText(x[6]+"");else CH13.setText(" "+x[6]);
            T13.setText("CH7");
            CH14.setText("");T14.setText("");
            if(x[7]<0)CH15.setText(x[7]+"");else CH15.setText(" "+x[7]);
            T15.setText("CH8");
            CH16.setText("");T16.setText("");
        }
        else if(num4==4){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            if(x[1]<0)CH5.setText(x[1]+"");else CH5.setText(" "+x[1]);
            T5.setText("CH2");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[2]<0)CH9.setText(x[2]+"");else CH9.setText(" "+x[2]);
            T9.setText("CH3");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            if(x[3]<0)CH13.setText(x[3]+"");else CH13.setText(" "+x[3]);
            T13.setText("CH4");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
        else if(num4==2){
            if(x[0]<0)CH1.setText(x[0]+"");else CH1.setText(" "+x[0]);
            T1.setText("CH1");
            CH2.setText("");T2.setText("");
            CH3.setText("");T3.setText("");
            CH4.setText("");T4.setText("");
            CH5.setText("");T5.setText("");
            CH6.setText("");T6.setText("");
            CH7.setText("");T7.setText("");
            CH8.setText("");T8.setText("");
            if(x[1]<0)CH9.setText(x[1]+"");else CH9.setText(" "+x[1]);
            T9.setText("CH2");
            CH10.setText("");T10.setText("");
            CH11.setText("");T11.setText("");
            CH12.setText("");T12.setText("");
            CH13.setText("");T13.setText("");
            CH14.setText("");T14.setText("");
            CH15.setText("");T15.setText("");
            CH16.setText("");T16.setText("");
        }
    }

    public static DiviceModeFragment newInstance(Bundle args) {
        DiviceModeFragment fragment = new DiviceModeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

