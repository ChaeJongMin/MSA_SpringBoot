package com.example.catalogservice.Controller;

import com.example.catalogservice.Entity.CatalogEntity;
import com.example.catalogservice.Service.CatalogService;
import com.example.catalogservice.Vo.ResponseCatalog;
import com.example.catalogservice.dto.CatalogDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {
    private final Environment environment;
    private final CatalogService catalogService;
    @GetMapping("/health_check")
    public String status(){
        return String.format("카탈로그 서비스가 작동 중입니다. ( 포트번호: %s ))", environment.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs(){
        List<CatalogEntity> catalogList=catalogService.getAllCatalogs();

        List<ResponseCatalog> catalogsList=new ArrayList<>();
        catalogList.forEach(v -> {
            catalogsList.add(new ModelMapper().map(v,ResponseCatalog.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(catalogsList);
    }
}
