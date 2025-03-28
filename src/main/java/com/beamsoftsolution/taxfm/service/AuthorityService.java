package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Authority;

import java.util.List;

public interface AuthorityService {
	
	Authority getAuthorityById(Long id) throws TaxFMException;
	Authority getAuthorityByAuthority(String authority) throws TaxFMException;
	List<Authority> getAllAuthorities();
	Authority addAuthority(Authority authority);
	Authority updateAuthority(Authority authority) throws TaxFMException;
	void deleteAuthority(Authority authority);
	void deleteAuthorityById(Long id);
}