package com.bridgei2i.common.controller;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bridgei2i.common.constants.ApplicationConstants;
import com.bridgei2i.common.dao.PlanningDAO;
import com.bridgei2i.common.exception.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class AutomaticUploadScheduler {
	public static int isAutoUploadInProgress=0;
	@Autowired(required=true)
	private PlanningDAO planningDAO;

	@Scheduled(cron ="${schedulerExpression.cron}")
		public void automaticUpload(){
			try {
				if(isAutoUploadInProgress==0){
					isAutoUploadInProgress=1;
					System.out.println("Auto Upload Started");
					planningDAO.automaticUpload();
					System.out.println("Auto Upload Completed");
				}else{
					System.out.println("Auto Uplaod already in progress...");
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isAutoUploadInProgress=0;
		}
}
