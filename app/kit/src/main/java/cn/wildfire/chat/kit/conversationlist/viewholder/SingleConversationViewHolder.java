package cn.wildfire.chat.kit.conversationlist.viewholder;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.qiangdong.chat.R;

import java.util.ArrayList;
import java.util.List;

import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.annotation.ConversationInfoType;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.UserInfoCallBack;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.ConversationInfo;
import cn.wildfirechat.model.UserInfo;

@ConversationInfoType(type = Conversation.ConversationType.Single, line = 0)
@EnableContextMenu
public class SingleConversationViewHolder extends ConversationViewHolder {
    public SingleConversationViewHolder(Fragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    List<String> conversations = new ArrayList<>();

    @Override
    protected void onBindConversationInfo(ConversationInfo conversationInfo) {

        if (!conversations.contains(conversationInfo.conversation.target)) {
//            Log.e("userSource", "SingleConversationViewHolder.onBindConversationInfo" + conversationInfo.conversation.target);
            conversations.add(conversationInfo.conversation.target);
            ChatManagerHolder.gChatManager.getUserInfo(conversationInfo.conversation.target, false, new UserInfoCallBack() {
                @Override
                public void onUserCallback(UserInfo userInfo) {
                    UserViewModel userViewModel = ViewModelProviders.of(fragment).get(UserViewModel.class);
                    String name = userViewModel.getUserDisplayName(userInfo);
                    String portrait;
                    portrait = userInfo.portrait;
                    GlideApp
                            .with(fragment)
                            .load(portrait)
                            .placeholder(R.mipmap.avatar_def)
                            .transforms(new CenterCrop(), new RoundedCorners(10))
                            .into(portraitImageView);

                    nameTextView.setText(name);
                }
            });
        }
    }

}
