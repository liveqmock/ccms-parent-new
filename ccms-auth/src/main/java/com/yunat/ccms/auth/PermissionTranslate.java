package com.yunat.ccms.auth;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

import com.yunat.ccms.core.support.auth.SupportOp;

/**
 * 在org.springframework.security.acls.domain.BasePermission
 * 和com.yunat.ccms.module.ModuleSupportOp之间和稀泥的类.
 * 
 * @author MaGiCalL
 */
public enum PermissionTranslate {
	READ(BasePermission.READ, SupportOp.VIEW), //
	WRITE(BasePermission.WRITE, SupportOp.UPDATE), //
	CREATE(BasePermission.CREATE, SupportOp.ADD), //
	DEL(BasePermission.DELETE, SupportOp.DEL), //
	CLICK(new BasePermission(findNextMask(SupportOp.CLICK.mask), SupportOp.CLICK.code) {
		private static final long serialVersionUID = -1199013629514316066L;
	}, SupportOp.CLICK), //
	;

	private static final Map<SupportOp, PermissionTranslate> SO_TO_PT_MAP = new EnumMap<SupportOp, PermissionTranslate>(
			SupportOp.class);
	static {
		for (final PermissionTranslate pt : values()) {
			SO_TO_PT_MAP.put(pt.supportOp, pt);
		}
	}

	public static PermissionTranslate valueOf(final SupportOp supportOp) {
		return SO_TO_PT_MAP.get(supportOp);
	}

	private final List<Permission> permissions;
	private final SupportOp supportOp;

	private PermissionTranslate(final Permission permission, final SupportOp supportOp) {
		permissions = Arrays.asList(permission);
		this.supportOp = supportOp;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public SupportOp getSupportOp() {
		return supportOp;
	}

	/**
	 * 为了避免mask数位与BasePermission的冲突,故写了这个方法.
	 * 它寻找BasePermission占用的最大数位,与给定的数位比较,如果给定的数位较大,说明不冲突,直接使用给定的数位;
	 * 如果BasePermission占用的最大数位较大或与给定的数位相同,说明冲突,使用BasePermission没有占用的下一个数位.
	 * 
	 * @param defaultMask
	 * @return
	 */
	private static int findNextMask(final int defaultMask) {
		final Class<?> clazz = BasePermission.class;
		final Field[] fields = clazz.getFields();
		int max = 0;
		for (final Field f : fields) {
			if (Permission.class.isAssignableFrom(f.getType()) && Modifier.isStatic(f.getModifiers())) {
				try {
					final Permission permission = (Permission) f.get(null);
					final int mask = permission.getMask();
					if (mask > max) {
						max = mask;
					}
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		if (max < defaultMask) {
			return defaultMask;
		}
		return max << 1;
	}

}