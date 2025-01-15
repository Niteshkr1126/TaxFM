package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Authority;

import java.util.List;

public interface AuthorityService {
	
	Authority getAuthorityById(Integer id) throws TaxFMException;
	
	List<Authority> getAllAuthorities();
	
	Authority addAuthority(Authority authority);
	
	Authority updateAuthority(Authority authority) throws TaxFMException;
	
	void deleteAuthority(Authority authority);
	
	void deleteAuthorityById(Integer id);
}