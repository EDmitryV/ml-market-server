package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskListDTO {
    private Long id;
    private String name;
    private String description;
    private Long imageId;
    public TaskListDTO(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.imageId = task.getImage().getId();

    }
}
