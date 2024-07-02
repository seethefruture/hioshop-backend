package com.example.api;

import com.example.po.Category;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/index")
    public ResponseEntity<?> index(@RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> result = categoryService.getCategoryList(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/current")
    public ResponseEntity<?> current(@RequestParam("id") Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/currentlist")
    public ResponseEntity<?> currentlist(@RequestBody Map<String, Object> payload) {
        int page = (int) payload.get("page");
        int size = (int) payload.get("size");
        Long id = ((Number) payload.get("id")).longValue();
        Map<String, Object> result = categoryService.getGoodsList(page, size, id);
        return ResponseEntity.ok(result);
    }
}
