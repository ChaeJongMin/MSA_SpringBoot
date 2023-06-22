package com.example.catalogservice.Service;

import com.example.catalogservice.Entity.CatalogEntity;
import com.example.catalogservice.dto.CatalogDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CatalogService {
    List<CatalogEntity> getAllCatalogs();


}
