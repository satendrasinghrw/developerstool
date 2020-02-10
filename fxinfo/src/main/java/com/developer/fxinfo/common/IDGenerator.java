package com.developer.fxinfo.common;


import java.util.concurrent.atomic.AtomicLong;

public final class IDGenerator {

	private static final AtomicLong idGen = new AtomicLong(System.nanoTime());

	private IDGenerator() {

	}

	public static long newId() {
		return idGen.getAndIncrement();
	}

}
