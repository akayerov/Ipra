package ru.javabegin.training;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private AccessDecisionManager accessDecisionManager;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
	public ModelAndView accesssDenied(Principal user) {

		ModelAndView model = new ModelAndView();

		// пока русский текст без локализации, хотя так не рекомендуется!
		if (user != null) {
			model.addObject("errorMsg", user.getName() + " у вас нет доступа к этой странице!");
		} else {
			model.addObject("errorMsg", "У вас нет доступа к этой странице!");
		}

		model.setViewName("/content/accessDenied");
		return model;

	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String mainPage() {
		printUserDetails();
		return "/content/user";
	}

	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String managerPage() {
		printUserDetails();
		return "/content/manager";
	}

	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage() {

		return "/content/admin";

	}

	private void printUserDetails() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		logger.info("password = " + userDetails.getPassword());
		logger.info("username = " + userDetails.getUsername());

		for (GrantedAuthority auth : userDetails.getAuthorities()) {
			logger.info(auth.getAuthority());
		}

	}

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username or password!");
		}

		model.setViewName("login");

		return model;

	}

}
