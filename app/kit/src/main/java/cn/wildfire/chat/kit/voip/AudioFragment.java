package cn.wildfire.chat.kit.voip;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiangdong.chat.MyApp;
import com.qiangdong.chat.R;
import com.qiangdong.chat.ui.PayActivity;

import org.webrtc.StatsReport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.UserInfoCallBack;
import cn.wildfirechat.avenginekit.AVEngineKit;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class AudioFragment extends Fragment implements AVEngineKit.CallSessionCallback {
    private AVEngineKit gEngineKit;
    private boolean micEnabled = true;
    private boolean isSpeakerOn = false;

    @BindView(R.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.muteImageView)
    ImageView muteImageView;
    @BindView(R.id.speakerImageView)
    ImageView spearImageView;
    @BindView(R.id.incomingActionContainer)
    ViewGroup incomingActionContainer;
    @BindView(R.id.outgoingActionContainer)
    ViewGroup outgoingActionContainer;
    @BindView(R.id.descTextView)
    TextView descTextView;
    @BindView(R.id.durationTextView)
    TextView durationTextView;

    public static VideoFragment newInstance(String targetId, boolean isOutgoing) {

        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putBoolean("outgoing", isOutgoing);
        args.putString("targetId", targetId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.av_p2p_audio_layout, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void didCallEndWithReason(AVEngineKit.CallEndReason reason) {
        // never called
    }

    @Override
    public void didChangeState(AVEngineKit.CallState state) {
        runOnUiThread(() -> {
            if (state == AVEngineKit.CallState.Connected) {
                incomingActionContainer.setVisibility(View.GONE);
                outgoingActionContainer.setVisibility(View.VISIBLE);
                descTextView.setVisibility(View.GONE);
                durationTextView.setVisibility(View.VISIBLE);
            } else {
                // do nothing now
            }
        });
    }

    @Override
    public void didChangeMode(boolean audioOnly) {
        // never called
    }

    @Override
    public void didCreateLocalVideoTrack() {
        // never called
    }

    @Override
    public void didReceiveRemoteVideoTrack() {
        // should never called
    }

    @Override
    public void didError(String error) {

    }


    @Override
    public void didGetStats(StatsReport[] reports) {
        runOnUiThread(() -> {
            //hudFragment.updateEncoderStatistics(reports);
            // TODO
        });
    }

    @OnClick(R.id.muteImageView)
    public void mute() {
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session != null && session.getState() != AVEngineKit.CallState.Idle) {
            if (session.muteAudio(!micEnabled)) {
                micEnabled = !micEnabled;
            }
            muteImageView.setSelected(!micEnabled);
        }
    }

    @OnClick({R.id.incomingHangupImageView, R.id.outgoingHangupImageView})
    public void hangup() {
        Intent intent = new Intent();
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(MyApp.getContext(), PayActivity.class);
        startActivity(intent);
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        session.endCall();
        getActivity().finish();
//        if (session != null) {
//            session.endCall();
//        } else {
//            getActivity().finish();
//        }
    }

    @OnClick(R.id.acceptImageView)
    public void onCallConnect() {
        Intent intent = new Intent();
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(MyApp.getContext(), PayActivity.class);
        startActivity(intent);
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        session.endCall();
        getActivity().finish();
//        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
//        if (session != null && session.getState() == AVEngineKit.CallState.Incoming) {
//            session.answerCall(false);
//        } else {
//            getActivity().finish();
//        }
    }

    @OnClick(R.id.minimizeImageView)
    public void minimize() {
        ((SingleVoipCallActivity) getActivity()).showFloatingView();
    }

    @OnClick(R.id.speakerImageView)
    public void speakerClick() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (isSpeakerOn) {
            isSpeakerOn = false;
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            isSpeakerOn = true;
            audioManager.setMode(AudioManager.MODE_NORMAL);

        }
        spearImageView.setSelected(isSpeakerOn);
        audioManager.setSpeakerphoneOn(isSpeakerOn);
    }

    private void init() {
        gEngineKit = ((SingleVoipCallActivity) getActivity()).getEngineKit();
        if (gEngineKit.getCurrentSession() != null && gEngineKit.getCurrentSession().getState() == AVEngineKit.CallState.Connected) {
            descTextView.setVisibility(View.GONE);
            outgoingActionContainer.setVisibility(View.VISIBLE);
            durationTextView.setVisibility(View.VISIBLE);
        } else {
            if (((SingleVoipCallActivity) getActivity()).isOutgoing()) {
                descTextView.setText(R.string.av_waiting);
                outgoingActionContainer.setVisibility(View.VISIBLE);
                incomingActionContainer.setVisibility(View.GONE);
            } else {
                descTextView.setText(R.string.av_audio_invite);
                outgoingActionContainer.setVisibility(View.GONE);
                incomingActionContainer.setVisibility(View.VISIBLE);
            }
        }
        String targetId = ((SingleVoipCallActivity) getActivity()).getTargetId();
        Log.e("userSource", "AudioFragment.init");

        ChatManager.Instance().getUserInfo(targetId, false, new UserInfoCallBack() {
            @Override
            public void onUserCallback(UserInfo userInfo) {
                Glide.with(AudioFragment.this).load(userInfo.portrait).into(portraitImageView);
                UserViewModel userViewModel = ViewModelProviders.of(AudioFragment.this).get(UserViewModel.class);
                nameTextView.setText(userViewModel.getUserDisplayName(userInfo));
                muteImageView.setSelected(!micEnabled);
                updateCallDuration();
            }
        });

    }

    private void runOnUiThread(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }

    private Handler handler = new Handler();

    private void updateCallDuration() {
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session != null && session.getState() == AVEngineKit.CallState.Connected) {
            long s = System.currentTimeMillis() - session.getStartTime();
            s = s / 1000;
            String text;
            if (s > 3600) {
                text = String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
            } else {
                text = String.format("%02d:%02d", s / 60, (s % 60));
            }
            durationTextView.setText(text);
        }
        handler.postDelayed(this::updateCallDuration, 1000);
    }
}
