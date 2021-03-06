package com.pnu.dev.radioserviceapi.controller.admin;

import com.pnu.dev.radioserviceapi.dto.form.ProgramForm;
import com.pnu.dev.radioserviceapi.mongo.Program;
import com.pnu.dev.radioserviceapi.service.ProgramService;
import com.pnu.dev.radioserviceapi.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.pnu.dev.radioserviceapi.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/admin/programs")
public class ProgramAdminController {

    private ProgramService programService;

    @Autowired
    public ProgramAdminController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping
    public String listPrograms(Model model,
                               @RequestParam(value = "q", required = false) String query,
                               @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                       Pageable pageable) {

        if (StringUtils.isNoneBlank(query)) {
            Page<Program> programsPage = programService.findByTitleContains(query, pageable);
            model.addAttribute("programsPage", programsPage);
            model.addAttribute("searchKeyword", query);
        } else {
            Page<Program> programsPage = programService.findAll(pageable);
            model.addAttribute("programsPage", programsPage);
        }

        return "programs/index";
    }

    @GetMapping("/new")
    public String createForm() {
        return "programs/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        Program program = programService.findById(id);

        model.addAttribute("program", program);
        return "programs/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirmation(Model model, @PathVariable("id") String id, HttpServletRequest request) {

        programService.findById(id);

        model.addAttribute("message", "Ви впевнені, що справді хочете видалити програму " +
                "(буде також видалено всі записи з розкладу, пов'язані з цією програмою)?");

        model.addAttribute("returnBackUrl", HttpUtils.getPreviousPageUrl(request));

        return "common/deleteConfirmation";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute ProgramForm programForm, RedirectAttributes redirectAttributes) {
        programService.create(programForm);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Програму було успішно створено");
        return "redirect:/admin/programs";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String id,
                         @ModelAttribute ProgramForm programForm, RedirectAttributes redirectAttributes) {
        programService.update(id, programForm);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Програму було успішно оновлено");
        return "redirect:/admin/programs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        programService.deleteById(id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Програму було успішно видалено");
        return "redirect:/admin/programs";
    }

}
