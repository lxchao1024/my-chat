package com.qiangdong.chat.bean.userinfo;

import com.qiangdong.chat.bean.BaseBean;

import java.util.List;

public class TrendsBean extends BaseBean<TrendsBean.TrendsInfo> {

    public class TrendsInfo {
        public int pageNo;
        public int pageSize;
        public int totalCount;
        public int totalPages;
        public int nextPage;
        public int prePage;
        public int numOfElements;
        public List<TrendsContext> result;

        public class TrendsContext {
            public String context;
            public String createTime;
            public List<String> image;
        }

    }
}
