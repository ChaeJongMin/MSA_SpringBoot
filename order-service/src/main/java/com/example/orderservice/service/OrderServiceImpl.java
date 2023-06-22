package com.example.orderservice.service;

import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.domain.OrderRepository;
import com.example.orderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        int userIdLen = (int) (Math.random() * 5) + 6;
        String randomUserId= RandomStringUtils.random(userIdLen,true,true);

        orderDto.setOrderId(randomUserId);
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        orderRepository.save(mapper.map(orderDto, OrderEntity.class));

        return orderDto;

    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        return mapper.map(orderRepository.findByOrderId(orderId), OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrderByUserId(String userId) {
        List<OrderEntity> orderEntityList = orderRepository.findByUserId(userId);

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(orderEntityList, new TypeToken<List<OrderDto>>() {}.getType());
    }
}
