package cn.wildfire.chat.kit.conversation;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProviders;

import com.qiangdong.chat.R;
import com.qiangdong.chat.bean.main.MainBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;
import com.qiangdong.chat.ui.activity.UserStateActivity;

import cn.wildfire.chat.kit.IMServiceStatusViewModel;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.UserInfoCallBack;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class ConversationActivity extends WfcBaseActivity {
    private boolean isInitialized = false;
    private ConversationFragment conversationFragment;
    private Conversation conversation;

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    private void setConversationBackground() {
        // you can setup your conversation background here
//        getWindow().setBackgroundDrawableResource(R.mipmap.splash);
    }

    @Override
    protected void afterViews() {
        IMServiceStatusViewModel imServiceStatusViewModel = ViewModelProviders.of(this).get(IMServiceStatusViewModel.class);
        imServiceStatusViewModel.imServiceStatusLiveData().observe(this, aBoolean -> {
            if (!isInitialized && aBoolean) {
                init();
                isInitialized = true;
            }
        });
        conversationFragment = new ConversationFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerFrameLayout, conversationFragment, "content")
                .commit();

        setConversationBackground();
    }

    @Override
    protected int menu() {
        return R.menu.conversation;
    }

    public ConversationFragment getConversationFragment() {
        return conversationFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_conversation_info) {
            showConversationInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!conversationFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    private void showConversationInfo() {
        Intent intent = new Intent(ConversationActivity.this, UserStateActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", conversation.target);
//        intent.putExtra("conversationInfo", ChatManager.Instance().getConversation(conversation));
        startActivity(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        conversation = intent.getParcelableExtra("conversation");
        long initialFocusedMessageId = intent.getLongExtra("toFocusMessageId", -1);
        String channelPrivateChatUser = intent.getStringExtra("channelPrivateChatUser");
        conversationFragment.setupConversation(conversation, null, initialFocusedMessageId, channelPrivateChatUser, null);
    }


    private void init() {
        Intent intent = getIntent();
        conversation = intent.getParcelableExtra("conversation");
        String conversationTitle = intent.getStringExtra("conversationTitle");
        UserInfoBean.UserBean user = (UserInfoBean.UserBean) intent.getSerializableExtra("user");
        long initialFocusedMessageId = intent.getLongExtra("toFocusMessageId", -1);
        if (conversation == null) {
            finish();
        }
        conversationFragment.setupConversation(conversation, conversationTitle, initialFocusedMessageId, null, user);
    }

    public static Intent buildConversationIntent(Context context, Conversation.ConversationType type, String target, int line) {
        return buildConversationIntent(context, type, target, line, -1);
    }

    public static Intent buildConversationIntent(Context context, Conversation.ConversationType type, String target, int line, long toFocusMessageId) {
        Conversation conversation = new Conversation(type, target, line);
        return buildConversationIntent(context, conversation, null, toFocusMessageId);
    }

    public static Intent buildConversationIntent(Context context, Conversation.ConversationType type, String target, int line, String channelPrivateChatUser) {
        Conversation conversation = new Conversation(type, target, line);
        return buildConversationIntent(context, conversation, null, -1);
    }

    public static Intent buildConversationIntent(Context context, Conversation conversation, String channelPrivateChatUser, long toFocusMessageId) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("conversation", conversation);
        intent.putExtra("toFocusMessageId", toFocusMessageId);
        intent.putExtra("channelPrivateChatUser", channelPrivateChatUser);
        return intent;
    }
}
