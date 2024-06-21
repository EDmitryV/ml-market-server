package com.edmitryv.ml_market_server.marketplace.specifications;

import com.edmitryv.ml_market_server.marketplace.models.Category;
import com.edmitryv.ml_market_server.marketplace.models.Category_;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecifications {
    public static Specification<Category> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            return cb.isTrue(
                    cb.function(
                            "tsvector_match",
                            Boolean.class,
                            root.get(Category_.descriptionSearchVector),
                            cb.function("plainto_tsquery", String.class, cb.literal(search))
                    )
            );
        };
    }
}
