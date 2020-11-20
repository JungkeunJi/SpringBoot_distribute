package com.example.distribute.service;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.CheckApiResponse;
import com.example.distribute.model.api.response.DistributeApiResponse;
import com.example.distribute.model.api.response.ReceiveApiResponse;
import com.example.distribute.model.entity.Distribution;
import com.example.distribute.repository.DistributionRepository;
import com.example.distribute.repository.DistributionStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MainService {

    private final DistributionRepository distributionRepository;
    private final DistributionStateRepository distributionStateRepository;

    /**
     * 뿌리기 생성
     * distribute 테이블에 뿌리기 데이터 저장 및 distribute_state 테이블에 뿌린 돈 분배 관련 상태 정보 저
     * TODO. map 연결 시켜서 distribution_state에 데이터 생성, 뿌린돈 분배는 50프로씩 총인원 - 하면서 분배하고(최초는 소수점내림, 마지막 인원은 남은돈 전부)
     * TODO. 분배돈 리스트 컬렉션 셔플 해서 무작위로 인원수만큼 레코드 insert
     * @param userId
     * @param roomId
     * @param requestData
     * @return
     */
    public Header<DistributeApiResponse> setDistribution(int userId, String roomId, DistributeApiRequest requestData) {
        return Optional.ofNullable(requestData)
                .map(data -> {
                    String token = makeUniqueToken();

                    if(token == null)
                        return null;

                    Distribution distribution = Distribution.builder()
                            .userId(userId)
                            .roomId(roomId)
                            .totalMoney(data.getTotalMoney())
                            .totalDistributeNum(data.getTotalDistributeNum())
                            .token(token)
                            .registeredAt(LocalDateTime.now())
                            .build();

                    return distribution;
                })
                .orElseGet(() -> Header.ERROR("데이터 생성 실패")); //response 만들면 에러 없어질

    }

    public Header<ReceiveApiResponse> receiveDistribution(int userId, String roomId, String token) {
        return null;
    }

    public Header<CheckApiResponse> checkDistribution(int userId, String roomId, String token) {
        return null;
    }

    /**
     * UUID에서 substring을 통한 3자리 random 토큰 생성, 기존 레코드에 해당 token이 없을 경우 반환
     * @return
     */
    private String makeUniqueToken() {
        int maxRetryCount = 5;
        int retryCount = 0;
        boolean find = true;
        String randomUUID = UUID.randomUUID().toString().substring(0, 3);

        while(!distributionRepository.findFirstByToken(randomUUID).isPresent()) {
            if(retryCount >= maxRetryCount) {
                find = false;
                break;
            }
            retryCount++;
        }

        if(find)
            return randomUUID;
        else
            return null;
    }

}
