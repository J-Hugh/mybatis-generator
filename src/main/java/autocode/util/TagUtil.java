package autocode.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import dataset.viewmodel.IAddField;
import dataset.viewmodel.IQueryField;
import dataset.viewmodel.ITag;
import dataset.viewmodel.ITagAttribute;
import dataset.viewmodel.IVMField;
import dataset.viewmodel.impl.AddField;
import dataset.viewmodel.impl.Tag;
import dataset.viewmodel.impl.TagAttribute;

/**
 * 页面组件模版工具类： 基于展现模型定义与页面组件VM模版生成页面组件的HTML和JavaScript字符串
 * 
 * @author BizFoundation Team: LiuJun
 * 
 *         BizFoudation基础平台页面组件的VM模版定义包括两个部分：HTML 和 JavaScript
 * 
 * @version 1.0
 * @since 4.2
 */
public class TagUtil {

	private static final String ATTR_VALUE = "value";
	private static final String FORM_ID = "formId";
	private static final String SEPARATOR = "separator";
	// 页面组件模版路径字典：根据页面组件type获得vm模版相对路径
	private static Map vmMap;

	/**
	 * 根据展现模型字段定义生成相应的（页面组件）HTML字符串
	 * 
	 * @param entityName
	 *            实体名称
	 * @param field
	 *            展现模型字段
	 * @return 展现模型字段对应页面组件的HTML字符串
	 */
	public static String buildHtml(String entityName, IVMField field) {
		return buildHtml(entityName, field, null);

	}

	public static String buildHtml(String entityName, IVMField field,
			Map resetAttrs) {
		return buildHtml(entityName, field, resetAttrs, true);
	}

	public static String buildHtml(String entityName, IVMField field,
			Map resetAttrs, boolean isGenerateValueAttr) {
		ITag tag = field.getTag();
		String path = getHtmlVmPath(tag.getTagType());
		boolean isValueDef = false;
		String separator = null;
		VelocityContext velocityContext = new VelocityContext();
		Iterator iterator = tag.getAttributes().iterator();
		while (iterator.hasNext()) {
			ITagAttribute attr = (ITagAttribute) iterator.next();
			if (resetAttrs != null && resetAttrs.containsKey(attr.getName())) {
				velocityContext.put(attr.getName(),
						resetAttrs.get(attr.getName()));
			} else {
				velocityContext.put(attr.getName(), attr.getAttrValue());
			}
			if (!isValueDef && attr.getName().toLowerCase().equals(ATTR_VALUE)) {
				isValueDef = true;
			}
			if (attr.getName().toLowerCase().equals(SEPARATOR)) {
				separator = attr.getAttrValue();
			}
		}
		// 生成value属性，使用EL表达式(biz:out)进行数据回填
		if (isGenerateValueAttr) {
			if (!isValueDef) {
				String attrSeparator = "";
				String fieldName = field.getFieldName();
				if (tag.getTagType().equalsIgnoreCase("checkbox")) {
					if (!StringUtil.isBlank(separator)) {
						attrSeparator = ",".equals(separator) ? ""
								: " separator='" + separator + "' ";
					}
				}
				velocityContext.put(ATTR_VALUE, "${" + entityName + "." + fieldName + "}" + attrSeparator);
			}
		}
		String html = buildString(path, velocityContext);
		return html.replaceAll("\r\n", "").replaceAll("\n", "").trim();
	}

	/**
	 * 根据展现模型字段定义生成相应的（页面组件）Script字符串
	 * 
	 * @param field
	 *            展现模型字段
	 * @return 展现模型字段对应页面组件的Script字符串
	 */
	public static String buildScript(String entityName, IVMField field,
			String formId) {
		return buildScript(entityName, field, formId, null);
	}

	public static String buildScript(String entityName, IVMField field,
			String formId, Map resetAttrs) {
		return buildScript(entityName, field, formId, resetAttrs, true);
	}

