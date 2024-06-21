package com.edmitryv.ml_market_server.marketplace.controllers;

import com.edmitryv.ml_market_server.marketplace.dtos.CategoryDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.TaskDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.TaskListDTO;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private final TaskService taskService;

    @GetMapping("/search/{text}")
    public ResponseEntity<List<TaskListDTO>> search(@PathVariable String text
    ) {
        return ResponseEntity.ok(taskService.search(text));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable long id){
        Task task = taskService.findById(id);
        return ResponseEntity.ok(new TaskDTO(task));
    }
}
