package com.tzcpa.controller.treatment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tzcpa.service.student.IHseService;

/**
 * @author WangYao
 * 2019年5月10日
 */
@RestController
@RequestMapping("/question")
public class QuestionEventController {

	@Autowired
	@Qualifier("qeService")
	private IHseService hseService;
}

