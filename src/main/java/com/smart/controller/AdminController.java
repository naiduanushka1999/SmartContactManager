package com.smart.controller;


import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.AdminRepository;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Admin;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);

		// get the user using usernamne(Email)

		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER " + user);
		model.addAttribute("user", user);

	}

	
	@RequestMapping("/dashboard")
	public String adminLogin(Model model,@ModelAttribute Admin admin) {
		model.addAttribute("title", "Admin - Smart Contact Manager");	
		//Pageable pageable = PageRequest.of(page, 8);
		//Page<User> listOfUsers =this.userRepository.findUserByRole("ROLE_USER",pageable);
		//model.addAttribute("listOfUsers", listOfUsers);
		//model.addAttribute("currentPage", page);
		//model.addAttribute("totalPages", listOfUsers.getTotalPages());
		admin.setUsername("admin@gmail.com");
		admin.setPassword("Admin@1234");
		adminRepository.save(admin);
		return "admin/admin_dashboard";
	}
	
	@GetMapping("/show-users/{page}")
	public String showUsers( @PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show Registered Users");
		// contact ki list ko bhejni hai

		String userName = principal.getName();

		User user = this.userRepository.getUserByUserName(userName);
		
		List<User> listOfUsers=this.userRepository.findUserByRole("ROLE_USER");
		m.addAttribute("listOfUsers", listOfUsers);

		// currentPage-page
		// Contact Per page - 5
		Pageable pageable = PageRequest.of(page, 8);

		Page<User> users = this.userRepository.findUserByRole(user.getRole(), pageable);

		//m.addAttribute("users", users);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", users.getTotalPages());

		return "admin/show_users";
	}
	
	@GetMapping("/delete/{cid}")
	@Transactional
	public String deleteUser(@PathVariable("cid") Integer cId, Model model, HttpSession session,
			Principal principal) {
		System.out.println("CID " + cId);
		String ruser=principal.getName();
		System.out.println(ruser);
		
		//Contact contact = this.contactRepository.findById(cId).get();
		// check...Assignment..image delete

		// delete old photo

		User user = this.userRepository.getOne(cId);
		System.out.println(user);

		//user.getContacts().remove(contact);

		this.userRepository.delete(user);
		
		//this.contactRepository.delete(contact);

		System.out.println("DELETED");
		session.setAttribute("message", new Message("User deleted successfully...", "success"));

		 return "redirect:/admin/show-users/0";
	}
	
	@GetMapping("/deactivate/{cid}")
	@Transactional
	public String deactivateUser(@PathVariable("cid") Integer cId, Model model, HttpSession session,
			Principal principal) {
		System.out.println("CID_ " + cId);
		String ruser=principal.getName();
		System.out.println(ruser);
		
		//Contact contact = this.contactRepository.findById(cId).get();
		// check...Assignment..image delete

		// delete old photo
		//delete old photo

		User user = this.userRepository.getOne(cId);
		if(user.isEnabled()==true) {
		user.setEnabled(false);
		session.setAttribute("message", new Message("User deactivated successfully...", "success"));
		}
		else {
		user.setEnabled(true);	
		session.setAttribute("message", new Message("User activated successfully...", "success"));
		}
		
		
		System.out.println(user);

		//user.getContacts().remove(contact);

		this.userRepository.save(user);
		
		//this.contactRepository.delete(contact);

		System.out.println("DELETED");
		

		 return "redirect:/admin/show-users/0";
	}
	
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		return "admin/profile";
	}


}
