
package com.bridgei2i.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bi2i.login.EncryptService;
import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.constants.ApplicationErrorCodes;
import com.bridgei2i.common.controller.LoadApplicationCacheService;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.common.util.ApplicationUtil;
import com.bridgei2i.common.vo.Roles;
import com.bridgei2i.common.vo.Users;
import com.bridgei2i.form.HomeBean;
import com.bridgei2i.service.ApplicationService;
import com.bridgei2i.service.PlanningService;
import com.bridgei2i.service.UtilitiesService;
import com.bridgei2i.vo.PlanningCycle;

@Controller
@SessionAttributes
@Scope("session")
public class HomeController {

	private static Logger logger = Logger.getLogger(HomeController.class);

	@Autowired(required = true)
	private ApplicationService applicationService;

	@Autowired
	private PlanningService planningService;
	
	@Autowired(required=true)
	private UtilitiesService utilitiesService;

	@RequestMapping(value = "/homePage.htm")
	public ModelAndView homePage(@ModelAttribute("homeBean") HomeBean homeBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered and leaving from homePage");
		Users users = (Users) ApplicationUtil.getObjectFromSession(
				ApplicationConstants.APPLICATION_LOGGED_IN_USER, request);
		if (users == null) {
			try {
				users = applicationService.getUserFromUserName(request
						.getRemoteUser());
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			List roles = users.getRoles();
			List rolesNamesList = new ArrayList();
			if (roles != null) {
				int size = roles.size();
				for (int i = 0; i < size; i++) {
					Roles roles2 = (Roles) roles.get(i);
					rolesNamesList.add(roles2.getRole());
				}
			}
			users.setRolesList(rolesNamesList);
			ApplicationUtil
					.setObjectInSession(
							ApplicationConstants.APPLICATION_LOGGED_IN_USER, users,
							request);
			ApplicationUtil
			.setObjectInSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, users,
					request);
		}
		
		List planningCycleList  = planningService
				.getAllPlanningCycleValue();
		PlanningCycle planningCycle = (PlanningCycle)planningCycleList.get(0);
		String startWeek = planningCycle.getStartWeek();
		int stWeek = Integer.parseInt(startWeek);
		String startYear = planningCycle.getStartYear();
		int stYear = Integer.parseInt(startYear);
		List<String> activeWeekList = new LinkedList<String>();
		for (int i = 0; i < 5; i++) {
			activeWeekList.add("W" + (++stWeek) + "-" + stYear);
		}
		startWeek = "W" + startWeek + "-" + stYear ;
		planningCycle.setStartWeek(startWeek);
		homeBean.setActiveWeekList(activeWeekList);
		homeBean.setPlanningCycleList(planningCycleList);
		PlanningCycle activePlanningCycle = (PlanningCycle)planningCycleList.get(1);
		startWeek = activePlanningCycle.getStartWeek();
        startYear = activePlanningCycle.getStartYear();
        startWeek = "W" + startWeek + "-" + stYear ;
        activePlanningCycle.setCycleStartDate(startWeek);
		homeBean.setLogicalName(activePlanningCycle.getLogicalName());
		ApplicationUtil.setObjectInSession(ApplicationConstants.ACTIVE_PLANNINGCYCLE_OBJ, activePlanningCycle, request);
		ApplicationUtil.setObjectInSession("headerFrom", "planning", request);
		return new ModelAndView("home", "model", homeBean);
	}

	@RequestMapping(value = "/privacy-policy.htm")
	public String privacyPolicy(
			@ModelAttribute("homeBean") HomeBean homeBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered and leaving from Privacy Policy");
		return "redirect:privacyPolicy.jsp";
	}

	@RequestMapping(value = "/save-active-week.htm")
	public String saveActiveWeek(
			@ModelAttribute("homeBean") HomeBean homeBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered and leaving into save-active-week");
		
		String selectedWeek=homeBean.getSelectedActiveWeek();
		
		System.out.println(selectedWeek+"selectedWeek");
		
		PlanningCycle planningCycle=new PlanningCycle();
		
		planningCycle.setStartWeek(getActiveWeekDate(selectedWeek )[0]);
		planningCycle.setStartYear(getActiveWeekDate(selectedWeek )[1]);
		planningCycle.setCreateDate(new Date());
		
		String status=planningService.getMasterPlanningStatusByStatusName("Upload");
		
		planningCycle.setStatusName(status);
		
		Integer id=(Integer)request.getSession().getAttribute("id");
		try {
		if(null!=id){
			planningService.updateSavedWeek(id,getActiveWeekDate(selectedWeek )[0],getActiveWeekDate(selectedWeek )[1]);
		
		}else{
			Integer savedId=planningService.savedWeek(planningCycle);
			request.getSession().setAttribute("id", savedId);
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("selectedWeek", selectedWeek);
		//return new ModelAndView("terms_Of_Use", "model", homeBean);
		
	}catch (ApplicationException e) {
		ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, e, request);					
		e.printStackTrace();
	}
		return "redirect:" + "homePage.htm";
}
	private String[] getActiveWeekDate(String selectedWeek ){
		String activeWeek=selectedWeek.substring(1);
		return activeWeek.split("-");
	}
	
	
	
