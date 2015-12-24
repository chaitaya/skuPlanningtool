package com.bridgei2i.common.util;

import java.lang.reflect.Field;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.vo.DropDownDisplayVo;
import com.bridgei2i.common.vo.PageTemplate;
import com.bridgei2i.form.WorkspaceBean;
import com.bridgei2i.vo.TemplateChart;
import com.bridgei2i.vo.TemplateChartCategory;
import com.bridgei2i.vo.TemplateChartInfo;
import com.bridgei2i.vo.VariableNames;

public class ApplicationUtil
{
	static Logger logger = Logger.getLogger(ApplicationUtil.class);
    public ApplicationUtil()
    {
    }
    public static String backlashReplace(String myStr)
    {
        StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(myStr);
        for(char character = iterator.current(); character != '\uFFFF'; character = iterator.next())
            if(character == '\\')
                result.append("/");
            else
                result.append(character);

        return result.toString();
    }
    
    public static final boolean isEmptyOrNull(Object obj){
		boolean isEmptyOrNull = true;
		if(obj != null){
			if(obj instanceof String){
				String str = (String)obj;
				if(!str.trim().equalsIgnoreCase("")){
					isEmptyOrNull = false;
				}
			}else{
				isEmptyOrNull = false;
			}
		}
		return isEmptyOrNull;
	}
    
    public static Field[] getAllFields(Class c) {
    	List fieldList = new ArrayList();
    	do {       
	    	Field[] fields = c.getDeclaredFields();
	    	fieldList.addAll(Arrays.asList(fields));
	    	c = c.getSuperclass();     
    	} while (c != Object.class);
    	Field[] flArray = new Field[fieldList.size()];
    	return (Field[])fieldList.toArray(flArray);
   } 
    
    public static String getChartXmlData(Collection detailValueObjs,String categoryField,boolean isFromCombinationChart){
		String xmlChartData = getMultiSeriesChartData(detailValueObjs,categoryField,isFromCombinationChart);
		return xmlChartData;
   }
   
   public static String getChartXmlData(Collection detailValueObjs,String categoryField){
	   	Object valueObj = ((ArrayList)detailValueObjs).get(0);
		Field[] fields = ApplicationUtil.getAllFields(valueObj.getClass());
		String xmlChartData="";
		if(fields != null && fields.length > 0) {
			if(fields.length>2){
				xmlChartData =  getMultiSeriesChartData(detailValueObjs,categoryField,false);
			}else{
				xmlChartData = getSingleSeriesChartData(detailValueObjs,categoryField);
			}
		}
		return xmlChartData;
   }
   
