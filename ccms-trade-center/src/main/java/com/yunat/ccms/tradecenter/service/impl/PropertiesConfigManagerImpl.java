package com.yunat.ccms.tradecenter.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.PropertiesConfigDomain;
import com.yunat.ccms.tradecenter.repository.PropertiesConfigRepository;
import com.yunat.ccms.tradecenter.service.PropertiesConfigManager;
import com.yunat.ccms.tradecenter.support.bean.NameValueBean;

@Component("propertiesConfigManager")
public class PropertiesConfigManagerImpl implements PropertiesConfigManager{
	public static final String separator = ",";

	@Autowired
	private PropertiesConfigRepository propertiesConfigRepository;

	@Override
	public String[] getStringArray(String dpId, String name) {
		PropertiesConfigDomain propertiesConfigDomain = null;

		if (dpId != null) {
			propertiesConfigDomain = propertiesConfigRepository.getByDpIdAndName(dpId, name);
		} else {
			propertiesConfigDomain = propertiesConfigRepository.getByName(name);
		}

		if (propertiesConfigDomain == null) {
			return new String[0];
		}

		String[] strArray = new String[0];
		if (propertiesConfigDomain.getValue() != null) {
			strArray = propertiesConfigDomain.getValue().split(separator);
		}

		return strArray;
	}

	@Override
	public int[] geIntArray(String dpId, String name) {
		PropertiesConfigDomain propertiesConfigDomain = null;
		if (dpId != null) {
			propertiesConfigDomain = propertiesConfigRepository.getByDpIdAndName(dpId, name);
		} else {
			propertiesConfigDomain = propertiesConfigRepository.getByName(name);
		}

		if (propertiesConfigDomain == null) {
			return new int[0];
		}

		int[] intArray = new int[0];

		if (propertiesConfigDomain.getValue() != null) {

			String[] strArray = propertiesConfigDomain.getValue().split(separator);
			intArray = new int[strArray.length];
			for (int i = 0; i < strArray.length; i++) {
				intArray[i] = Integer.parseInt(strArray[i]);
			}
		}

		return intArray;
	}

	@Override
	public String getString(String dpId, String name) {

		PropertiesConfigDomain propertiesConfigDomain = null;
		if (dpId != null) {
			propertiesConfigDomain = propertiesConfigRepository.getByDpIdAndName(dpId, name);
		} else {
			propertiesConfigDomain = propertiesConfigRepository.getByName(name);
		}

		if (propertiesConfigDomain == null) {
			return null;
		}

		return propertiesConfigDomain.getValue();
	}

	@Override
	public Integer getInt(String dpId, String name) {

		PropertiesConfigDomain propertiesConfigDomain = null;
		if (dpId != null) {
			propertiesConfigDomain = propertiesConfigRepository.getByDpIdAndName(dpId, name);
		} else {
			propertiesConfigDomain = propertiesConfigRepository.getByName(name);
		}

		if (propertiesConfigDomain == null) {
			return null;
		}

		Integer value = null;
		if (propertiesConfigDomain.getValue() != null) {
			value = Integer.parseInt(propertiesConfigDomain.getValue());
		}
		return value;
	}

	@Override
	public void saveProperties(String dpId, String name, Object[] valueArray) {

		String value = "";
		for (int i = 0; i < valueArray.length; i++) {
			Object obj = valueArray[i];
			value += obj.toString();

			if (i < valueArray.length - 1) {
				value += ",";
			}
		}

		PropertiesConfigDomain properties = new PropertiesConfigDomain();
		properties.setCreated(new Date());
		properties.setUpdated(new Date());
		properties.setName(name);
		properties.setDpId(dpId);
		properties.setValue(value);
		saveOrUpdateProperties(properties);
	}

	@Override
	public void saveProperties(String dpId, String name, Object value) {
		PropertiesConfigDomain properties = new PropertiesConfigDomain();
		properties.setCreated(new Date());
		properties.setUpdated(new Date());
		properties.setName(name);
		properties.setDpId(dpId);
		properties.setValue(value.toString());
		saveOrUpdateProperties(properties);
	}

	@Override
	public void saveProperties(String dpId, String name, Object value, String groupName) {
		PropertiesConfigDomain properties = new PropertiesConfigDomain();
		properties.setCreated(new Date());
		properties.setUpdated(new Date());
		properties.setName(name);
		properties.setDpId(dpId);
		properties.setValue(value.toString());
		properties.setGroupName(groupName);
		saveOrUpdateProperties(properties);
	}

	//更新或保存
	private synchronized void saveOrUpdateProperties(PropertiesConfigDomain properties) {
		PropertiesConfigDomain srcProperties = propertiesConfigRepository.getByDpIdAndName(properties.getDpId(), properties.getName());
		if (srcProperties == null) {
			propertiesConfigRepository.save(properties);
		} else {
			srcProperties.setName(properties.getName());
			srcProperties.setValue(properties.getValue());

			if (properties.getGroupName() != null) {
				srcProperties.setGroupName(properties.getGroupName());
			}

			if (properties.getDescription() != null) {
				srcProperties.setDescription(properties.getDescription());
			}

			srcProperties.setUpdated(new Date());

			propertiesConfigRepository.save(srcProperties);
		}
	}

	@Override
	public List<NameValueBean> findNameValueByGroup(String dpId, String groupName) {
        List<PropertiesConfigDomain> propertieses = new ArrayList<PropertiesConfigDomain>();
        if (dpId != null) {
            propertieses = propertiesConfigRepository.findByDpIdAndGroupName(dpId, groupName);
        } else {
            propertieses = propertiesConfigRepository.findByGroupName(groupName);
        }

		List<NameValueBean> nameValues = new ArrayList<NameValueBean>();

		for (PropertiesConfigDomain properties : propertieses) {
			NameValueBean nameValue = new NameValueBean();
			nameValue.setName(properties.getName());
			nameValue.setValue(properties.getValue());
			nameValues.add(nameValue);
		}

		return nameValues;
	}

    @Override
    public Map<String, String> getNameValueMap(String dpId, String groupName) {
        Map<String, String> nameValueMap = new HashMap<String, String>();

        List<PropertiesConfigDomain> propertieses = new ArrayList<PropertiesConfigDomain>();
        if (dpId != null) {
            propertieses = propertiesConfigRepository.findByDpIdAndGroupName(dpId, groupName);
        } else {
            propertieses = propertiesConfigRepository.findByGroupName(groupName);
        }

        for (PropertiesConfigDomain properties : propertieses) {
            nameValueMap.put(properties.getName(), properties.getValue());
        }
        return nameValueMap;
    }

	@Override
	@Transactional
	public void deleteByName(String name) {
		propertiesConfigRepository.deleteByName(name);

	}

	@Override
	@Transactional
	public void deleteByGroupName(String groupName) {
		propertiesConfigRepository.deleteByGroupName(groupName);
	}

	@Override
	@Transactional
	public void batchReplace(String dpId, String[] names, String[] values, String groupName) {
		propertiesConfigRepository.deleteByGroupName(groupName);
		for (int i = 0; i < names.length; i++) {
			saveProperties(dpId, names[i], values[i], groupName);
		}
	}

}
