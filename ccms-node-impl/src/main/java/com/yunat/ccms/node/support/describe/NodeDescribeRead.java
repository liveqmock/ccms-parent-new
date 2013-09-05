package com.yunat.ccms.node.support.describe;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.yunat.ccms.configuration.describe.DescribeScan;
import com.yunat.ccms.core.support.annotation.Descriptor;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.workflow.validation.NodeConstraint;

public class NodeDescribeRead implements NodeDescribe {
	private static Logger logger = LoggerFactory.getLogger(NodeDescribeRead.class);

	private static final String RESOURCE_PATTERN = "/**/*.class";
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private Map<String, NodeConstraint> nodeConfigMap;
	private ApplicationContext applicationContext;

	public NodeDescribeRead() {
	}

	public NodeDescribeRead(Map<String, NodeConstraint> nodeConfigMap_, ApplicationContext applicationContext_) {
		this.nodeConfigMap = nodeConfigMap_;
		this.applicationContext = applicationContext_;
	}

	@Override
	public void load() {
		try {
			DescribeScan ds = (DescribeScan) applicationContext.getBean(DescribeScan.class);
			String basePackage = ds.getBasePackage();
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(basePackage) + RESOURCE_PATTERN;

			Resource[] resources = this.resourcePatternResolver.getResources(pattern);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader reader = readerFactory.getMetadataReader(resource);
					if (matchesFilter(reader, readerFactory)) {
						String className = reader.getClassMetadata().getClassName();
						logger.info("show loop className : [{}]", className);
						Class<?> clazz = this.resourcePatternResolver.getClassLoader().loadClass(className);
						handler(clazz);
					}
				}
			}
		} catch (IOException ex) {
			throw new CcmsBusinessException("Failed to scan classpath for unlisted classes", ex);
		} catch (ClassNotFoundException ex) {
			throw new CcmsBusinessException("Failed to scan classpath for unlisted classes", ex);
		}
	}

	private TypeFilter[] annotationTypeFilter = new TypeFilter[] { new AnnotationTypeFilter(Descriptor.class, false) };

	private boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
		if (this.annotationTypeFilter != null) {
			for (TypeFilter filter : annotationTypeFilter) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}

	private void handler(Class<?> clazz) {
		if (clazz != null && clazz.isAnnotationPresent(Descriptor.class)) {
			Descriptor describe = clazz.getAnnotation(Descriptor.class);
			parseDescribeNode(clazz, describe);
		}
	}

	@SuppressWarnings("rawtypes")
	private void parseDescribeNode(Class entityClass, Descriptor describe) {
		// second: deal with DescribeNode
		boolean hasCountLog = describe.hasCountLog();
		boolean cloneConfig = describe.cloneable();
		Boolean editableWhileJobExecuting = describe.editableWhileJobExecuting();
		Class validatorClass = describe.validatorClass();
		Class handlerClass = describe.handlerClass();
		Class processorClass = describe.processorClass();

		NodeConstraint nodeConfig = new NodeConstraint(describe.type());
		nodeConfig.setHasCountLog(hasCountLog);
		nodeConfig.setCloneable(cloneConfig);
		nodeConfig.setEditableWhileJobExecuting(editableWhileJobExecuting);

		nodeConfig.setHandlerClass(handlerClass);
		nodeConfig.setEntityClass(entityClass);
		nodeConfig.setValidatorClass(validatorClass);
		nodeConfig.setProcessorClass(processorClass);

		nodeConfigMap.put(describe.type(), nodeConfig);
	}
}