   private static String getSingleSeriesChartData(Collection detailValueObjs,String categoryField){
    	StringBuffer buffer = new StringBuffer();
    	int size = detailValueObjs.size();
    	ArrayList valueObjs = (ArrayList)detailValueObjs;
		for(int i=0;i<size;i++){
			Object valueObj = valueObjs.get(i);
			Field[] fields = ApplicationUtil.getAllFields(valueObj.getClass());
			if(fields != null){
				buffer.append("<set ");
				for(int j = 0; j < fields.length; j++) {
					if(fields[j] != null) {
						fields[j].setAccessible(true);
						String fieldName = fields[j].getName();
						Object obj=null;
						try {
							obj = fields[j].get(valueObj);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(fieldName.equals(categoryField)){
							buffer.append("label='"+obj+"' ");
						}else{
							buffer.append("value='"+obj+"' ");
						}
					}
				}
				buffer.append("/>");
			}
		}
    	return buffer.toString();
    }
    
    private static String getMultiSeriesChartData(Collection detailValueObjs,String categoryField,boolean isFromCombinationChart){
    	StringBuffer categoryBuffer = new StringBuffer();
    	StringBuffer dataSetbuffer = new StringBuffer();
    	int size = detailValueObjs.size();
    	ArrayList valueObjs = (ArrayList)detailValueObjs;
    	Object valueObj = valueObjs.get(0);
		Field[] fields = ApplicationUtil.getAllFields(valueObj.getClass());
		if(fields != null){
			if(!isFromCombinationChart){
				categoryBuffer.append("<categories>");
			}
			int length = fields.length;
			for(int i=0;i<length;i++){
				String fieldName = fields[i].getName();
				if(!fieldName.equals(categoryField)){
					if(isFromCombinationChart){
						dataSetbuffer.append("<dataset seriesName='"+fieldName.toUpperCase()+"' parentYAxis='S'>");
					}else{
						dataSetbuffer.append("<dataset seriesName='"+fieldName.toUpperCase()+"'>");
					}
				}
				for(int j=0;j<size;j++){
					valueObj = valueObjs.get(j);
					Object obj="";
					try {
						fields[i].setAccessible(true);
						obj = fields[i].get(valueObj);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(fieldName.equals(categoryField) && !isFromCombinationChart){
						categoryBuffer.append("<category name='"+obj+"' />");
					}else{
						dataSetbuffer.append("<set value='"+obj+"' />");
					}
				}
				if(!fieldName.equals(categoryField)){
					dataSetbuffer.append("</dataset>");
				}
			}
			if(!isFromCombinationChart){
				categoryBuffer.append("</categories>");
			}
			categoryBuffer.append(dataSetbuffer.toString());
		}
    	return categoryBuffer.toString();
    }
    
    public static void setObjectInSession(String key,Object obj,HttpServletRequest request){
    	HttpSession httpSession =  request.getSession();
    	httpSession.setAttribute(key, obj);
    }
    
    public static void removeObjectFromSession(String key,HttpServletRequest request){
    	HttpSession httpSession =  request.getSession();
    	httpSession.removeAttribute(key);
    }
    
    public static Object getObjectFromSession(String key,HttpServletRequest request){
    	HttpSession httpSession =  request.getSession();
    	return httpSession.getAttribute(key);
    }
    
    public static void sendEMail(String to, String subject, String body,final String userName,final String password,Properties props,String errorMsg) throws Exception 
    {
       logger.debug("Entered into sendEMail");
        Session session = Session.getInstance(props, new Authenticator() {
 
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(userName, password);
            }
        });
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        }
        catch(MessagingException e)
        {
              logger.error(e.getMessage());
              throw e;
        }
    }
    
