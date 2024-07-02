package com.example.api;

import com.example.po.Region;
import com.example.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    /**
     * 查询地域信息
     *
     * @param regionId .
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity<?> info(@RequestParam("regionId") Long regionId) {
        Region region = regionService.getRegionInfo(regionId);
        return ResponseEntity.ok(region);
    }

    /**
     * 查询parent下所有的ragion
     *
     * @param parentId 。
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam("parentId") Long parentId) {
        List<Region> regionList = regionService.getRegionList(parentId);
        return ResponseEntity.ok(regionList);
    }

    @PostMapping("/data")
    public ResponseEntity<?> data(@RequestBody Map<String, Long> payload) {
        Long parentId = payload.get("parent_id");
        List<Region> regionList = regionService.getRegionList(parentId);
        return ResponseEntity.ok(regionList);
    }

    /**
     * 根据region的name查询对应的regionid,可能有bug
     *
     * @param payload .
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<?> code(@RequestBody Map<String, Long> payload) {
        Long province = payload.get("Province");
        Long city = payload.get("City");
        Long country = payload.get("Country");
        Map<String, Long> data = regionService.getCode(province, city, country);
        return ResponseEntity.ok(data);
    }
}
