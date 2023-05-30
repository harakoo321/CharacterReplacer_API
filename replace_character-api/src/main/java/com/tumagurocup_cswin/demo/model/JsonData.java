package com.tumagurocup_cswin.demo.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JsonData {
	@JsonProperty("text")
	private List<String> text;
	@JsonProperty("x")
	private List<Integer> x;
	@JsonProperty("y")
	private List<Integer> y;
	@JsonProperty("width")
	private List<Integer> width;
	@JsonProperty("height")
	private List<Integer> height;
	@JsonProperty("imageWidth")
	private int imageWidth;
	@JsonProperty("imageHeight")
	private int imageHeight;
	@JsonProperty("base64ByteImage")
	private String base64ByteImage;
}
