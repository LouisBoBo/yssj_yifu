///**
// * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.yssj.huanxin.activity;
//
//import java.util.UUID;
//
//import android.media.AudioManager;
//import android.media.RingtoneManager;
//import android.media.SoundPool;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.widget.Button;
//import android.widget.Chronometer;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.chat.EMCallStateChangeListener;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMVideoCallHelper;
//import com.easemob.exceptions.EMServiceNotReadyException;
//import com.yssj.activity.R;
//import com.yssj.huanxin.controller.HXSDKHelper;
//import com.yssj.huanxin.utils.CameraHelper;
//
//public class VideoCallActivity extends CallActivity implements OnClickListener {
//
//    private SurfaceView localSurface;
//    private SurfaceHolder localSurfaceHolder;
//    private static SurfaceView oppositeSurface;
//    private SurfaceHolder oppositeSurfaceHolder;
//
//    private boolean isMuteState;
//    private boolean isHandsfreeState;
//    private boolean isAnswered;
//    private int streamID;
//    private boolean endCallTriggerByMe = false;
//
//    EMVideoCallHelper callHelper;
//    private TextView callStateTextView;
//
//    private Handler handler = new Handler();
//    private LinearLayout comingBtnContainer;
//    private Button refuseBtn;
//    private Button answerBtn;
//    private Button hangupBtn;
//    private ImageView muteImage;
//    private ImageView handsFreeImage;
//    private TextView nickTextView;
//    private Chronometer chronometer;
//    private LinearLayout voiceContronlLayout;
//    private RelativeLayout rootContainer;
//    private RelativeLayout btnsContainer;
//    private CameraHelper cameraHelper;
//    private LinearLayout topContainer;
//    private LinearLayout bottomContainer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(savedInstanceState != null){
//        	finish();
//        	return;
//        }
//        setContentView(R.layout.activity_video_call);
//
//        HXSDKHelper.getInstance().isVideoCalling = true;
//        getWindow().addFlags(
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//
//        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
//        comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
//        rootContainer = (RelativeLayout) findViewById(R.id.root_layout);
//        refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
//        answerBtn = (Button) findViewById(R.id.btn_answer_call);
//        hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
//        muteImage = (ImageView) findViewById(R.id.iv_mute);
//        handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
//        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
//        nickTextView = (TextView) findViewById(R.id.tv_nick);
//        chronometer = (Chronometer) findViewById(R.id.chronometer);
//        voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);
//        btnsContainer = (RelativeLayout) findViewById(R.id.ll_btns);
//        topContainer = (LinearLayout) findViewById(R.id.ll_top_container);
//        bottomContainer = (LinearLayout) findViewById(R.id.ll_bottom_container);
//
//        refuseBtn.setOnClickListener(this);
//        answerBtn.setOnClickListener(this);
//        hangupBtn.setOnClickListener(this);
//        muteImage.setOnClickListener(this);
//        handsFreeImage.setOnClickListener(this);
//        rootContainer.setOnClickListener(this);
//
//        msgid = UUID.randomUUID().toString();
//        // ????????????????????????????????????
//        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
//        username = getIntent().getStringExtra("username");
//
//        // ???????????????
//        nickTextView.setText(username);
//
//        // ?????????????????????surfaceview
//        localSurface = (SurfaceView) findViewById(R.id.local_surface);
//        localSurface.setZOrderMediaOverlay(true);
//        localSurface.setZOrderOnTop(true);
//        localSurfaceHolder = localSurface.getHolder();
//
//        // ??????callHelper,cameraHelper
//        callHelper = EMVideoCallHelper.getInstance();
//        cameraHelper = new CameraHelper(callHelper, localSurfaceHolder);
//
//        // ?????????????????????surfaceview
//        oppositeSurface = (SurfaceView) findViewById(R.id.opposite_surface);
//        oppositeSurfaceHolder = oppositeSurface.getHolder();
//        // ???????????????????????????surfaceview
//        callHelper.setSurfaceView(oppositeSurface);
//
//        localSurfaceHolder.addCallback(new localCallback());
//        oppositeSurfaceHolder.addCallback(new oppositeCallback());
//
//        // ??????????????????
//        addCallStateListener();
//
//        if (!isInComingCall) {// ????????????
//            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
//            outgoing = soundPool.load(this, R.raw.outgoing, 1);
//
//            comingBtnContainer.setVisibility(View.INVISIBLE);
//            hangupBtn.setVisibility(View.VISIBLE);
//            String st = getResources().getString(R.string.Are_connected_to_each_other);
//            callStateTextView.setText(st);
//
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    streamID = playMakeCallSounds();
//                }
//            }, 300);
//        } else { // ???????????????
//            voiceContronlLayout.setVisibility(View.INVISIBLE);
//            localSurface.setVisibility(View.INVISIBLE);
//            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            audioManager.setMode(AudioManager.MODE_RINGTONE);
//            audioManager.setSpeakerphoneOn(true);
//            ringtone = RingtoneManager.getRingtone(this, ringUri);
//            ringtone.play();
//        }
//    }
//
//    /**
//     * ??????SurfaceHolder callback
//     *
//     */
//    class localCallback implements SurfaceHolder.Callback {
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            cameraHelper.startCapture();
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//        }
//    }
//
//    /**
//     * ??????SurfaceHolder callback
//     */
//    class oppositeCallback implements SurfaceHolder.Callback {
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            callHelper.onWindowResize(width, height, format);
//            if (!cameraHelper.isStarted()) {
//                if (!isInComingCall) {
//                    try {
//                        // ??????????????????
//                        EMChatManager.getInstance().makeVideoCall(username);
//                        // ??????cameraHelper??????????????????
//                        cameraHelper.setStartFlag(true);
//                    } catch (EMServiceNotReadyException e) {
//                        Toast.makeText(VideoCallActivity.this, R.string.Is_not_yet_connected_to_the_server , 1).show();
//                    }
//                }
//
//            } else {
//            }
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//        }
//
//    }
//
//    /**
//     * ????????????????????????
//     */
//    void addCallStateListener() {
//        callStateListener = new EMCallStateChangeListener() {
//
//            @Override
//            public void onCallStateChanged(CallState callState, CallError error) {
//                // Message msg = handler.obtainMessage();
//                switch (callState) {
//
//                case CONNECTING: // ??????????????????
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            callStateTextView.setText(R.string.Are_connected_to_each_other);
//                        }
//
//                    });
//                    break;
//                case CONNECTED: // ????????????????????????
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            callStateTextView.setText(R.string.have_connected_with);
//                        }
//
//                    });
//                    break;
//
//                case ACCEPTED: // ??????????????????
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            try {
//                                if (soundPool != null)
//                                    soundPool.stop(streamID);
//                            } catch (Exception e) {
//                            }
//                            openSpeakerOn();
//                            ((TextView)findViewById(R.id.tv_is_p2p)).setText(EMChatManager.getInstance().isDirectCall()
//                                    ? R.string.direct_call : R.string.relay_call);
//                            handsFreeImage.setImageResource(R.drawable.icon_speaker_on);
//                            isHandsfreeState = true;
//                            chronometer.setVisibility(View.VISIBLE);
//                            chronometer.setBase(SystemClock.elapsedRealtime());
//                            // ????????????
//                            chronometer.start();
//                            nickTextView.setVisibility(View.INVISIBLE);
//                            callStateTextView.setText(R.string.In_the_call);
//                            callingState = CallingState.NORMAL;
//                        }
//
//                    });
//                    break;
//                case DISCONNNECTED: // ????????????
//                    final CallError fError = error;
//                    runOnUiThread(new Runnable() {
//                        private void postDelayedCloseMsg() {
//                            handler.postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    saveCallRecord(1);
//                                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
//                                    animation.setDuration(800);
//                                    rootContainer.startAnimation(animation);
//                                    finish();
//                                }
//
//                            }, 200);
//                        }
//
//                        @Override
//                        public void run() {
//                            chronometer.stop();
//                            callDruationText = chronometer.getText().toString();
//                            String s1 = getResources().getString(R.string.The_other_party_refused_to_accept);
//                            String s2 = getResources().getString(R.string.Connection_failure);
//                            String s3 = getResources().getString(R.string.The_other_party_is_not_online);
//                            String s4 = getResources().getString(R.string.The_other_is_on_the_phone_please);
//                            String s5 = getResources().getString(R.string.The_other_party_did_not_answer);
//
//                            String s6 = getResources().getString(R.string.hang_up);
//                            String s7 = getResources().getString(R.string.The_other_is_hang_up);
//                            String s8 = getResources().getString(R.string.did_not_answer);
//                            String s9 = getResources().getString(R.string.Has_been_cancelled);
//
//                            if (fError == CallError.REJECTED) {
//                                callingState = CallingState.BEREFUESD;
//                                callStateTextView.setText(s1);
//                            } else if (fError == CallError.ERROR_TRANSPORT) {
//                                callStateTextView.setText(s2);
//                            } else if (fError == CallError.ERROR_INAVAILABLE) {
//                                callingState = CallingState.OFFLINE;
//                                callStateTextView.setText(s3);
//                            } else if (fError == CallError.ERROR_BUSY) {
//                                callingState = CallingState.BUSY;
//                                callStateTextView.setText(s4);
//                            } else if (fError == CallError.ERROR_NORESPONSE) {
//                                callingState = CallingState.NORESPONSE;
//                                callStateTextView.setText(s5);
//                            } else {
//                                if (isAnswered) {
//                                    callingState = CallingState.NORMAL;
//                                    if (endCallTriggerByMe) {
////                                        callStateTextView.setText(s6);
//                                    } else {
//                                        callStateTextView.setText(s7);
//                                    }
//                                } else {
//                                    if (isInComingCall) {
//                                        callingState = CallingState.UNANSWERED;
//                                        callStateTextView.setText(s8);
//                                    } else {
//                                        if (callingState != CallingState.NORMAL) {
//                                            callingState = CallingState.CANCED;
//                                            callStateTextView.setText(s9);
//                                        } else {
//                                            callStateTextView.setText(s6);
//                                        }
//                                    }
//                                }
//                            }
//                            postDelayedCloseMsg();
//                        }
//
//                    });
//
//                    break;
//
//                default:
//                    break;
//                }
//
//            }
//        };
//        EMChatManager.getInstance().addVoiceCallStateChangeListener(callStateListener);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//        case R.id.btn_refuse_call: // ????????????
//            refuseBtn.setEnabled(false);
//            if (ringtone != null)
//                ringtone.stop();
//            try {
//                EMChatManager.getInstance().rejectCall();
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                saveCallRecord(1);
//                finish();
//            }
//            callingState = CallingState.REFUESD;
//            break;
//
//        case R.id.btn_answer_call: // ????????????
//            answerBtn.setEnabled(false);
//            if (ringtone != null)
//                ringtone.stop();
//            if (isInComingCall) {
//                try {
//                    callStateTextView.setText("????????????...");
//                    EMChatManager.getInstance().answerCall();
//                    cameraHelper.setStartFlag(true);
//
//                    openSpeakerOn();
//                    handsFreeImage.setImageResource(R.drawable.icon_speaker_on);
//                    isAnswered = true;
//                    isHandsfreeState = true;
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    saveCallRecord(1);
//                    finish();
//                    return;
//                }
//            }
//            comingBtnContainer.setVisibility(View.INVISIBLE);
//            hangupBtn.setVisibility(View.VISIBLE);
//            voiceContronlLayout.setVisibility(View.VISIBLE);
//            localSurface.setVisibility(View.VISIBLE);
//            break;
//
//        case R.id.btn_hangup_call: // ????????????
//            hangupBtn.setEnabled(false);
//            if (soundPool != null)
//                soundPool.stop(streamID);
//            chronometer.stop();
//            endCallTriggerByMe = true;
//            callStateTextView.setText(getResources().getString(R.string.hanging_up));
//            try {
//                EMChatManager.getInstance().endCall();
//            } catch (Exception e) {
//                e.printStackTrace();
//                saveCallRecord(1);
//                finish();
//            }
//            break;
//
//        case R.id.iv_mute: // ????????????
//            if (isMuteState) {
//                // ????????????
//                muteImage.setImageResource(R.drawable.icon_mute_normal);
//                audioManager.setMicrophoneMute(false);
//                isMuteState = false;
//            } else {
//                // ????????????
//                muteImage.setImageResource(R.drawable.icon_mute_on);
//                audioManager.setMicrophoneMute(true);
//                isMuteState = true;
//            }
//            break;
//        case R.id.iv_handsfree: // ????????????
//            if (isHandsfreeState) {
//                // ????????????
//                handsFreeImage.setImageResource(R.drawable.icon_speaker_normal);
//                closeSpeakerOn();
//                isHandsfreeState = false;
//            } else {
//                handsFreeImage.setImageResource(R.drawable.icon_speaker_on);
//                openSpeakerOn();
//                isHandsfreeState = true;
//            }
//            break;
//        case R.id.root_layout:
//            if (callingState == CallingState.NORMAL) {
//                if (bottomContainer.getVisibility() == View.VISIBLE) {
//                    bottomContainer.setVisibility(View.GONE);
//                    topContainer.setVisibility(View.GONE);
//
//                } else {
//                    bottomContainer.setVisibility(View.VISIBLE);
//                    topContainer.setVisibility(View.VISIBLE);
//
//                }
//            }
//
//            break;
//        default:
//            break;
//        }
//
//    }
//
//    /*@Override
//	protected void onResume() {
//		super.onResume();
//		JPushInterface.onResume(this);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		JPushInterface.onPause(this);
//
//	}*/
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        HXSDKHelper.getInstance().isVideoCalling = false;
//        try {
//			callHelper.setSurfaceView(null);
//			cameraHelper.stopCapture();
//			oppositeSurface = null;
//			cameraHelper = null;
//		} catch (Exception e) {
//		}
//    }
//
//    @Override
//    public void onBackPressed() {
//        EMChatManager.getInstance().endCall();
//        callDruationText = chronometer.getText().toString();
//        saveCallRecord(1);
//        finish();
//    }
//
//}
