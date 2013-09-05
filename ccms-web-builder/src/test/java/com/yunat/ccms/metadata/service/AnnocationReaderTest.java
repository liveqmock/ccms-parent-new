package com.yunat.ccms.metadata.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.junit.Test;

import com.yunat.ccms.metadata.face.FaceCatalog;

public class AnnocationReaderTest {

	@Test
	public void readAnnocation() {

		String leftClassName = "com.yunat.ccms.metadata.pojo.DatabaseColumn";
		String rightClassName = "com.yunat.ccms.metadata.pojo.DatabaseTable";
		try {
			Class clazz_c = Class.forName(leftClassName);
			Class clazz_t = Class.forName(rightClassName);
			Annotation[] annocations = clazz_c.getDeclaredAnnotations();
			for (Annotation anno : annocations) {
				if ("Table".equals(anno.annotationType().getSimpleName())) {
					Table table = (Table) anno;
					System.out.println(table.name());
				}
			}
			for (Method method : clazz_c.getDeclaredMethods()) {
				if (method.getReturnType().equals(clazz_t)) {
					Annotation[] field_annos = method.getDeclaredAnnotations();
					for (Annotation field_anno : field_annos) {
						if ("JoinColumn".equals(field_anno.annotationType().getSimpleName())) {
							JoinColumn jc = (JoinColumn) field_anno;
							System.out.println(jc.name());
						}
					}
					for (Annotation anno : clazz_t.getDeclaredAnnotations()) {
						if ("Table".equals(anno.annotationType().getSimpleName())) {
							Table table = (Table) anno;
							System.out.println(table.name());
						}
					}

					Method method_id = getIdMethod(clazz_t);
					for (Annotation field_anno : method_id.getDeclaredAnnotations()) {
						if ("Column".equals(field_anno.annotationType().getSimpleName())) {
							Column jc = (Column) field_anno;
							System.out.println(jc.name());
						}
					}

				}
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Method getIdMethod(Class clazz) {

		for (Method method : clazz.getDeclaredMethods()) {
			for (Annotation anno : method.getDeclaredAnnotations()) {
				if ("Id".equals(anno.annotationType().getSimpleName())) {
					return method;
				}
			}
		}
		return null;
	}

	@Test
	public void readReverse() {

	}

	private void load(FaceCatalog catalog) {

	}
}
