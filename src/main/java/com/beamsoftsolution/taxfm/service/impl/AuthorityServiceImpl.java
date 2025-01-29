package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.repository.AuthorityRepository;
import com.beamsoftsolution.taxfm.service.AuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	public List<Authority> getAllAuthorities() {
		return authorityRepository.findAll();
	}
	
	@Override
	public Authority getAuthorityById(Integer id) throws TaxFMException {
		Optional<Authority> optionalAuthority = authorityRepository.findById(id);
		if(optionalAuthority.isEmpty()) {
			throw new TaxFMException("authority.not.found").withErrorCode(404);
		}
		return optionalAuthority.get();
	}
	
	public Authority getAuthorityByAuthority(String authority) throws TaxFMException {
		return authorityRepository.findByAuthority(authority)
		                          .orElseThrow(() -> new TaxFMException("Authority '" + authority + "' not found"));
	}
	
	@Override
	public Authority addAuthority(Authority authority) {
		return authorityRepository.save(authority);
	}
	
	@Override
	public Authority updateAuthority(Authority authority) throws TaxFMException {
		Authority authorityInDB = getAuthorityById(authority.getAuthorityId());
		authorityInDB.setAuthority(authority.getAuthority());
		return authorityRepository.save(authorityInDB);
	}
	
	@Override
	public void deleteAuthority(Authority authority) {
		authorityRepository.delete(authority);
	}
	
	@Override
	public void deleteAuthorityById(Integer authorityId) {
		authorityRepository.deleteById(authorityId);
	}
}