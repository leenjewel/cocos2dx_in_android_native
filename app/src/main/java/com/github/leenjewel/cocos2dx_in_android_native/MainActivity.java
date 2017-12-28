package com.github.leenjewel.cocos2dx_in_android_native;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxAudioFocusManager;
import org.cocos2dx.lib.Cocos2dxEngineDataManager;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxHelper;
import org.cocos2dx.lib.Cocos2dxRenderer;

public class MainActivity extends AppCompatActivity implements Cocos2dxHelper.Cocos2dxHelperListener {

    private Cocos2dxGLSurfaceView mGLSurfaceView = null;
    private int[] mGLContextAttrs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            System.loadLibrary("cocos2djs");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cocos2dxActivity.setContext(this);

        Cocos2dxHelper.init(this);

        this.mGLContextAttrs = Cocos2dxActivity.getGLContextAttrs();

        this.mGLSurfaceView = (Cocos2dxGLSurfaceView) this.findViewById(
                R.id.cocos2d_gl_surface_view);

        if (this.mGLContextAttrs[3] > 0) {
            mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }

        Cocos2dxActivity.Cocos2dxEGLConfigChooser chooser = new Cocos2dxActivity.Cocos2dxEGLConfigChooser(
                this.mGLContextAttrs);
        this.mGLSurfaceView.setEGLConfigChooser(chooser);
        this.mGLSurfaceView.setCocos2dxRenderer(new Cocos2dxRenderer());
        this.mGLSurfaceView.setZOrderOnTop(true);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Cocos2dxEngineDataManager.init(this, mGLSurfaceView);
    }

    @Override
    public void showDialog(final String pTitle, final String pMessage) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                (new AlertDialog.Builder(MainActivity.this))
                        .setMessage(pMessage)
                        .setTitle(pTitle)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    @Override
    public void runOnGLThread(Runnable pRunnable) {
        this.mGLSurfaceView.queueEvent(pRunnable);
    }

    @Override
    public void setKeepScreenOn(boolean value) {
        final boolean newValue = value;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGLSurfaceView.setKeepScreenOn(newValue);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cocos2dxAudioFocusManager.registerAudioFocusListener(this);
        Cocos2dxHelper.onResume();
        mGLSurfaceView.onResume();
        Cocos2dxEngineDataManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Cocos2dxAudioFocusManager.unregisterAudioFocusListener(this);
        Cocos2dxHelper.onPause();
        mGLSurfaceView.onPause();
        Cocos2dxEngineDataManager.pause();
    }

    @Override
    protected void onDestroy() {
        Cocos2dxAudioFocusManager.unregisterAudioFocusListener(this);
        super.onDestroy();
        Cocos2dxEngineDataManager.destroy();
    }
}
