//package com.example.audiorecord;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.media.MediaRecorder;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * 实现录音等功能
// */
//
//@SuppressLint("AppCompatCustomView")
//public class AudioManager {
//
//    //手指滑动 距离
//    private static final int DISTANCE_Y_CANCEL = 50;
//    //状态
//    public static final int STATE_NORMAL = 1;
//    public static final int STATE_RECORDING = 2;
//    public static final int STATE_WANT_TO_CANCEL = 3;
//    //当前状态
//    public static int mCurState = STATE_NORMAL;
//    //已经开始录音
//    public static boolean isRecording = false;
//
//    public static AudioFinishRecorderListener audioFinishRecorderListener;
//
//    private static DialogManager mDialogManager;
//
//    //    private static AudioManager mAudioManager;
//    private static AudioManager mInstance;
//
//    public static long mTime;
//    //是否触发onlongclick
//    public static boolean mReady;
//
//
//    private static final String TAG = "AudioManager";
//    private MediaRecorder mMediaRecorder;
//    private String mDir;
//    private String mCurrentFilePath;
//
//
//    private boolean isPrepared;
//
//
//    public void init(Context context) {
//        mDialogManager = new DialogManager(context);
//        //偷个懒，并没有判断 是否存在， 是否可读。
//        String dir = Environment.getExternalStorageDirectory() + "/recorder_audios";
//        getInstance().setFileDir(dir);
//    }
//
//    public static DialogManager getDialogManager() {
//        return mDialogManager;
//    }
//
//    private void setFileDir(String dir) {
//        mDir = dir;
//    }
//
//
//    public AudioManager() {
//    }
//
//    public static AudioManager getInstance() {
//        if (mInstance == null) {
//            synchronized (AudioManager.class) {
//                if (mInstance == null) {
//                    mInstance = new AudioManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    private static IViewChangeListener viewChangeListener;
//
//    public void setOnViewChangeListener(IViewChangeListener viewChangeListener) {
//        AudioManager.viewChangeListener = viewChangeListener;
//    }
//
//    public AudioStateListener mAudioStateListener;
//
//    public void setOnAudioStateListener(AudioStateListener mAduioStateListener) {
//        this.mAudioStateListener = mAduioStateListener;
//    }
//
//
//    /**
//     * 回调准备完毕
//     */
//    public interface AudioStateListener {
//        void wellPrepared();
//    }
//
//
//    /**
//     * 准备
//     */
//    public void prepareAudio() {
//        try {
//            isPrepared = false;
//            File dir = new File(mDir);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//            String fileName = generateFileName();
//
//            File file = new File(dir, fileName);
//
//            mCurrentFilePath = file.getAbsolutePath();
//            Log.e(TAG, "录音文件路径: " + mCurrentFilePath);
//            mMediaRecorder = new MediaRecorder();
//            //设置输出文件
//            mMediaRecorder.setOutputFile(file.getAbsolutePath());
//            //设置MediaRecorder的音频源为麦克风
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            //设置音频格式
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
//            //设置音频的格式为amr
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            mMediaRecorder.prepare();
//            mMediaRecorder.start();
//            //准备结束
//            isPrepared = true;
//            if (mAudioStateListener != null) {
//                mAudioStateListener.wellPrepared();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //    生成UUID唯一标示符
////    算法的核心思想是结合机器的网卡、当地时间、一个随即数来生成GUID
////    .amr音频文件
//    private String generateFileName() {
//        return UUID.randomUUID().toString() + ".amr";
//    }
//
//    public int getVoiceLevel(int maxLevel) {
//        if (isPrepared) {
//            //获得最大的振幅getMaxAmplitude() 1-32767
//            try {
//                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
//            } catch (Exception e) {
//
//            }
//        }
//        return 1;
//    }
//
//    /**
//     * java.lang.RuntimeException: stop failed.
//     * at android.media.MediaRecorder.stop(Native Method)
//     * at com.example.audiorecord.AudioManager.release(AudioManager.java:112)
//     * at com.example.audiorecord.AudioManager.cancel(AudioManager.java:118)
//     * <p>
//     * 解决方法需要捕获异常;
//     */
//    public void release() {
//        if (mMediaRecorder != null) {
//            mMediaRecorder.setOnErrorListener(null);
//            mMediaRecorder.setOnInfoListener(null);
//            mMediaRecorder.setPreviewDisplay(null);
//            try {
//                mMediaRecorder.stop();
//                mMediaRecorder.release();
//                mMediaRecorder = null;
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//                Log.e(TAG, "IllegalStateException");
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//                Log.e(TAG, "RuntimeException");
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e(TAG, "Exception");
//            }
//        }
//
//    }
//
//    public void cancel() {
//        release();
//        if (mCurrentFilePath != null) {
//            File file = new File(mCurrentFilePath);
//            file.delete();
//            mCurrentFilePath = null;
//        }
//    }
//
//    public String getCurrentFilePath() {
//        return mCurrentFilePath;
//    }
//
//
//    public static boolean isCancelled(View view, MotionEvent event) {
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40) {
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * 开始录音
//     */
//    public void startRecord() {
//        changeState(STATE_RECORDING);
//    }
//
//    public void willCancelRecord() {
//        changeState(STATE_WANT_TO_CANCEL);
//    }
//
//    public void continueRecord() {
//        changeState(STATE_RECORDING);
//    }
//
//
//    /**
//     * 录音完成后的回调
//     */
//    public interface AudioFinishRecorderListener {
//        //时长  和 文件
//        void onFinish(long seconds, String filePath);
//    }
//
//
//    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
//        audioFinishRecorderListener = listener;
//    }
//
//    //获取音量大小的Runnable
//    private static Runnable mGetVoiceLevelRunnable = new Runnable() {
//        @Override
//        public void run() {
//            while (isRecording) {
//                try {
//                    Thread.sleep(100);
//                    mTime += 0.1;
//                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };
//
//    public static final int MSG_AUDIO_PREPARED = 0X110;
//    private static final int MSG_VOICE_CHANGED = 0X111;
//    public static final int MSG_DIALOG_DIMISS = 0X112;
//
//
//    public static Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_AUDIO_PREPARED:
//                    //TODO 真正现实应该在audio end prepared以后
//                    mDialogManager.showRecordingDialog();
//                    isRecording = true;
//                    new Thread(mGetVoiceLevelRunnable).start();
//                    break;
//                case MSG_VOICE_CHANGED:
//                    mDialogManager.updateVoiceLevel(getInstance().getVoiceLevel(7));
//                    break;
//                case MSG_DIALOG_DIMISS:
//                    mDialogManager.dimissDialog();
//                    break;
//            }
//            return false;
//        }
//    });
//
//
//    /**
//     * 恢复状态 标志位
//     */
//    public static void reset() {
//        isRecording = false;
//        mReady = false;
//        changeState(STATE_NORMAL);
//        mTime = 0;
//    }
//
//
//    //改变状态
//    private static void changeState(int state) {
//        if (mCurState != state) {
//            mCurState = state;
//            switch (state) {
//                case STATE_NORMAL:
//                    if (viewChangeListener != null) {
//                        viewChangeListener.onChange(STATE_NORMAL);
//                    }
//                    break;
//                case STATE_RECORDING:
//                    if (viewChangeListener != null) {
//                        viewChangeListener.onChange(STATE_RECORDING);
//                    }
//                    break;
//                case STATE_WANT_TO_CANCEL:
//                    if (viewChangeListener != null) {
//                        viewChangeListener.onChange(STATE_WANT_TO_CANCEL);
//                    }
//                    break;
//            }
//        }
//    }
//
//
//}
