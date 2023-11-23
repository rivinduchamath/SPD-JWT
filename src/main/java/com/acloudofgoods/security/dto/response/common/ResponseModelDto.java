package com.acloudofgoods.security.dto.response.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModelDto implements Serializable {
    Object data;
    Object error;
    Object message;
    String code;
    String httpStatus;
    String description;

    public ResponseModelDto(String code, String httpStatus, Object message, String description, Object data, Object error) {
        this.data = data;
        this.error = error;
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
