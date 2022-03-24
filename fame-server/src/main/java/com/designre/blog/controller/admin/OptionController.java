package com.designre.blog.controller.admin;

import com.designre.blog.util.RestResponse;
import com.designre.blog.service.SysOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/option")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OptionController {

    private final SysOptionService sysOptionService;


    @GetMapping("all")
    public RestResponse<Map<String, String>> getAllOptions() {
        return RestResponse.ok(sysOptionService.getAllOptionMap());
    }

    @PostMapping("save")
    public RestResponse<RestResponse.Empty> saveAllOptions(@RequestBody Map<String, String> options) {
        sysOptionService.save(options);
        return RestResponse.ok();
    }
}
