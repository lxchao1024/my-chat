package com.qiangdong.chat.bean.main;

import com.qiangdong.chat.bean.BaseBean;
import com.qiangdong.chat.bean.userinfo.UserInfoBean;

import java.io.Serializable;
import java.util.List;

public class MainBean extends BaseBean<MainBean.MainDataBean> implements Serializable {

    public static class MainDataBean implements Serializable {
        private int pageNo;//页码
        private int pageSize;//每页记录数
        private int numOfElements;//当前页实际的记录数
        private int nextPage;//下一页页号
        private int prePage;//上一页页号
        private int totalCount;//记录总条数
        private int totalPages;//总页数
        private boolean hasNext;//是否还有下一页
        private boolean hasPre;//是否还有上一页
        private List<UserInfoBean.UserBean> result;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getNumOfElements() {
            return numOfElements;
        }

        public void setNumOfElements(int numOfElements) {
            this.numOfElements = numOfElements;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public boolean isHasPre() {
            return hasPre;
        }

        public void setHasPre(boolean hasPre) {
            this.hasPre = hasPre;
        }

        public List<UserInfoBean.UserBean> getMainUserItem() {
            return result;
        }

        public void setMainUserItem(List<UserInfoBean.UserBean> mainUserItem) {
            this.result = mainUserItem;
        }
    }
}
