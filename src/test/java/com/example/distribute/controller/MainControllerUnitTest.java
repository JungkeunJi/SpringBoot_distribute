package com.example.distribute.controller;

import com.example.distribute.model.api.Header;
import com.example.distribute.model.api.request.DistributeApiRequest;
import com.example.distribute.model.api.response.DistributeApiResponse;
import com.example.distribute.model.api.response.ReceiveApiResponse;
import com.example.distribute.model.entity.Distribution;
import com.example.distribute.model.entity.DistributionState;
import com.example.distribute.model.enumclass.AllocatedStatus;
import com.example.distribute.service.MainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
@AutoConfigureMockMvc
public class MainControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MainService mainService;

    /**
     * 뿌릴 인원, 뿌릴 돈 요청값, 토큰 반환
     *
     * @throws Exception
     */
    @Test
    @DisplayName("뿌리기 생성 테스트")
    public void setDistributionTest() throws Exception {
        //mock request 값
        int userId = 2;
        String roomId= "abc";
        int totalDistributeNum = 4;
        long totalMoney = 10000;

        //mock response 값
        String token = "a3c";

        //예상 응답 토큰값
        String expectedToken = "a3c";

        URI uri = UriComponentsBuilder.newInstance()
                .path("/api/distribution")
                .build()
                .toUri();

        DistributeApiRequest mockReq = DistributeApiRequest.builder()
                .totalDistributeNum(totalDistributeNum)
                .totalMoney(totalMoney)
                .build();

        Header<DistributeApiResponse> mockRes = new Header<>();

        DistributeApiResponse resData = DistributeApiResponse.builder()
                .token(token)
                .build();
        mockRes.setData(resData);

        given(mainService.setDistribution(userId, roomId, mockReq)).willReturn(mockRes);

        mockMvc.perform(post(uri)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(objectMapper.writeValueAsString(mockReq))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value(expectedToken))
                .andDo(print());
    }

    /**
     * 토큰 요청값, 할당 금액 반환
     * 뿌리기 당 한번만 받을 수 있음, 뿌린 사람은 못받음, 동일 대화방만 가능, 10분 내에 받아야 함, 아직 할당되지 않은 값이 존재해야함
     *
    * @throws Exception
     */
    @Test
    @DisplayName("뿌리돈 받기 테스트")
    public void receiveDistributionTest() throws Exception {
        //mock request 값
        int userId = 2;
        String roomId= "abc";
        String token = "a3c";

        //mock response 값
        long allocatedMoney = 2500;

        //예상 응답 response 값
        long expectedAllocatedMoney = 2500;

        //요청에 대한 mock entity 값
        Distribution distribution = Distribution.builder()
                .userId(1)
                .roomId(roomId)
                .registeredAt(LocalDateTime.now().minusMinutes(5))
                .token(token)
                .totalDistributeNum(1)
                .totalMoney(allocatedMoney)
                .build();

        DistributionState distributionState = DistributionState.builder()
                .distribution(distribution)
                .allocatedStatus(AllocatedStatus.UNALLOCATED)
                .allocatedMoney(allocatedMoney)
                .token(token)
                .build();

        //제약조건 확인
        Assertions.assertNotEquals(distribution.getUserId(), userId); //뿌린 사람은 못받음
        Assertions.assertEquals(distribution.getRoomId(), roomId); //동일 대화방만 가능
        Assertions.assertFalse(LocalDateTime.now().isAfter(distribution.getRegisteredAt().plusMinutes(10))); //뿌리기 10분 내에 받아야함
        Assertions.assertNotEquals(distributionState.getAllocatedUserId(), userId); //뿌리기 당 한번만 받을 수 있음(확인)
        Assertions.assertEquals(distributionState.getAllocatedStatus(), AllocatedStatus.UNALLOCATED); //아직 할당되지 않은 값이 존재해야함

        URI uri = UriComponentsBuilder.newInstance()
                .path("/api/distribution/" + token)
                .build()
                .toUri();

        Header<ReceiveApiResponse> mockRes = new Header<>();

        ReceiveApiResponse resData = ReceiveApiResponse.builder()
                .allocatedMoney(allocatedMoney)
                .build();

        mockRes.setData(resData);

        given(mainService.receiveDistribution(userId, roomId, token)).willReturn(mockRes);

        mockMvc.perform(put(uri)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.allocated_money").value(expectedAllocatedMoney))
                .andDo(print());
    }

    @Test
    @DisplayName("뿌리기 조회 테스트")
    public void checkDistributionTest() throws Exception {

    }
}
