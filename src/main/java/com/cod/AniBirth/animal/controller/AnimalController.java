package com.cod.AniBirth.animal.controller;

import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.animal.service.AnimalService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/animal")
public class AnimalController {
    private final AnimalService animalService;

}
