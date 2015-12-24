package com.bridgei2i.service;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.exception.ApplicationException;
import com.bridgei2i.vo.Contact;

@Service("controllerService")
public class ControllerService{

	@Autowired(required=true)
	private ApplicationDAO applicationDAO;
	
	public ControllerService() {
	}

	public void saveContact(Contact contact) throws ApplicationException {
		try {
			applicationDAO.saveContact(contact);
		} catch (ApplicationException e) {
			throw e;
		}
	}
}