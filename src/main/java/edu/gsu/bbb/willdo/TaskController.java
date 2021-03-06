package edu.gsu.bbb.willdo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class TaskController {
    @Autowired
    TaskRepository repository;

//    Obsolete because tasks are found by group now
//    @GetMapping("/tasks") //get all tasks
//    public List<Task> all() {
//        return repository.findAll();
//    }

    @GetMapping("/tasks/groups/{groupId}") //get one specific task
    public List<Task> taskFromGroup(@PathVariable String groupId) {
        return repository.findAllByGroupId(groupId); //Uses List from TaskRepository to generate queries by GroupId
    }

    @GetMapping("/tasks/{taskId}")
    public Optional<Task> findTask(@PathVariable String taskId){
        Optional<Task> findTask = repository.findById(taskId);
        Optional<Task> empty = Optional.empty();
        if(!findTask.isPresent()){
            return empty;
        }else{
            return findTask;
        }
    }

    @PostMapping("/tasks/{groupId}") //saves new task as new doc in DB
    public Object newTaskToGroup(@RequestBody Task newTask, @PathVariable String groupId) {
        newTask.setGroupId(groupId); //Sets the PathVariable GroupId into the new Task
        if(newTask.getSummary() == null){
            newTask.setSummary("New Task");
        }
        return repository.save(newTask);
    }

    @PutMapping("/tasks/{taskId}") //updates task already in DB
    public Task updateTask(@RequestBody Task newTask, @PathVariable String taskId) {
        if (repository.findById(taskId).isPresent()) {
            Optional<Task> oldTaskInfo = repository.findById(taskId)
                    .map(task -> { //Gets the data from the Optional and maps them to a new Task
                        if (newTask.getSummary() != null) {
                            task.setSummary(newTask.getSummary());
                        }
                        if (newTask.getDescription() != null) {
                            task.setDescription(newTask.getDescription());
                        }
                        if (newTask.getDate() != null) {
                            task.setDate(newTask.getDate());
                        }
                        if (newTask.isState() != task.isState()) {
                            task.setState(newTask.isState());
                        }
                        if (newTask.getGroupId() != null) {
                            task.setGroupId(newTask.getGroupId());
                        }
                        return repository.save(task);
                    });
        }
        return newTask; //sends original request body so we can see what broke it
    }

    @DeleteMapping("/tasks/{taskId}")
    public void deleteTask(@PathVariable String taskId){
        Optional<Task> delTask = repository.findById(taskId);
        repository.delete(delTask.get());
    }
}
