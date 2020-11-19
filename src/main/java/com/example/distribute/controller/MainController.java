package com.example.distribute.controller;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.CheckApiResponse;
import com.example.distribute.model.api.response.DistributeApiResponse;
import com.example.distribute.model.api.response.ReceiveApiResponse;
import com.example.distribute.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/distribution")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @PostMapping("")
    public Header<DistributeApiResponse> setDistribution(HttpServletRequest httpServletRequest, @RequestBody DistributeApiRequest request) {
        //httpServletRequest.getHeader()
        return mainService.setDistribution(request);
    }

    @PutMapping("{token}")
    public Header<ReceiveApiResponse> receiveDistribution(@PathVariable String token) {
        return mainService.receiveDistribution(token);
    }

    @GetMapping("{token}")
    public Header<CheckApiResponse> checkDistribution(@PathVariable String token) {
        return mainService.checkDistribution(token);
    }
}
