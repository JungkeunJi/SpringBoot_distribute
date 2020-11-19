package com.example.distribute.service;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.CheckApiResponse;
import com.example.distribute.model.api.response.DistributeApiResponse;
import com.example.distribute.model.api.response.ReceiveApiResponse;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    public Header<DistributeApiResponse> setDistribution(DistributeApiRequest request) {
        return null;
    }

    public Header<ReceiveApiResponse> receiveDistribution(String token) {
        return null;
    }

    public Header<CheckApiResponse> checkDistribution(String token) {
        return null;
    }
}
