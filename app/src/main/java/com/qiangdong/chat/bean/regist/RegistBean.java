package com.qiangdong.chat.bean.regist;

import com.qiangdong.chat.bean.BaseBean;

public class RegistBean extends BaseBean<RegistBean.RegistData> {
    public class RegistData {
        private int userId;
        private String account;
        private String token;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "RegistData{" +
                    "userId=" + userId +
                    ", account='" + account + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }
}
