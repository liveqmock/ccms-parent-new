package com.yunat.ccms.core.support.ucenter;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.repository.AppPropertiesRepository;
import com.yunat.ccms.core.support.cons.ProductEdition;
import com.yunat.ccms.core.support.ucenter.cons.UCenterCons;
import com.yunat.ccms.core.support.utils.net.HttpClientUrlConnector;
import com.yunat.ccms.core.support.utils.net.UrlConnector;
import com.yunat.ccms.core.support.utils.net.UrlConnectorConfig;

/**
 * <pre>
 * 与订购中心有关的工具方法。
 * 订购中心的一些概念：
 * Subscriber：客户。即通常意义上的User/法人。此人在订购中心购买了一些产品。
 * ProductSet：产品集合。UCenter中叫Set，因Set含义丰富，故在这里改了个名字。
 * 	CCMS是订购中心众多产品中的若干个，每个版本是一个产品，所以CCMS的所谓版本号其实是ProductSet的id（订购中心的Set的setId）
 * SetSubscription：客户订购的产品。
 * Shop：客户的店铺。
 * Account：客户的账户。
 * </pre>
 * 
 * @author MaGiCalL
 */
@Component
public class ToUCenter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ToUCenter.class);
	@Autowired
	private AppPropertiesRepository appPropertiesRepository;

	private static final Map<String, ProductEdition> SET_ID_TO_EDITION = Maps.newHashMap();
	static {
		// set_id和程序内部版本号的对应关系
		// 数据赢家免费版 0-EX
		// 数据赢家基础版L1 0-B1
		// 数据赢家基础版L2 0-B2
		// 数据赢家基础版L3 0-B3
		// 数据赢家标准版 0-SD
		SET_ID_TO_EDITION.put("0-EX", ProductEdition.FREE);// 免费版
		SET_ID_TO_EDITION.put("0-B1", ProductEdition.BASIC_L1);// 基础版L1
		SET_ID_TO_EDITION.put("0-B2", ProductEdition.BASIC_L2);// 基础版L2
		SET_ID_TO_EDITION.put("0-B3", ProductEdition.BASIC_L3);// 基础版L3
		SET_ID_TO_EDITION.put("0-B4", ProductEdition.BASIC_L3);// 0-B4:小范围发布,当L3
		SET_ID_TO_EDITION.put("0-SD", ProductEdition.STANDARD);// 标准版
	}
	private static final UrlConnector URL_CONNECTOR = new HttpClientUrlConnector();

	/**
	 * 获取本用户购买的CCMS版本。
	 * 注：“用户”指的不是CCMS系统用户，而是“客户”，是订购中心的用户，可能是正在使用CCMS的系统用户所在公司或老板。
	 * 
	 * @param ccmsUserName
	 * @return
	 */
	public ProductEdition getProductEdition(final String ccmsUserName) {
		// 获取用户
		final Subscriber subscriber = getSubscriber(ccmsUserName);

		if (subscriber == null) {
			LOGGER.debug("从订购中心没有找到用户" + ccmsUserName);
			throw exception("没有找到用户" + ccmsUserName);
		}
		final Long subscriberId = subscriber.getSubscriberId();
		if (subscriberId == null) {
			// 基本不可能
			LOGGER.debug("从订购中心返回的数据有误。");
			throw exception("数据有误。");
		}
		// 获取用户已购买了的东西
		final List<SetSubscription> setSubscriptions = getSetSubscriptions(subscriberId);
		if (setSubscriptions == null || setSubscriptions.isEmpty()) {
			LOGGER.debug("该用户没有购买CCMS。");
			throw exception("您没有购买CCMS。");
		}
		// 获取CCMS的产品集合
		final List<ProductSet> productSets = getProductSets();
		for (final ProductSet productSet : productSets) {
			final String productSetId = productSet.getSetId();
			for (final SetSubscription setSubscription : setSubscriptions) {
				if (productSetId.equalsIgnoreCase(setSubscription.getSetId())) {
					// XXX:他有没有可能购买n个版本呢?
					final ProductEdition edition = SET_ID_TO_EDITION.get(productSetId);
					if (edition != null) {
						return edition;
					}
				}
			}
		}
		LOGGER.debug("该用户没有购买CCMS!");
		throw exception("您没有购买CCMS!");
	}

	private List<SetSubscription> getSetSubscriptions(final long subscriberId) {
		final UrlConnectorConfig config = new UrlConnectorConfig(//
				ucenterUrlPrefix() + UCenterCons.URL_SET_SUBSCRIPTION_SERVICE_PART + "&subscriber_id=" + subscriberId);
		final JsonContentHandler<SetSubscriptionResult> handler//
		= new JsonContentHandler<SetSubscriptionResult>(SetSubscriptionResult.class);

		return listDataFromUCenter(config, handler);
	}

	/**
	 * 获得一个CCMS用户在订购中心的第一个账户（在订购中心称为Subscriber）
	 * 
	 * @param ccmsUserName
	 * @return
	 */
	private Subscriber getSubscriber(final String ccmsUserName) {
		final List<Subscriber> subscribers = getSubscribers(ccmsUserName);
		if (subscribers == null || subscribers.isEmpty()) {
			return null;
		}
		return subscribers.get(0);
	}

	/**
	 * 获得一个CCMS用户在订购中心的账户（在订购中心称为Subscriber）
	 * 
	 * @param ccmsUserName
	 * @return
	 */
	private List<Subscriber> getSubscribers(final String ccmsUserName) {
		LOGGER.debug("正在获取用户在订购中心的账户:" + ccmsUserName);

		final UrlConnectorConfig config = new UrlConnectorConfig(//
				ucenterUrlPrefix() + UCenterCons.URL_SUBSCRIBER_SERVICE_PART + "&account_name=" + ccmsUserName);
		final JsonContentHandler<SubscriberResult> handler//
		= new JsonContentHandler<SubscriberResult>(SubscriberResult.class);

		return listDataFromUCenter(config, handler);
	}

	/**
	 * @return
	 */
	private String ucenterUrlPrefix() {
		final String ucenterUrlPrefix = appPropertiesRepository.retrieveConfiguration(//
				CCMSPropertiesEnum.UCENTER_SERVICE_REST_URL.getProp_group(),//
				CCMSPropertiesEnum.UCENTER_SERVICE_REST_URL.getProp_name());
		return ucenterUrlPrefix;
	}

	/**
	 * 获取CCMS在订购中心的所有ProductSet（在订购中心称为Set）。这是与用户无关的。
	 * 
	 * @return
	 */
	public List<ProductSet> getProductSets() {
		final UrlConnectorConfig config = new UrlConnectorConfig(ucenterUrlPrefix() + UCenterCons.URL_SET_SERVICE_PART);
		final JsonContentHandler<ProductSetResult> handler//
		= new JsonContentHandler<ProductSetResult>(ProductSetResult.class);

		return listDataFromUCenter(config, handler);
	}

	private <T> List<T> listDataFromUCenter(final UrlConnectorConfig urlConnectorConfig,
			final JsonContentHandler<? extends UCenterResult<T>> jsonContentHandler) {
		URL_CONNECTOR.downBytes(urlConnectorConfig, jsonContentHandler);

		final UCenterResult<T> result = jsonContentHandler.getData();

		if (!UCenterCons.STATUS_OK.equalsIgnoreCase(result.getStatus())) {
			LOGGER.warn("订购中心返回的状态不对:" + result.getStatus());
			throw exception(result.getMessage());
		}
		return result.getData();
	}

	private static RuntimeException exception(final String msg) {
		return new RuntimeException(msg);
	}
}
