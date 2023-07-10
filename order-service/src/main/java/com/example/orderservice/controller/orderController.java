package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrderVo;
import com.example.orderservice.vo.ResponseOrderVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
@Slf4j
public class orderController {
    private final OrderService orderService;
    private final ModelMapper mapper;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrderVo> createOrder(@PathVariable ("userId") String userId
            ,@RequestBody RequestOrderVo orderVo){
        log.info("Before added order data");
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto=mapper.map(orderVo, OrderDto.class);
        orderDto.setUserId(userId);

        OrderDto createOrder=orderService.createOrder(orderDto);
        ResponseOrderVo responseOrder =mapper.map(createOrder,ResponseOrderVo.class);

//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(orderVo.getQty() * orderVo.getUnitPrice());
//
        kafkaProducer.send("example-catalog-topic",orderDto);
//        orderProducer.send("orders", orderDto);
//
//        ResponseOrderVo responseOrder = mapper.map(orderDto, ResponseOrderVo.class);
        log.info("After added order data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrderVo>> getOrder(@PathVariable ("userId") String userId){
        log.info("Before retrieve order data");
        List<OrderDto> orderDtoList = orderService.getOrderByUserId(userId);

        List<ResponseOrderVo> result = new ArrayList<>();
        orderDtoList.forEach(object -> {
            result.add(mapper.map(object,ResponseOrderVo.class));
        });
        log.info("After retrieve order data");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
