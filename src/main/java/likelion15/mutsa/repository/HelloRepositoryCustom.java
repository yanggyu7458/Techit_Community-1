package likelion15.mutsa.repository;

import likelion15.mutsa.dto.HelloResponse;

import java.util.List;

public interface HelloRepositoryCustom {
    List<HelloResponse> findByHello();
}
