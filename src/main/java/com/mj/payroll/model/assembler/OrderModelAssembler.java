package com.mj.payroll.model.assembler;

import com.mj.payroll.controller.OrderController;
import com.mj.payroll.model.Order;
import com.mj.payroll.model.constant.Status;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order entity) {
        EntityModel<Order> entityModel = EntityModel.of(entity,
                linkTo(methodOn(OrderController.class).getOrderById(entity.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"));
        if (entity.getStatus().equals(Status.IN_PROGRESS)) {
            entityModel.add(linkTo(methodOn(OrderController.class).cancelOrderById(entity.getId())).withRel("cancel"));
            entityModel.add(linkTo(methodOn(OrderController.class).completeOrderById(entity.getId())).withRel("complete"));
        }
        return entityModel;
    }
}
