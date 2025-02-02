package com.dongyang.core.domain.gpt.api;

import static com.dongyang.core.global.response.SuccessCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dongyang.core.domain.gpt.service.GptService;
import com.dongyang.core.external.gpt.dto.gpt.GptQuestionResponse;
import com.dongyang.core.external.gpt.dto.gpt.GptRequest;
import com.dongyang.core.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Gpt")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GptController {
	private final GptService gptService;

	@Operation(summary = "변수명 추천")
	@PostMapping("/gpt/recommendVariableName")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<GptQuestionResponse> recommendVariableName(@Valid @RequestBody GptRequest request) {
		return ApiResponse.success(RECOMMEND_VARIABLE_NAME_SUCCESS, gptService.recommendVariableName(request));
	}

	@Operation(summary = "설명 주석 추가")
	@PostMapping("/gpt/addComment")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<GptQuestionResponse> addComment(@Valid @RequestBody GptRequest request) {
		return ApiResponse.success(ADD_COMMENT_SUCCESS, gptService.addComment(request));
	}

	@Operation(summary = "언어 변환")
	@PostMapping("/gpt/changeLanguage")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<GptQuestionResponse> changeLanguage(@Valid @RequestBody GptRequest request) {
		return ApiResponse.success(CHANGE_LANGUAGE_SUCCESS, gptService.changeLanguage(request));
	}

	@Operation(summary = "코드 리팩토링")
	@PostMapping("/gpt/refactorCode")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<GptQuestionResponse> code(@Valid @RequestBody GptRequest request) {
		return ApiResponse.success(REFACTOR_CODE_SUCCESS, gptService.refactorCode(request));
	}

}
