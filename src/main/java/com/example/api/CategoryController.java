package com.example.api;

import com.example.po.CategoryPO;
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
    public ResponseEntity<?> index(@RequestParam(value = "id", required = false) String id) {
        Map<String, Object> result = categoryService.getCategoryList(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/current")
    public ResponseEntity<?> current(@RequestParam("id") String id) {
        CategoryPO category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/currentlist")
    public ResponseEntity<?> currentlist(@RequestBody Map<String, Object> payload) {
        int page = (int) payload.get("page");
        int size = (int) payload.get("size");
        String id = String.valueOf(payload.get("id"));
        Map<String, Object> result = categoryService.getGoodsList(page, size, id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/updateSort")
    public ResponseEntity<?> updateSort(@RequestBody Map<String, Object> payload) {
        String id = (String) payload.get("id");
        Integer sort = (Integer) payload.get("sort");
        int result = categoryService.updateSortOrder(id, sort);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/topCategory")
    public ResponseEntity<?> getTopCategory() {
        List<CategoryPO> topCategory = categoryService.getTopCategory();
        return ResponseEntity.ok(topCategory);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(@RequestParam("id") String id) {
        CategoryPO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/store")
    public ResponseEntity<?> store(@RequestBody CategoryPO category) {
        categoryService.saveOrUpdateCategory(category);
        return ResponseEntity.ok(category);
    }

    @PostMapping("/destroy")
    public ResponseEntity<?> destroy(@RequestBody Map<String, Object> payload) {
        String id = String.valueOf(payload.get("id"));
        boolean result = categoryService.deleteCategory(id);
        return result ? ResponseEntity.ok("Success") : ResponseEntity.badRequest().body("Failed");
    }

    @GetMapping("/showStatus")
    public ResponseEntity<?> updateShowStatus(@RequestParam("id") String id, @RequestParam("status") boolean status) {
        categoryService.updateShowStatus(id, status);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/channelStatus")
    public ResponseEntity<?> updateChannelStatus(@RequestParam("id") String id, @RequestParam("status") boolean status) {
        categoryService.updateChannelStatus(id, status);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/categoryStatus")
    public ResponseEntity<?> updateCategoryStatus(@RequestParam("id") String id, @RequestParam("status") boolean status) {
        categoryService.updateCategoryStatus(id, status);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/deleteBannerImage")
    public ResponseEntity<?> deleteBannerImage(@RequestBody Map<String, Object> payload) {
        String id = String.valueOf(payload.get("id"));
        categoryService.deleteBannerImage(id);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/deleteIconImage")
    public ResponseEntity<?> deleteIconImage(@RequestBody Map<String, Object> payload) {
        String id = String.valueOf(payload.get("id"));
        categoryService.deleteIconImage(id);
        return ResponseEntity.ok("Success");
    }
}
