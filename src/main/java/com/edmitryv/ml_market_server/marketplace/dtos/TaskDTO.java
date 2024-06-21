package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Long imageId;
    private String categoryName;
    private Long categoryId;
    private List<AppModelListDTO> apps;
    public TaskDTO(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.imageId = task.getImage().getId();
        this.categoryName = task.getCategory().getName();
        this.categoryId = task.getCategory().getId();
        this.apps = new ArrayList<>();
        for(AppModel app:task.getAppModels()){
            this.apps.add(new AppModelListDTO(app));
        }
    }
}
