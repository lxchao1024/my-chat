package cn.wildfire.chat.kit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.qiangdong.chat.R;
import com.qiangdong.chat.ui.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.app.NotificationCompat;

import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfirechat.UserInfoCallBack;
import cn.wildfirechat.message.Message;
import cn.wildfirechat.message.core.MessageDirection;
import cn.wildfirechat.message.notification.RecallMessageContent;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

import static androidx.core.app.NotificationCompat.CATEGORY_MESSAGE;
import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static cn.wildfirechat.message.core.PersistFlag.Persist_And_Count;
import static cn.wildfirechat.model.Conversation.ConversationType.Single;

public class WfcNotificationManager {
    private WfcNotificationManager() {

    }

    private static WfcNotificationManager notificationManager;

    public synchronized static WfcNotificationManager getInstance() {
        if (notificationManager == null) {
            notificationManager = new WfcNotificationManager();
        }
        return notificationManager;
    }

    public void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        notificationConversations.clear();
    }

    private void showNotification(Context context, String tag, int id, String title, String content, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "wfc_notification";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "wildfire chat message",
                    NotificationManager.IMPORTANCE_HIGH);

            channel.enableLights(true); //???????????????icon????????????????????????
            channel.setLightColor(Color.GREEN); //???????????????
            channel.setShowBadge(true); //??????????????????????????????????????????????????????
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setCategory(CATEGORY_MESSAGE)
                .setDefaults(DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        builder.setContentText(content);

        notificationManager.notify(tag, id, builder.build());
    }

    public void handleRecallMessage(Context context, Message message) {
        handleReceiveMessage(context, Collections.singletonList(message));
    }

    String title = "";

    public void handleReceiveMessage(Context context, List<Message> messages) {

        if (messages == null || messages.isEmpty()) {
            return;
        }

        for (Message message : messages) {
            if (message.direction == MessageDirection.Send || (message.content.getPersistFlag() != Persist_And_Count && !(message.content instanceof RecallMessageContent))) {
                continue;
            }
            String pushContent = message.content.pushContent;
            if (TextUtils.isEmpty(pushContent)) {
                pushContent = message.content.digest(message);
            }

            int unreadCount = ChatManager.Instance().getUnreadCount(message.conversation).unread;
            if (unreadCount > 1) {
                pushContent = "[" + unreadCount + "???]" + pushContent;
            }


            if (message.conversation.type == Single) {
                Log.e("userSource","WfcNotificationManager.handleReceiveMessage");
//                String name = ChatManager.Instance().getUserDisplayName(message.conversation.target);
                ChatManager.Instance().getUserInfo(message.conversation.target, false, new UserInfoCallBack() {
                    @Override
                    public void onUserCallback(UserInfo userInfo) {
                        title = TextUtils.isEmpty(userInfo.displayName) ? "?????????" : userInfo.displayName;
                    }
                });

            } else if (message.conversation.type == Conversation.ConversationType.Group) {
                GroupInfo groupInfo = ChatManager.Instance().getGroupInfo(message.conversation.target, false);
                title = groupInfo == null ? "??????" : groupInfo.name;
            } else {
                title = "?????????";
            }
            Intent mainIntent = new Intent(context, MainActivity.class);
            Intent conversationIntent = new Intent(context, ConversationActivity.class);
            conversationIntent.putExtra("conversation", message.conversation);
            PendingIntent pendingIntent = PendingIntent.getActivities(context, notificationId(message.conversation), new Intent[]{mainIntent, conversationIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
            String tag = "wfc notification tag";
            showNotification(context, tag, notificationId(message.conversation), title, pushContent, pendingIntent);
        }
    }

    private List<Conversation> notificationConversations = new ArrayList<>();

    private int notificationId(Conversation conversation) {
        if (!notificationConversations.contains(conversation)) {
            notificationConversations.add(conversation);
        }
        return notificationConversations.indexOf(conversation);
    }
}
