package com.yunat.ccms.rule.center.runtime.fact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.taobao.api.ApiException;
import com.taobao.api.ApiRuleException;
import com.taobao.api.domain.BasicMember;
import com.taobao.api.domain.User;
import com.taobao.api.request.CrmMembersGetRequest;
import com.taobao.api.request.UserGetRequest;
import com.taobao.api.response.CrmMembersGetResponse;
import com.taobao.api.response.UserGetResponse;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.taobao.handler.CommonInvokerHandler;
import com.yunat.ccms.core.support.utils.ListUtils;
import com.yunat.ccms.rule.center.RuleCenterCons;
import com.yunat.ccms.rule.center.RuleCenterRuntimeException;
import com.yunat.ccms.rule.center.engine.FactResolver;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;
import com.yunat.channel.external.taobao.MemberTaobao;

/**
 * 客户(Customer)的属性从淘宝接口取,订单(Order)的属性从数据库取 l 满足如下任意一个条件判断为新客人 调用如下接口
 * http://api.taobao.com/apidoc/api.htm?spm=0.0.0.0.JrU1TE&path=scopeId:10529-
 * apiId:10838 1、返回错误码“isv.buyer-not-exist:buyer_nick”
 * 2、获得了数据，但是grade=0并且trade_count=0
 * 
 * @author wenjian.liang
 */
@Component("orderResolver")
public class OrderResolver implements FactResolver<Long> {
	private static final String GET_ONE_SQL = "select * "//
			+ "from vw_taobao_order_quota where tid =:tid";

	private static final String NEW_CUSTOMER = "1";// 新客
	private static final String REGULARS = "2";// 回头客

	protected static final ThreadLocal<Date> NOW_LOCAL = new ThreadLocal<Date>();

	protected static Date getNow() {
		Date now = NOW_LOCAL.get();
		if (now == null) {
			now = new Date();
			NOW_LOCAL.set(now);
		}
		return now;
	}

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	protected AccessTokenService accessTokenService;
	@Autowired
	protected CommonInvokerHandler invokerHandler;

