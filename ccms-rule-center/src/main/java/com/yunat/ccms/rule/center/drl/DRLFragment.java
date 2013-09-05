package com.yunat.ccms.rule.center.drl;

import java.util.List;

public class DRLFragment {
	private String packageRegion;
	private List<String> importRegion;
	private List<RuleFragment> rules;

	public String getPackageRegion() {
		return packageRegion;
	}

	public void setPackageRegion(String packageRegion) {
		this.packageRegion = packageRegion;
	}

	public List<String> getImportRegion() {
		return importRegion;
	}

	public void setImportRegion(List<String> importRegion) {
		this.importRegion = importRegion;
	}

	public List<RuleFragment> getRules() {
		return rules;
	}

	public void setRules(List<RuleFragment> rules) {
		this.rules = rules;
	}
	
	public String toFileContent() {
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(packageRegion).append(";").append("\r\n");
		sb.append("\r\n");
		
		List<String> importRegion = getImportRegion();
		for (String string : importRegion) {
			sb.append("import ").append(string).append(";").append("\r\n");
		}
		sb.append("\r\n");
		
		List<RuleFragment> rfs = getRules();
		for (RuleFragment ruleFragment : rfs) {
			sb.append(ruleFragment.toStatement()).append("\r\n");
		}
		
		return sb.toString();
	}

}