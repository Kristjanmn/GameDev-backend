package io.nqa.gamedev.service;

import java.util.List;

public interface IGlobalService {

    boolean isNull(Object ... objects);

    boolean isBlank(String ... args);

    boolean isEmpty(List... lists);
}
