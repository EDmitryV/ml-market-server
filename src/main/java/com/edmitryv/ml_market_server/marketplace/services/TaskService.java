package com.edmitryv.ml_market_server.marketplace.services;

import com.edmitryv.ml_market_server.marketplace.dtos.CategoryDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.TaskDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.TaskListDTO;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.repos.TaskRepository;
import com.edmitryv.ml_market_server.marketplace.specifications.CategorySpecifications;
import com.edmitryv.ml_market_server.marketplace.specifications.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskListDTO> search(String text){
        List<TaskListDTO> taskListDTOS = taskRepository.findAll(TaskSpecifications.search(text)).stream().map(TaskListDTO::new).toList();
        return taskListDTOS;
    }
    public List<Task> findAllByCategoryId(Long id){
        return taskRepository.findAllByCategoryId(id);
    }
    public Task findById(Long id){return taskRepository.findById(id).orElse(null);}
    public Task saveTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTaskById(Long id){
        taskRepository.deleteById(id);
    }
}