	@RequestMapping(value = "/terms-of-use.htm")
	public String termsOfUse(
			@ModelAttribute("homeBean") HomeBean homeBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered and leaving from Terms Of Use");
		return "redirect:termsOfUse.jsp";
		//return new ModelAndView("terms_Of_Use", "model", homeBean);
	}
	
	@RequestMapping(value = "/actAsUser.htm")
	public String actAsUser(
			@ModelAttribute("homeBean") HomeBean homeBean,
			BindingResult result, HttpServletRequest request) {
		String selectedUserId = (String)request.getParameter("selectedUserId");
		Map usersMap = (Map)LoadApplicationCacheService.applicationCacheObject.get(ApplicationConstants.APPLICATION_USERS_MAP);
		if(usersMap != null){
			Users users  = (Users)usersMap.get(selectedUserId);
			ApplicationUtil
			.setObjectInSession(
					ApplicationConstants.APPLICATION_ACT_AS_USER, users,
					request);
			ApplicationException ae = ApplicationException
					.createApplicationException("HomeController",
							"actAsUser",
							ApplicationErrorCodes.APP_EC_3,
							ApplicationConstants.INFORMATION, null);
		ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);					
		}
		System.out.println(selectedUserId);
		return "redirect:" + "homePage.htm";
	}

	@RequestMapping(value = "/tabs.htm")
	public ModelAndView tabs(
			@ModelAttribute("homeBean") HomeBean homeBean,
			BindingResult result, HttpServletRequest request) {
		logger.debug("Entered and leaving from Terms Of Use");
		return new ModelAndView("tabs", "model", homeBean);
	}
		
	@RequestMapping(value = "/forgotPassword.htm")
	public String forgotPassword(@ModelAttribute("user") Users user,
			BindingResult result, HttpServletRequest request) {
		String email = user.getEmailId();
		String type = request.getParameter("button");
		System.out.println("BUTTON NAME:" + type);
		
		
		if (null!=type && type.equals("Reset")) {
			user = utilitiesService.getUserByEmail(email);
				if (user == null) {
					request.getSession().setAttribute("errorMessage", "User Does Not Exists");
				} else {
					try {
						utilitiesService.createPassword(user);
						request.getSession().setAttribute("Message",
								"Password Has Been Changed And Sent To Your Registered Mail Id");
						
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					request.getSession().setAttribute("errorMessage",
							"Password Has Been Changed And Sent To Your Registered Mail Id");
				}
			} else {
				request.getSession().setAttribute("errorMessage", "Enter a Valid Mail Id");

			}
		return "redirect:forgotPassword.jsp";
	}
	

	@RequestMapping(value = "/changePassword.htm")
	public String changePassword(
			@ModelAttribute("user") Users user,
			BindingResult result, HttpServletRequest request) {
		
		
		if (user.getNewPassword().equals(
				user.getConfirmNewPassword())) {
			String email = user.getEmailId();
			Users users = utilitiesService.getUserByEmail(email);
			if(users!=null){
					String enteredPassword=user.getPassword();
					EncryptService epts = EncryptService.getInstance();
					String existingPassword = epts.decrypt(users.getPassword()); 
					
					if (enteredPassword.equals(existingPassword)) {
						users.setPassword(user.getNewPassword());
						utilitiesService.updatePassword(users);
						request.getSession().setAttribute("Message",
								"Password Changed Successfully");
					} else {
						request.getSession().setAttribute("Message",
								"Wrong User Mail Id Or Password");
					}
			
				}else{
					request.getSession().setAttribute("Message",	"User Does Not Exist");
				}
			} else {
				request.getSession().setAttribute("Message",	"Password Does Not Match");
			}
	    	logger.debug("Entering and leaving from changePassword");
	    	return "redirect:changePassword.jsp";
	}
	@RequestMapping(value = "/refreshCacheData.htm")
	public String refreshCacheData(HttpServletRequest request) {
		try {
			planningService.loadCacheData();
			ApplicationException ae = ApplicationException
					.createApplicationException("HomeController",
							"refreshCacheData",
							ApplicationErrorCodes.APP_EC_24,
							ApplicationConstants.INFORMATION, null);
		ApplicationUtil.setObjectInSession(ApplicationConstants.APPLICATION_ERRORS_SESSION_KEY, ae, request);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return "redirect:" + "homePage.htm";
	}
}
