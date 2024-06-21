package com.edmitryv.ml_market_server.marketplace.specifications;

import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.models.Task_;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {
    public static Specification<Task> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            return cb.isTrue(
                    cb.function(
                            "tsvector_match",
                            Boolean.class,
                            root.get(Task_.descriptionSearchVector),
                            cb.function("plainto_tsquery", String.class, cb.literal(search))
                    )
            );
        };
    }
}