	protected RowMapper<Order> rowMapper = new RowMapper<Order>() {
		@Override
		public Order mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return buildOrder(rs);
		}
	};

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected AccessToken accessToken;

	protected Order buildOrder(final ResultSet rs) throws SQLException {
		final Order order = new Order();
		order.setDiscountFee(rs.getDouble("discount_fee"));

		final String numIidsStr = rs.getString("num_iids");
		final String[] numIids = RuleCenterCons.COMMA.split(numIidsStr.trim());
		order.setHasProducts(Arrays.asList(numIids));

		order.setPayment(rs.getDouble("payment"));
		order.setPostFee(rs.getDouble("post_fee"));
		order.setProductAmount(rs.getLong("product_amount"));// 商品件数
		order.setProductCount(rs.getLong("product_count"));// 商品种数
		order.setReceiverLocation(rs.getString("receiver_location"));
		order.setTid(rs.getString("tid"));
		order.setTradeFrom(rs.getString("trade_from"));

		final String buyerNick = rs.getString("customerno");
		final String shopId = rs.getString("dp_id");
		order.setCustomer(getCustomer(buyerNick, shopId));

		return order;
	}

	/**
	 * 根据店铺id和买家昵称获取买家的Customer对象.
	 * 
	 * @param buyerNick
	 * @param shopId
	 * @return
	 */
	public Customer getCustomer(final String buyerNick, final String shopId) throws RuleCenterRuntimeException {
		final Customer customer = new Customer();

		final User user = getTaobaoUserObject(buyerNick, shopId);
		customer.setBuyerGoodRatio(String.valueOf(user.getBuyerCredit().getLevel()));// 买家信用等级.淘宝返回的是数值型
		customer.setVipInfo(user.getVipInfo());// 全站等级

		final BasicMember member = getTaobaoMemberObject(buyerNick, shopId);
		if (member == null) {// 新顾客
			customer.setCustomerType(NEW_CUSTOMER);// 客人类型(回头客/新客)
			customer.setGrade(null);
			customer.setTradeCount(null);
			customer.setTradeAmount(null);
			customer.setLastTradeDateDiff(null);// 最后交易间隔(天)
		} else {
			final Long grade = member.getGrade();
			final Long tradeCount = member.getTradeCount();
			customer.setCustomerType(grade == 0 && tradeCount == 0 ? NEW_CUSTOMER : REGULARS);// 客人类型(回头客/新客)
			customer.setGrade(grade == 0 ? null : String.valueOf(grade));// 会员等级:不去看grade=0的问题。
			customer.setTradeCount(tradeCount);// 交易成功笔数
			customer.setTradeAmount(Double.valueOf(member.getTradeAmount()));// 交易成功的金额

			final Date lastTradeDate = member.getLastTradeTime();
			if (lastTradeDate != null) {
				customer.setLastTradeDateDiff(TimeUnit.MILLISECONDS.toDays(getNow().getTime() - lastTradeDate.getTime()));// 最后交易间隔(天)
			}
		}

		return customer;
	}

	public User getTaobaoUserObject(final String buyerNick, final String shopId) {
		// taobao.user.get
		// 获取单个用户信息:http://api.taobao.com/apidoc/api.htm?path=cid:1-apiId:1
		// User对象
		// http://api.taobao.com/apidoc/dataStruct.htm?path=cid:1-dataStructId:3-apiId:1-invokePath:user
		final UserGetRequest req = new UserGetRequest();
		req.setFields("buyer_credit,vip_info");
		req.setNick(buyerNick);
		final AccessToken accessToken = getSessionKey(shopId);
		final UserGetResponse response = invokerHandler.execute(req, accessToken.getAccessToken());// UserInfoTaobao.getUserInfo(req);
		if (response == null) {
			logger.warn("淘宝平台api返回出错:baseResponse=null");
			throw new RuleCenterRuntimeException("淘宝平台api返回出错");
		}
		if (!response.isSuccess()) {
			logger.warn("淘宝平台api返回出错:baseResponse not success."//
					+ " errCode:" + response.getSubCode()//
					+ ", errMsg:" + response.getSubMsg());
			throw new RuleCenterRuntimeException("淘宝平台api返回出错");
		}
		final User user = response.getUser();
		if (user == null) {
			logger.warn("淘宝平台api返回出错:user=null");
			throw new RuleCenterRuntimeException("淘宝平台api返回出错");
		}
		return user;
	}

	/**
	 * 根据店铺id和买家昵称获取买家的BasicMember对象(淘宝api的对象).
	 * 
	 * @param buyerNick
	 * @param shopId
	 * @return
	 * @throws ApiRuleException
	 * @throws ApiException
	 */
	protected BasicMember getTaobaoMemberObject(final String buyerNick, final String shopId) {
		// taobao.crm.members.get
		// 获取卖家的会员（基本查询）:http://api.taobao.com/apidoc/api.htm?spm=0.0.0.0.DW75Hf&path=scopeId:10529-apiId:10838
		// BasicMember
		// 对象:http://api.taobao.com/apidoc/dataStruct.htm?path=cid:10102-dataStructId:10400-apiId:10838-invokePath:members
		try {
			final CrmMembersGetRequest request = new CrmMembersGetRequest();
			request.setBuyerNick(buyerNick);
			request.setCurrentPage(1L);
			request.setPageSize(100L);
			final AccessToken accessToken = getSessionKey(shopId);
			final BaseResponse<CrmMembersGetResponse> baseResponse = MemberTaobao.getUserMemberGrade(request,
					accessToken.getAccessToken());
			if (baseResponse == null) {
				logger.warn("淘宝平台api返回出错:baseResponse=null");
				throw new RuleCenterRuntimeException("淘宝平台api返回出错");
			}
			if (!baseResponse.isSuccess()) {
				if ("isv.buyer-not-exist:buyer_nick".equalsIgnoreCase(baseResponse.getErrCode())) {
					// 当前的会员不存在或者不是当前卖家的会员.错误码参考方法第一行的链接.
					return null;
				}
				logger.warn("淘宝平台api返回出错:errCode:" + baseResponse.getErrCode()//
						+ ", errDesc:" + baseResponse.getErrDesc()//
						+ ", errMsg:" + baseResponse.getErrMsg());
				throw new RuleCenterRuntimeException("淘宝平台api返回出错");
			}
			final CrmMembersGetResponse response = baseResponse.getRtnData();
			if (response == null) {
				logger.warn("淘宝平台api返回出错:rtnData=null");
				throw new RuleCenterRuntimeException("淘宝平台api返回出错");
			}
			final List<BasicMember> members = response.getMembers();
			if (ListUtils.isEmpty(members)) {
				logger.warn("淘宝平台api返回出错:members empty");
				throw new RuleCenterRuntimeException("淘宝平台api返回出错");
			}
			final BasicMember member = members.get(0);
			if (member == null) {
				logger.warn("淘宝平台api返回出错:member=null");
				throw new RuleCenterRuntimeException("淘宝平台api返回出错");
			}
			return member;
		} catch (final ApiRuleException e) {
			logger.warn("淘宝平台api返回出错:code:" + e.getErrCode() + ",errMsg:" + e.getErrMsg());
			throw new RuleCenterRuntimeException("淘宝平台api返回出错", e);
		} catch (final ApiException e) {
			logger.warn("淘宝平台api返回出错:code:" + e.getErrCode() + ",errMsg:" + e.getErrMsg());
			throw new RuleCenterRuntimeException("淘宝平台api返回出错", e);
		}
	}

	/**
	 * @param shopId
	 * @return
	 */
	protected AccessToken getSessionKey(final String shopId) {
		return accessTokenService.getAccessToken(PlatEnum.taobao, shopId);
	}

	@Override
	public Order resolve(final Long tid) {
		try {
			return namedParameterJdbcTemplate.queryForObject(GET_ONE_SQL,//
					Collections.singletonMap("tid", tid), rowMapper);
		} catch (final DataAccessException e) {
			e.printStackTrace();// TODO:调试用
			logger.warn("没取到订单:" + tid, e);
			return null;
		}
	}

	@Override
	public boolean support(final Class<?> clazz) {
		return clazz == Order.class;
	}
}
