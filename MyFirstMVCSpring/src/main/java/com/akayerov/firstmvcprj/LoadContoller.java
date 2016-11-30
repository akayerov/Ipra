package com.akayerov.firstmvcprj;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoadContoller {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView main(Locale locale, Model model) {
		return new ModelAndView("login","user", new User());
	}

	@RequestMapping(value = "/check_user")
	public ModelAndView checkUser(@ModelAttribute("user") User user) {
		return new ModelAndView("check_user", "user", user);
	}
}
