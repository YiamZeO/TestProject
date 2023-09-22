package com.example.usersdb.controllers;

import com.example.usersdb.dto.OrderDTO;
import com.example.usersdb.responseObjects.FilteringResponseObj;
import com.example.usersdb.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/orders")
@Validated
public class OrdersController {
    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/add_order")
    public void addOrder(@RequestBody @Valid OrderDTO orderDTO){
        ordersService.addOrder(orderDTO);
    }

    @PostMapping("/update_order")
    public void updateOrder(@RequestBody @Valid OrderDTO orderDTO){
        ordersService.updateOrder(orderDTO);
    }

    @DeleteMapping("/delete_order")
    public void deleteOrder(@RequestBody @NotNull Long orderId){
        ordersService.delOrder(orderId);
    }

    @GetMapping("/find_orders")
    public FilteringResponseObj findOrders(){
        return ordersService.findOrders();
    }
}
