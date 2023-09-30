package pl.maciejnierzwicki.mcshop.web.controller.admin.mainpageconfiguration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring6.SpringTemplateEngine;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.User;
import pl.maciejnierzwicki.mcshop.utils.FilesHelper;
import pl.maciejnierzwicki.mcshop.web.controller.ControllerCommons;
import pl.maciejnierzwicki.mcshop.web.form.PageContentForm;

@Controller
@RequestMapping("/admin/mainpageconfiguration")
@Slf4j
public class MainPageController {
	
	@Autowired
	private String templatesDirPath;
	@Autowired
	private SpringTemplateEngine templateEngine;
	private final String contentStartTag = "<content>";
	private final String contentEndTag = "</content>";
	private final String serversReplacement = "<!-- LISTA SERWERÓW: START --> <div th:replace=\\\"home :: servers\\\"></div> <!-- LISTA SERWERÓW: KONIEC -->";
	
	@ModelAttribute("mainPageContent")
	public PageContentForm mainPageContentForm() {
		PageContentForm form = new PageContentForm();
		form.setContent(getContentSection(getHomeFileContent()));
		return form;
	}
	
	
	@GetMapping
	public String showMainPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("mainPageContent") PageContentForm form) {
		model.addAttribute("VIEW_FILE", "admin/mainpageconfiguration/mainpage");
		model.addAttribute("VIEW_NAME", "mainpage");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	@PostMapping
	public String updateMainPage(@AuthenticationPrincipal User user, Model model, @ModelAttribute("mainPageContent") PageContentForm form) {
		String final_content = form.getContent().replaceAll("\\[SERWERY\\]", serversReplacement);
		setContentSection(getHomeFileContent(), final_content);
		form.setContent(final_content);
		templateEngine.clearTemplateCache();
		log.debug("Template cache cleared");
		model.addAttribute("mainPageContent", form);
		model.addAttribute("VIEW_FILE", "admin/mainpageconfiguration/mainpage");
		model.addAttribute("VIEW_NAME", "mainpage");
		return ControllerCommons.ROOT_VIEW_FILE_NAME;
	}
	
	private String getHomeFileContent() {
		String home_content = FilesHelper.getTextContentFromFile(templatesDirPath + "home.html");
		return home_content;
	}
	
	public void setHomeFileContent(String content) {
		FilesHelper.saveTextContentToFile(content, templatesDirPath + "home.html");
	}
	
	private void setContentSection(String template, String content) {
		if(template == null || !template.contains(contentStartTag) || !template.contains(contentEndTag)) { return; }
		String start_part = template.split(contentStartTag)[0];
		String end_part = template.split(contentEndTag)[1];
		StringBuilder builder = new StringBuilder();
		builder.append(start_part);
		builder.append(contentStartTag);
		builder.append(content);
		builder.append(contentEndTag);
		builder.append(end_part);
		
		String home_file_content = builder.toString();
		setHomeFileContent(home_file_content);
	}
	
	private String getContentSection(String template) {
		if(template == null || !template.contains(contentStartTag) || !template.contains(contentEndTag)) { return ""; }
		int start = template.indexOf(contentStartTag);
		int end = template.indexOf(contentEndTag);
		StringBuilder builder = new StringBuilder();
		for(int i = start + contentStartTag.length(); i < end; i++) {
			builder.append(template.charAt(i));
		}
		String content = builder.toString();
		return content;
	}

}
