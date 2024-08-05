package com.cod.AniBirth.animal.service;

import com.cod.AniBirth.ApiResponse;
import com.cod.AniBirth.animal.AnimalSearchDTO;
import com.cod.AniBirth.animal.AnimalSpecification;
import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.animal.repository.AnimalRepository;
import com.cod.AniBirth.category.entity.Category;
import com.cod.AniBirth.category.repository.CategoryRepository;
import com.cod.AniBirth.global.security.DataNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final CategoryRepository categoryRepository;

    @Value("${custom.genFileDirPath}")
    private String imageDirectory;

    public Page<Animal> getList(int page, String kw, AnimalSearchDTO searchDTO) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("noticeDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));


        Specification<Animal> spec = AnimalSpecification.searchWith(searchDTO);
//        return animalRepository.findAll(spec, pageable);
//        return animalRepository.findAll(AnimalSpecification.searchWith(searchDTO), pageable);
        return animalRepository.findAllByKeyword(kw, pageable);
//        return animalRepository.findByFilters(kw, searchDTO.getClassification(), searchDTO.getGender(),
//                searchDTO.getWeight(), searchDTO.getAge(), pageable);
    }

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }

    public void saveAnimals(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponse apiResponse = objectMapper.readValue(json, ApiResponse.class);
        List<Animal> animals = apiResponse.getMsgBody().getItems();

        for (Animal animal : animals) {
            if ("1".equals(animal.getAdoptionStatusCd())) {
                animal.setAdoptionStatusCd("입양 공고중");
            } else if ("2".equals(animal.getAdoptionStatusCd())) {
                animal.setAdoptionStatusCd("입양가능");
            } else if ("3".equals(animal.getAdoptionStatusCd())) {
                animal.setAdoptionStatusCd("입양예정");
            } else if ("4".equals(animal.getAdoptionStatusCd())) {
                animal.setAdoptionStatusCd("입양완료");
            } else if ("7".equals(animal.getAdoptionStatusCd())) {
                animal.setAdoptionStatusCd("주인반환");
            }
            else {
                animal.setAdoptionStatusCd("기타상태");
            }
//            if ("1".equals(animal.getGender())) {
//                Category genderCategory = categoryRepository.findByName("암");
//                if (genderCategory != null) {
//                    animal.setGender(genderCategory);
//                }
//            } else if ("2".equals(animal.getGender())) {
//                Category genderCategory = categoryRepository.findByName("수");
//                if (genderCategory != null) {
//                    animal.setGender(genderCategory);
//                }
//            }
//            if ("1".equals(animal.getClassification())) {
//                Category classificationCategory = categoryRepository.findByName("개");
//                if (classificationCategory != null) {
//                    animal.setClassification(classificationCategory);
//                }
//            } else if ("2".equals(animal.getClassification())) {
//                Category classificationCategory = categoryRepository.findByName("고양이");
//                if (classificationCategory != null) {
//                    animal.setClassification(classificationCategory);
//                }
//            } else {
//                Category classificationCategory = categoryRepository.findByName("기타동물");
//                if (classificationCategory != null) {
//                    animal.setClassification(classificationCategory);
//                }
//            }
            if ("1".equals(animal.getGender())) {
                animal.setGender("암");
            } else if ("2".equals(animal.getGender())) {
                animal.setGender("수");
            }
            if ("1".equals(animal.getClassification())) {
                animal.setClassification("개");
            } else if ("2".equals(animal.getClassification())) {
                animal.setClassification("고양이");
            } else {
                animal.setClassification("기타동물");
            }

            // gu 변환
            switch (animal.getGu()) {
                case "1":
                    animal.setGu("동구");
                    break;
                case "2":
                    animal.setGu("중구");
                    break;
                case "3":
                    animal.setGu("서구");
                    break;
                case "4":
                    animal.setGu("유성구");
                    break;
                case "5":
                    animal.setGu("대덕구");
                    break;
                default:
                    animal.setGu("기타구");
                    break;
            }
        }
        animalRepository.saveAll(animals);
    }

    public void saveAnimal(Animal animal) {
        // 카테고리 설정
//        setCategory(animal);

        // Animal 엔티티 저장
        animalRepository.save(animal);
    }

    private void setCategory(Animal animal) {
        String gender = animal.getGender(); // 예: "1" 또는 "Male"
        String age = animal.getAge(); // 예: "Puppy"
        String classification = animal.getClassification(); // 예: "Dog"
        String weight = animal.getWeight(); // 예: "Small"

        // Gender 카테고리 설정
        Category genderCategory = categoryRepository.findByName(gender);
        animal.setCategory(genderCategory);

        // Age 카테고리 설정
        Category ageCategory = categoryRepository.findByName(age);
        animal.setCategory(ageCategory);

        // Classification 카테고리 설정
        Category classificationCategory = categoryRepository.findByName(classification);
        animal.setCategory(classificationCategory);

        // Weight 카테고리 설정
        Category weightCategory = categoryRepository.findByName(weight);
        animal.setCategory(weightCategory);
    }

    public Animal getAnimal(Long id) {
        Optional<Animal> animal = animalRepository.findById(id);

        if( animal.isPresent()) {
            return animal.get();
        } else {
            throw new DataNotFoundException("animal not found");
        }
    }

//    public Page<Animal> getListByCategory(int page, String kw, Long categoryId) {
//        if ( categoryId == null) {
//            return getList(page, kw);
//        }
//        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "createDate"));
//        if (kw == null || kw.isBlank()) {
//            return animalRepository.findAllByCategory_Id(categoryId, pageable);
//        }
//        return animalRepository.findAll((root, query, criteriaBuilder) ->
//                criteriaBuilder.and(
//                        criteriaBuilder.equal(root.get("category").get("id"), categoryId),
//                        criteriaBuilder.like(root.get("title"), "%" + kw + "%")
//                ), pageable);
//    }

//    public Page<Animal> getListByFilters(int page, String kw, Long classificationId, Long genderId, String weightId, String ageId) {
//        if ( classificationId == null ) {
//            return getList(page, kw);
//        }
//
//        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "createDate"));
//
//        if (kw == null || kw.isBlank()) {
//            return animalRepository.findAllByCategory_Id(classificationId, pageable);
//        }
//
//        return animalRepository.findAllByFilters(kw, classificationId, genderId, weightId, ageId, pageable);
//    }
}