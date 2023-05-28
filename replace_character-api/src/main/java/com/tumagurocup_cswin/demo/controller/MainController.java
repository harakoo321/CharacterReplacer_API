package com.tumagurocup_cswin.demo.controller;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tumagurocup_cswin.demo.model.JsonData;
import com.tumagurocup_cswin.demo.service.CharacterReplacer;

@RestController
public class MainController {
	@Autowired
	private CharacterReplacer replacer;
	
	@PostMapping("/api")
	@ResponseBody
	public String replacecharacter(@RequestBody JsonData jsondata) throws IOException{
		var rectList = replacer.CreateRectList(jsondata.getX(), jsondata.getY(), jsondata.getWidth(), jsondata.getHeight());
		byte[] inputBytes = Base64.getDecoder().decode(jsondata.getBase64ByteImage());
		byte[] outputBytes = replacer.ReplaceCharacter(jsondata.getText(), rectList, replacer.ConvertByteToMat(inputBytes));
		return Base64.getEncoder().encodeToString(outputBytes);
	}
	
	@RequestMapping("/hello")
	public String index() {
		return "Hello World!";
	}
}
