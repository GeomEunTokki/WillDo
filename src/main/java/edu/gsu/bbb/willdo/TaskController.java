package edu.gsu.bbb.willdo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    @Autowired
    TaskRepository repository;

    @GetMapping("/tasks")
    public List<Task> all() {
        return repository.findAll();
    }

    @GetMapping("/tasks/{id}")
    public Optional<Task> task(@PathVariable String id) {
        Optional<Task> findTask = repository.findById(id);
        Optional<Task> empty = Optional.empty(); //to see if it leaves loop

        if(!findTask.isPresent()) {
            //some response annotation; invalid parameters?
        } else {
            return findTask;
        }
        return empty; //should not return this ever
    }

    @PostMapping("/tasks")
    public Object newTask(@RequestBody Task newTask) {
        Optional<Task> empty = Optional.empty(); //to see if it leaves loop

        if(newTask.getSummary() == null){
            //some response annotation; null values
        } else {
            return repository.save(newTask);
        }
        return empty; //should not return this ever
    }

//    @PutMapping("/tasks/{id}")
//    public Task updateTask(@RequestBody Task newTask, @PathVariable String id) {
//
//    }
}
