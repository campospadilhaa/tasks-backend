package br.ce.wcaquino.taskbackend.controller;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTest {

	// para que o repository (TaskRepo) funcione é necessário instalar o SpringBoot, então, 
	// para executar o teste utiliza-se o Mock. Uma classe injetada no lugar do TaskRepo para responder em seu lugar
	// o TaskRepo estará instanciado pelo Mockito e não pelo Spring
	@Mock
	private TaskRepo taskRepo;

	// anotação que injeta o Mock (TaskRepo) na classe TaskController que precisa de um TaskRepo "verdadeiro"
	@InjectMocks
	private TaskController taskController;

	// configurando o Mockito para executar toda a parametrização realizada nas linhas anteriores
	// o Before é executado antes de cada teste:
	// . injeta os Mocks dentro do TaskController, e executa o teste
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void naoDeveSalvarTaskSemDescricao() {

		Task task = new Task();
//		task.setTask("Descrição da tarefa");
		task.setDueDate(LocalDate.now());

		try {
			taskController.save(task);
			Assert.fail("Não deveria chegar neste ponto! Teste do método sem DESCRICAO");
		} catch (ValidationException e) {
			
			// primeiro parâmetro valor ESPERADO
			// segundo parâmetro valor RETORNADO
			Assert.assertEquals("Fill the task description", e.getMessage());
		}
	}

	@Test
	public void naoDeveSalvarTaskSemData() {

		Task task = new Task();
		task.setTask("Descrição da tarefa");
//		task.setDueDate(LocalDate.now());

		try {
			taskController.save(task);			
			Assert.fail("Não deveria chegar neste ponto! Teste do método sem DATA");
		} catch (ValidationException e) {
			
			// primeiro parâmetro valor ESPERADO
			// segundo parâmetro valor RETORNADO
			Assert.assertEquals("Fill the due date", e.getMessage());
		}
	}

	@Test
	public void naoDeveSalvarTaskComDataPassada() {
		
		Task task = new Task();
		task.setTask("Descrição da tarefa");
		task.setDueDate(LocalDate.of(2010, 01, 01));

		try {
			taskController.save(task);			
			Assert.fail("Não deveria chegar neste ponto! Teste do método COM DATAATUAL ou DATAFUTURA");
		} catch (ValidationException e) {
			
			// primeiro parâmetro valor ESPERADO
			// segundo parâmetro valor RETORNADO
			Assert.assertEquals("Due date must not be in past", e.getMessage());
		}
	}

	@Test
	public void deveSalvarTaskComSucesso() throws ValidationException {
		
		Task task = new Task();
		task.setTask("Descrição da tarefa");
		task.setDueDate(LocalDate.now());

		taskController.save(task);

		// Verificando extarnamente ao TaskRepo o comportamento interno do seu save
		Mockito.verify(taskRepo).save(task);
	}
}