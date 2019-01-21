package com.yanzhenjie.andserver.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ServerManager extends BroadcastReceiver
{

    private static final String ACTION = "com.yanzhenjie.andserver.receiver";

    private static final String CMD_NAME = "CMD_NAME";
    private static final String CMD_EXTRA = "CMD_EXTRA";

    private static final int CMD_START = 1;
    private static final int CMD_ERROR = 2;
    private static final int CMD_STOP = 4;
    
    public static void onServerStart(Context context, String hostAddress)
    {
        sendBroadcast(context, CMD_START, hostAddress);
    }

  
    public static void onServerError(Context context, String error)
    {
        sendBroadcast(context, CMD_ERROR, error);
    }

   
    public static void onServerStop(Context context)
    {
        sendBroadcast(context, CMD_STOP);
    }

    
    
    private static void sendBroadcast(Context context, int cmd)
    {
        sendBroadcast(context, cmd, null);
    }

    private static void sendBroadcast(Context context, int cmd, String message)
    {
        Intent broadcast = new Intent(ACTION);
        broadcast.putExtra(CMD_NAME, cmd);
        broadcast.putExtra(CMD_EXTRA, message);
        context.sendBroadcast(broadcast);
    }
    
    
    

    private MainActivity mActivity;
    private Intent serviceIntent;

    public ServerManager(MainActivity activity)
    {
        this.mActivity = activity;
        serviceIntent = new Intent(activity, CoreService.class);
    }

   
    public void register()
    {
        IntentFilter filter = new IntentFilter(ACTION);
        mActivity.registerReceiver(this, filter);
    }

   
    public void unRegister()
    {
        mActivity.unregisterReceiver(this);
    }

    public void startServer()
    {
        mActivity.startService(serviceIntent);
    }

    public void stopServer()
    {
        mActivity.stopService(serviceIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (ACTION.equals(action))
        {
            int cmd = intent.getIntExtra(CMD_NAME, 0);
            switch (cmd)
            {
                case CMD_START:
                {
                    String ip = intent.getStringExtra(CMD_EXTRA);
                    mActivity.onServerStart(ip);
                    break;
                }
                case CMD_ERROR:
                {
                    String error = intent.getStringExtra(CMD_EXTRA);
                    mActivity.onServerError(error);
                    break;
                }
                case CMD_STOP:
                {
                    mActivity.onServerStop();
                    break;
                }
            }
        }
    }
}