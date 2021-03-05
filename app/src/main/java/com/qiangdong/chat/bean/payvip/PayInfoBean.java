package com.qiangdong.chat.bean.payvip;

public class PayInfoBean {
    private String code;
    private String msg;
    private String timestamp;

    public class Data {
        private String amount;
        private String currency;
        private String doing;
        private String merchantNo;
        private String message;
        private String processAmount;
        private String processCurrency;
        private String resultCode;
        private String sign;
        private String tradeNo;
        private String url;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDoing() {
            return doing;
        }

        public void setDoing(String doing) {
            this.doing = doing;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getProcessAmount() {
            return processAmount;
        }

        public void setProcessAmount(String processAmount) {
            this.processAmount = processAmount;
        }

        public String getProcessCurrency() {
            return processCurrency;
        }

        public void setProcessCurrency(String processCurrency) {
            this.processCurrency = processCurrency;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
