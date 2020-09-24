package com.example.todo.controllers;

import com.example.todo.entities.Project;
import com.example.todo.entities.Task;
import com.example.todo.entities.User;
import com.example.todo.repositories.ProjectRepo;
import com.example.todo.repositories.TaskRepo;
import com.example.todo.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private MainService mainService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model){

        model.addAttribute("projects", mainService.showAll(user));
        model.addAttribute("user", user);

        return "home";
    }

    @PostMapping("/add-project")
    public String addProject(@RequestParam(required = false) String name,
                             @AuthenticationPrincipal User user, Model model){

        if(!mainService.addProject(user, name)){
            model.addAttribute("projectExists", "Same project already exits");
        }

        return "redirect:/";
    }
    @PostMapping("/delete-project/{id}")
    public String deleteProject(@PathVariable(value = "id")long projectId){

        mainService.deleteProject(projectId);

        return "redirect:/";
    }
    @PostMapping("/add-task/{id}")
    public String addTask(@PathVariable(value = "id")long projectId,
                          @RequestParam(required = false) String name,
                          Model model){

        if(!mainService.addTask(name, projectId)){
            model.addAttribute("taskExists", "Same task already exits");
        }

        return "redirect:/";
    }
    @PostMapping("/delete-task/{id}")
    public String deleteTask(@PathVariable(value = "id")long taskId){

        mainService.deleteTask(taskId);

        return "redirect:/";
    }
    @GetMapping("/edit-project/{id}")
    public String editProject(@PathVariable(value = "id")long projectId, Model model){

        Project project = mainService.getProject(projectId);

        model.addAttribute("project", project);

        return "edit-project";
    }
    @GetMapping("/edit-task/{id}")
    public String editTask(@PathVariable(value = "id")long taskId, Model model){

        Task task = mainService.getTask(taskId);

        model.addAttribute("task", task);

        return "edit-task";
    }
    @PostMapping("/edit-project/{id}")
    public String updateProject(@PathVariable(value = "id")long projectId,
                                @RequestParam(required = false) String name,
                                Model model){

        Project project = mainService.getProject(projectId);

        if(mainService.updateProject(project, name)){
            model.addAttribute("message", "Project was updated!");
        }
        return "redirect:/";
    }

    @PostMapping("/edit-task/{id}")
    public String updateTask(@PathVariable(value = "id")long taskId,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) boolean doneTask,
                                @RequestParam(required = false) int year,
                                @RequestParam(required = false) int month,
                                @RequestParam(required = false) int day,
                                Model model){

        Task task = mainService.getTask(taskId);

        mainService.updateTask(task, name);

        mainService.updateStatus(task, doneTask);

        mainService.updateDate(task, year, month, day);

        return "redirect:/";
    }

    @PostMapping("/up/{idp}/{idt}")
    public String up(@PathVariable(value = "idp")long projectId,
                     @PathVariable(value = "idt")long taskId,
                     @AuthenticationPrincipal User user,
                     Model model){

        Task currentTask = taskRepo.findById(taskId);

        int currentPriority = currentTask.getPriority();

        int newPriority = currentPriority + 1;

        mainService.updatePriority(currentTask, newPriority);

        return "redirect:/";
    }
    @PostMapping("/down/{idp}/{idt}")
    public String down(@PathVariable(value = "idp")long projectId,
                       @PathVariable(value = "idt")long taskId,
                       @AuthenticationPrincipal User user,
                       Model model){

        Task currentTask = taskRepo.findById(taskId);

        int currentPriority = currentTask.getPriority();

        int newPriority = currentPriority - 1;

        mainService.updatePriority(currentTask, newPriority);

        return "redirect:/";
    }
}
