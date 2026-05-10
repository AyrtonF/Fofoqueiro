package com.security.fofoqueiro.application.common;

public interface IUseCase<I, O> {
    O execute(I input);
}
