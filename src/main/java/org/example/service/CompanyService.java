package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.repository.CompanyRepository;
import org.example.dto.CompanyReadDto;
import org.example.mapper.CompanyReadMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyReadMapper companyReadMapper;

    public List<CompanyReadDto> findAll() {
        return companyRepository.findAll().stream()
                .map(companyReadMapper::map)
                .toList();
    }


}
