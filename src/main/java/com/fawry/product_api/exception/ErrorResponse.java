package com.fawry.product_api.exception;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> error
) {
}
