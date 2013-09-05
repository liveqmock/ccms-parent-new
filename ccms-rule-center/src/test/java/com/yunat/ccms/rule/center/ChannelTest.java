package com.yunat.ccms.rule.center;

import java.util.List;

import com.taobao.api.ApiRuleException;
import com.taobao.api.domain.User;
import com.taobao.api.request.UsersGetRequest;
import com.taobao.api.response.UsersGetResponse;
import com.yunat.base.response.BaseResponse;
import com.yunat.channel.external.taobao.UserInfoTaobao;
import com.yunat.channel.external.taobao.handle.TaobaoClientInit;

/**
 * 调渠道接口
 * 
 * @author MaGiCalL
 */
public class ChannelTest {
	public static void main(final String[] args) {
//		TaobaoClientInit.setConfig("http://gw.api.tbsandbox.com/router/rest", "1012283535",
//				"sandbox7a760229ab34bca67cc87fcf2"); //依次是url,appkey,seccret
		TaobaoClientInit.setConfig("http://gw.api.taobao.com/router/rest", "12283535",
				"4283e0d7a760229ab34bca67cc87fcf2"); //依次是url,appkey,seccret
		final UsersGetRequest req = new UsersGetRequest();
		final String nicks = "miaomiaozhu0620";
		req.setFields("user_id,nick");
		req.setNicks(nicks);
		UsersGetResponse response = new UsersGetResponse();
		try {
			final BaseResponse<UsersGetResponse> res = UserInfoTaobao.getUserInfo(req);
			if (res.isSuccess()) {
				response = res.getRtnData();
				//TODO 需要处理的结果集
				final List<User> users = response.getUsers();
				// TODO 对返回的具体数据处理
				for (final User user : users) {
					// 以下为非隐私属性。
					System.out.println(user.getUserId());
					System.out.println(user.getUid());
					System.out.println(user.getNick());
					System.out.println(user.getSex());
					System.out.println(user.getBuyerCredit());
					System.out.println(user.getSellerCredit());
					System.out.println(user.getLocation());
					System.out.println(user.getCreated());
					System.out.println(user.getLastVisit());
					System.out.println(user.getType());
					System.out.println(user.getAvatar());
					System.out.println(user.getHasShop());
					System.out.println(user.getIsLightningConsignment());
					System.out.println(user.getVipInfo());
				}
			} else if (!res.isSuccess()) {
				//TODO 处理失败
				System.out.println(res.getErrCode());
				System.out.println(res.getErrMsg());
			}
		} catch (final ApiRuleException e) {
			// TODO 规则校验失败处理，规则为：nicks不为空
		}
	}
}
