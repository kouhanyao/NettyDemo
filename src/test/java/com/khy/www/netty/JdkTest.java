package com.khy.www.netty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JdkTest {
    public static void main(String[] args) {
        List<String> cost = Arrays.asList("java","scala","python");//Arrays.asList(10.0, 20.0,30.0);
        cost.stream().filter(x->{return (x.equals("java") || x.equals("scala"));}).collect(Collectors.toList()).forEach(x-> System.out.println(x));

    }
}
