package cn.wildfire.chat.kit.group;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qiangdong.chat.R;

import java.util.Collections;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.GroupMember;

public class GroupInfoActivity extends WfcBaseActivity {
    private String userId;
    private String groupId;
    private GroupInfo groupInfo;
    private boolean isJoined;
    private GroupViewModel groupViewModel;
    @BindView(R.id.groupNameTextView)
    TextView groupNameTextView;
    @BindView(R.id.portraitImageView)
    ImageView groupPortraitImageView;
    @BindView(R.id.actionButton)
    Button actionButton;

    private MaterialDialog dialog;
    private int actionCount = 0;

    @Override
    protected void afterViews() {
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);

        groupInfo = groupViewModel.getGroupInfo(groupId, true);
        if (groupInfo == null) {
            groupViewModel.groupInfoUpdateLiveData().observe(this, groupInfos -> {
                for (GroupInfo info : groupInfos) {
                    if (info.target.equals(groupId)) {
                        this.groupInfo = info;
                        showGroupInfo(info);
                        dismissLoading();
                    }
                }
            });
        } else {
            showGroupInfo(groupInfo);
        }
        List<GroupMember> groupMembers = groupViewModel.getGroupMembers(groupId, true);
        UserViewModel userViewModel =ViewModelProviders.of(this).get(UserViewModel.class);
        userId = userViewModel.getUserId();
        if (groupMembers == null || groupMembers.isEmpty()) {
            showLoading();
            groupViewModel.groupMembersUpdateLiveData().observe(this, members -> {
                if (members.get(0).groupId.equals(groupId)) {
                    List<GroupMember> gMembers = groupViewModel.getGroupMembers(groupId, false);
                    for (GroupMember member : gMembers) {
                        if (member.type != GroupMember.GroupMemberType.Removed && member.memberId.equals(userId)) {
                            this.isJoined = true;
                        }
                    }
                    dismissLoading();
                    updateActionButtonStatus();
                }
            });
        } else {
            for (GroupMember member : groupMembers) {
                if (member.type != GroupMember.GroupMemberType.Removed && member.memberId.equals(userId)) {
                    this.isJoined = true;
                }
            }
            updateActionButtonStatus();
        }
        showGroupInfo(groupInfo);
    }

    private void updateActionButtonStatus() {
        if (isJoined) {
            actionButton.setText("进入群聊");
        } else {
            actionButton.setText("加入群聊");
        }
    }

    private void showLoading() {
        actionCount++;
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(this)
                    .progress(true, 100)
                    .build();
            dialog.show();
        }
    }

    private void dismissLoading() {
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        actionCount--;
        if (actionCount <= 0) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void showGroupInfo(GroupInfo groupInfo) {
        if (groupInfo == null) {
            return;
        }
        GlideApp.with(this)
                .load(groupInfo.portrait)
                .placeholder(R.mipmap.ic_group_cheat)
                .into(groupPortraitImageView);
        groupNameTextView.setText(groupInfo.name);
    }

    @Override
    protected int contentLayout() {
        return R.layout.group_info_activity;
    }

    @OnClick(R.id.actionButton)
    void action() {
        if (isJoined) {
            Intent intent = ConversationActivity.buildConversationIntent(this, Conversation.ConversationType.Group, groupId, 0);
            startActivity(intent);
            finish();
        } else {
            groupViewModel.addGroupMember(groupInfo, Collections.singletonList(userId));
        }
    }
}
