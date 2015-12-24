package com.bridgei2i.common.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginLogoutController {
        
	protected static Logger logger = Logger.getLogger("controller");

	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public String getLoginPage(@RequestParam(value="error", required=false) boolean error, 
			ModelMap model) {
		if (error == true) {
			model.put("error", "You have entered an invalid username or password");
		}
		return "redirect:login.jsp";
	}
	
	@RequestMapping(value = "/denied.htm", method = RequestMethod.GET)
 	public String getDeniedPage() {
		return "redirect:denied.jsp";
	}
}