package com.example.catalogservice.Service;

import com.example.catalogservice.Entity.CatalogEntity;
import com.example.catalogservice.Entity.CatalogRepository;
import com.example.catalogservice.dto.CatalogDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class CatalogServiceImpl implements CatalogService{
    private final CatalogRepository catalogRepository;


    @Override
    public List<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
