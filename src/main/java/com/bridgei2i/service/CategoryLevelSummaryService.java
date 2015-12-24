package com.bridgei2i.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.dao.CategoryLevelSummaryDAO;
import com.bridgei2i.common.dao.PlanningDAO;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.form.SkuListForm;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.vo.MasterPlanningStatus;
import com.bridgei2i.vo.PlanningCycle;
import com.bridgei2i.vo.PlanningCycleVO;

@Service("categoryLevelSummaryService")
public class CategoryLevelSummaryService {
	
	private static Logger logger = Logger.getLogger(PlanningService.class);

	@Autowired(required=true)
	private CategoryLevelSummaryDAO categoryLevelSummaryDAO;

	   public Map getCategoryLevelSummary(final int planningCycleId,int statusId,HttpServletRequest request) throws ApplicationException{
			  return categoryLevelSummaryDAO.getCategoryLevelSummary(planningCycleId,statusId,request);
		   }
	   
	   public void releaseSkuToOverride(int userId,final String category,final int releaseFlag,final int planningCycleId)throws ApplicationException{
		   	 categoryLevelSummaryDAO.releaseSkuToOverride(userId,category,releaseFlag,planningCycleId);
	   } 
	   
	   public void commitCategory(final int userId,final String category,final int planningCycleId)throws ApplicationException{
		   	 categoryLevelSummaryDAO.commitCategory(userId,category,planningCycleId);
	   }
	   
	   public void changeWorkFlowStatus(int planningCycleId)throws ApplicationException{
		   	 categoryLevelSummaryDAO.changeWorkFlowStatus(planningCycleId);
	   }
	   
	   public String getWorkflowStatus(final int planningCycleId)
	   {
		   return categoryLevelSummaryDAO.getWorkflowStatus(planningCycleId);
	   }
	   
	   public void setOverrideFreeze(final int planningCycleId)
	   {
		   try {
			categoryLevelSummaryDAO.setOverrideFreeze(planningCycleId);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	   }
}