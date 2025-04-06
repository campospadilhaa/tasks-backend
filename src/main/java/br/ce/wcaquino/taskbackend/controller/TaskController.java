package br.ce.wcaquino.taskbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.DateUtils;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

@RestController
@RequestMapping(value ="/todo")
public class TaskController {

	@Autowired
	private TaskRepo todoRepo;
	
	@GetMapping
	public List<Task> findAll() {
		return todoRepo.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Task> save(@RequestBody Task task) throws ValidationException {

		// criar teste: sem descrição
		if(task.getTask() == null || task.getTask() == "") {
			throw new ValidationException("Fill the task description");
		}

		// criar teste: sem data
		if(task.getDueDate() == null) {
			throw new ValidationException("Fill the due date");
		}

		// criar teste: sem data passada
		if(!DateUtils.isEqualOrFutureDate(task.getDueDate())) {
			throw new ValidationException("Due date must not be in past");
		}

		// criar teste: tarefa perfeita 
		Task saved = todoRepo.save(task);

		return new ResponseEntity<Task>(saved, HttpStatus.CREATED);
	}
}	