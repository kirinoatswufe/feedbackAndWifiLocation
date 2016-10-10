package com.example.kirin.feedback_master;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.location.GpsStatus;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.view.View;
import android.view.View.OnClickListener;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.widget.Button;
import android.R.string;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.*;
import java.util.*;
import java.io.*;
import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
//android wifi location;
//

public class sensorAndFeedback extends AppCompatActivity implements View.OnClickListener {

    int tempLevel = 0;
    private String temp = Sensor.STRING_TYPE_AMBIENT_TEMPERATURE;
    private TextView show;
    private SeekBar seekBar;
    private Button subBut, nextBut;
    private Context mContext;
    private TextView barView1, barView2;
    private AlertDialog.Builder builder;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_and_feedback);
        //findId

        show = (TextView) findViewById(R.id.showTemp);
        show.setText(temp + "C");

        mContext = sensorAndFeedback.this;

        subBut = (Button) findViewById(R.id.subButton);
        nextBut = (Button) findViewById(R.id.nextButton);
        subBut.setOnClickListener(this);
        nextBut.setOnClickListener(this);
        seekBar = (SeekBar) findViewById(R.id.seekBar3);
        barView1 = (TextView) findViewById(R.id.barView1);
        barView2 = (TextView) findViewById(R.id.barView2);
        seekBar.setOnSeekBarChangeListener(seekbarChangeListener);
        String path = Environment.getExternalStorageDirectory()
                .getPath()
                + File.separator + "TESTDATA.txt";
        File fileName = new File(path);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
                FileWriter fw = new FileWriter(fileName, true);
                BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
                out.write("Date\t");
                out.write("Temperature\t");
                out.write("Feedback Level\n");
                out.flush();
                out.close();
                fw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // dialog method

    private void Dialog(View view) {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Thank You!");
        builder.setMessage("Do you want to submit?");
        builder.setPositiveButton(R.string.postive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), R.string.toast_postive, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), R.string.toast_negative, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subButton:
                Dialog(v);
                hotWrite();
            case R.id.nextButton:

        }
    }


    private OnSeekBarChangeListener seekbarChangeListener = new OnSeekBarChangeListener() {

        // 停止拖动时执行
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            int seekProgress = seekBar.getProgress();
            if (seekProgress < 25) {
                seekBar.setProgress(0);
            } else if (seekProgress >= 25 && seekProgress < 50) {
                seekBar.setProgress(25);
            } else if (seekProgress >= 50 && seekProgress < 75) {
                seekBar.setProgress(50);
            } else if (seekProgress >= 75 && seekProgress < 100) {
                seekBar.setProgress(75);
            }
            barView2.setText("Stopped");

        }

        // 在进度开始改变时执行
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            //barView2.setText("What is your feeling");
        }

        // 当进度发生改变时执行
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            barView2.setText("Please select");
            Message message = new Message();

            Bundle bundle = new Bundle();// 存放数据

            float pro = seekBar.getProgress();

            float num = seekBar.getMax();

            float result = (pro / num) * 4;

            tempLevel = (int) result;
            bundle.putInt("key", tempLevel);

            message.setData(bundle);

            message.what = 0;

            handler.sendMessage(message);

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("key") == 0)
                barView1.setText("You feel VERY COLD");
            if (msg.getData().getInt("key") == 1)
                barView1.setText("You feel A LITTLE COLD");
            if (msg.getData().getInt("key") == 2)
                barView1.setText("You feel ");
            if (msg.getData().getInt("key") == 3)
                barView1.setText("You feel A LITTLE HOT");
            if (msg.getData().getInt("key") == 4)
                barView1.setText("You feel VERY HOT");

        }

    };


    public void hotWrite() {
        String path = Environment.getExternalStorageDirectory()
                .getPath()
                + File.separator + "TESTDATA.txt";
        File fileName = new File(path);
        if (fileName.exists()) {
            try {
                FileWriter fw = new FileWriter(fileName, true);
                BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
                out.write("Date\t");
                out.write("Temperature\t");
                out.write("Feedback Level\n");
                out.flush();
                out.close();
                fw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}