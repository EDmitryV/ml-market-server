package com.edmitryv.ml_market_server.core.data;

import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.core.repos.ImageRepository;
import com.edmitryv.ml_market_server.marketplace.models.Category;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.repos.CategoryRepository;
import com.edmitryv.ml_market_server.marketplace.repos.TaskRepository;
import com.edmitryv.ml_market_server.marketplace.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;
    private ImageRepository imageRepository;


    @Autowired
    public DataLoader(TaskRepository taskRepository, CategoryRepository categoryRepository, ImageRepository imageRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    public void run(ApplicationArguments args) {
//        if(categoryRepository.findById(1L).orElse(null) == null){
            var category = new Category();
            category.setId(1L);
            category.setName("Обработка изображений");
            category.setDescription("Обработка информации в формате изображений и видеопотока с любым выходным форматом.");
            var image = new Image();
            image.setId(1L);
           image = imageRepository.save(image);
            category.setImage(image);
            categoryRepository.save(category);

            category = new Category();
            category.setId(2L);
            category.setName("Обработка аудио");
            category.setDescription("Обработка информации в формате аудио файлов или аудио потока с любым выходным форматом.");
            image = new Image();
            image.setId(2L);
            image = imageRepository.save(image);
            category.setImage(image);
            categoryRepository.save(category);

            category = new Category();
            category.setId(3L);
            category.setName("Обработка текста");
            category.setDescription("Обработка информации в виде текстового представления с ответом в виде текста, сгенерированного изображения, аудио или др.");
            image = new Image();
            image.setId(3L);
            image = imageRepository.save(image);
            category.setImage(image);
            List<Task> tasks = new ArrayList<>();
            category = categoryRepository.save(category);
            var task = new Task();
            task.setId(1L);
            task.setName("Текст в речь");
            task.setDescription("Озвучивает текстовый файл возвращая файл аудио формата.");
            image = new Image();
            image.setId(4L);
            image = imageRepository.save(image);
            task.setImage(image);
            task.setCategory(category);
            taskRepository.save(task);
            tasks.add(task);
            task = new Task();
            task.setId(2L);
            task.setName("Чат бот");
            task.setDescription("Анализирует текст и генерирует наиболее контекстуально подходящий ответ в текстовом формате.");
            image = new Image();
            image.setId(5L);
            image = imageRepository.save(image);
            task.setImage(image);
            task.setCategory(category);
            taskRepository.save(task);
            tasks.add(task);
            categoryRepository.save(category);

            category = new Category();
            category.setId(4L);
            category.setName("Обработка данных");
            category.setDescription("Обработка информации в формате многомерного массива данных представленного файлами .csv, .xlsx или другими с любым выходным форматом.");
            image = new Image();
            image.setId(6L);
            image = imageRepository.save(image);
            category.setImage(image);
            categoryRepository.save(category);
//        }

    }
}