package com.qiangdong.chat.bean;

public class ConfigBean extends BaseBean<ConfigBean.Config> {
    public class Config {
        private String upToken;
        private int chatNumber;
        private int dokyPay;
        private int googlePay;
        private int rabbit;
        private int sayHelloNumber;

        public String getUpToken() {
            return upToken;
        }

        public void setUpToken(String upToken) {
            this.upToken = upToken;
        }

        public int getChatNumber() {
            return chatNumber;
        }

        public void setChatNumber(int chatNumber) {
            this.chatNumber = chatNumber;
        }

        public int getDokyPay() {
            return dokyPay;
        }

        public void setDokyPay(int dokyPay) {
            this.dokyPay = dokyPay;
        }

        public int getGooglePay() {
            return googlePay;
        }

        public void setGooglePay(int googlePay) {
            this.googlePay = googlePay;
        }

        public int getRabbit() {
            return rabbit;
        }

        public void setRabbit(int rabbit) {
            this.rabbit = rabbit;
        }

        public int getSayHelloNumber() {
            return sayHelloNumber;
        }

        public void setSayHelloNumber(int sayHelloNumber) {
            this.sayHelloNumber = sayHelloNumber;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "upToken='" + upToken + '\'' +
                    ", chatNumber=" + chatNumber +
                    ", dokyPay=" + dokyPay +
                    ", googlePay=" + googlePay +
                    ", rabbit=" + rabbit +
                    ", sayHelloNumber=" + sayHelloNumber +
                    '}';
        }
    }
}
