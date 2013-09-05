package com.yunat.ccms.core.support.jooq;

import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;

import com.yunat.ccms.jooq.Ccms;

public class MySQLFactorySingleton extends org.jooq.util.mysql.MySQLFactory {
	/**
	 *
	 */
	private static final long serialVersionUID = 4726700156628850605L;

	private static MySQLFactorySingleton instance;

	private MySQLFactorySingleton() {
		super();
		initDefaultSchema();
	}

	/**
	 * Create a factory with a connection and some settings
	 * 
	 * @param settings
	 *            The settings to apply to objects created from this factory
	 */
	private MySQLFactorySingleton(org.jooq.conf.Settings settings) {
		super(settings);
		initDefaultSchema();
	}

	/**
	 * Initialise the render mapping's default schema.
	 * <p>
	 * For convenience, this schema-specific factory should override any
	 * pre-existing setting
	 */
	private final void initDefaultSchema() {
		SettingsTools.getRenderMapping(getSettings()).setDefaultSchema(Ccms.CCMS.getName());
	}

	public static MySQLFactorySingleton getInstance() {
		if (instance == null) {
			Settings settings = new Settings().withRenderSchema(false);
			instance = new MySQLFactorySingleton(settings);
		}
		return instance;
	}

}