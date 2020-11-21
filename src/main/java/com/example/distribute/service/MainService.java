package com.example.distribute.service;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.CheckApiResponse;
import com.example.distribute.model.api.response.DistributeApiResponse;
import com.example.distribute.model.api.response.ReceiveApiResponse;
import com.example.distribute.model.entity.Distribution;
import com.example.distribute.model.entity.DistributionState;
import com.example.distribute.model.enumclass.AllocatedStatus;
import com.example.distribute.repository.DistributionRepository;
import com.example.distribute.repository.DistributionStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MainService {

    private final DistributionRepository distributionRepository;
    private final DistributionStateRepository distributionStateRepository;

    /**
     * 뿌리기 생성
     * distribute 테이블에 뿌리기 데이터 저장 및 distribute_state 테이블에 뿌린 돈 분배 관련 상태 정보 저장
     * @param userId
     * @param roomId
     * @param requestData
     * @return
     */
    public Header<DistributeApiResponse> setDistribution(int userId, String roomId, DistributeApiRequest requestData) {
        String token = makeUniqueToken();

        if(token == null)
            return Header.ERROR("토큰 생성 실패. 다시 시도해주세요.");

        return Optional.ofNullable(requestData)
                .map(data -> {
                    if(data.getTotalDistributeNum() <= 0 || data.getTotalMoney() < data.getTotalDistributeNum())
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
                .map(newData -> distributionRepository.save(newData))
                .map(newData -> {
                    ArrayList<Long> allocatedMoneyList = allocateMoneyByDistributeNum(newData.getTotalMoney(), newData.getTotalDistributeNum());

                    for(Long allocatedMoney : allocatedMoneyList) {
                        DistributionState distributionState = DistributionState.builder()
                                .token(newData.getToken())
                                .allocatedMoney(allocatedMoney)
                                .allocatedStatus(AllocatedStatus.UNALLOCATED)
                                .distribution(newData)
                                .build();

                        distributionStateRepository.save(distributionState);
                    }

                    return newData.getToken();
                }) //state 테이블에 입력
                .map(newToken -> {
                    DistributeApiResponse body = DistributeApiResponse.builder()
                            .token(newToken)
                            .build();

                    return Header.OK(body);
                }) //OK response
                .orElseGet(() -> Header.ERROR("요청 데이터를 확인해주세요."));
    }

    /**
     * 뿌린돈 받기
     * 뿌리기 당 한번만 받을 수 있음 / 뿌리기 한 사람은 못받음 / 뿌리기 대화방에서만 가능 / 10분 지나면 못받음
     * @param userId
     * @param roomId
     * @param token
     * @return
     */
    public Header<ReceiveApiResponse> receiveDistribution(int userId, String roomId, String token) {
        Distribution distribution = distributionRepository.findFirstByToken(token).orElse(null);

        if(distribution == null)
            return Header.ERROR("데이터가 존재하지 않습니다.");
        else if(distribution.getUserId() == userId)
            return Header.ERROR("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
        else if(!distribution.getRoomId().equals(roomId))
            return Header.ERROR("해당 뿌리기 방에 존재하지 않는 사용자입니다.");
        else if(LocalDateTime.now().isAfter(distribution.getRegisteredAt().plusMinutes(10)))
            return Header.ERROR("해당 뿌리기는 유효시간이 초과하였습니다.");
        else {
            //해당 토큰으로 state 테이블 조회해서 userid에 자신이 있는지 확인
            if(distributionStateRepository.findFirstByTokenAndAllocatedUserId(token, userId).isPresent())
                return Header.ERROR("뿌리기 당 한번만 받을 수 있습니다.");
            //없을 경우 하나 레코드를 상태값 update
            distributionStateRepository.findFirstByTokenAndAllocatedStatus_Unallocated(token)
                    .map(data -> {
                        data.setAllocatedStatus(AllocatedStatus.ALLOCATED)
                                .setAllocatedUserId(userId)
                                ;

                        return data;
                    })
                    .map(changeData -> distributionStateRepository.save(changeData))
                    .map(changeData -> {
                        ReceiveApiResponse body = ReceiveApiResponse.builder()
                                .allocatedMoney(changeData.getAllocatedMoney())
                                .build();

                        return Header.OK(body);
                    })
                    .orElseGet(() -> Header.ERROR("해당 뿌리기는 모든 사용자가 이미 받았습니다."));
        }

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

    /**
     * 뿌린 돈을 뿌릴 인원만큼 랜덤하게 분배
     * 50%, 25%, 12.5%.... 식으로 분배 후, 리스트안에서 shuffle
     * @param money
     * @param distributeNum
     * @return
     */
    private ArrayList<Long> allocateMoneyByDistributeNum(long money, int distributeNum) {
        ArrayList<Long> allocatedMoneyList = new ArrayList<>();

        while(distributeNum != 0) {
            if(distributeNum == 1) {
                allocatedMoneyList.add(money);
                break;
            }
            long allocateMoney = money / 2;

            allocatedMoneyList.add(allocateMoney);
            money = money - allocateMoney;

            distributeNum--;
        }

        Collections.shuffle(allocatedMoneyList);

        return allocatedMoneyList;
    }
}
