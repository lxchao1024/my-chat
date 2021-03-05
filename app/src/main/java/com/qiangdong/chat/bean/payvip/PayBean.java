package com.qiangdong.chat.bean.payvip;

import com.qiangdong.chat.bean.BaseBean;

public class PayBean extends BaseBean<PayBean.PayInfo> {
    public class PayInfo {
        private String amount;//订单金额 （2位小数）
        private String mid;//mid
        private String orderId;//dokypay在paytm的订单id
        private String orderNumber;//订单号
        private String txnToken;//当前订单唯一标识

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getTxnToken() {
            return txnToken;
        }

        public void setTxnToken(String txnToken) {
            this.txnToken = txnToken;
        }
    }
}
