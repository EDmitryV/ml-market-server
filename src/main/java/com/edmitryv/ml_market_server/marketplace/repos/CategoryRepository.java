package com.edmitryv.ml_market_server.marketplace.repos;
import com.edmitryv.ml_market_server.marketplace.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

}
