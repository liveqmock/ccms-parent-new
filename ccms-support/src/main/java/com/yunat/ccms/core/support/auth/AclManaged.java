package com.yunat.ccms.core.support.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 有时候,我们会有这样的情况:用UserForDB类的对象进行数据库交互,在service层变成使用User类,在web层又改成UserForWeb
 * 而springSecurity的acl组件的class表默认保存的是类的全名,在这种情况下,我们可能会对UserForDB创建acl,
 * 在返回UserForWeb的时候就无法取得acl了,因为UserForDB和UserForWeb在计算机里没有对应关系.
 * 为了避免这种情况,我们要给UserForDB和UserForWeb这些"在逻辑上指代同一事物"的类加上一个相同的标志,于是产生了本注解.
 * 将本注解加在这些相应的类上,注意value的值必须相等.
 * 以上面这个例子为例:
 * <code>
 * @AclClassId("user") public class UserForDB
 * @AclClassId("user") public class UserForWeb
 * </code>
 * 参考CcmsObjectIdentityRetrievalStrategy类的实现算法
 * 
 * 
 * 需要注意:暂时不支持id为字符串型的，并且不支持联合主键!!!!!!
 * 原因:
 * spring security的表acl_object_id中的字段object_id_identity的类型为bigint,即Long,用字符串做id的对象无法匹配上.
 * 后续可选改进:将字符串的id进行一种运算,变为一个long.这种运算必须是严格一一对应的.
 * 在spring security的javadoc中有这样一句:
 * “我们不打算在Spring Security ACL模块中支持非long identifier，
 * 因为long和所有的数据库sequence兼容，也是最为常用的identifier数据类型，同时有足够的长度支持通常的应用场景。”
 * </pre>
 * 
 * @author wenjian.liang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AclManaged {

	/**
	 * 在逻辑上的名字.比如user、role、permission、shop、order（订单）……
	 * 
	 * @return
	 */
	String value();

	String getIdMethodName() default "getId";
}
