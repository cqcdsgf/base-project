/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sgf.base.demo.crud.web;

import com.sgf.base.demo.crud.domain.Message;
import com.sgf.base.demo.crud.dao.MessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Rob Winch
 * @author Doo-Hwan Kwak
 */
@Controller
@RequestMapping("/demo/crud")
public class MessageController {

	private final MessageRepository messageRepository;

	public MessageController(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@GetMapping("/list")
	public ModelAndView list() {
		Iterable<Message> messages = this.messageRepository.findAll();
		return new ModelAndView("demo/crud/list", "messages", messages);
		//return new ModelAndView("demo/cruds/list");
	}

	@GetMapping("/view/{id}")
	public ModelAndView view(@PathVariable("id") Message message) {
		return new ModelAndView("demo/crud/view", "message", message);
	}

	@GetMapping(path = "/create")
	public String createForm(@ModelAttribute Message message) {
		return "demo/crud/form";
	}

	@PostMapping(path = "/create", params = "form")
	public ModelAndView create(@Valid Message message, BindingResult result,RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView("demo/crud/form", "formErrors", result.getAllErrors());
		}
		message = this.messageRepository.save(message);
		redirect.addFlashAttribute("globalMessage", "Successfully created a new message");
		return new ModelAndView("redirect:/demo/crud/view/{message.id}", "message.id", message.getId());
	}

	@GetMapping(value = "/delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) {
		this.messageRepository.deleteMessage(id);
		Iterable<Message> messages = this.messageRepository.findAll();
		return new ModelAndView("demo/crud/list", "messages", messages);
	}

	@GetMapping(value = "/modify/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Message message) {
		return new ModelAndView("demo/crud/form", "message", message);
	}



}
