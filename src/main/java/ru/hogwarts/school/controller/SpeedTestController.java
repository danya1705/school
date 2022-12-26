package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.SpeedTestService;

@RestController
public class SpeedTestController {

    private final SpeedTestService speedTestService;

    public SpeedTestController(SpeedTestService speedTestService) {
        this.speedTestService = speedTestService;
    }

    @GetMapping("/stream-speed-test")
    public long testStreamSpeed() {
        return speedTestService.testStreamSpeed();
    }
}
