package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Role;

import java.util.List;

public interface RoleService {
	
	List<Role> getAllRoles();
	Role getRoleByRole(String role_customer) throws TaxFMException;
}