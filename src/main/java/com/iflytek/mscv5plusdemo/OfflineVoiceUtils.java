package com.iflytek.mscv5plusdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

/**
 *  离线语音播报
 *
 * **/

public class OfflineVoiceUtils implements InitListener, SynthesizerListener {

    // 语音合成对象
    public static SpeechSynthesizer mTts;
    // 默认本地发音人
    public static String voicerLocal = "xiaoyan";
    Context mcontext;
    private static OfflineVoiceUtils instance = null;
    private boolean isInitSuccess = false;

    // 获取发音人资源路径
    public String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();

        // 合成通用资源

        tempBuffer.append(ResourceUtil.generateResourcePath(mcontext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        // 发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mcontext, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + OfflineVoiceUtils.voicerLocal + ".jet"));
        return tempBuffer.toString();
    }

    //开始合成
    public void speak(String msg , Context context) {
        mcontext = context;
        if (isInitSuccess){
            if (mTts.isSpeaking()) {
                // stop();
                return;
            }

            mTts.startSpeaking(msg, this);
        }else {
            init();
            mTts.startSpeaking(msg, this);
        }
    }

    public void init() {
        mTts = SpeechSynthesizer.createSynthesizer(mcontext, this);
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
        // 设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        // 设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }

    public static OfflineVoiceUtils getInstance(final Context context) {
        if (instance == null){
            instance = new OfflineVoiceUtils();
            instance.setContext(context);
        }
        return instance;
    }


    /***
     * 播报回调
     *
     * */

    public void setContext(Context context) {
        this.mcontext = context;
    }

    @Override
    public void onInit(int i) {

        if (i != ErrorCode.SUCCESS) {
            Log.e("contect初始化失败,错误码:","i");
        }else {
            Log.e("offlineTTS","success");
        }
        if (i == ErrorCode.SUCCESS) {
            isInitSuccess = true;
        }
    }

    @Override
    public void onSpeakBegin() {
        Log.e("offlineTTS","speek begin");
    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {
        Log.e("offlineTTS",s);
    }

    @Override
    public void onSpeakPaused() {
        Log.e("offlineTTS","pause");
    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {
        Log.e("offlineTTS","finish");
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        Log.e("offlineTTS","bundle");
    }

    public void release() {
        if (null != mTts) {
            mTts.stopSpeaking();
            mTts.destroy();  //退出时释放
        }
    }
}
