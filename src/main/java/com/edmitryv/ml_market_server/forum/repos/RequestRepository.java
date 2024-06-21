package com.edmitryv.ml_market_server.forum.repos;

import com.edmitryv.ml_market_server.forum.models.Request;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Category;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {
    List<Request> findAllByTask(Task task);

}
