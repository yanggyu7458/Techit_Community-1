package likelion15.mutsa.controller;

import likelion15.mutsa.dto.HelloResponse;
import likelion15.mutsa.entity.Hello;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.repository.CommentRepository;
import likelion15.mutsa.service.BoardService;
import likelion15.mutsa.service.HelloService;
import likelion15.mutsa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class HelloController {
    public final HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        Hello hi = helloService.save(Hello.builder()
                .name("hi")
                .build()
        );

        List<HelloResponse> byHello = helloService.findByHello();
        ModelAndView modelAndView = new ModelAndView("/hello");

        modelAndView.addObject("hello", byHello);

        log.info("test hello");
        return "/hello";
    }


}