package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import lombok.extern.slf4j.Slf4j;
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
	
	@GetMapping("/career")
	public String careers(Model model) {
		model.addAttribute("title", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
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
