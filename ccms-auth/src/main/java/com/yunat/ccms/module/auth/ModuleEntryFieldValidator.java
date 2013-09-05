package com.yunat.ccms.module.auth;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.module.Module;

public interface ModuleEntryFieldValidator {
	enum ValidateResult {
		/**
		 * 精确包含。ModuleEntry中字段不为null，且与AuthContext精确相等。
		 */
		EXACT_CONTAINING(1), //
		/**
		 * 范围包含。ModuleEntry中字段取值是个范围/区间/集合，AuthContext中的值落在其中，所以接受
		 */
		RANGE_CONTAINING(2), //
		/**
		 * ModuleEntry中字段为null（表示不限制），所以接受AuthContext中的值
		 */
		ALL_CONTAINING(3), //
		/**
		 * 不包含。
		 */
		NOT_CONTAINING(4), //
		;
		public final int value;

		private ValidateResult(final int value) {
			this.value = value;
		}

		/**
		 * 检验本结果是否比另一结果的匹配度更高.
		 * 如相等也认为本结果更匹配.如果需要精确考虑,请注意调用方.
		 * 
		 * @param other
		 * @return
		 */
		public boolean isMatcherThan(final ValidateResult other) {
			return value < other.value;
		}
	}

	ValidateResult accpept(final AuthContext authContext, final Module module, final ModuleEntry moduleEntry);
}