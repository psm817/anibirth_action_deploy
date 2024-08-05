package com.cod.AniBirth.adopt.controller;

import com.cod.AniBirth.adopt.AdoptForm;
import com.cod.AniBirth.adopt.entity.AdoptApply;
import com.cod.AniBirth.adopt.service.AdoptService;
import com.cod.AniBirth.animal.AnimalSearchDTO;
import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.animal.service.AnimalService;
import com.cod.AniBirth.category.entity.Category;
import com.cod.AniBirth.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adopt")
public class AdoptController {
    private final AnimalService animalService;
    private final CategoryService categoryService;
    private final AdoptService adoptService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @ModelAttribute AnimalSearchDTO searchDTO
    ) {

//        Page<Animal> paging = animalService.getList(page, kw);
        Page<Animal> paging = animalService.getList(page, kw, searchDTO);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("classifications", categoryService.getClassifications());
        model.addAttribute("genders", categoryService.getGenders());
        model.addAttribute("weights", categoryService.getWeights());
        model.addAttribute("ages", categoryService.getAges());

        return "adopt/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Animal animal = animalService.getAnimal(id);

        model.addAttribute("animal", animal);
        return "adopt/detail";
    }

    @GetMapping("/apply")
    public String showApplyForm(AdoptForm adoptForm) {
        return "adopt/form";
    }

    @PostMapping("/apply")
    public String submitApplyForm(@Valid AdoptForm adoptForm, @RequestParam("isGender") boolean isGender, @RequestParam("isMarried") boolean isMarried, @RequestParam("file")MultipartFile file) {
        adoptService.apply(adoptForm.getName(),adoptForm.getPhone(),adoptForm.getEmail(),adoptForm.getAge(),adoptForm.getCompany(),
                adoptForm.getPostCode(),adoptForm.getAddress(),adoptForm.getDetailAddress(),adoptForm.getExtraAddress(),
                isGender,isMarried,adoptForm.getFile());

        return "redirect:/adopt/list";
    }

}