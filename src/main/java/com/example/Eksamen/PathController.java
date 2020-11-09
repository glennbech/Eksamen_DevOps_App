package com.example.Eksamen;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    @GetMapping(path = "/pr")
    public String pr(){
        return "Does the pull request and travis push work?";
    }

    @GetMapping(path = "/creator")
    public String creator(){
        return "Hello creator, does this work?! ";
    }

    @GetMapping(path = "/pong")
    public String pong(){
        return "ping is the word";
    }

    @GetMapping(path = "/pingpong")
    public String pingPong(){
        return "pingPong pingpong";
    }

}
