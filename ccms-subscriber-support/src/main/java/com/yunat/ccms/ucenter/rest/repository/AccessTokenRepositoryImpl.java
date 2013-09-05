package com.yunat.ccms.ucenter.rest.repository;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.yunat.base.enums.app.AppEnum;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.external.ChannelParamBuilder;
import com.yunat.ccms.core.support.utils.UrlBuilder;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;
import com.yunat.ccms.ucenter.rest.vo.RESTResponse;

@Repository
public class AccessTokenRepositoryImpl implements AccessTokenRepository {

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenRepositoryImpl.class);

	// 测试地址是 "http://apptest.fenxibao.com/ucenter-restful-impl"
	private String ucenter_rest_service_url = null;

	private static final String FIND_ACCESS_TOKEN_URI = "/rest/services/access_token_service/action/find/invoke";
	private static final String ticket = "UxoIMYZYw11oQY6qD4qvCIYoV";

	@Override
	public AccessToken getAccessToken(PlatEnum plat, String shopId) {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		String app_id = AppEnum.CCMS.getAppId();
		String plat_code = plat.toString();
		String shop_id = ChannelParamBuilder.shopKey(plat, shopId);
		UrlBuilder builder = new UrlBuilder(ucenter_rest_service_url).append(FIND_ACCESS_TOKEN_URI)
				.addParam("ticket", ticket).addParam("app_id", app_id).addParam("plat_code", plat_code)
				.addParam("shop_id", shop_id);

		String URL_GET = builder.build();
		logger.info("尝试从订购中心获取AccessToken,URL:{}", URL_GET);
		Client client = Client.create(clientConfig);
		WebResource resource = client.resource(URL_GET);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			logger.error("从订购中心获取AccessToken失败:HTTP error code :{}", response.getStatus());
		}
		RESTResponse<AccessToken> restResponse = null;
		try {
			restResponse = response.getEntity(new GenericType<RESTResponse<AccessToken>>() {
			});
		} catch (ClientHandlerException e) {
			e.printStackTrace();
		} catch (UniformInterfaceException e) {
			e.printStackTrace();
		}
		if (restResponse == null || restResponse.getData() == null || restResponse.getData().size() == 0) {
			logger.error("从订购中心获取AccessToken失败");
			return null;
		}
		AccessToken token = restResponse.getData().get(0);
		logger.info("从订购中心获取AccessToken：{}", token);
		return token;
	}

	public void setUcenterServerPath(String ucenterServerPath) {
		Assert.isTrue(StringUtils.isNotBlank(ucenterServerPath));
		logger.info("订购中心地址设置为：{}", ucenterServerPath);
		this.ucenter_rest_service_url = ucenterServerPath;
	}

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@PostConstruct
	public void init() {
		String ucenterServerPath = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.UCENTER_SERVICE_REST_URL);
		this.setUcenterServerPath(ucenterServerPath);
	}
}
