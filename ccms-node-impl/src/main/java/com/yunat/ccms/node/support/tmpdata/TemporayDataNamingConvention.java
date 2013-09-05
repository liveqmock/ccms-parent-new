package com.yunat.ccms.node.support.tmpdata;

import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.annotation.ResultType;

/**
 * 临时产出(表或者视图)的名称约定
 * @author yinwei
 */
@Component
public class TemporayDataNamingConvention {

	public String getTemporaryDataName(Long subjobId, String suffix, ResultType resultType) {
		String suffixFull;
		if (suffix == null) {
			suffixFull = "_" + subjobId;
		} else {
			suffixFull = "_" + subjobId + "_" + suffix;
		}

		return TemporaryDataPrefix.getTemporaryDataName(resultType) + suffixFull;
	}

	public String getTemporaryViewName(Long subjobId) {
		return getTemporaryDataName(subjobId, null, ResultType.VIEW);
	}

	public String getTemporaryViewName(Long subjobId, String suffix) {
		return getTemporaryDataName(subjobId, suffix, ResultType.VIEW);
	}

}