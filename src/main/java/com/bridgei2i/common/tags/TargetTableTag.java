package com.bridgei2i.common.tags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.util.PropertiesUtil;

public class TargetTableTag extends TagSupport {
	private  Map targetTableMap;
	private Integer tableColorRange;
	private String reportType;

	public Map getTargetTableMap() {
		return targetTableMap;
	}

	public void setTargetTableMap(Map targetTableMap) {
		this.targetTableMap = targetTableMap;
	}
	
	public Integer getTableColorRange() {
		return tableColorRange;
	}

	public void setTableColorRange(Integer tableColorRange) {
		this.tableColorRange = tableColorRange;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public int doStartTag() {
		try {
			
			JspWriter out = pageContext.getOut();
			String width="";
			 String numberOfActualsStr= PropertiesUtil.getProperty("numberOfActuals");
             int numberOfActuals=0;
             if(!ApplicationUtil.isEmptyOrNull(numberOfActualsStr)){
            	 numberOfActuals=Integer.parseInt(numberOfActualsStr);
             }
			String prefix="";
			String suffix="";
			String tempStr1="";
			String tempStr2="";
			Iterator iterator = targetTableMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String tableHeader=(String) entry.getKey();
				out.println("<div class=\"panel-heading\" ><b>"+entry.getKey()+"</b></div>");
				out.println("<div class=\"table-responsive\" >");
				List tableListObj = (List) entry.getValue();
				for(int i=0;i<tableListObj.size();i++){
					Map entryMap=	(Map) tableListObj.get(i);
					Iterator it = entryMap.entrySet().iterator();
					while (it.hasNext()){
						out.println("<table class=\"table table-striped table-bordered table-hover\">");
						out.println("<tbody>");
						Map.Entry mapEntry = (Map.Entry) it.next();
						List valueList=(List) mapEntry.getValue();
						List categoryValues=null;
						List targetValues=null;
						for(int j=0;j<valueList.size();j++){
							if(!ApplicationUtil.isEmptyOrNull(tableHeader) && (tableHeader.equalsIgnoreCase("ASP") || tableHeader.equalsIgnoreCase("ESC") || tableHeader.equalsIgnoreCase("Revenue")) ){
								prefix="$";
							}else {
								prefix="";
							}
							if(!ApplicationUtil.isEmptyOrNull(tableHeader) && tableHeader.equalsIgnoreCase("PM%")){
								suffix="%";
							}else{
								suffix="";
							}
							List value=(List) valueList.get(j);
							if(j==0){
								out.println("<tr>");
								for(int k=0;k<value.size();k++){
									if(k==0){
										width="120px";
									}else{
										width="";
									}
									if(!ApplicationUtil.isEmptyOrNull(reportType) && reportType.equalsIgnoreCase("planogram")){
										out.println("<td style=\"background-color: #8FD8D8;min-width:"+width+";\">"+value.get(k)+"</td>");
									}else{
										if(k<tableColorRange+1){
											out.println("<td style=\"min-width:"+width+";\">"+value.get(k)+"</td>");
										} else{
											out.println("<td style=\"background-color: #8FD8D8;min-width:"+width+";\">"+value.get(k)+"</td>");
										}
										
									}
									
									
								}
								out.println("</tr>");
								
							} else{
								out.println("<tr>");
								for(int k=0;k<value.size();k++){
									if(k==0){
										width="120px";
										tempStr1=prefix;
										tempStr2=suffix;
										prefix="";
										suffix="";
									}else{
										width="";
										prefix=tempStr1;
										suffix=tempStr2;
									}
									if(j==1){
										if(k==0){
											categoryValues=new ArrayList();
										}
										
										categoryValues.add(value.get(k));
									}
									if(j==2){
										if(k==0){
											targetValues=new ArrayList();
										}
										
										targetValues.add(value.get(k));
									}
									String tableCellColor="";
									if(j==3 && k!=0 && !ApplicationUtil.isEmptyOrNull(reportType) && reportType.equalsIgnoreCase("report")){
										if(!(targetValues.get(k)).equals("--") && !ApplicationUtil.isEmptyOrNull((String) categoryValues.get(k)) && !ApplicationUtil.isEmptyOrNull((String) targetValues.get(k))){
											double actualValue =Double.parseDouble((String) categoryValues.get(k));
		                                	double targetValue =Double.parseDouble((String) targetValues.get(k));
											if(actualValue<targetValue){
												tableCellColor="#FFE5E5";
												
											}else{
												tableCellColor="#E7FFBE";
											}
										}
										
									}
									String cellValue=(String) value.get(k);
									if(!ApplicationUtil.isEmptyOrNull(reportType) && (reportType.equalsIgnoreCase("planogram") || ApplicationUtil.isEmptyOrNull(cellValue)) || cellValue.equalsIgnoreCase("--")){
										prefix="";
										suffix="";
									}
									if(j==valueList.size()-1){
										prefix="";
										suffix="";
									}
									
									out.println("<td style=\"background-color:"+tableCellColor+";min-width:"+width+";\">"+prefix+value.get(k)+suffix+"</td>");
								}
								out.println("</tr>");
							}
						}
						out.println("</table>");
						out.println("</tbody>");
					}
				
				}
				out.println("</div>");
				out.println("<br/>");
			}
			/*for (Map.entrySet() entry : targetTableMap.entrySet())
			{
			    System.out.println(entry.getKey() + "/" + entry.getValue());
			}*/
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Must return SKIP_BODY because we are not supporting a body for this
		// tag.
		return SKIP_BODY;
	}

	/**
	 * doEndTag is called by the JSP container when the tag is closed
	 */
	public int doEndTag() {
		return (EVAL_PAGE);
	}

}
