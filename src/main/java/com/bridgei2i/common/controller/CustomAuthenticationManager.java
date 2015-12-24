package com.bridgei2i.common.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.bi2i.login.EncryptService;
import com.bridgei2i.common.dao.ApplicationDAO;
import com.bridgei2i.common.vo.Roles;
import com.bridgei2i.common.vo.Users;

public class CustomAuthenticationManager implements AuthenticationManager {

	protected static Logger logger = Logger.getLogger(CustomAuthenticationManager.class);

	@Autowired(required=true)
	private ApplicationDAO applicationDAO;

	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {

		logger.debug("Performing custom authentication");
		Users user = null;
		
		try {
			user = applicationDAO.getUserFromUserName(auth.getName());
			if(user==null){
				logger.error("User does not exists");
				throw new Exception("User does not exists");
			}else{
				/*boolean isEnabled =  user.getEnabled();
				if(!isEnabled){
					logger.error("User is In-Active!");
					throw new Exception("User is In-Active");
				}*/
			}
		} catch (Exception e) {
			logger.error("User does not exists!");
			throw new BadCredentialsException("User does not exists!");
		}
		String encryptedPassword =null;
		try {
			String password = (String)auth.getCredentials();
			EncryptService encryptService=EncryptService.getInstance();
			encryptedPassword = encryptService.encrypt(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error while encrypting password");
			e.printStackTrace();
		}
		if (!user.getPassword().equals(encryptedPassword)) {
			logger.error("Wrong password!");
			throw new BadCredentialsException("Wrong password!");
		}
		System.out.println("Authentication Success for User "+auth.getName());
		logger.debug("Authentication Success for User "+auth.getName());
		return new UsernamePasswordAuthenticationToken(
					auth.getName(), 
					auth.getCredentials(), 
					getAuthorities(user));
	}
	
	 public Collection<GrantedAuthority> getAuthorities(Users users) {
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
			List roles = users.getRoles();
			if(roles != null){
				int size = roles.size();
				for(int i=0;i<size;i++){
					Roles role = (Roles)roles.get(i);
					authList.add(new GrantedAuthorityImpl(role.getRole()));
				}
			}
			return authList;
	  }
}