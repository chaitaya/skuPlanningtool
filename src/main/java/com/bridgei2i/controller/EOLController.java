package com.bridgei2i.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.EOLBean;
import com.bridgei2i.service.ApplicationService;
import com.bridgei2i.service.PlanningService;

@Controller
@SessionAttributes
@Scope("session")
public class EOLController {

	private static Logger logger = Logger.getLogger(EOLController.class);

	@Autowired(required = true)
	private ApplicationService applicationService;

	@Autowired
	private PlanningService planningService;

	@RequestMapping(value = "/eolData.htm")
	public ModelAndView eolPage(
			@ModelAttribute("eolBeanBean") EOLBean eolBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered and leaving from homePage");
		Users users = (Users)ApplicationUtil.getObjectFromSession(ApplicationConstants.APPLICATION_ACT_AS_USER, request);
if(users!=null){
		Integer userId=users.getLogin_id();
		List rolesList = users.getRolesList();
		List SkuList = planningService.getAllSkulist(rolesList,userId);
		
		eolBean.setEolList(SkuList);
}
		ApplicationUtil.setObjectInSession(ApplicationConstants.ACTIVE_UTILITIES_TAB, ApplicationConstants.EOL_UTILITIES_TAB, request);
		return new ModelAndView("eol", "model", eolBean);
	}
	@RequestMapping(value = "/updateEolData.htm")
	public String updateEOL(
			@ModelAttribute("eolBean") EOLBean eolBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered and leaving into save-year-week");
		
		String selectedYear=eolBean.getEolYear();
		String selectedWeek=eolBean.getEolWeek();
		String id=eolBean.getC9();
		
		System.out.println(selectedYear+"selectedYear");
		
		try {
			planningService.eolUpdateData(Integer.valueOf(id),selectedWeek,selectedYear );
			ApplicationException ae = ApplicationException
					.createApplicationException("EOLController",
							"eolUpdateData",
							ApplicationErrorCodes.APP_EC_21,
							ApplicationConstants.INFORMATION, null);
		ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);	
		} catch (NumberFormatException e) {
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
			e.printStackTrace();
		} catch (ApplicationException e) {
			ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:" + "eolData.htm";
		
	}
	
	
	
}
