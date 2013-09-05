package com.yunat.ccms.report.support.template;

import java.io.PrintWriter;
import java.util.List;

public interface GeneratorFileCallback {
	public void writeFileHeader(List<String> columnHeaders, PrintWriter writer) throws Exception;
	public void writeFileContent(StringBuilder sql, PrintWriter writer) throws Exception;
}