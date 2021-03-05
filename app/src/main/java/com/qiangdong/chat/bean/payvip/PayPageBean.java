package com.qiangdong.chat.bean.payvip;

import com.qiangdong.chat.bean.BaseBean;

public class PayPageBean extends BaseBean<PayPageBean.PageBean> {
    public class PageBean {
        private String amount;
        private String explain;
        private int id;
        private String textOne;
        private String textTwo;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTextOne() {
            return textOne;
        }

        public void setTextOne(String textOne) {
            this.textOne = textOne;
        }

        public String getTextTwo() {
            return textTwo;
        }

        public void setTextTwo(String textTwo) {
            this.textTwo = textTwo;
        }
    }
}
