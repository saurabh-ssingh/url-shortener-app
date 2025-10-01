package io.shortlink.tinyurler.controller;

import io.shortlink.tinyurler.dto.ShortenUrlRequest;
import io.shortlink.tinyurler.dto.ShortenUrlResponse;
import io.shortlink.tinyurler.service.UrlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
    private final UrlService urlService;

    public PageController(UrlService urlService){
        this.urlService = urlService;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }


    @PostMapping("/shorten")
    public String shortenUrl(@ModelAttribute ShortenUrlRequest request, Model model){
        ShortenUrlResponse response = this.urlService.shortenUrl(request);
        model.addAttribute("response",response);
        return "index";
    }


}
