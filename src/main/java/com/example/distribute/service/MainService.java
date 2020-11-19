package com.example.distribute.service;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.CheckApiResponse;
import com.example.distribute.model.api.response.DistributeApiResponse;
import com.example.distribute.model.api.response.ReceiveApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    public Header<DistributeApiResponse> setDistribution(int userId, String roomId, DistributeApiRequest requestData) {
        return null;
    }

    public Header<ReceiveApiResponse> receiveDistribution(int userId, String roomId, String token) {
        return null;
    }

    public Header<CheckApiResponse> checkDistribution(int userId, String roomId, String token) {
        return null;
    }


}
