package likelion15.mutsa.service;

import jakarta.transaction.Transactional;
import likelion15.mutsa.dto.HelloResponse;
import likelion15.mutsa.entity.Hello;
import likelion15.mutsa.repository.HelloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HelloService {

    private final HelloRepository helloRepository;

    public Hello save(Hello hello){
        return helloRepository.save(hello);
    }

    public List<HelloResponse> findByHello() {
        return helloRepository.findByHello();
    }

}
