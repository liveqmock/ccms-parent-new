package com.yunat.ccms.report.support.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jooq.impl.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.core.support.jooq.MySQLFactorySingleton;
import com.yunat.ccms.report.support.cons.ReportFileSpec;
import com.yunat.ccms.report.support.template.GeneratorFileCallback;
import com.yunat.ccms.report.support.template.GeneratorFileTemplate;

@Component
@Scope(value = "prototype")
public class CopyAndWriteStorageData {
	private static Logger logger = LoggerFactory.getLogger(CopyAndWriteStorageData.class);
	private static final int NORMAL_LOOP = 5000;
	// private static final int RANDOM_PREVIEW = 100;

	@Autowired
	private JdbcPaginationHelper jdbcPaginationHelper;

	private Factory create = MySQLFactorySingleton.getInstance();

	public void generate(String filename, StringBuilder sql, List<String> columnHeaders) {
		// 检查文件扩展名类型
		checkFilenameExtension(filename, sql, columnHeaders);

		// 生成文件
		generateFile(filename, sql, columnHeaders);
	}

	private void checkFilenameExtension(String filename, StringBuilder sql, List<String> columnHeaders) {
		String filenameExtension = filename.substring(filename.lastIndexOf(".") + 1);
		ReportFileSpec.Extension[] fe = ReportFileSpec.Extension.values();
		List<String> feList = Lists.newArrayList();
		for (ReportFileSpec.Extension _filenameExtension : fe) {
			feList.add(_filenameExtension.toString().toLowerCase());
		}

		boolean isContains = CollectionUtils.contains(feList.iterator(), filenameExtension);
		if (!isContains) {
			throw new CcmsBusinessException("文件扩展名不在处理的范围之内, 支持的文件扩展名:" + Arrays.deepToString(fe));
		}
	}

	private void generateFile(String filename, StringBuilder sql, List<String> columnHeaders) {
		String filenameExtension = filename.substring(filename.lastIndexOf(".") + 1);
		if (ReportFileSpec.Extension.JSON.toString().equalsIgnoreCase(filenameExtension)) {
			generateJsonFile(filename, sql, columnHeaders);
		} else if (ReportFileSpec.Extension.CSV.toString().equalsIgnoreCase(filenameExtension)) {
			generateCsvFile(filename, sql, columnHeaders);
		}
	}

	private void generateJsonFile(final String filename, final StringBuilder sql, final List<String> columnHeaders) {
		try {
			GeneratorFileTemplate.run(filename, sql, columnHeaders, new GeneratorFileCallback() {

				@Override
				public void writeFileHeader(List<String> columnHeaders, PrintWriter writer) throws Exception {
					ObjectMapper mapper = new ObjectMapper();
					writer.println(mapper.writeValueAsString(columnHeaders));
				}

				@Override
				public void writeFileContent(StringBuilder sql, PrintWriter writer) throws Exception {
					// XXX
					String limlitSql = sql.append(" limit 100 offset 0 ").toString();
					logger.info("获取数据：[{}]", limlitSql);
					List<Map<String, Object>> results = jdbcPaginationHelper.getJdbcOperations()
							.queryForList(limlitSql);
					printJsonResults(results, writer);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void generateCsvFile(final String filename, final StringBuilder sql, final List<String> columnHeaders) {
		try {
			GeneratorFileTemplate.run(filename, sql, columnHeaders, new GeneratorFileCallback() {

				@Override
				public void writeFileHeader(List<String> columnHeaders, PrintWriter writer) throws Exception {
					writer.println(Joiner.on(",").join(columnHeaders.iterator()));
				}

				@Override
				public void writeFileContent(StringBuilder sql, PrintWriter writer) throws Exception {
					// 构建临时表名
					String tmpTblName = createTempTableName(filename);

					// 删除临时表tmpTblName
					logger.info("删除客户组下载临时表");
					dropTableAsSQL(tmpTblName);

					// 创建临时表
					logger.info("创建客户组下载需要的临时表");
					createTableAsSQL(tmpTblName, sql);

					String countSql = create.selectCount().from(tmpTblName).getSQL(true);
					int total = jdbcPaginationHelper.getJdbcOperations().queryForInt(countSql);
					int resultNum = 0;
					for (int i = 0; i <= total; i++) {
						String limlitSql = create.select().from(tmpTblName).limit(i * NORMAL_LOOP, NORMAL_LOOP)
								.getSQL(true);
						logger.info("获取数据：[{}]", limlitSql);
						List<Map<String, Object>> results = jdbcPaginationHelper.getJdbcOperations().queryForList(
								limlitSql);
						printCsvResults(results, writer);
						resultNum += results.size();
						if (resultNum == total) {
							break;
						}
					}

					// 删除临时表tmpTblName
					logger.info("删除客户组下载临时表");
					dropTableAsSQL(tmpTblName);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createTableAsSQL(String tmpTblName, StringBuilder sql) {
		jdbcPaginationHelper.getJdbcOperations().execute("create table " + tmpTblName + " as " + sql.toString());
	}

	/**
	 * @param filename
	 * @return
	 */
	private String createTempTableName(String filename) {
		String tmpTblName = filename.substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')),
				filename.lastIndexOf('.'));
		tmpTblName = "tmpDownLoad" + tmpTblName.substring(tmpTblName.indexOf('_'));
		return tmpTblName;
	}

	private void dropTableAsSQL(String tmpTblName) {
		jdbcPaginationHelper.getJdbcOperations().execute("DROP TABLE IF EXISTS " + tmpTblName);
	}

	public void compressToZip(String filename) {
		ZipOutputStream out = null;
		String zipFilename = filename.substring(0, filename.lastIndexOf(".")) + "."
				+ ReportFileSpec.Extension.ZIP.toString().toLowerCase();
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFilename));
			InputStream in = null;
			File file = new File(filename);
			out.putNextEntry(new ZipEntry(file.getName()));
			in = new FileInputStream(file);
			byte[] b = new byte[8192];
			int len = -1;
			while ((len = in.read(b)) > -1) {
				out.write(b, 0, len);
			}
			in.close();
			file.delete();
			out.closeEntry();
		} catch (Exception e) {
			logger.info("压缩文件异常 : [{}]", e.getMessage());
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void printCsvResults(List<Map<String, Object>> results, PrintWriter writer) {
		for (Map<String, Object> result : results) {
			StringBuilder resultsStringBuilder = new StringBuilder();
			Set<Entry<String, Object>> entrys = result.entrySet();
			for (Entry<String, Object> entry : entrys) {
				Object value = entry.getValue();
				resultsStringBuilder.append("\"");
				if (value != null) {
					resultsStringBuilder.append(value);
				}
				resultsStringBuilder.append("\",");
			}
			resultsStringBuilder.deleteCharAt(resultsStringBuilder.length() - 1);
			writer.println(resultsStringBuilder);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void printJsonResults(List<Map<String, Object>> results, PrintWriter writer) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			for (Map<String, Object> result : results) {
				Set<Entry<String, Object>> entrys = result.entrySet();
				List row = Lists.newArrayList();
				for (Entry<String, Object> entry : entrys) {
					row.add(entry.getValue());
				}
				writer.print(mapper.writeValueAsString(row));
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}