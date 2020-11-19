package com.example.distribute.controller;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.CheckApiResponse;
import com.example.distribute.model.api.response.DistributeApiResponse;
import com.example.distribute.model.api.response.ReceiveApiResponse;
import com.example.distribute.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/distribution")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @PostMapping("")
    public Header<DistributeApiResponse> setDistribution(@RequestHeader("X-USER-ID") int userId, @RequestHeader("X-ROOM-ID") String roomId,
                                                         @RequestBody DistributeApiRequest request) {
        log.info(userId + "");
        log.info(roomId);
        return mainService.setDistribution(userId, roomId, request);
    }

    @PutMapping("{token}")
    public Header<ReceiveApiResponse> receiveDistribution(@RequestHeader("X-USER-ID") int userId, @RequestHeader("X-ROOM-ID") String roomId,
                                                          @PathVariable String token) {
        return mainService.receiveDistribution(userId, roomId, token);
    }

    @GetMapping("{token}")
    public Header<CheckApiResponse> checkDistribution(@RequestHeader("X-USER-ID") int userId, @RequestHeader("X-ROOM-ID") String roomId,
                                                      @PathVariable String token) {
        return mainService.checkDistribution(userId, roomId, token);
    }
}
