package com.xuecheng.teaching.convert;

import com.xuecheng.api.teaching.model.dto.CompanyDTO;
import com.xuecheng.teaching.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CompanyConvert {
    CompanyConvert INSTANCE = Mappers.getMapper(CompanyConvert.class);

    @Mapping(source = "id", target = "companyId")
    CompanyDTO entity2dto(Company entity);


    @Mappings({
            @Mapping(source = "companyId", target = "id"),
            @Mapping(target = "orgType", constant = CompanyDTO.ORG_TYPE_COMPANY)
    })
    Company dto2entity(CompanyDTO dto);

    List<CompanyDTO> entitys2dtos(List<Company> entitys);



}
