package com.bni.report.service;

import com.bni.report.entities.User;
import com.bni.report.entities.validators.Validator;
import com.bni.report.repositories.KegiatanRepository;
import com.bni.report.repositories.ValidatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ValidatorService {
    @Autowired
    private ValidatorRepository validatorRepository;
    @Autowired
    private KegiatanRepository kegiatanRepository;
    @Autowired
    private UserService userService;

    public Page<Validator> paginateGetALl(int currPage, int pageSize, String sortDirection, String sortField, String user) {
        User userGet = userService.findByName(user).orElseThrow(() -> new RuntimeException("not found"));
        Integer id = userGet.getKelompok().getId();
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currPage - 1, pageSize, sort);
        return getAll(pageable, id);
    }

    public Page<Validator> getAll(Pageable pageable, int kelompok) {
        List<Validator> collect = validatorRepository.findAll().stream()
                .filter(validator -> validator.getProgram().getBeban().getKelompok().getId() == kelompok)
                .collect(Collectors.toList());
        return new PageImpl<>(collect);
    }

    public Optional<Validator> findById(Integer id) {
        return validatorRepository.findById(id);
    }

    public Validator create(Validator validator) {
        return validatorRepository.save(validator);
    }

    public void delete(Integer id) {
        validatorRepository.deleteById(id);
    }
}
