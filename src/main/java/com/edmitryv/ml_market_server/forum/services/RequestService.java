package com.edmitryv.ml_market_server.forum.services;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.forum.dtos.RequestListDTO;
import com.edmitryv.ml_market_server.forum.models.Request;
import com.edmitryv.ml_market_server.forum.models.RequestScore;
import com.edmitryv.ml_market_server.forum.repos.RequestRepository;
import com.edmitryv.ml_market_server.forum.repos.RequestScoreRepository;
import com.edmitryv.ml_market_server.forum.specifications.RequestSpecifications;
import com.edmitryv.ml_market_server.marketplace.dtos.TaskListDTO;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.specifications.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestScoreRepository requestScoreRepository;

    public List<RequestListDTO> search(String text){
        List<RequestListDTO> requestListDTOS = requestRepository.findAll(RequestSpecifications.search(text)).stream().map(RequestListDTO::new).toList();
        return requestListDTOS;
    }

    public List<Request> getAllRequests(){
        return requestRepository.findAll();
    }

    public Request findById(Long id){
        return requestRepository.findById(id).orElse(null);
    }

    public Request save(Request request){
        return requestRepository.save(request);
    }

    public void deleteRequestById(Long id){
        requestRepository.deleteById(id);
    }

    public RequestScore saveRequestScore(RequestScore requestScore){
       return requestScoreRepository.save(requestScore);
    }
    public void deleteRequestScoreById(Long id){requestScoreRepository.deleteById(id);}

    public List<Request> findAllByTask(Task task){
        return requestRepository.findAllByTask(task);
    }

    public RequestScore findScoreByRequestAndAuthor(Request request, CustomerAccount author){
        List<RequestScore> scoreList = requestScoreRepository.findByTargetRequestAndAuthor(request, author);
        return scoreList.isEmpty()?null:scoreList.get(0);
    }
}
