package com.qiangdong.chat.bean.login;

import com.qiangdong.chat.bean.BaseBean;

public class LoginBean extends BaseBean<LoginBean.loginData> {
    public class loginData{
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
            return "loginData{" +
                    "userId=" + userId +
                    ", account='" + account + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

}
