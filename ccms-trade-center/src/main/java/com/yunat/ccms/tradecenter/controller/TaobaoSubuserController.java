package com.yunat.ccms.tradecenter.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taobao.api.domain.SubUserInfo;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.SimpleSubuserVO;
import com.yunat.ccms.tradecenter.controller.vo.TaobaoSubuserRequest;
import com.yunat.ccms.tradecenter.domain.LoginSubuserRelaDomain;
import com.yunat.ccms.tradecenter.service.LoginSubuserRelaService;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoSubuserManager;

/**
 * User: weilin.li
 * Date: 13-7-31
 * Time: 上午11:54
 */
@Controller
@RequestMapping(value = "/taobaosubuser/*")
public class TaobaoSubuserController {

    @Autowired
    private TaobaoSubuserManager taobaoSubuserManager;

    @Autowired
    private LoginSubuserRelaService loginSubuserRelaService;

    @RequestMapping(value = "/subuserList", method = RequestMethod.POST)
    @ResponseBody
    public ControlerResult subuserList(@RequestBody TaobaoSubuserRequest taobaoSubuserRequest) throws Exception {

        List<SubUserInfo> subUserInfoList = taobaoSubuserManager.getSubuserList(taobaoSubuserRequest.getNick(), taobaoSubuserRequest.getShopId());

        return ControlerResult.newSuccess(subUserInfoList);
    }

    @RequestMapping(value = "/simpleSubuserList", method = RequestMethod.POST)
    @ResponseBody
    public ControlerResult simpleSubuserList(@RequestBody TaobaoSubuserRequest taobaoSubuserRequest) throws Exception {

        List<SubUserInfo> subUserInfoList = taobaoSubuserManager.getSubuserList(taobaoSubuserRequest.getNick(), taobaoSubuserRequest.getShopId());

        List<SimpleSubuserVO> simpleSubuserVOs = new ArrayList<SimpleSubuserVO>();

        for (SubUserInfo subUserInfo : subUserInfoList) {
            SimpleSubuserVO simpleSubuserVO = new SimpleSubuserVO();
            simpleSubuserVO.setFullName(subUserInfo.getFullName());
            simpleSubuserVO.setNick(subUserInfo.getNick());
            simpleSubuserVO.setOnline(subUserInfo.getIsOnline());

            simpleSubuserVOs.add(simpleSubuserVO);
        }

        return ControlerResult.newSuccess(simpleSubuserVOs);
    }

    @RequestMapping(value = "/checkIfBindSubuser", method = RequestMethod.GET)
    @ResponseBody
    public ControlerResult checkIfBindSubuser(String dp_id) throws Exception {

    	LoginSubuserRelaDomain lsrd = loginSubuserRelaService.findloginSubuserRela(dp_id);
    	if(lsrd == null || StringUtils.isEmpty(lsrd.getTaobaoSubuser())){
            return ControlerResult.newError("未绑定旺旺子账户，请绑定先！");
    	}else{
    		return ControlerResult.newSuccess(lsrd.getTaobaoSubuser());
    	}
    }

    @RequestMapping(value = "/saveBindSubuser", method = RequestMethod.POST)
    @ResponseBody
    public ControlerResult saveBindSubuser(@RequestBody LoginSubuserRelaDomain loginSubuserRelaDomain) throws Exception {
    	boolean flag = loginSubuserRelaService.save(loginSubuserRelaDomain);
    	if(flag){
    		return ControlerResult.newSuccess("成功绑定旺旺子账号");
    	}else{
    		return ControlerResult.newError();
    	}
    }
}
