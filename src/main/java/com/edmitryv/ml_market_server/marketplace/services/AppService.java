package com.edmitryv.ml_market_server.marketplace.services;

import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.marketplace.dtos.AppModelListDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.TaskListDTO;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.repos.AppRepository;
import com.edmitryv.ml_market_server.marketplace.specifications.AppModelSpecifications;
import com.edmitryv.ml_market_server.marketplace.specifications.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppService {
    @Autowired
    private AppRepository appRepository;

    public List<AppModel> search(String text){
        List<AppModel> appModels = appRepository.findAll(AppModelSpecifications.search(text));
        return appModels;
    }

//    public List<AppModel> searchAlt(String text){
//        List<AppModel> appModelListDTOS = appRepository.search(text);
//        return appModelListDTOS;
//    }

    public List<AppModel> findAllByTask(Task task){
        return appRepository.findAllByTask(task);
    }
    public List<AppModel> findAllByAuthor(DeveloperAccount author){
        return appRepository.findAllByAuthor(author);
    }
    public List<AppModel> findAllByOwnerId(Long id){
        return appRepository.findAllByOwners_Id(id);
    }
    public List<AppModel> findByOffersId(Long id){
        return appRepository.findByOffersId(id);
    }

    public AppModel findById(Long id){
        return appRepository.findById(id).orElse(null);
    }

    public AppModel save(AppModel app){
        return appRepository.save(app);
    }

    public void delete(AppModel app){
        appRepository.delete(app);
    }
}
