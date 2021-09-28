package com.blockwit.bwf.model.rest.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ConfigDTO implements Serializable {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	Long mainPageId;

}
