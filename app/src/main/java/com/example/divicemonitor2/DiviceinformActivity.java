package com.example.divicemonitor2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.divicemonitor2.tools.DropEditText;
import com.google.zxing.client.android.MNScanManager;
import com.google.zxing.client.android.model.MNScanConfig;
import com.google.zxing.client.android.other.MNScanCallback;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

//设备信息界面（首页），负责读取设备信息和配置，填写设备信息等
public class DiviceinformActivity extends Fragment {
    private static final String SECTION_STRING = "diviceinform";

    //shared快速存储
    public static SharedPreferences mypreferences1;     //最多存储6组设备数据
    public static SharedPreferences.Editor editor1;
    public static SharedPreferences mypreferences2;
    public static SharedPreferences.Editor editor2;
    public static SharedPreferences mypreferences3;
    public static SharedPreferences.Editor editor3;
    public static SharedPreferences mypreferences4;
    public static SharedPreferences.Editor editor4;
    public static SharedPreferences mypreferences5;
    public static SharedPreferences.Editor editor5;
    public static SharedPreferences mypreferences6;
    public static SharedPreferences.Editor editor6;

    //设备信息
    public static String diviceID="";    //设备ID
    public static String diviceIP="";           //设备IP
    public static String divicename="";
    public static int diviceport;
    public static String registime="";  //注册时间
    public static int divicettcount;    //模块总数
    public static String serverIP="192.168.0.57";   //服务器IP
    public static String[] diviceIDinf=new String[100];      //模块ID，转为string存储在preference中
    public static int[] divicexulieinf=new int[100];   //模块序列号，转为string存储在preference中
    public static int Starting_add=1;    //设备监控的起始地址
    public static int diviceonline=1;   //设备状态，0离线，1在线
    public static String usecompany;  //使用单位

    //界面输入栏等控件
    public static DropEditText diviceID_string;  //ID输入栏
    public static EditText divicename_string;  //设备名称输入栏
    public static EditText diviceIP_string;  //设备IP输入栏
    public static EditText diviceport_int;  //设备端口输入栏
    public static EditText divicetotalcount_int; //模块总数输入栏
    public static EditText diviceregistime_string;   //设备入网时间
    public static TextView errtext; //错误提示
    public static ImageView diviceimage;  //设备图片




    //辅助变量
    private int scantype=0;   //扫描模式，0代表扫描设备ID，1代表扫描子模块ID
    public int dijimode=1;   //扫描子模块时的序号
    private ArrayAdapter<String> det_adapter;  //下拉edittext框的string适配器

