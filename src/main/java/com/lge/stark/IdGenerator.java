package com.lge.stark;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

	private static final int MAX_32_BIT_SIGNED_INTEGER = 0x7FFFFFFF;

	private final AtomicInteger generator;
	private static final Random RANDOM = new Random();

	static {
		RANDOM.setSeed((new Date()).getTime());
	}

	public static String newId() {
		return UUID.randomUUID().toString();
	}

	public IdGenerator(int initVal) {
		generator = new AtomicInteger(initVal);
	}

	public int nextInteger() {
		return (generator.getAndAdd(1) & MAX_32_BIT_SIGNED_INTEGER);
	}

	public int nextEvenNumber() {
		return (generator.getAndAdd(2) & MAX_32_BIT_SIGNED_INTEGER);
	}

}