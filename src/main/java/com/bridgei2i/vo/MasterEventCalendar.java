package com.bridgei2i.vo;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="master_event_calender")
public class MasterEventCalendar {
	@Id
    @Column(name="id")
    @GeneratedValue
    private Integer id;
       
    @Column(name="fiscalWeek")
    private String fiscalWeek;
    
      
    @Column(name="eventName")
    private String eventName;
    
    @Column(name="business")
    private String business;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFiscalWeek() {
		return fiscalWeek;
	}

	public void setFiscalWeek(String fiscalWeek) {
		this.fiscalWeek = fiscalWeek;
	}

	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}
	
	
}