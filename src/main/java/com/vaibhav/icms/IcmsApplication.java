package com.vaibhav.icms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IcmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IcmsApplication.class, args);
		// ok
	}

	
}
 /*
 
 @Bean
 public CommandLineRunner testRepository(UserRepository userRepository){
	 return args -> {
		 // 1. creating a raw user object ( bypassing dtos/service at the moment)
		 User user = new User();
		 user.setName("vaibav");
		 user.setEmail("tet@xample.com");
		 user.setPassword("sert@123");
		 user.setRole(Role.SITE_ENGINEER);
 
 
		 //save it
		 userRepository.save(user);
		 System.out.println("🔴 user saved with id" + user.getName());
 
		 // fetch it
		 User fetchedUser = userRepository.findById(user.getId()).orElse(null);
		 if(fetchedUser != null){
			 System.out.println("user has been fetced 🔴" + fetchedUser.getId());
		 }
 
 
	 };
 
 }
 */