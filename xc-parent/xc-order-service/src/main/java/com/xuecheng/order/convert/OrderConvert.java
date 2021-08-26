package com.xuecheng.order.convert;

import com.xuecheng.api.order.model.dto.OrdersDTO;
import com.xuecheng.order.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderConvert {

	OrderConvert INSTANCE = Mappers.getMapper(OrderConvert.class);

	@Mappings({
			@Mapping(source = "id", target = "orderId"),
	})
	OrdersDTO entity2dto(Orders entity);

	@Mapping(source = "orderId", target = "id")
    Orders dto2entity(OrdersDTO dto);

	List<OrdersDTO> entitys2dtos(List<Orders> list);

}