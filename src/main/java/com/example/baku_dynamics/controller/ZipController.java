package com.example.baku_dynamics.controller;

import com.example.baku_dynamics.entity.JsonWrapper;
import com.example.baku_dynamics.entity.ZipEntity;
import com.example.baku_dynamics.service.ZipService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;



@RestController
public class ZipController {

    @Autowired
    private ZipService zipService;

    @PostMapping("/zip")
    public JsonWrapper getPath(@RequestBody ZipEntity zipEntity) {
        return new JsonWrapper(zipService.directoryZip(zipEntity));
    }

    @GetMapping("/status/{id}")
    public ZipEntity getStatus(@PathVariable int id){
        return zipService.getResult(id);
    }

}
