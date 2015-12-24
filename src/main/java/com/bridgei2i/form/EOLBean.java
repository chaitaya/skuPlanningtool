package com.bridgei2i.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.bridgei2i.common.form.CombinationChartForm;
import com.bridgei2i.common.form.ListForm;
import com.bridgei2i.vo.PlanningCycleVO;

public class EOLBean extends ListForm  {

	private String c9;
	private String c11;
	private String c28;
	private String c6;
	private String c7;
	private String c19;
	private String eolYear;
	private String eolWeek;
	private String eolListId;
	private int planningCycleId;
	
	public String getEolListId() {
		return eolListId;
	}
	public void setEolListId(String eolListId) {
		this.eolListId = eolListId;
	}
	private List eolList = new ArrayList();
	
	public String getC9() {
		return c9;
	}
	public void setC9(String c9) {
		this.c9 = c9;
	}
	public String getC11() {
		return c11;
	}
	public void setC11(String c11) {
		this.c11 = c11;
	}
	public String getC28() {
		return c28;
	}
	public void setC28(String c28) {
		this.c28 = c28;
	}
	public String getC6() {
		return c6;
	}
	public void setC6(String c6) {
		this.c6 = c6;
	}
	public String getC7() {
		return c7;
	}
	public void setC7(String c7) {
		this.c7 = c7;
	}
	public String getC19() {
		return c19;
	}
	public void setC19(String c19) {
		this.c19 = c19;
	}
	public List getEolList() {
		return eolList;
	}
	public void setEolList(List eolList) {
		this.eolList = eolList;
	}
	public String getEolYear() {
		return eolYear;
	}
	public void setEolYear(String eolYear) {
		this.eolYear = eolYear;
	}
	public String getEolWeek() {
		return eolWeek;
	}
	public void setEolWeek(String eolWeek) {
		this.eolWeek = eolWeek;
	}
	public int getPlanningCycleId() {
		return planningCycleId;
	}
	public void setPlanningCycleId(int planningCycleId) {
		this.planningCycleId = planningCycleId;
	}
	
	

}
