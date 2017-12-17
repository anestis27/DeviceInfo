package com.example.orion.leitourgika;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.BatteryManager;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    private MyDataBase b;
    private TabHost tabHost;
    private TextView devname, androidversion, kernversion, internalmem, externalmem, battery, uptime, connection;
    private Button clearHistory;
    private ListView lv, lv2;
    private Cursor cr;
    private SpaceAdapter adapter;
    private ProcessAdapter processAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        FirstTabProcess();
        SecondTabProcess();
        ThirdTabProcess();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        //Setaro ta tabs
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec ts = tabHost.newTabSpec("first");
        ts.setContent(R.id.tab1);
        ts.setIndicator("History");
        tabHost.addTab(ts);
        TabHost.TabSpec ts2 = tabHost.newTabSpec("second");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("Active Processes");
        tabHost.addTab(ts2);
        TabHost.TabSpec ts3 = tabHost.newTabSpec("third");
        ts3.setContent(R.id.tab3);
        ts3.setIndicator("Info");
        tabHost.addTab(ts3);
        clearHistory = (Button) findViewById(R.id.butClear);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.onUpgrade(b.getWritableDatabase(), 1, 2);
                History();
            }
        });
        lv = (ListView) findViewById(R.id.History);
        lv2 = (ListView) findViewById(R.id.Process);
        devname = (TextView) findViewById(R.id.devname);
        androidversion = (TextView) findViewById(R.id.androidversion);
        kernversion = (TextView) findViewById(R.id.kernversion);
        internalmem = (TextView) findViewById(R.id.internalmem);
        externalmem = (TextView) findViewById(R.id.externalmem);
        battery = (TextView) findViewById(R.id.battery);
        uptime = (TextView) findViewById(R.id.uptime);
        connection = (TextView) findViewById(R.id.connection);
    }

    private void FirstTabProcess() {
        b = new MyDataBase(MainActivity.this);
        History();
    }

    private void History() {
        double interSize = getAvailableInternalMemorySize();
        double exterSize = getAvailableExternalMemorySize();
        NumberFormat df = new DecimalFormat("#.00");
        b.InsertSpace(new Space(df.format(interSize), df.format(exterSize)));
        cr = b.getAllSpaceItems();
        startManagingCursor(cr);
        adapter = new SpaceAdapter(getApplicationContext(), cr);
        lv.setAdapter(adapter);
    }

    private void SecondTabProcess() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        ArrayList<SimpleProcess> ListItemOfProcess = new ArrayList<SimpleProcess>();
        processAdapter = new ProcessAdapter(this, ListItemOfProcess);
        lv2.setAdapter(processAdapter);
        for (ActivityManager.RunningAppProcessInfo process : runningProcesses) {
            String name;
            int pid, ram;
            double mbram;
            name = process.processName;
            pid = process.pid;
            Debug.MemoryInfo mi = new Debug.MemoryInfo();
            mi = getMemoryInfo(pid);
            ram = mi.otherPss + mi.nativePss + mi.dalvikPss;
            mbram = ram * 1.0 / 1024;
            SimpleProcess pr = new SimpleProcess(name, pid, String.format("%.2f", mbram));
            ListItemOfProcess.add(pr);
        }
        processAdapter.addAll();
    }

    public Debug.MemoryInfo getMemoryInfo(int pid) {
        int processPID[] = new int[1];
        processPID[0] = pid;
        ActivityManager activitymgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Debug.MemoryInfo[] memInfoProc = activitymgr.getProcessMemoryInfo(processPID);
        Debug.MemoryInfo aman = memInfoProc[0];
        return aman;
    }

    private void ThirdTabProcess() {
        devname.setText(Info.getDeviceName());
        androidversion.setText(Info.getAndroidVersion());
        kernversion.setText(Info.getKernelVersion());
        //externalmem, battery, uptime, connection
        double interSize = getAvailableInternalMemorySize();
        double exterSize = getAvailableExternalMemorySize();
        NumberFormat df = new DecimalFormat("#.00");
        internalmem.setText(df.format(interSize) + " GB");
        externalmem.setText(df.format(exterSize) + " GB");
        battery.setText(getBatteryLevel() + "%");
        uptime.setText(Info.Uptime() + " hours");
        connection.setText(Info.getConnectivityStatusString(this));
    }

    public static double getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1073741824.0;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static double getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return (availableBlocks * blockSize) / 1073741824.0;
        } else {
            return 0;
        }
    }

    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100.0f;
    }

}
