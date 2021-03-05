package com.qiangdong.chat.bean.regist;

import com.qiangdong.chat.bean.BaseBean;

public class DefuatlNickNameBean extends BaseBean<DefuatlNickNameBean.NickNameBean> {
    public class NickNameBean {
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Override
        public String toString() {
            return "NickNameBean{" +
                    "nickname='" + nickname + '\'' +
                    '}';
        }
    }
}
