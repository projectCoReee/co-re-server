package com.dongyang.core.external.gpt;

import static com.dongyang.core.domain.gpt.constant.GptConstant.*;
import static com.dongyang.core.global.common.constants.message.GptErrorMessage.*;
import static com.dongyang.core.global.common.constants.message.WebClientErrorMessage.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.dongyang.core.external.gpt.dto.gpt.GptMessage;
import com.dongyang.core.external.gpt.dto.gpt.GptQuestionResponseDto;
import com.dongyang.core.external.gpt.dto.gpt.GptRequest;
import com.dongyang.core.global.common.exception.model.BadGatewayException;
import com.dongyang.core.global.common.exception.model.ValidationException;
import com.dongyang.core.global.common.exception.model.WebClientException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebClientGptApiCaller implements GptApiCaller {

	private final WebClient webClient;

	@Value("${gpt.apikey}")
	private String API_KEY;

	@Value("${gpt.uri}")
	private String API_URI;

	@Override
	public GptQuestionResponseDto sendRequest(GptRequest request, List<GptMessage> gptMessages) {

		return webClient.post()
			.uri(API_URI)
			.headers(headers -> {
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.setBearerAuth(API_KEY);
			})
			.bodyValue(createRequestBody(gptMessages, request))
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
				Mono.error(new ValidationException(
					WRONG_GPT_ACCESS_ERROR_MESSAGE)))
			.onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
				Mono.error(new BadGatewayException(GPT_INTERLOCK_ERROR_MESSAGE)))
			.bodyToMono(GptQuestionResponseDto.class)
			.doOnError(error -> {
				throw new WebClientException(WEB_CLIENT_CONNECTION_ERROR_MESSAGE, error);
			})
			.block();
	}

	private Map<String, Object> createRequestBody(List<GptMessage> gptMessages, GptRequest request) {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put(MESSAGES, gptMessages);
		requestBody.put(MODEL, request.getFunction().getModel());
		requestBody.put(MAX_TOKENS, request.getFunction().getMaxToken());
		requestBody.put(TEMPERATURE, request.getFunction().getTemperature());

		return requestBody;
	}
}
