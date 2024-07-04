package com.example.api;

import com.example.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipper")
public class ShipperController {

    @Autowired
    private ShipperService shipperService;

    @GetMapping("/index")
    public ResponseEntity<?> index() {
        try {
            return ResponseEntity.ok(shipperService.getEnabledShippers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
