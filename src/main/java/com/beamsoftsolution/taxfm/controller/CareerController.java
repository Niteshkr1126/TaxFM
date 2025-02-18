package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping
@Slf4j
public class CareerController {
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping("/career")
	public String careers(Model model) {
		taxFMUtils.setCompanyAttributes(model);
		return "landing/career";
	}
	
	@PostMapping("/submit-application")
	public String submitApplication(@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("position") String position,
			@RequestParam("resume") MultipartFile resume) {
		log.info("Received application from: " + name);
		return "redirect:/career?success";
	}
}