	public static String buildScript(String entityName, IVMField field,
			String formId, Map resetAttrs, boolean isGenerateValueAttr) {
		ITag tag = field.getTag();
		String path = getScriptVmPath(tag.getTagType());
		boolean isValueDef = false;
		String separator = null;
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put(FORM_ID, formId);
		Iterator iterator = tag.getAttributes().iterator();
		while (iterator.hasNext()) {
			ITagAttribute attr = (ITagAttribute) iterator.next();
			if (resetAttrs != null && resetAttrs.containsKey(attr.getName())) {
				velocityContext.put(attr.getName(),
						resetAttrs.get(attr.getName()));
			} else {
				velocityContext.put(attr.getName(), attr.getAttrValue());
			}
			if (!isValueDef && attr.getName().toLowerCase().equals(ATTR_VALUE)) {
				isValueDef = true;
			}
			if (attr.getName().toLowerCase().equals(SEPARATOR)) {
				separator = attr.getAttrValue();
			}
		}
		// 生成value属性，使用EL表达式(biz:out)进行数据回填
		if (isGenerateValueAttr) {
			if (!isValueDef) {
				String attrSeparator = "";
				String fieldName = field instanceof IQueryField ? ((IQueryField)field).getFieldNameInMultiValueCase() : field.getFieldName();
				if (tag.getTagType().equalsIgnoreCase("checkbox")) {
					if (!StringUtil.isBlank(separator)) {
						attrSeparator = ",".equals(separator) ? ""
								: " separator='" + separator + "' ";
					}
				}

				velocityContext.put(ATTR_VALUE, "<biz:out value='${"
						+ entityName + "." + fieldName + "}'" + attrSeparator
						+ "/>");
			}
		}

		return buildString(path, velocityContext);
	}

	/**
	 * 获取页面组件html部分的vm模版路径字典：根据页面组件type获得vm模版相对路径
	 * 
	 * @param tagType
	 *            页面组件类型
	 * @return vm模版路径
	 */
	public static String getHtmlVmPath(String tagType) {
		// return "template/WebApp/csi-ui/页面组件/component/" + tagType + "/" +
		// tagType + ".vm";
		return TagUtil.vmMap.get(tagType) + tagType + ".vm";
	}

	/**
	 * 获取页面组件JavaScript部分的vm模版路径
	 * 
	 * @param tag
	 *            页面组件
	 * @return vm模版路径
	 */
	public static String getScriptVmPath(String tagType) {
		// return "template/WebApp/csi-ui/页面组件/component/" + tagType + "/" +
		// tagType + "_script.vm";
		return TagUtil.vmMap.get(tagType) + tagType + "_script.vm";
	}

	/**
	 * 把velocityContext里面的值赋给vmFileName文件并得到对应的字符串
	 * 
	 * @param vmFileName
	 *            vm模版的完整路径
	 * @param velocityContext
	 *            vm模版Context内容
	 * @return
	 */
	private static String buildString(String vmFileName,
			VelocityContext velocityContext) {
		String[] dirPathAndFileNameArr = FileUtil
				.getDirPathAndFileName(vmFileName);
		return VmFileUtil.getVmString(dirPathAndFileNameArr[0],
				dirPathAndFileNameArr[1], velocityContext, null);
	}

	/**
	 * 设置页面组件VM模版的
	 * 
	 * @param vmMap
	 */
	public static void setVmMap(Map vmMap) {
		TagUtil.vmMap = vmMap;
	}

	public static void main(String[] args) {
		// 未定义value值
		IVMField field = getField(null);
		System.out.println(buildHtml("user", field));
		System.err.println(buildScript("user", field, ""));

		// 定义了Value值
		field = getField("20");
		System.out.println(buildHtml("user", field));
		System.err.println(buildScript("user", field, ""));
	}

	private static IVMField getField(String value) {
		IVMField field = new AddField();
		field.setFieldName("age");
		ITag tag = new Tag();
		tag.setTagType("text");
		field.setTag(tag);
		List attributes = new ArrayList();
		tag.setAttributes(attributes);
		TagAttribute attribute = new TagAttribute();
		attribute.setName("name");
		attribute.setAttrValue("age");
		attributes.add(attribute);

		attribute = new TagAttribute();
		attribute.setName("displayName");
		attribute.setAttrValue("年龄");
		attributes.add(attribute);

		attribute = new TagAttribute();
		attribute.setName("id");
		attribute.setAttrValue("age12");
		attributes.add(attribute);

		if (value != null) {
			attribute = new TagAttribute();
			attribute.setName("value");
			attribute.setAttrValue(value);
			attributes.add(attribute);
		}

		attribute = new TagAttribute();
		attribute.setName("maxlength");
		attribute.setAttrValue("12");
		attributes.add(attribute);

		attribute = new TagAttribute();
		attribute.setName("onclick");
		attribute.setAttrValue("onclick();");
		attributes.add(attribute);

		return field;
	}
}
