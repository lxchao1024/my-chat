package com.qiangdong.chat.bean.userinfo;

import com.qiangdong.chat.Constant;
import com.qiangdong.chat.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

public class UserInfoBean extends BaseBean<UserInfoBean.UserBean> {
    public static class UserBean implements Serializable {
        private String userId;
        private String nickname;
        private String headImg;
        private int gender;
        private String profile;
        private String age;
        private String account;
        private String vipType;
        private int maritalStatus;
        private String birthday;
        private String height;
        private String weight;
        private String work;
        private String hobby;
        private int followCount;
        private int fansCount;
        private String countryCode;
        private List<String> album;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getVipType() {
            return vipType;
        }

        public void setVipType(String vipType) {
            this.vipType = vipType;
        }

        public int getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(int maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public List<String> getAlbum() {
            return album;
        }

        public void setAlbum(List<String> album) {
            this.album = album;
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "userId='" + userId + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", headImg='" + headImg + '\'' +
                    ", gender=" + gender +
                    ", profile='" + profile + '\'' +
                    ", age='" + age + '\'' +
                    ", account='" + account + '\'' +
                    ", vipType='" + vipType + '\'' +
                    ", maritalStatus='" + maritalStatus + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", height='" + height + '\'' +
                    ", weight='" + weight + '\'' +
                    ", work='" + work + '\'' +
                    ", hobby='" + hobby + '\'' +
                    ", followCount=" + followCount +
                    ", fansCount=" + fansCount +
                    ", countryCode='" + countryCode + '\'' +
                    ", album=" + album +
                    '}';
        }
    }
}
