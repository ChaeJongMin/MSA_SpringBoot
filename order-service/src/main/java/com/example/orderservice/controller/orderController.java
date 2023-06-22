package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrderVo;
import com.example.orderservice.vo.ResponseOrderVo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class orderController {
    private final OrderService orderService;
    private final ModelMapper mapper;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrderVo> createOrder(@PathVariable ("userId") String userId
            ,@RequestBody RequestOrderVo orderVo){
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto=mapper.map(orderVo, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createOrder=orderService.createOrder(orderDto);
        ResponseOrderVo responseOrderVo =mapper.map(createOrder,ResponseOrderVo.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrderVo);

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrderVo>> getOrder(@PathVariable ("userId") String userId){
        List<OrderDto> orderDtoList = orderService.getOrderByUserId(userId);

        List<ResponseOrderVo> result = new ArrayList<>();
        orderDtoList.forEach(object -> {
            result.add(mapper.map(object,ResponseOrderVo.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
