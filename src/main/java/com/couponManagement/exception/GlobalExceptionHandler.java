package com.couponManagement.exception;

import com.couponManagement.constants.ApiResponseCode;
import com.couponManagement.VO.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ResultVO> handleCouponNotFoundException(CouponNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResultVO(ApiResponseCode.FAILURE, ex.getMessage()));
    }

    @ExceptionHandler(CouponAlreadyExistsException.class)
    public ResponseEntity<ResultVO> handleCouponAlreadyExistsException(CouponAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResultVO(ApiResponseCode.FAILURE, ex.getMessage()));
    }

    @ExceptionHandler(InvalidCouponException.class)
    public ResponseEntity<ResultVO> handleInvalidCouponException(InvalidCouponException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResultVO(ApiResponseCode.FAILURE, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultVO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResultVO(ApiResponseCode.FAILURE, "Validation failed: " + errors.toString()));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ResultVO> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ResultVO(ApiResponseCode.FAILURE, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResultVO(ApiResponseCode.FAILURE, "An unexpected error occurred: " + ex.getMessage()));
    }

}
