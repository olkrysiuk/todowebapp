package com.example.todo.services;

import com.example.todo.entities.Project;
import com.example.todo.entities.Task;
import com.example.todo.entities.User;
import com.example.todo.repositories.ProjectRepo;
import com.example.todo.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private TaskRepo taskRepo;

    public ArrayList<Project> showAll(User user){
        Iterable<Project> iterableProjects;
        iterableProjects = projectRepo.findAll();
        ArrayList<Project> res = new ArrayList<>();
        for (Project i : iterableProjects) {
            if(i.getAuthorName().equals(user.getUsername())){
                res.add(i);
            }
        }
        return res;
    }

    public boolean addProject(User user, String name){

//        Project projectFromDb = projectRepo.findByName(name);
//
        if (name.isEmpty()){
            return false;
        }
        Project project = new Project(user, name);
        this.projectRepo.save(project);

        return true;
    }
    public void deleteProject(long projectId){

        Project project = projectRepo.findById(projectId);

        List<Task> tasks = project.getTasks();

        for (Task task:tasks) {
            taskRepo.delete(task);
        }

        this.projectRepo.delete(project);

    }

    public boolean addTask(String name, long projectId){

//        Task taskFromDb = taskRepo.findByName(name);
//
        if (name.isEmpty()){
            return false;
        }
        Task task = new Task(name);

        Project project = projectRepo.findById(projectId);

        project.getTasks().add(task);

        this.projectRepo.save(project);

        return true;
    }

    public void deleteTask(long taskId) {

        Task task = taskRepo.findById(taskId);

        taskRepo.delete(task);
    }

    public Project getProject(long projectId){

        return projectRepo.findById(projectId);
    }

    public Task getTask(long taskId){

        return taskRepo.findById(taskId);
    }

    public boolean updateProject(Project project, String name){
        project.setName(name);
        projectRepo.save(project);
        return true;
    }

    public void updateTask(Task task, String name){
        task.setName(name);
        taskRepo.save(task);
    }
    public void updateStatus(Task task, boolean doneTask){

        task.setStatus(!doneTask);
    }
    public void updateDate(Task task, int year, int month, int day) {

        Calendar defaultDeadline = new GregorianCalendar(2021, Calendar.JANUARY, 1);

        Calendar newDeadline = new GregorianCalendar(year, month, day);
        if(!defaultDeadline.equals(newDeadline)){
            try {
                task.setDeadline(newDeadline);
            } catch (Exception e) {
                System.out.println("Wrong date!");
            }
        }
        taskRepo.save(task);
    }
        public void updatePriority (Task task, int priority){

            if(priority != 0 && priority != 8)

            task.setPriority(priority);

            taskRepo.save(task);
        }
}
