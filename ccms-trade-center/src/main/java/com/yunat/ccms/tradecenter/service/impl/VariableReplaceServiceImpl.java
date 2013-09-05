/**
 *
 */
package com.yunat.ccms.tradecenter.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.tradecenter.constant.ConstantTC;
import com.yunat.ccms.tradecenter.domain.DictDomain;
import com.yunat.ccms.tradecenter.repository.DictRepository;
import com.yunat.ccms.tradecenter.service.VariableReplaceService;
import com.yunat.ccms.tradecenter.util.BeanUtil;

/**
 *变量替换服务
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-7 下午07:01:51
 */
@Service("variableReplaceService")
public class VariableReplaceServiceImpl implements VariableReplaceService{

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DictRepository dictRepository;

	@Override
	public String replaceSmsContent(String template, List<Object> listObject) {

		if(listObject!=null){
			Map<String, Object> map = new HashMap<String, Object>();
			for(Object o : listObject){
				Map<String, Object> mapT = BeanUtil.beanToMap(o);
				map.putAll(mapT);
			}
			logger.info("短信内容要替换的MAP："+map);
			logger.info("短信模板："+template);
			if(map!=null&&map.size()>0&&StringUtils.isNotEmpty(template)){
				List<DictDomain> list = dictRepository.getByType(ConstantTC.VARIABLE_REPLACE);
				for(DictDomain domain : list){
					if(StringUtils.contains(template, domain.getCode())){
						if(map.get(domain.getName())!=null){
							if (map.get(domain.getName()) instanceof Date) {
								Date date = (Date)map.get(domain.getName());
								template = template.replace(domain.getCode(), formatTime(date));
							}else{
								template = template.replace(domain.getCode(), map.get(domain.getName()).toString());
							}
						}else{
							return "000";
						}
					}
				}
				return template;
			}else{
				return "000";
			}
		}else{
			return "000";
		}
	}

	private String formatTime(Date d){
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	    String time = format.format(d);
	    int month = Integer.parseInt(time.split("-")[1]);
	    int day = Integer.parseInt(time.split("-")[2]);
	    int hour = Integer.parseInt(time.split("-")[3]);
	    return month + "月" + day + "日" + hour + "时";
	}

}
