package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Category;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long imageId;
    private List<TaskListDTO> tasks;
    public CategoryDTO(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.imageId = category.getImage().getId();
        this.tasks = new ArrayList<>();
        for(Task task:category.getTasks()){
            this.tasks.add(new TaskListDTO(task));
        }
    }
}
