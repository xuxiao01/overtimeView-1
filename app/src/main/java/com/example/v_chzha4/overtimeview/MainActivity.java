package com.example.v_chzha4.overtimeview;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private int        year;
    private int        month;
    private int        day;
    private TextView   showBasicWage;
    private MyEditText enterBasicWage;
    private String     mybasicmoney;
    private Button     addButton;
    private AlertDialog dialogBuilder;
    private TextView   showhourwage;
    private Double     calhourwage;
    private AlertDialog daysettingDialog;
    private MyEditText  daysettingEdit;
    private AlertDialog monthlydialog;
    private Double      doublehourwage;
    private TextView    textovertimewage;
    private TextView    textg1;
    private TextView    textg2;
    private TextView    textg3;
    private Double      moneyg1;
    private Double      moneyg2;
    private Double      moneyg3;
    private Double      moneytotalovertime;
    private SharedPreferences share;
    private SharedPreferences.Editor edit;
    private SharedPreferences loadshare;
    private int workdaynumber;

    private int g1Count = 0;
    private int g2Count = 0;
    private int g3Count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initData();
        initCalender();
        LoadData();
    }

    public void initData(){
        datePicker = (DatePicker)findViewById(R.id.datepicker);
        showBasicWage = (TextView)findViewById(R.id.show_basicwage);
        addButton = (Button)findViewById(R.id.btn_ok);
        showhourwage = (TextView)findViewById(R.id.show_hourwage);

        share = PreferenceManager.getDefaultSharedPreferences(this);
        edit = share.edit(); //编辑文件


    }

    public void initCalender(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(MainActivity.this, year + "年" + (monthOfYear+1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
                countovertime(datePicker);
            }
        });
    }

    public void LoadData() {
        //指定操作的文件名称
        loadshare = PreferenceManager.getDefaultSharedPreferences(this);
        if(loadshare.contains("showbasicwage")){
            mybasicmoney = share.getString("showbasicwage", "");
            showBasicWage.setText(mybasicmoney);
        }else{
            showBasicWage.setText("");
        }
        if(loadshare.contains("daynumber")){
            workdaynumber = share.getInt("daynumber",22);
            Log.e("daynumber",""+workdaynumber);
            calhourwage = Double.parseDouble(mybasicmoney)/workdaynumber/8;
            doublehourwage = Double.parseDouble(String.format("%.2f", calhourwage));
            showhourwage.setText(String.format("%.2f", calhourwage));
        }else{
            workdaynumber = 22;
        }

    }

    public void addOK(View v){
        if(enterBasicWage.getText().toString().equals("")){
            Toast.makeText(this, "请输入本月基本工资！" , Toast.LENGTH_SHORT).show();
            dialogBuilder.dismiss();
        }else{
            mybasicmoney = enterBasicWage.getText().toString();
            showBasicWage.setText(mybasicmoney);
            edit.putString("showbasicwage",mybasicmoney);
            edit.commit();
            calhourwage = Double.parseDouble(mybasicmoney)/22/8;
            doublehourwage = Double.parseDouble(String.format("%.2f", calhourwage));
            showhourwage.setText(String.format("%.2f", calhourwage));
            dialogBuilder.dismiss();
        }
    }

    public void enterBasicWage(View v){
        dialogBuilder = new AlertDialog.Builder(v.getContext()).create() ;
        LayoutInflater inflater = LayoutInflater.from(v.getContext()) ;
        View view = inflater.inflate(R.layout.addbasicwage, null) ;
        enterBasicWage = (MyEditText) view.findViewById(R.id.edit_basicwage);
        dialogBuilder.setView(view) ;
        dialogBuilder.show() ;
    }
    public void dayOK(View v){
        workdaynumber = 0;
        String workday = daysettingEdit.getText().toString();
        if(workday.equals("")){

        }else{
            workdaynumber = Integer.parseInt(workday);
            edit.putInt("daynumber",workdaynumber);
            edit.commit();
        }

        if(workday.equals("")){
            Toast.makeText(this, "当前默认一个月天数为22天", Toast.LENGTH_SHORT).show();
            daysettingDialog.dismiss();
        }else if(workdaynumber > 31 | workdaynumber <=0){
            Toast.makeText(this, "您输入的天数不正确！", Toast.LENGTH_SHORT).show();
        }else{
            calhourwage = Double.parseDouble(mybasicmoney)/workdaynumber/8;
            doublehourwage = Double.parseDouble(String.format("%.2f", calhourwage));
            showhourwage.setText(String.format("%.2f", calhourwage));
            daysettingDialog.dismiss();
        }

    }
    public void daySetting(View v){
        if(showBasicWage.getText().equals("")){
            Toast.makeText(this, "请先输入本月基本工资 ！！", Toast.LENGTH_SHORT).show();
        }else{
            daysettingDialog = new AlertDialog.Builder(v.getContext()).create() ;
            LayoutInflater inflater = LayoutInflater.from(v.getContext()) ;
            View view = inflater.inflate(R.layout.daysettinglayout, null) ;
            daysettingEdit = (MyEditText)view.findViewById(R.id.edit_daysetting);
            daysettingDialog.setView(view) ;
            daysettingDialog.show() ;
        }
    }
    public void monthlyWage(View v){
        monthlydialog = new AlertDialog.Builder(v.getContext()).create() ;
        LayoutInflater inflater = LayoutInflater.from(v.getContext()) ;
        View view = inflater.inflate(R.layout.monthlydialogsetting, null) ;
        textovertimewage = (TextView)view.findViewById(R.id.hour_totalwage);
        textg1 = (TextView)view.findViewById(R.id.hour_g1);
        textg2 = (TextView)view.findViewById(R.id.hour_g2);
        textg3 = (TextView)view.findViewById(R.id.hour_g3);
        moneyg1 = g1Count*1.5*doublehourwage;
        moneyg2 = g2Count*2*doublehourwage;
        moneyg3 = g3Count*3*doublehourwage;
        moneytotalovertime = moneyg1+moneyg2+moneyg3;
        textg1.setText(String.valueOf(g1Count));
        textg2.setText(String.valueOf(g2Count));
        textg3.setText(String.valueOf(g3Count));
        textovertimewage.setText(String.valueOf(String.format("%.2f",moneytotalovertime)));
        monthlydialog.setView(view);
        monthlydialog.show() ;
    }

    public void countovertime(View view) {
        LayoutInflater inflater = getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.dialog_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("添加工时");
        builder.setView(view1);
        builder.create();
        final AlertDialog dialog = builder.show();
        final RadioButton g1 = (RadioButton) view1.findViewById(R.id.G1);
        final RadioButton g2 = (RadioButton) view1.findViewById(R.id.G2);
        final RadioButton g3 = (RadioButton) view1.findViewById(R.id.G3);
        Button confime = (Button) view1.findViewById(R.id.confime);
        confime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) view1.findViewById(R.id.edit1);
                final int count = Integer.valueOf(editText.getText().toString());
                if (g1.isChecked()) {
                    g1Count += count;
                    String str1 = String.valueOf(g1Count);
                    Toast.makeText(MainActivity.this, str1, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    //System.out.println(g1Count);
                } else if (g2.isChecked()) {
                    g2Count += count;
                    String str2 = String.valueOf(g2Count);
                    Toast.makeText(MainActivity.this, str2, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    //System.out.println(g2Count);
                } else if (g3.isChecked()) {
                    g3Count += count;
                    String str3 = String.valueOf(g3Count);
                    Toast.makeText(MainActivity.this, str3, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    //System.out.println(g3Count);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        edit.clear();
//        edit.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
//            MainActivity.this.onPause();
//            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
