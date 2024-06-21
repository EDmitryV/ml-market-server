package com.edmitryv.ml_market_server.forum.specifications;

import com.edmitryv.ml_market_server.forum.models.Request;
import com.edmitryv.ml_market_server.forum.models.Request_;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public class RequestSpecifications {
    public static Specification<Request> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            return cb.isTrue(
                    cb.function(
                            "tsvector_match",
                            Boolean.class,
                            root.get(Request_.description),
                            cb.function("plainto_tsquery", String.class, cb.literal(search))
                    )
            );
        };
    }

}
