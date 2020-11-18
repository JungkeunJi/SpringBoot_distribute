package com.example.distribute.controller;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.DistributeApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    @PostMapping
    public Header<DistributeApiResponse> create(@RequestBody DistributeApiRequest) {

    }
}