    private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public static String getPublishRandomKey()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 5; i++)
        {
            int ndx = (int)(Math.random() * (double)ALPHA_NUM.length());
            sb.append(ALPHA_NUM.charAt(ndx));
        }
 
        return sb.toString();
    }
    
    public static void loadPageTemplate(List pageTemplateObjList){
    	List pageTemplatList = new ArrayList();
    	if(pageTemplateObjList != null){
			int len = pageTemplateObjList.size();
			for(int i=0;i<len;i++){
				PageTemplate pageTemplate= (PageTemplate)pageTemplateObjList.get(i);
				String pageTitle = pageTemplate.getPageTitle();
				Integer id = pageTemplate.getId();
				DropDownDisplayVo displayVo = new DropDownDisplayVo();
				displayVo.setDisplayName(pageTitle);
				displayVo.setValue(id+"");
				pageTemplatList.add(displayVo);
			}
			LoadApplicationCacheService.applicationCacheObject.put(ApplicationConstants.ALL_PAGE_TEMPLATES_CACHE_KEY, pageTemplatList);
    		
    		
    		
    	}
    	
    }

    public static void loadVariableNamesCache(List variableNameObjList){
    	List variableNamesList = new ArrayList();
		if(variableNameObjList != null){
			Map variableNameMapCache = new HashMap();
			Map notesMapObj= new HashMap();
			Map topLevelsLimitMapObj= new HashMap();
			Map columnNameMapCache = new HashMap();
			int len = variableNameObjList.size();
			for(int i=0;i<len;i++){
				VariableNames variableNames= (VariableNames)variableNameObjList.get(i);
				String name = variableNames.getVariableName();
				String id = variableNames.getColumnName();
				DropDownDisplayVo displayVo = new DropDownDisplayVo();
				displayVo.setDisplayName(name);
				displayVo.setValue(id+"");
				variableNamesList.add(displayVo);
				variableNameMapCache.put(id.trim().toLowerCase(), variableNames.getVariableName());
				columnNameMapCache.put(variableNames.getVariableName(), id);
			}
			LoadApplicationCacheService.applicationCacheObject.put(ApplicationConstants.ALL_VARIABLE_NAMES_CACHE_KEY, variableNamesList);
			LoadApplicationCacheService.applicationCacheObject.put(ApplicationConstants.VARIABLE_NAMES_MAP_CACHE_KEY , variableNameMapCache);
			LoadApplicationCacheService.applicationCacheObject.put(ApplicationConstants.COLUMN_NAMES_MAP_CACHE_KEY , columnNameMapCache);
		}
    }
    public static void loadCodeBookCache(List codeBookData )
    {
    	Map codeBookColumnObj = new HashMap();
		if(codeBookData != null && codeBookData.size()>0){
			int size= codeBookData.size();
			String tempColumnName="";
			Map codeObj =null;
			Map codeDisplayObj =null;
			for(int i=0;i<size;i++){
				Object row[] = (Object[])codeBookData.get(i);
				if(row != null){
					String columnName = (String)row[0];
					String actualValueStr = (String)row[1];
					String recodeValueStr = (String)row[2];
					String displayValueStr = (String)row[3];
					if(ApplicationUtil.isEmptyOrNull(tempColumnName) || !tempColumnName.equalsIgnoreCase(columnName)){
						tempColumnName=columnName;
						codeObj=  new HashMap();
						codeDisplayObj = new HashMap();
						codeBookColumnObj.put(columnName, codeObj);
						codeBookColumnObj.put(columnName+"display", codeDisplayObj);
					}
					codeObj.put(actualValueStr, recodeValueStr);
					codeDisplayObj.put(actualValueStr, displayValueStr);
				}
			}
		}
		LoadApplicationCacheService.applicationCacheObject.put(ApplicationConstants.CODEBOOK_ACTUAL_RECODE_VALUES_CACHE_KEY, codeBookColumnObj);
    }
    
    public static void loadTemplateChartTypes(TemplateChart templateChart)
    {
    	List templateChartCategories =  templateChart.getTemplateChartCategories();
		if(templateChartCategories != null){
			int size = templateChartCategories.size();
			for(int i=0;i<size;i++){
				TemplateChartCategory templateChartCategory =  (TemplateChartCategory)templateChartCategories.get(i);
				List templateChartInfoList = templateChartCategory.getTemplateChartInfoList();
				if(templateChartInfoList != null){
					int size1 = templateChartInfoList.size();
					for(int j=0;j<size1;j++){
						TemplateChartInfo templateChartInfo = (TemplateChartInfo)templateChartInfoList.get(j);
						String xAxis = templateChartInfo.getxAxis();
						String yAxis = templateChartInfo.getyAxis();
						String benchMark = templateChartInfo.getBenchMark();
						Integer functionId= templateChartInfo.getFunctionId();
						List chartTypes = new ArrayList();
						if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
							if(!ApplicationUtil.isEmptyOrNull(benchMark) && !benchMark.equals("-1")){
								DropDownDisplayVo displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Column Line Chart");
								displayVo.setValue("MSCombi2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Dual Y Line Chart");
								displayVo.setValue("MSCombiDY2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Table View");
								displayVo.setValue("TABLE_VIEW");
								chartTypes.add(displayVo);									
							}else if(functionId != null && functionId==5){
								DropDownDisplayVo displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Bar Chart");
								displayVo.setValue("Bar2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Wordcloud");
								displayVo.setValue("Wordcloud");
								chartTypes.add(displayVo);
								
							}
							else{
								DropDownDisplayVo displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Stacked Bar Chart");
								displayVo.setValue("StackedBar2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Stacked Column Chart");
								displayVo.setValue("StackedColumn2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Clustered Bar Chart");
								displayVo.setValue("MSBar2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Clustered Column Chart");
								displayVo.setValue("MSColumn2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("100percent Stacked Bar Chart");
								displayVo.setValue("HUNDRED_PERCENT_STACKED_BAR");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("100percent Stacked Column Chart");
								displayVo.setValue("HUNDRED_PERCENT_STACKED_COLUMN");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("MultiSeries Line Chart");
								displayVo.setValue("MSLine");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Table View");
								displayVo.setValue("TABLE_VIEW");
								chartTypes.add(displayVo);				
								
							}
						}else{
							if(functionId != null && functionId==1){
								if(xAxis != null && xAxis.contains(",")){
									DropDownDisplayVo displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Stacked Bar Chart");
									displayVo.setValue("StackedBar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Stacked Column Chart");
									displayVo.setValue("StackedColumn2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Clustered Bar Chart");
									displayVo.setValue("MSBar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Clustered Column Chart");
									displayVo.setValue("MSColumn2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("100percent Stacked Bar Chart");
									displayVo.setValue("HUNDRED_PERCENT_STACKED_BAR");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("100percent Stacked Column Chart");
									displayVo.setValue("HUNDRED_PERCENT_STACKED_COLUMN");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("MultiSeries Line Chart");
									displayVo.setValue("MSLine");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Table View");
									displayVo.setValue("TABLE_VIEW");
									chartTypes.add(displayVo);									
									
									
								}else{
									DropDownDisplayVo displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Bar Chart");
									displayVo.setValue("Bar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Column Chart");
									displayVo.setValue("Column2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Pie Chart");
									displayVo.setValue("Pie2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Line Chart");
									displayVo.setValue("Line");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Table View");
									displayVo.setValue("TABLE_VIEW");
									chartTypes.add(displayVo);							
									
									
								}
							} else if(functionId != null && functionId==5){
								DropDownDisplayVo displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Bar Chart");
								displayVo.setValue("Bar2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Wordcloud");
								displayVo.setValue("Wordcloud");
								chartTypes.add(displayVo);
								
							}
							else{
								if(!ApplicationUtil.isEmptyOrNull(benchMark) && !benchMark.equals("-1")){
									if(xAxis != null && xAxis.contains(",")){
										DropDownDisplayVo displayVo = new DropDownDisplayVo();
										displayVo.setDisplayName("Column Line Chart");
										displayVo.setValue("MSCombi2D");
										chartTypes.add(displayVo);
										
										displayVo = new DropDownDisplayVo();
										displayVo.setDisplayName("Dual Y Line Chart");
										displayVo.setValue("MSCombiDY2D");
										chartTypes.add(displayVo);
										
										displayVo = new DropDownDisplayVo();
										displayVo.setDisplayName("Table View");
										displayVo.setValue("TABLE_VIEW");
										chartTypes.add(displayVo);
									} else{
										DropDownDisplayVo displayVo = new DropDownDisplayVo();
										displayVo.setDisplayName("Column Line Chart");
										displayVo.setValue("Column2D");
										chartTypes.add(displayVo);
										
										displayVo = new DropDownDisplayVo();
										displayVo.setDisplayName("Table View");
										displayVo.setValue("TABLE_VIEW");
										chartTypes.add(displayVo);
									}
									
								}else{
									DropDownDisplayVo displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Bar Chart");
									displayVo.setValue("Bar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Column Chart");
									displayVo.setValue("Column2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Pie Chart");
									displayVo.setValue("Pie2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Line Chart");
									displayVo.setValue("Line");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Table View");
									displayVo.setValue("TABLE_VIEW");
									chartTypes.add(displayVo);
									
								}
							}
						}
						templateChartInfo.setChartTypesList(chartTypes);
					}
				}
			}
		}
    }
    
    public static void loadTemplateChartTypes_RunReports(WorkspaceBean workspaceBean)
    {
						String xAxis = workspaceBean.getxAxis();
						String yAxis = workspaceBean.getyAxis();
						String benchMark = workspaceBean.getBenchMark();
						Integer functionId= workspaceBean.getFunctionId();
						List chartTypes = new ArrayList();
						if(!ApplicationUtil.isEmptyOrNull(yAxis) && !yAxis.equals("-1")){
							if(!ApplicationUtil.isEmptyOrNull(benchMark) && !benchMark.equals("-1")){
								DropDownDisplayVo displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Column Line Chart");
								displayVo.setValue("MSCombi2D");
								chartTypes.add(displayVo);
							}else{
								DropDownDisplayVo displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Stacked Bar Chart");
								displayVo.setValue("StackedBar2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Stacked Column Chart");
								displayVo.setValue("StackedColumn2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Clustered Bar Chart");
								displayVo.setValue("MSBar2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("Clustered Column Chart");
								displayVo.setValue("MSColumn2D");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("100percent Stacked Bar Chart");
								displayVo.setValue("HUNDRED_PERCENT_STACKED_BAR");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("100percent Stacked Column Chart");
								displayVo.setValue("HUNDRED_PERCENT_STACKED_COLUMN");
								chartTypes.add(displayVo);
								
								displayVo = new DropDownDisplayVo();
								displayVo.setDisplayName("MultiSeries Line Chart");
								displayVo.setValue("MSLine");
								chartTypes.add(displayVo);
							}
						}else{
							if(functionId != null && functionId==1){
								if(xAxis != null && xAxis.contains(",")){
									DropDownDisplayVo displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Stacked Bar Chart");
									displayVo.setValue("StackedBar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Stacked Column Chart");
									displayVo.setValue("StackedColumn2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Clustered Bar Chart");
									displayVo.setValue("MSBar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Clustered Column Chart");
									displayVo.setValue("MSColumn2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("100percent Stacked Bar Chart");
									displayVo.setValue("HUNDRED_PERCENT_STACKED_BAR");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("100percent Stacked Column Chart");
									displayVo.setValue("HUNDRED_PERCENT_STACKED_COLUMN");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("MultiSeries Line Chart");
									displayVo.setValue("MSLine");
									chartTypes.add(displayVo);
								}else{
									DropDownDisplayVo displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Bar Chart");
									displayVo.setValue("Bar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Column Chart");
									displayVo.setValue("Column2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Pie Chart");
									displayVo.setValue("Pie2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Line Chart");
									displayVo.setValue("Line");
									chartTypes.add(displayVo);
								}
							}else{
								if(!ApplicationUtil.isEmptyOrNull(benchMark) && !benchMark.equals("-1")){
									DropDownDisplayVo displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Column Line Chart");
									displayVo.setValue("MSCombi2D");
									chartTypes.add(displayVo);
								}else{
									DropDownDisplayVo displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Bar Chart");
									displayVo.setValue("Bar2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Column Chart");
									displayVo.setValue("Column2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Pie Chart");
									displayVo.setValue("Pie2D");
									chartTypes.add(displayVo);
									
									displayVo = new DropDownDisplayVo();
									displayVo.setDisplayName("Line Chart");
									displayVo.setValue("Line");
									chartTypes.add(displayVo);
								}
							}
						}
						workspaceBean.setChartTypesList(chartTypes);
			}
    public static String getUniqueKey(int keyLength) {
		final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer(keyLength);
		for (int i = 0; i < keyLength; i++) {
			int ndx = (int) (Math.random() * ALPHA_NUM.length());
			sb.append(ALPHA_NUM.charAt(ndx));
		}

		return sb.toString();
	}
    
}