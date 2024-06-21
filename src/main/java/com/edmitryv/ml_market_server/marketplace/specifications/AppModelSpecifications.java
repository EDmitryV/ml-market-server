package com.edmitryv.ml_market_server.marketplace.specifications;

import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.AppModel_;
import org.springframework.data.jpa.domain.Specification;

public class AppModelSpecifications {
    public static Specification<AppModel> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            return cb.isTrue(
                    cb.function(
                            "tsvector_match",
                            Boolean.class,
                            root.get(AppModel_.description),
                            cb.function("plainto_tsquery", String.class, cb.literal(search))
                    )
            );
        };
    }
}
