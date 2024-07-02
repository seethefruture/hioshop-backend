package com.example.api;

import com.example.vo.Category;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/updateSort")
    public ResponseEntity<?> updateSort(@RequestBody Map<String, Object> payload) {
        Long id = Long.valueOf((Integer) payload.get("id"));
        Integer sort = (Integer) payload.get("sort");
        int result = categoryService.updateSortOrder(id, sort);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/topCategory")
    public ResponseEntity<?> getTopCategory() {
        List<Category> topCategory = categoryService.getTopCategory();
        return ResponseEntity.ok(topCategory);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(@RequestParam("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/store")
    public ResponseEntity<?> store(@RequestBody Category category) {
        categoryService.saveOrUpdateCategory(category);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/destroy")
    public ResponseEntity<?> destroy(@RequestBody Map<String, Object> payload) {
        Long id = Long.valueOf((Integer) payload.get("id"));
        boolean result = categoryService.deleteCategory(id);
        return result ? ResponseEntity.ok("Success") : ResponseEntity.badRequest().body("Failed");
    }

    @GetMapping("/showStatus")
    public ResponseEntity<?> updateShowStatus(@RequestParam("id") Long id, @RequestParam("status") boolean status) {
        categoryService.updateShowStatus(id, status);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/channelStatus")
    public ResponseEntity<?> updateChannelStatus(@RequestParam("id") Long id, @RequestParam("status") boolean status) {
        categoryService.updateChannelStatus(id, status);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/categoryStatus")
    public ResponseEntity<?> updateCategoryStatus(@RequestParam("id") Long id, @RequestParam("status") boolean status) {
        categoryService.updateCategoryStatus(id, status);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/deleteBannerImage")
    public ResponseEntity<?> deleteBannerImage(@RequestBody Map<String, Object> payload) {
        Long id = Long.valueOf((Integer) payload.get("id"));
        categoryService.deleteBannerImage(id);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/deleteIconImage")
    public ResponseEntity<?> deleteIconImage(@RequestBody Map<String, Object> payload) {
        Long id = Long.valueOf((Integer) payload.get("id"));
        categoryService.deleteIconImage(id);
        return ResponseEntity.ok("Success");
    }
}
