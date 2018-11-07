package com.apap.tutorial8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apap.tutorial8.model.PasswordModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		userService.addUser(user);
		return "home";
	}

	@RequestMapping(value = "/updatePass")
	private String updatePass() {
		return "update-password";
	}

	@RequestMapping(value = "/updatePassSubmit", method = RequestMethod.POST)
	private String updatePassSubmit(@ModelAttribute PasswordModel pass, Model model) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserRoleModel user = userService
				.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String message = "";
		if (pass.getConfirmedPassword().equals(pass.getNewPassword())) {
			if (passwordEncoder.matches(pass.getOldPassword(), user.getPassword())) {
				message = "Update password success";
				userService.changePassword(user, pass.getNewPassword());
			} else {
				message = "Old password not match";
			}
		} else {
			message = "Confirmation password not match";
		}
		model.addAttribute("message", message);
		return "update-password";
	}

}
