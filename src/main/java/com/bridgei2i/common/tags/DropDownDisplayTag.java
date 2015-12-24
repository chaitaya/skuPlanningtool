package com.bridgei2i.common.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.DropDownDisplayVo;


/**
 * This is a implementation of the NewEditDeleteButtonTag 
 */
public class DropDownDisplayTag extends TagSupport {
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(DropDownDisplayTag.class.getName());
	private static final long serialVersionUID = 1L;
	
	private List list;
	private String selectedValue=null;
	private String defaultValue=null;
	private String id=null;
	private String path=null;
	private String script=null;
	private String disable=null;
    private String multipleValue=null;
    private String classAttribute=null;
    private String height="60px";
	

	public String getClassAttribute() {
		return classAttribute;
	}

	public void setClassAttribute(String classAttribute) {
		this.classAttribute = classAttribute;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	public String getDisable() {
		return disable;
	}

	public void setDisable(String disable) {
		this.disable = disable;
	}
	
	public String getMultipleValue() {
		return multipleValue;
	}

	public void setMultipleValue(String multipleValue) {
		this.multipleValue = multipleValue;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	/**
	* doStartTag is called by the JSP container when the tag is encountered
	*/
	public int doStartTag() {
		logger.info("Entering Inside doStartTag ");
		try {
			JspWriter out = pageContext.getOut();
			String javaScript= "";
			if(!ApplicationUtil.isEmptyOrNull(script)){
				javaScript = script;
			}
			
			String disabled="";
			if(!ApplicationUtil.isEmptyOrNull(disable)){
				disabled = "disabled=\""+disable+"\"";
			}
			String str = "";
			List selectedValuesList = new ArrayList();
			if(!ApplicationUtil.isEmptyOrNull(selectedValue)){
				String selectedValueAry[] = selectedValue.split(",");
				int len = selectedValueAry.length;
				for(int i=0;i<len;i++){
					String val = (String)selectedValueAry[i];
					selectedValuesList.add(val);
				}
			}
			String str1="";
			if(!ApplicationUtil.isEmptyOrNull(multipleValue) && multipleValue.equalsIgnoreCase("true")){
				str="multiple='multiple'";
				str1="style=\"height:"+height+"\"";
			}
			
            out.println("<select "+str1+" name=\""+path+"\" id=\""+id+"\" "+str+" class=\"fields "+classAttribute+"\" "+javaScript+" "+disabled+">");
            if(!ApplicationUtil.isEmptyOrNull(defaultValue)){
            	out.println("<option title=\""+defaultValue+"\" value=\"-1\">"+defaultValue+"</option>");
            }
            if(list != null){
            	int length = list.size();
            	for(int i=0;i<length;i++){
            		DropDownDisplayVo downDisplayVo = (DropDownDisplayVo)list.get(i);
            		String name = downDisplayVo.getDisplayName();
            		String value = downDisplayVo.getValue();
            		if(!ApplicationUtil.isEmptyOrNull(selectedValue)){
            			if(selectedValuesList.contains(value)){
            				out.println("<option title=\""+name+"\" value=\""+value+"\" selected=\"true\" >"+name+"</option>");
            			}else{
            				out.println("<option title=\""+name+"\" value=\""+value+"\" >"+name+" </option>");
            			}
            		}else{
            			out.println("<option title=\""+name+"\" value=\""+value+"\" >"+name+"</option>");
            		}
            	}
            }
			out.println("</select>");
			
			
			out.println("<br/><br/>");
		} catch (Exception ex) {
			logger.error("Exception Occured :"+'\n'+ex.getStackTrace());
            ex.printStackTrace();
		}
		// Must return SKIP_BODY because we are not supporting a body for this tag.
		logger.info("Exiting From doStartTag ");
		return SKIP_BODY;
	}

	/**
 	* doEndTag is called by the JSP container when the tag is closed
 	*/
	public int doEndTag(){
        return (EVAL_PAGE);
	}
}