    //fragment
    public static DiviceinformActivity newInstance(String sectionNumber) {
        DiviceinformActivity fragment = new DiviceinformActivity();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //添加右上角菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.diviceinformtoolbarmenu,menu);
    }
    //菜单事件
    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //扫码获取ID，若保存了会自动补全信息，没有保存的信息匹配则自动连接服务器获取，超时则提示手动输入    未实现自动补全
        if(id==R.id.action_divicescan){
            scantype=0;
            MNScanConfig scanConfig = new MNScanConfig.Builder()
                    //设置完成震动
                    .isShowVibrate(false)
                    //扫描完成声音
                    .isShowBeep(true)
                    //显示相册功能
                    .isShowPhotoAlbum(true)
                    //显示闪光灯
                    .isShowLightController(true)
                    //自定义文案
                    .setScanHintText("请将二维码放入框中")
                    //自定义文案颜色
                    .setScanHintTextColor("#FFFF00")
                    //自定义文案大小（单位sp）
                    .setScanHintTextSize(16)
                    //扫描线的颜色
                    .setScanColor("#FFFF00")
                    //是否显示缩放控制器
                    .isShowZoomController(true)
                    //显示缩放控制器位置
                    .setZoomControllerLocation(MNScanConfig.ZoomControllerLocation.Bottom)
                    //扫描线样式
                    .setLaserStyle(MNScanConfig.LaserStyle.Grid)
                    //背景颜色
                    .setBgColor("#33FF0000")
                    //网格扫描线的列数
                    .setGridScanLineColumn(30)
                    //网格高度
                    .setGridScanLineHeight(150)
                    //高度偏移值（单位px）+向上偏移，-向下偏移
                    .setScanFrameHeightOffsets(150)
                    //是否全屏范围扫描
                    .setFullScreenScan(true)
                    //是否支持多二维码同时扫出,默认false,多二维码状态不支持条形码
                    .setSupportMultiQRCode(true)
                    .builder();
            MNScanManager.startScan(getActivity(), scanConfig, new MNScanCallback() {
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    switch (resultCode) {
                        case MNScanManager.RESULT_SUCCESS:
                            String resultSuccess = data.getStringExtra(MNScanManager.INTENT_KEY_RESULT_SUCCESS);
                            Toast.makeText(getActivity(), resultSuccess,Toast.LENGTH_SHORT).show();
                            if(!resultSuccess.equals(""))
                                scanresult(scantype,resultSuccess);
                            break;
                        case MNScanManager.RESULT_FAIL:
                            String resultError = data.getStringExtra(MNScanManager.INTENT_KEY_RESULT_ERROR);
                            Toast.makeText(getActivity(), resultError,Toast.LENGTH_SHORT).show();
                            break;
                        case MNScanManager.RESULT_CANCLE:
                            Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

        //扫码添加子模块，自动比对
        else if(id==R.id.action_scanmode){
            scantype=1;
            MNScanConfig scanConfig = new MNScanConfig.Builder()
                    //设置完成震动
                    .isShowVibrate(false)
                    //扫描完成声音
                    .isShowBeep(true)
                    //显示相册功能
                    .isShowPhotoAlbum(true)
                    //显示闪光灯
                    .isShowLightController(true)
                    //自定义文案
                    .setScanHintText("请将二维码放入框中")
                    //自定义文案颜色
                    .setScanHintTextColor("#FFFF00")
                    //自定义文案大小（单位sp）
                    .setScanHintTextSize(16)
                    //扫描线的颜色
                    .setScanColor("#FFFF00")
                    //是否显示缩放控制器
                    .isShowZoomController(true)
                    //显示缩放控制器位置
                    .setZoomControllerLocation(MNScanConfig.ZoomControllerLocation.Bottom)
                    //扫描线样式
                    .setLaserStyle(MNScanConfig.LaserStyle.Grid)
                    //背景颜色
                    .setBgColor("#33FF0000")
                    //网格扫描线的列数
                    .setGridScanLineColumn(30)
                    //网格高度
                    .setGridScanLineHeight(150)
                    //高度偏移值（单位px）+向上偏移，-向下偏移
                    .setScanFrameHeightOffsets(150)
                    //是否全屏范围扫描
                    .setFullScreenScan(true)
                    //是否支持多二维码同时扫出,默认false,多二维码状态不支持条形码
                    .setSupportMultiQRCode(true)
                    .builder();
            MNScanManager.startScan(getActivity(), scanConfig, new MNScanCallback() {
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    switch (resultCode) {
                        case MNScanManager.RESULT_SUCCESS:
                            String resultSuccess = data.getStringExtra(MNScanManager.INTENT_KEY_RESULT_SUCCESS);
                            Toast.makeText(getActivity(), resultSuccess,Toast.LENGTH_SHORT).show();
                            if(!resultSuccess.equals(""))
                                scanresult(scantype,resultSuccess);
                            break;
                        case MNScanManager.RESULT_FAIL:
                            String resultError = data.getStringExtra(MNScanManager.INTENT_KEY_RESULT_ERROR);
                            Toast.makeText(getActivity(), resultError,Toast.LENGTH_SHORT).show();
                            break;
                        case MNScanManager.RESULT_CANCLE:
                            Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

        else if(id==R.id.action_connecttype){


        }

        else if(id==R.id.action_netchange){

        }

        return super.onOptionsItemSelected(item);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_diviceinform,container,false);
        initview(view);
        setHasOptionsMenu(true);    //添加右上角菜单
        initpreferences();
        dropedittextinit();   //下拉框
        showinform();       //打开APP显示上次连接的设备，初始化信息
        System.out.println("打开了diviceinform界面");

        //点击设备图片，即打开设备监控界面
        diviceimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                divicettcount=Integer.parseInt(0+divicetotalcount_int.getText().toString());
                if(diviceIDinf[0]==null){
                    Toast.makeText(getActivity(), "没有已保存的设备", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(getActivity(),MonitorActivity.class);
                    startActivity(intent);
                }
            }
        });

        //刚打开APP弹出是否连接上一次设备,监控上次设备的IO流
        if(!MainPage.isAPPstart && MainPage.islastdivicestore && diviceID_string.getText().length()>2){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("是否连接上次设备");
            builder.setMessage("设备ID：" + diviceID_string.getText()+"\n"+"设备IP：" + diviceIP_string.getText()+"\n"+"设备端口：" + diviceport_int.getText()+"\n"+"模块个数"+ divicetotalcount_int.getText()+"" +
                    "\n"+"首模块："+ diviceIDinf[0]);
            builder.setPositiveButton("确定",null);
            builder.setNegativeButton("取消",null);
            final AlertDialog dialog = builder.create();
            dialog.show();
            //确定按钮
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!"".equals(diviceIP_string.getText())&&(Integer.parseInt(divicetotalcount_int.getText().toString())+0>0)&&diviceIDinf[0]!=null) {
                        MainPage.connecttype=2;
                        //设备监控开始

                        MainPage.ismonitorstart=true;
                        MainPage.isthreadstart=true;
                        MainPage.threadmain.start();
                        Toast.makeText(getActivity(), "已连接，注意保持手机连接设备网络！！！", Toast.LENGTH_SHORT).show();

                        DiviceinformActivity.errtext.setText("");
                        MainPage.image_link.setColorFilter(getResources().getColor(R.color.blue));
                        MainPage.linktext.setText("断开");
                        MainPage.isclicklink=true;
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "信息有误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //取消按钮
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        MainPage.isAPPstart=true;  //该弹窗只在APP启动时打开一次


        dijimode=1;


        //Timer timer = new Timer();    //handler与timer循环
        //timer.scheduleAtFixedRate(new Mytesk(), 1, 3000);   //每3秒更新连接灯的状态与错误提示

        return view;
    }

    //初始化diviceinform界面控件
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initview(View view){
        System.out.println("界面初始化");
        diviceID_string= view.findViewById(R.id.dropEditText10);
        divicename_string= view.findViewById(R.id.editTextTextPersonName9);
        diviceIP_string= view.findViewById(R.id.editTextTextPersonName10);
        diviceport_int= view.findViewById(R.id.editTextTextPersonName11);
        divicetotalcount_int=(EditText)view.findViewById(R.id.editTextTextPersonName13);
        diviceregistime_string=(EditText)view.findViewById(R.id.editTextTextPersonName12);
        errtext=(TextView)view.findViewById(R.id.textView38);

        diviceimage=(ImageView)view.findViewById(R.id.imageView1);


    }

    //初始化快速prefer存储
    private void initpreferences(){
        mypreferences1=getActivity().getSharedPreferences("divice1",Context.MODE_PRIVATE);
        editor1=mypreferences1.edit();
        mypreferences2=getActivity().getSharedPreferences("divice2",Context.MODE_PRIVATE);
        editor2=mypreferences2.edit();
        mypreferences3=getActivity().getSharedPreferences("divice3",Context.MODE_PRIVATE);
        editor3=mypreferences3.edit();
        mypreferences4=getActivity().getSharedPreferences("divice4",Context.MODE_PRIVATE);
        editor4=mypreferences4.edit();
        mypreferences5=getActivity().getSharedPreferences("divice5",Context.MODE_PRIVATE);
        editor5=mypreferences5.edit();
        mypreferences6=getActivity().getSharedPreferences("divice6",Context.MODE_PRIVATE);
        editor6=mypreferences6.edit();
    }

    //扫描二维码结果处理
    public void scanresult(int scantype,String scanres){
        final String[] temp = {""};
        if(scantype==0){
            diviceID=scanres;
            qingkong();
            diviceID_string.setText(diviceID);
            if(idmatchinform(diviceID))System.out.println("匹配成功");

        }
        else if(scantype==1){
            Context context=getContext();
            LinearLayout layout=new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText editText1 = new EditText(getActivity());
            final EditText editText2=new EditText(getActivity());
            editText1.setText(dijimode+"");
            editText2.setText(scanres);
            layout.addView(editText2);    //模块ID
            layout.addView(editText1);    //第几模块
            AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
            builder1.setTitle("扫描子模块");
            builder1.setMessage("子模块ID与位置序号：");
            builder1.setView(layout);
            builder1.setPositiveButton("确定", null);
            builder1.setNegativeButton("取消", null);
            final AlertDialog dialog1 = builder1.create();
            dialog1.show();
            dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText1.getText().length() != 0) {
                        if (Integer.parseInt(editText1.getText().toString())>0) {
                            dijimode=Integer.parseInt(editText1.getText().toString());
                            temp[0] =editText2.getText().toString();
                            if(diviceIDinf[dijimode-1].equals(temp[0])){
                                dijimode++;
                                dialog1.dismiss();
                            }
                            else{
                                AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
                                builder2.setMessage(temp[0] +"和已保存ID："+diviceIDinf[dijimode-1]+"不同，是否修改");
                                builder2.setPositiveButton("确定", null);
                                builder2.setNegativeButton("取消", null);
                                final AlertDialog dialog2 = builder2.create();
                                dialog2.show();
                                dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        diviceIDinf[dijimode-1]= temp[0];
                                        if(dijimode>divicettcount){
                                            divicettcount=dijimode;
                                        }
                                        save(diviceID);
                                        dijimode++;
                                        dialog2.dismiss();
                                    }
                                });
                                dialog2.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });
                                dialog1.dismiss();
                            }
                        } else
                            Toast.makeText(getActivity(), "不能小于0", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });
        }
    }

    //扫码获取ID和保存的设备信息匹配，若匹配则自动补全
    public boolean idmatchinform(String id){
        int j;
        String[] temp1;
        String id1=mypreferences1.getString("diviceID","");
        String id2=mypreferences2.getString("diviceID","");
        String id3=mypreferences3.getString("diviceID","");
        String id4=mypreferences4.getString("diviceID","");
        String id5=mypreferences5.getString("diviceID","");
        String id6=mypreferences6.getString("diviceID","");
        if (id.equals(id1)) {
            diviceID=id;
            divicename=mypreferences1.getString("divicename","");
            diviceIP=mypreferences1.getString("diviceIP", "");
            diviceport=mypreferences1.getInt("diviceport", 0);
            divicettcount=mypreferences1.getInt("divicetotalcount", 0);
            registime=mypreferences1.getString("registime", "");
            tianxie();
            if(mypreferences1.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences1.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences1.getString("divicexulieinf","").split(",");
            for(j=0;j<temp1.length;j++){
                divicexulieinf[j] = Integer.parseInt(temp1[j]);
            }
            Starting_add = mypreferences1.getInt("startaddress", 1);
            return true;
        }
        else if (id.equals(id2)) {
            diviceID=id;
            divicename=mypreferences2.getString("divicename","");
            diviceIP=mypreferences2.getString("diviceIP", "");
            diviceport=mypreferences2.getInt("diviceport", 0);
            divicettcount=mypreferences2.getInt("divicetotalcount", 0);
            registime=mypreferences2.getString("registime", "");
            tianxie();
            if(mypreferences2.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences2.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences2.getString("divicexulieinf","").split(",");
            for(j=0;j<temp1.length;j++){
                divicexulieinf[j] = Integer.parseInt(temp1[j]);
            }
            Starting_add = mypreferences2.getInt("startaddress", 1);
            return true;
        }
        else if (id.equals(id3)) {
            diviceID=id;
            divicename=mypreferences3.getString("divicename","");
            diviceIP=mypreferences3.getString("diviceIP", "");
            diviceport=mypreferences3.getInt("diviceport", 0);
            divicettcount=mypreferences3.getInt("divicetotalcount", 0);
            registime=mypreferences3.getString("registime", "");
            tianxie();
            if(mypreferences3.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences3.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences3.getString("divicexulieinf","").split(",");
            for(j=0;j<temp1.length;j++){
                divicexulieinf[j] = Integer.parseInt(temp1[j]);
            }
            Starting_add = mypreferences3.getInt("startaddress", 1);
            return true;
        }
        else if (id.equals(id4)) {
            diviceID=id;
            divicename=mypreferences4.getString("divicename","");
            diviceIP=mypreferences4.getString("diviceIP", "");
            diviceport=mypreferences4.getInt("diviceport", 0);
            divicettcount=mypreferences4.getInt("divicetotalcount", 0);
            registime=mypreferences4.getString("registime", "");
            tianxie();
            if(mypreferences4.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences4.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences4.getString("divicexulieinf","").split(",");
            for(j=0;j<temp1.length;j++){
                divicexulieinf[j] = Integer.parseInt(temp1[j]);
            }
            Starting_add = mypreferences4.getInt("startaddress", 1);
            return true;
        }
        else if (id.equals(id5)) {
            diviceID=id;
            divicename=mypreferences5.getString("divicename","");
            diviceIP=mypreferences5.getString("diviceIP", "");
            diviceport=mypreferences5.getInt("diviceport", 0);
            divicettcount=mypreferences5.getInt("divicetotalcount", 0);
            registime=mypreferences5.getString("registime", "");
            tianxie();
            if(mypreferences5.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences5.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences5.getString("divicexulieinf","").split(",");
            for(j=0;j<temp1.length;j++){
                divicexulieinf[j] = Integer.parseInt(temp1[j]);
            }
            Starting_add = mypreferences5.getInt("startaddress", 1);
            return true;
        }
        else if (id.equals(id6)) {
            diviceID=id;
            divicename=mypreferences6.getString("divicename","");
            diviceIP=mypreferences6.getString("diviceIP", "");
            diviceport=mypreferences6.getInt("diviceport", 0);
            divicettcount=mypreferences6.getInt("divicetotalcount", 0);
            registime=mypreferences6.getString("registime", "");
            tianxie();
            if(mypreferences6.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences6.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences6.getString("divicexulieinf","").split(",");
            for(j=0;j<temp1.length;j++){
                divicexulieinf[j] = Integer.parseInt(temp1[j]);
            }
            Starting_add = mypreferences6.getInt("startaddress", 1);
            return true;
        }
        else{       //该设备没在已保存的设备里或者存储暂时为空
            return false;
        }
    }

    //保存或更新信息
    public static void save(String id){
        int a=1;int b;
        int []x=new int[6];
        //将divicexulieinf和diviceIDinf转为string存储
        StringBuilder str=new StringBuilder();
        for(int i=0;i<divicexulieinf.length;i++){
            str.append(divicexulieinf[i]).append(",");
        }
        StringBuilder str2=new StringBuilder();
        for(int j=0;j<diviceIDinf.length;j++){
            str2.append(diviceIDinf[j]).append(",");
        }
        String id1=mypreferences1.getString("diviceID","");
        String id2=mypreferences2.getString("diviceID","");
        String id3=mypreferences3.getString("diviceID","");
        String id4=mypreferences4.getString("diviceID","");
        String id5=mypreferences5.getString("diviceID","");
        String id6=mypreferences6.getString("diviceID","");
        //id已在保存设备中，则根据实际输入的更新
        if(id.equals(id1)||id.equals(id2)||id.equals(id3)||id.equals(id4)||id.equals(id5)||id.equals(id6)){
            if(id.equals(id1)){
                editor1.putString("divicename", divicename).commit();
                editor1.putString("diviceIP", diviceIP).commit();
                editor1.putString("registime", registime).commit();
                editor1.putInt("diviceport", diviceport).commit();
                editor1.putInt("divicetotalcount", divicettcount).commit();
                editor1.putString("diviceIDinf",str2.toString()).commit();
                editor1.putString("divicexulieinf",str.toString()).commit();
                record(1);
                System.out.println("更新设备1信息");
            }
            else if(id.equals(id2)){
                editor2.putString("divicename", divicename).commit();
                editor2.putString("diviceIP", diviceIP).commit();
                editor2.putString("registime", registime).commit();
                editor2.putInt("diviceport", diviceport).commit();
                editor2.putInt("divicetotalcount", divicettcount).commit();
                editor2.putString("diviceIDinf",str2.toString()).commit();
                editor2.putString("divicexulieinf",str.toString()).commit();
                record(2);
                System.out.println("更新设备2信息");
            }
            else if(id.equals(id3)){
                editor3.putString("divicename", divicename).commit();
                editor3.putString("diviceIP", diviceIP).commit();
                editor3.putString("registime", registime).commit();
                editor3.putInt("diviceport", diviceport).commit();
                editor3.putInt("divicetotalcount", divicettcount).commit();
                editor3.putString("diviceIDinf",str2.toString()).commit();
                editor3.putString("divicexulieinf",str.toString()).commit();
                record(3);
                System.out.println("更新设备3信息");
            }
            else if(id.equals(id4)){
                editor4.putString("divicename", divicename).commit();
                editor4.putString("diviceIP", diviceIP).commit();
                editor4.putString("registime", registime).commit();
                editor4.putInt("diviceport", diviceport).commit();
                editor4.putInt("divicetotalcount", divicettcount).commit();
                editor4.putString("diviceIDinf",str2.toString()).commit();
                editor4.putString("divicexulieinf",str.toString()).commit();
                record(4);
                System.out.println("更新设备4信息");
            }
            else if(id.equals(id5)){
                editor5.putString("divicename", divicename).commit();
                editor5.putString("diviceIP", diviceIP).commit();
                editor5.putString("registime", registime).commit();
                editor5.putInt("diviceport", diviceport).commit();
                editor5.putInt("divicetotalcount", divicettcount).commit();
                editor5.putString("diviceIDinf",str2.toString()).commit();
                editor5.putString("divicexulieinf",str.toString()).commit();
                record(5);
                System.out.println("更新设备5信息");
            }
            else {
                editor6.putString("divicename", divicename).commit();
                editor6.putString("diviceIP", diviceIP).commit();
                editor6.putString("registime", registime).commit();
                editor6.putInt("diviceport", diviceport).commit();
                editor6.putInt("divicetotalcount", divicettcount).commit();
                editor6.putString("diviceIDinf",str2.toString()).commit();
                editor6.putString("divicexulieinf",str.toString()).commit();
                record(5);
                System.out.println("更新设备5信息");
            }
        }
        else {
            //若ID为新设备，则根据情况保存
            //若以上ID为空，即文件还没信息，则按顺序取空白文件存储
            if (id1.equals("")) a = 1;
            else if (id2.equals("")) a = 2;
            else if (id3.equals("")) a = 3;
            else if (id4.equals("")) a = 4;
            else if (id5.equals("")) a = 5;
            else if (id6.equals("")) a = 6;
                //均不为空，则找最老的设备，即visitrecord最大的设备
            else {
                if (!mypreferences1.getBoolean("islast", false))
                    x[0] = mypreferences1.getInt("visitrecord", 0);
                else x[0] = 0;
                if (!mypreferences2.getBoolean("islast", false))
                    x[1] = mypreferences2.getInt("visitrecord", 0);
                else x[1] = 0;
                if (!mypreferences3.getBoolean("islast", false))
                    x[2] = mypreferences3.getInt("visitrecord", 0);
                else x[2] = 0;
                if (!mypreferences4.getBoolean("islast", false))
                    x[3] = mypreferences4.getInt("visitrecord", 0);
                else x[3] = 0;
                if (!mypreferences5.getBoolean("islast", false))
                    x[4] = mypreferences5.getInt("visitrecord", 0);
                else x[4] = 0;
                if (!mypreferences6.getBoolean("islast", false))
                    x[5] = mypreferences6.getInt("visitrecord", 0);
                else x[5] = 0;
                //取最大
                b = x[0];
                for (int i = 1; i < 5; i++) {
                    if (x[i] > b) {
                        b = x[i];
                        a = i + 1;
                    }
                }
            }
            //存储新设备
            switch (a) {
                case 1:
                    editor1.putString("diviceID", diviceID).commit();
                    editor1.putString("divicename", divicename).commit();
                    editor1.putString("diviceIP", diviceIP).commit();
                    editor1.putString("registime", registime).commit();
                    editor1.putInt("diviceport", diviceport).commit();
                    editor1.putInt("divicetotalcount", divicettcount).commit();
                    editor1.putString("diviceIDinf",str2.toString()).commit();
                    editor1.putString("divicexulieinf",str.toString()).commit();
                    record(1);
                    editor1.putInt("visitrecord",0).commit();  //新设备访问记录置为0
                    System.out.println("已保存至设备1");
                    break;
                case 2:
                    editor2.putString("diviceID", diviceID).commit();
                    editor2.putString("divicename", divicename).commit();
                    editor2.putString("diviceIP", diviceIP).commit();
                    editor2.putString("registime", registime).commit();
                    editor2.putInt("diviceport", diviceport).commit();
                    editor2.putInt("divicetotalcount", divicettcount).commit();
                    editor2.putString("diviceIDinf",str2.toString()).commit();
                    editor2.putString("divicexulieinf",str.toString()).commit();
                    record(2);
                    editor2.putInt("visitrecord",0).commit();
                    System.out.println("已保存至设备2");
                    break;
                case 3:
                    editor3.putString("diviceID", diviceID).commit();
                    editor3.putString("divicename", divicename).commit();
                    editor3.putString("diviceIP", diviceIP).commit();
                    editor3.putString("registime", registime).commit();
                    editor3.putInt("diviceport", diviceport).commit();
                    editor3.putInt("divicetotalcount", divicettcount).commit();
                    editor3.putString("diviceIDinf",str2.toString()).commit();
                    editor3.putString("divicexulieinf",str.toString()).commit();
                    record(3);
                    editor3.putInt("visitrecord",0).commit();
                    System.out.println("已保存至设备3");
                    break;
                case 4:
                    editor4.putString("diviceID", diviceID).commit();
                    editor4.putString("divicename", divicename).commit();
                    editor4.putString("diviceIP", diviceIP).commit();
                    editor4.putString("registime", registime).commit();
                    editor4.putInt("diviceport", diviceport).commit();
                    editor4.putInt("divicetotalcount", divicettcount).commit();
                    editor4.putString("diviceIDinf",str2.toString()).commit();
                    editor4.putString("divicexulieinf",str.toString()).commit();
                    record(4);
                    editor4.putInt("visitrecord",0).commit();
                    System.out.println("已保存至设备4");
                    break;
                case 5:
                    editor5.putString("diviceID", diviceID).commit();
                    editor5.putString("divicename", divicename).commit();
                    editor5.putString("diviceIP", diviceIP).commit();
                    editor5.putString("registime", registime).commit();
                    editor5.putInt("diviceport", diviceport).commit();
                    editor5.putInt("divicetotalcount", divicettcount).commit();
                    editor5.putString("diviceIDinf",str2.toString()).commit();
                    editor5.putString("divicexulieinf",str.toString()).commit();
                    record(5);
                    editor5.putInt("visitrecord",0).commit();
                    System.out.println("已保存至设备5");
                    break;
                case 6:
                    editor6.putString("diviceID", diviceID).commit();
                    editor6.putString("divicename", divicename).commit();
                    editor6.putString("diviceIP", diviceIP).commit();
                    editor6.putString("registime", registime).commit();
                    editor6.putInt("diviceport", diviceport).commit();
                    editor6.putInt("divicetotalcount", divicettcount).commit();
                    editor6.putString("diviceIDinf",str2.toString()).commit();
                    editor6.putString("divicexulieinf",str.toString()).commit();
                    record(6);
                    editor6.putInt("visitrecord",0).commit();
                    System.out.println("已保存至设备6");
                    break;
                default:
                    System.out.println("保存错误");
                    break;
            }
        }
    }

    //更新设备存储文件的访问情况,当前访问第X个存储，则其他4存储的访问情况+1,
    //在其他方法处需要覆盖原来的记录时，访问情况值最大的设备存储将被覆盖，访问情况变为1。
    public  static void record(int x){
        int y1,y2,y3,y4,y5;
        switch(x){
            case 1:
                editor1.putBoolean("islast",true).commit();
                editor2.putBoolean("islast",false).commit();
                editor3.putBoolean("islast",false).commit();
                editor4.putBoolean("islast",false).commit();
                editor5.putBoolean("islast",false).commit();
                editor6.putBoolean("islast",false).commit();
                y1=mypreferences2.getInt("visitrecord",0);
                y2=mypreferences3.getInt("visitrecord",0);
                y3=mypreferences4.getInt("visitrecord",0);
                y4=mypreferences5.getInt("visitrecord",0);
                y5=mypreferences6.getInt("visitrecord",0);
                editor2.putInt("visitrecord",y1+1).commit();
                editor3.putInt("visitrecord",y2+1).commit();
                editor4.putInt("visitrecord",y3+1).commit();
                editor5.putInt("visitrecord",y4+1).commit();
                editor6.putInt("visitrecord",y5+1).commit();
                break;
            case 2:
                editor1.putBoolean("islast",false).commit();
                editor2.putBoolean("islast",true).commit();
                editor3.putBoolean("islast",false).commit();
                editor4.putBoolean("islast",false).commit();
                editor5.putBoolean("islast",false).commit();
                editor6.putBoolean("islast",false).commit();
                y1=mypreferences1.getInt("visitrecord",0);
                y2=mypreferences3.getInt("visitrecord",0);
                y3=mypreferences4.getInt("visitrecord",0);
                y4=mypreferences5.getInt("visitrecord",0);
                y5=mypreferences6.getInt("visitrecord",0);
                editor1.putInt("visitrecord",y1+1).commit();
                editor3.putInt("visitrecord",y2+1).commit();
                editor4.putInt("visitrecord",y3+1).commit();
                editor5.putInt("visitrecord",y4+1).commit();
                editor6.putInt("visitrecord",y5+1).commit();
                break;
            case 3:
                editor1.putBoolean("islast",false).commit();
                editor2.putBoolean("islast",false).commit();
                editor3.putBoolean("islast",true).commit();
                editor4.putBoolean("islast",false).commit();
                editor5.putBoolean("islast",false).commit();
                editor6.putBoolean("islast",false).commit();
                y1=mypreferences1.getInt("visitrecord",0);
                y2=mypreferences2.getInt("visitrecord",0);
                y3=mypreferences4.getInt("visitrecord",0);
                y4=mypreferences5.getInt("visitrecord",0);
                y5=mypreferences6.getInt("visitrecord",0);
                editor1.putInt("visitrecord",y1+1).commit();
                editor2.putInt("visitrecord",y2+1).commit();
                editor4.putInt("visitrecord",y3+1).commit();
                editor5.putInt("visitrecord",y4+1).commit();
                editor6.putInt("visitrecord",y5+1).commit();
                break;
            case 4:
                editor1.putBoolean("islast",false).commit();
                editor2.putBoolean("islast",false).commit();
                editor3.putBoolean("islast",false).commit();
                editor4.putBoolean("islast",true).commit();
                editor5.putBoolean("islast",false).commit();
                editor6.putBoolean("islast",false).commit();
                y1=mypreferences1.getInt("visitrecord",0);
                y2=mypreferences2.getInt("visitrecord",0);
                y3=mypreferences3.getInt("visitrecord",0);
                y4=mypreferences5.getInt("visitrecord",0);
                y5=mypreferences6.getInt("visitrecord",0);
                editor1.putInt("visitrecord",y1+1).commit();
                editor2.putInt("visitrecord",y2+1).commit();
                editor3.putInt("visitrecord",y3+1).commit();
                editor5.putInt("visitrecord",y4+1).commit();
                editor6.putInt("visitrecord",y5+1).commit();
                break;
            case 5:
                editor1.putBoolean("islast",false).commit();
                editor2.putBoolean("islast",false).commit();
                editor3.putBoolean("islast",false).commit();
                editor4.putBoolean("islast",false).commit();
                editor5.putBoolean("islast",true).commit();
                editor6.putBoolean("islast",false).commit();
                y1=mypreferences1.getInt("visitrecord",0);
                y2=mypreferences2.getInt("visitrecord",0);
                y3=mypreferences3.getInt("visitrecord",0);
                y4=mypreferences4.getInt("visitrecord",0);
                y5=mypreferences6.getInt("visitrecord",0);
                editor1.putInt("visitrecord",y1+1).commit();
                editor2.putInt("visitrecord",y2+1).commit();
                editor3.putInt("visitrecord",y3+1).commit();
                editor4.putInt("visitrecord",y4+1).commit();
                editor6.putInt("visitrecord",y5+1).commit();
                break;
            case 6:
                editor1.putBoolean("islast",false).commit();
                editor2.putBoolean("islast",false).commit();
                editor3.putBoolean("islast",false).commit();
                editor4.putBoolean("islast",false).commit();
                editor5.putBoolean("islast",false).commit();
                editor6.putBoolean("islast",true).commit();
                y1=mypreferences1.getInt("visitrecord",0);
                y2=mypreferences2.getInt("visitrecord",0);
                y3=mypreferences3.getInt("visitrecord",0);
                y4=mypreferences4.getInt("visitrecord",0);
                y5=mypreferences5.getInt("visitrecord",0);
                editor1.putInt("visitrecord",y1+1).commit();
                editor2.putInt("visitrecord",y2+1).commit();
                editor3.putInt("visitrecord",y3+1).commit();
                editor4.putInt("visitrecord",y4+1).commit();
                editor5.putInt("visitrecord",y5+1).commit();
                break;
            default:System.out.println("访问记录错误");
                break;
        }
    }

    //主页在进入APP后加载上一次连接的设备信息
    public void showinform(){
        int j;
        String[] temp1;
        if(mypreferences1.getBoolean("islast",false)){
            MainPage.islastdivicestore=true;
            diviceID=mypreferences1.getString("diviceID", "");
            diviceIP=mypreferences1.getString("diviceIP", "");
            divicename=mypreferences1.getString("divicename", "");
            diviceport=mypreferences1.getInt("diviceport", 0);
            divicettcount=mypreferences1.getInt("divicetotalcount", 1);
            registime=mypreferences1.getString("registime", "");
            if(mypreferences1.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences1.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences1.getString("divicexulieinf","").split(",");
            if(temp1.length>2) {
                for (j = 0; j < temp1.length; j++) {
                    divicexulieinf[j] = Integer.parseInt(temp1[j]);
                }
            }
            Starting_add = mypreferences1.getInt("startaddress", 1);
            tianxie();
        }
        else if(mypreferences2.getBoolean("islast",false)){
            MainPage.islastdivicestore=true;
            diviceID=mypreferences2.getString("diviceID", "");
            diviceIP=mypreferences2.getString("diviceIP", "");
            divicename=mypreferences2.getString("divicename", "");
            diviceport=mypreferences2.getInt("diviceport", 0);
            divicettcount=mypreferences2.getInt("divicetotalcount", 1);
            registime=mypreferences2.getString("registime", "");
            if(mypreferences2.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences2.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences2.getString("divicexulieinf","").split(",");
            if(temp1.length>2) {
                for (j = 0; j < temp1.length; j++) {
                    divicexulieinf[j] = Integer.parseInt(temp1[j]);
                }
            }
            Starting_add = mypreferences2.getInt("startaddress", 1);
            tianxie();
        }
        else if(mypreferences3.getBoolean("islast",false)){
            MainPage.islastdivicestore=true;
            diviceID=mypreferences3.getString("diviceID", "");
            diviceIP=mypreferences3.getString("diviceIP", "");
            divicename=mypreferences3.getString("divicename", "");
            diviceport=mypreferences3.getInt("diviceport", 0);
            divicettcount=mypreferences3.getInt("divicetotalcount", 1);
            registime=mypreferences3.getString("registime", "");
            if(mypreferences3.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences3.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences3.getString("divicexulieinf","").split(",");
            if(temp1.length>2) {
                for (j = 0; j < temp1.length; j++) {
                    divicexulieinf[j] = Integer.parseInt(temp1[j]);
                }
            }
            Starting_add = mypreferences3.getInt("startaddress", 1);
            tianxie();
        }
        else if(mypreferences4.getBoolean("islast",false)){
            MainPage.islastdivicestore=true;
            diviceID=mypreferences4.getString("diviceID", "");
            diviceIP=mypreferences4.getString("diviceIP", "");
            divicename=mypreferences4.getString("divicename", "");
            diviceport=mypreferences4.getInt("diviceport", 0);
            divicettcount=mypreferences4.getInt("divicetotalcount", 1);
            registime=mypreferences4.getString("registime", "");
            if(mypreferences4.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences4.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences4.getString("divicexulieinf","").split(",");
            if(temp1.length>2) {
                for (j = 0; j < temp1.length; j++) {
                    divicexulieinf[j] = Integer.parseInt(temp1[j]);
                }
            }
            Starting_add = mypreferences4.getInt("startaddress", 1);
            tianxie();
        }
        else if(mypreferences5.getBoolean("islast",false)){
            MainPage.islastdivicestore=true;
            diviceID=mypreferences5.getString("diviceID", "");
            diviceIP=mypreferences5.getString("diviceIP", "");
            divicename=mypreferences5.getString("divicename", "");
            diviceport=mypreferences5.getInt("diviceport", 0);
            divicettcount=mypreferences5.getInt("divicetotalcount", 1);
            registime=mypreferences5.getString("registime", "");
            if(mypreferences5.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences5.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences5.getString("divicexulieinf","").split(",");
            if(temp1.length>2) {
                for (j = 0; j < temp1.length; j++) {
                    divicexulieinf[j] = Integer.parseInt(temp1[j]);
                }
            }
            Starting_add = mypreferences5.getInt("startaddress", 1);
            tianxie();
        }
        else if(mypreferences6.getBoolean("islast",false)){
            MainPage.islastdivicestore=true;
            diviceID=mypreferences6.getString("diviceID", "");
            diviceIP=mypreferences6.getString("diviceIP", "");
            divicename=mypreferences6.getString("divicename", "");
            diviceport=mypreferences6.getInt("diviceport", 0);
            divicettcount=mypreferences6.getInt("divicetotalcount", 1);
            registime=mypreferences6.getString("registime", "");
            if(mypreferences6.getString("diviceIDinf","").getBytes().length>0) {
                diviceIDinf=mypreferences6.getString("diviceIDinf","").split(",");
            }
            temp1=mypreferences6.getString("divicexulieinf","").split(",");
            if(temp1.length>2) {
                for (j = 0; j < temp1.length; j++) {
                    divicexulieinf[j] = Integer.parseInt(temp1[j]);
                }
            }
            Starting_add = mypreferences6.getInt("startaddress", 1);
            tianxie();
        }
        else System.out.println("显示错误");
    }

    //保存设备起始地址在mainactivity中调用
    public static boolean saveaddress(String id){
        int a=1;int b;
        int[] x=new int[5];
        String id1=mypreferences1.getString("diviceID","");
        String id2=mypreferences2.getString("diviceID","");
        String id3=mypreferences3.getString("diviceID","");
        String id4=mypreferences4.getString("diviceID","");
        String id5=mypreferences5.getString("diviceID","");
        String id6=mypreferences6.getString("diviceID","");
        //id已在保存设备中，则根据实际输入的更新
        if(id.equals(id1)||id.equals(id2)||id.equals(id3)||id.equals(id4)||id.equals(id5)||id.equals(id6)){
            if(id.equals(id1)){
                editor1.putInt("startaddress",Starting_add).commit();
                System.out.println("地址保存完成");
            }
            else if(id.equals(id2)){
                editor2.putInt("startaddress",Starting_add).commit();
                System.out.println("地址保存完成");
            }
            else if(id.equals(id3)){
                editor3.putInt("startaddress",Starting_add).commit();
                System.out.println("地址保存完成");
            }
            else if(id.equals(id4)){
                editor4.putInt("startaddress",Starting_add).commit();
                System.out.println("地址保存完成");
            }
            else if(id.equals(id5)){
                editor5.putInt("startaddress",Starting_add).commit();
                System.out.println("地址保存完成");
            }
            else if(id.equals(id6)){
                editor6.putInt("startaddress",Starting_add).commit();
                System.out.println("地址保存完成");
            }
            return true;
        }
        //没有匹配ID，即还没保存的设备
        else
            return false;
    }

    //清空界面信息
    public void qingkong(){
        diviceID_string.setText("");
        divicename_string.setText("");
        diviceIP_string.setText("");
        diviceport_int.setText("");
        divicetotalcount_int.setText("");
        diviceregistime_string.setText("");
    }

    //填写界面信息
    @SuppressLint("SetTextI18n")
    public static void tianxie(){
        diviceID_string.setText(diviceID+"");
        diviceIP_string.setText(diviceIP+"");
        divicename_string.setText(divicename+"");
        diviceregistime_string.setText(registime+"");
        diviceport_int.setText(diviceport+"");
        divicetotalcount_int.setText(divicettcount+"");
    }


    //获取界面中edittext的信息
    public static void getscreeninform(){

        diviceID =diviceID_string.getText().toString();
        diviceIP = diviceIP_string.getText().toString();
        divicename = divicename_string.getText().toString();
        registime = diviceregistime_string.getText().toString();
        diviceport = Integer.parseInt(0+diviceport_int.getText().toString());
        divicettcount = Integer.parseInt(0+divicetotalcount_int.getText().toString());
    }

    //下拉edittext初始化，还需要实现自动补全其他信息
    private void dropedittextinit(){
        String[] baocunid=new String[6];
        baocunid[0]=mypreferences1.getString("diviceID","");
        baocunid[1]=mypreferences2.getString("diviceID","");
        baocunid[2]=mypreferences3.getString("diviceID","");
        baocunid[3]=mypreferences4.getString("diviceID","");
        baocunid[4]=mypreferences5.getString("diviceID","");
        baocunid[5]=mypreferences6.getString("diviceID","");
        int j=0,k=0;
        int i;
        for(i=0;i<6;i++){
            if(!baocunid[i].equals(""))j++;
        }
        String[] strings=new String[j];
        for(i=0;i<6;i++){
            if(!baocunid[i].equals("")){
                strings[k++]=baocunid[i];
            }
        }
        if(j>0&&!strings[0].equals("")) {
            det_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strings);   //
            diviceID_string.setAdapter(det_adapter);
        }
    }

    //循环，连接指示,错误提示界面，下拉选择框
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    if(MainPage.ismonitorstart &&MainPage.islinksuccess==1) { //监控开始且连接成功
                        MainPage.connectsight.setIcon(R.drawable.linksuccess);
                        MainPage.connectsight.getIcon().setTint(Color.WHITE);
                    }
                    else if(MainPage.ismonitorstart==true&&MainPage.islinksuccess!=1)
                    {
                        MainPage.connectsight.setIcon(R.drawable.linkfalse);
                        MainPage.connectsight.getIcon().setTint(Color.WHITE);
                    }

                    if(!MainPage.ismonitorstart)errtext.setText("");
                    break;
                default:break;
            }
        }
    };

    private class Mytesk extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }


}
