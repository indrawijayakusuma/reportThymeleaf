package com.bni.report.service;

import com.bni.report.entities.Beban;
import com.bni.report.entities.Kegiatan;
import com.bni.report.entities.Kelompok;
import com.bni.report.entities.Validator;
import com.bni.report.repositories.BebanRepository;
import com.bni.report.repositories.KegiatanRepository;
import com.bni.report.repositories.KelompokRepository;
import com.bni.report.repositories.ValidatorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BebanService {
    @Autowired
    private BebanRepository bebanRepository;
    @Autowired
    private KegiatanRepository kegiatanRepository;
    @Autowired
    private KelompokRepository kelompokRepository;
    @Autowired
    private ValidatorRepository validatorRepository;
    @Autowired
    private KegiatanService kegiatanService;

    @Autowired
    private ValidatorService validatorService;

    public Page<Beban> getAll(Pageable pageable,Integer id){
        Page<Beban> all = bebanRepository.findByKelompokId(id, pageable);
        all.stream().map(beban -> {
            Integer idBeban = beban.getId();
            countSisa(idBeban);
            return beban;
        }).collect(Collectors.toList());
        return all;
    }

    public Beban findById(Integer id){
        countSisa(id);
        return bebanRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Beban create(Beban beban){
        return bebanRepository.save(beban);
    }

    public Beban edit(Beban beban){
        return bebanRepository.save(beban);
    }

    public void delete(Integer id){
        bebanRepository.deleteById(id);
    }

    public void countSisa(Integer id){
        Beban beban1 = bebanRepository.findById(id).orElse(new Beban());
        BigDecimal jumlahNominalKegiatan = kegiatanService.addNominalKegiatan(id);
        BigDecimal budget = beban1.getBudget();
        MathContext mc = new MathContext(10);
        beban1.setId(id);
        beban1.setRealisasi(jumlahNominalKegiatan);
        beban1.setSisa(budget.subtract(jumlahNominalKegiatan, mc));
        bebanRepository.save(beban1);
    }

    public Page<Beban> paginateGetAll(int currPage, int pageSize, String sortField, String sortDirection, Integer id){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currPage-1, pageSize, sort);
        return getAll(pageable, id);
    }

    public Page<Beban> paginateSearchingGetAll(int currPage, int pageSize, String sortField, String sortDirection, String keyword, Integer id){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(currPage-1, pageSize, sort);
        return bebanRepository.search(keyword, pageable, id);
    }

    @PostConstruct
    public void addbeban() {

        List<Kelompok> kelompokList = new ArrayList<>();
        kelompokList.add(new Kelompok("DCU"));
        kelompokList.add(new Kelompok("DEP"));
        kelompokList.add(new Kelompok("FPM"));
        kelompokList.add(new Kelompok("FPD 1"));
        kelompokList.add(new Kelompok("FPD 2"));
        kelompokList.add(new Kelompok("FPD 3"));


        List<Beban> bebanList = new ArrayList<>();
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","test", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));
        bebanList.add(new Beban("23424242424","beban penyuluhan", new BigDecimal(900000000), new Date(), new Kelompok(1)));


        List<Kegiatan> kegitanList = new ArrayList<>();
        kegitanList.add(new Kegiatan("kegitan olahrage", new Beban(1),"cat1","suni", new BigDecimal(800000), new Date()));
        kegitanList.add(new Kegiatan("kegitan senam", new Beban(1),"cat1","julian", new BigDecimal(89000), new Date()));
        kegitanList.add(new Kegiatan("kegitan gym", new Beban(1),"cat1","brian", new BigDecimal(324000), new Date()));
        kegitanList.add(new Kegiatan("kegitan kebugaran", new Beban(1),"cat1","hasim", new BigDecimal(24235000), new Date()));
        kegitanList.add(new Kegiatan("kegitan olahrage", new Beban(1),"cat1","suni", new BigDecimal(800000), new Date()));
        kegitanList.add(new Kegiatan("kegitan senam", new Beban(1),"cat1","julian", new BigDecimal(89000), new Date()));
        kegitanList.add(new Kegiatan("kegitan gym", new Beban(1),"cat1","brian", new BigDecimal(324000), new Date()));
        kegitanList.add(new Kegiatan("kegitan kebugaran", new Beban(1),"cat1","hasim", new BigDecimal(24235000), new Date()));
        kegitanList.add(new Kegiatan("kegitan olahrage", new Beban(1),"cat1","suni", new BigDecimal(800000), new Date()));
        kegitanList.add(new Kegiatan("kegitan senam", new Beban(1),"cat1","julian", new BigDecimal(89000), new Date()));
        kegitanList.add(new Kegiatan("kegitan gym", new Beban(1),"cat1","brian", new BigDecimal(324000), new Date()));
        kegitanList.add(new Kegiatan("kegitan kebugaran", new Beban(1),"cat1","hasim", new BigDecimal(24235000), new Date()));
        kegitanList.add(new Kegiatan("kegitan olahrage", new Beban(1),"cat1","suni", new BigDecimal(800000), new Date()));
        kegitanList.add(new Kegiatan("kegitan senam", new Beban(1),"cat1","julian", new BigDecimal(89000), new Date()));
        kegitanList.add(new Kegiatan("kegitan gym", new Beban(1),"cat1","brian", new BigDecimal(324000), new Date()));
        kegitanList.add(new Kegiatan("kegitan kebugaran", new Beban(1),"cat1","hasim", new BigDecimal(24235000), new Date()));
        kegitanList.add(new Kegiatan("kegitan olahrage", new Beban(1),"cat1","suni", new BigDecimal(800000), new Date()));
        kegitanList.add(new Kegiatan("kegitan senam", new Beban(1),"cat1","julian", new BigDecimal(89000), new Date()));
        kegitanList.add(new Kegiatan("kegitan gym", new Beban(1),"cat1","brian", new BigDecimal(324000), new Date()));
        kegitanList.add(new Kegiatan("kegitan kebugaran", new Beban(1),"cat1","hasim", new BigDecimal(24235000), new Date()));

        kegitanList.add(new Kegiatan("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        kegitanList.add(new Kegiatan("kegitan senam", new Beban(2),"cat1","julian", new BigDecimal(89000), new Date()));
        kegitanList.add(new Kegiatan("kegitan gym", new Beban(2),"cat1","brian", new BigDecimal(324000), new Date()));
        kegitanList.add(new Kegiatan("kegitan kebugaran", new Beban(2),"cat1","hasim", new BigDecimal(24235000), new Date()));
        kegitanList.add(new Kegiatan("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        kegitanList.add(new Kegiatan("kegitan senam", new Beban(2),"cat1","julian", new BigDecimal(89000), new Date()));
        kegitanList.add(new Kegiatan("kegitan gym", new Beban(2),"cat1","brian", new BigDecimal(324000), new Date()));
        kegitanList.add(new Kegiatan("kegitan kebugaran", new Beban(2),"cat1","hasim", new BigDecimal(24235000), new Date()));


        List<Validator> validatorList = new ArrayList<>();
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));
        validatorList.add(new Validator("kegitan olahrage", new Beban(2),"cat1","suni", new BigDecimal(800000), new Date()));

        kelompokRepository.saveAll(kelompokList);
        bebanRepository.saveAll(bebanList);
        kegiatanRepository.saveAll(kegitanList);
//        validatorRepository.saveAll(validatorList);
    }
}
