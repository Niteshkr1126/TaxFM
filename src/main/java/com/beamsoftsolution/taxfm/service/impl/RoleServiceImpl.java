package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Role;
import com.beamsoftsolution.taxfm.repository.RoleRepository;
import com.beamsoftsolution.taxfm.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}
	
	@Override
	public Role getRoleByRole(String role) throws TaxFMException {
		return roleRepository.findByRole(role).orElseThrow(() -> new TaxFMException("Role '" + role + "' not found"));
	}
}