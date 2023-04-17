package edu.ntnu.idatt2106.backend.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
@RequestMapping(value = "/api/items")
public class ItemController {
}
