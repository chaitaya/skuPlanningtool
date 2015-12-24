package com.bridgei2i.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.bridgei2i.service.ReportService;

@Controller
@Scope("session")
public class OverrideReportController {

	@Autowired(required=true)
    private ReportService reportService;
	
}
