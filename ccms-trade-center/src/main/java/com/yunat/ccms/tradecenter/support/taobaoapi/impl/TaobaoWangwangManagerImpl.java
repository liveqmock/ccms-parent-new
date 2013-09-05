package com.yunat.ccms.tradecenter.support.taobaoapi.impl;

import com.taobao.api.domain.GroupMember;
import com.taobao.api.request.WangwangEserviceGroupmemberGetRequest;
import com.taobao.api.response.WangwangEserviceGroupmemberGetResponse;
import com.yunat.ccms.tradecenter.service.TaobaoService;
import com.yunat.ccms.tradecenter.support.taobaoapi.BaseApiManager;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoWangwangManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: weilin.li
 * Date: 13-7-31
 * Time: 上午11:11
 */
@Component
public class TaobaoWangwangManagerImpl extends BaseApiManager implements TaobaoWangwangManager{

    @Override
    public List<String> getMemberListByShop(String shopName, String shopId) {
        //拼接账号
        String managerId  = "cntaobao" + shopName;
        WangwangEserviceGroupmemberGetRequest req=new WangwangEserviceGroupmemberGetRequest();
        req.setManagerId(managerId);

        //执行taobaoapi获取子账号组列表
        WangwangEserviceGroupmemberGetResponse response = execTaobao(shopId, req);
        List<GroupMember> groupMemberList = response.getGroupMemberList();

        //解析组账号（格式为：cntaobao旗舰店:张三,cntaobao旗舰店:李四    --> [旗舰店:张三, 旗舰店:李四]）
        List<String> memberList = new ArrayList<String>();
        for (GroupMember groupMember : groupMemberList) {
            String memberListStr = groupMember.getMemberList();

            if (!StringUtils.isEmpty(memberListStr)) {
                 String[]  memberArr = memberListStr.split(",");
                 for (String member : memberArr) {
                     String nickMember = member.substring(8);
                     memberList.add(nickMember);
                 }
            }
        }

        return memberList;
    }
}
