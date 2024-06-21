package com.edmitryv.ml_market_server.marketplace.repos;

import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Category;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppRepository extends JpaRepository<AppModel, Long>, JpaSpecificationExecutor<AppModel> {
    List<AppModel> findAllByTask(Task task);
    List<AppModel> findAllByAuthor(DeveloperAccount author);
    List<AppModel> findAllByOwners_Id(Long id);
    List<AppModel> findByOffersId(Long id);

}
