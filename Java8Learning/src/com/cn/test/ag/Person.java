package com.cn.test.ag;

public interface Person {

	void test();
	default int operate(int a, int b){
		return a - b;
	}

}
