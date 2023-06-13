package likelion15.mutsa.controller;

import likelion15.mutsa.dto.HelloResponse;
import likelion15.mutsa.entity.Hello;
import likelion15.mutsa.service.HelloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class HelloController {
    public final HelloService helloService;

    @GetMapping("/hello")
    @ResponseBody
    public ModelAndView hello(Map<String, Object> repsonse) {
        Hello hi = helloService.save(Hello.builder()
                .name("hi")
                .build()
        );

        List<HelloResponse> byHello = helloService.findByHello();
        ModelAndView modelAndView = new ModelAndView("/hello");

        modelAndView.addObject("hello", byHello);

        log.info("test hello");
        return modelAndView;
    }

    @GetMapping("/join")
    public String join(){

        return "join";
    }
    @GetMapping("/login")
    public String login(){

        return "login";
    }
}
