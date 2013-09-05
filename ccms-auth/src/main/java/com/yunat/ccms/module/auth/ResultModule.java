package com.yunat.ccms.module.auth;

import java.util.ArrayList;
import java.util.Collection;

import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.module.Module;

/**
 * 专供返回给前端用的对象,封装了Module对象,屏蔽了一些不需要暴露的方法.
 * 
 * @author MaGiCalL
 */
public class ResultModule {

	private final Module module;
	private transient Collection<ResultModule> containings;

	public ResultModule(final Module module) {
		super();
		this.module = module;
	}

	public Long getId() {
		return module.getId();
	}

	public ResultModule getContainerModule() {
		return new ResultModule(module.getContainerModule());
	}

	public String getName() {
		return module.getName();
	}

	public String getTip() {
		return module.getTip();
	}

	public Collection<ResultModule> getContainingModules() {
		if (containings != null) {
			return containings;
		}
		final Collection<Module> containingModules = module.getContainingModules();
		final Collection<ResultModule> rt = new ArrayList<ResultModule>(containingModules.size());
		for (final Module m : containingModules) {
			rt.add(new ResultModule(m));
		}
		containings = rt;
		return rt;
	}

	public Collection<SupportOp> getSupportOps() {
		return module.getSupportOps();
	}

	public int getLayer() {
		return module.getLayer();
	}

	public String getUrl() {
		return module.getUrl();
	}

	public String getDataUrl() {
		return module.getDataUrl();
	}
}
