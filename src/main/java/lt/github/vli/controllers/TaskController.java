package lt.github.vli.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import lt.github.vli.services.TaskService;

@RestController
public class TaskController {
	
	@Autowired
	TaskService service;
	
	
}
