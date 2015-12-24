package com.bridgei2i.common.util;

import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.exception.ApplicationException;

public class TeamReportTask implements Runnable{
	 
	String name;
	private ApplicationDAO applicationDAO;
	public TeamReportTask(String name,ApplicationDAO applicationDAO){
		this.name = name;
		this.applicationDAO =applicationDAO;
	}
 

	
	 	@Override
	  public void run() {
 
		try {
						
				Thread.sleep(10000);
			System.out.println(name+ "  is running");
			//applicationDAO.createTeamReport();
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
