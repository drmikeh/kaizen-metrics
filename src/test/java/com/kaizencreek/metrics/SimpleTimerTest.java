package com.kaizencreek.metrics;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTimerTest {

	private static final double TOLERANCE_FACTOR = 0.10;	// 10%

	@Test
	public void testUnstartedTimer() {

		// given: "A SimpleTimer that has not been started"
		SimpleTimer simpleTimer = new SimpleTimer();
		Assert.assertEquals(0, simpleTimer.getStartTime());
		Assert.assertEquals(0, simpleTimer.getEndTime());
		Assert.assertEquals(0, simpleTimer.getElapsedTimeMillis());
		Assert.assertEquals(0, simpleTimer.getDuration().getMillis());
		Assert.assertEquals(0, simpleTimer.getMeanLatencyInMillis(0), 0);
		Assert.assertEquals(0, simpleTimer.getThroughput(0), 0);
	}

	@Test
	public void testStartedTimer() {

		// given: "A SimpleTimer that has been started"
		SimpleTimer simpleTimer = new SimpleTimer().start();

		// expect: "getStartTime to return non-zero positive start time"
		Assert.assertTrue("getStartTime should be a non-zero positive value", simpleTimer.getStartTime() > 0);
	}

	@Test
	public void testEndedTimer() throws InterruptedException {

		// given: "A SimpleTimer that has ended after 200ms"
		SimpleTimer simpleTimer = new SimpleTimer().start();
		long sleepTime = 200L;
		Thread.sleep(sleepTime);
		simpleTimer.end();

		// expect: "An elapsed time of approximately 200ms"
		double tolerance = sleepTime * TOLERANCE_FACTOR;
		Assert.assertEquals(sleepTime, simpleTimer.getEndTime() - simpleTimer.getStartTime(), tolerance);
		Assert.assertEquals(sleepTime, simpleTimer.getElapsedTimeMillis(),    tolerance);
		Assert.assertEquals(sleepTime, simpleTimer.getDuration().getMillis(), tolerance);
	}

	@Test
	public void testReset() throws InterruptedException {
        // given: "A SimpleTimer that has ended after 200ms"
        SimpleTimer simpleTimer = new SimpleTimer().start();
        long sleepTime = 200L;
        Thread.sleep(sleepTime);
        simpleTimer.end();

        // expect: "An elapsed time of approximately 200ms"
        double tolerance = sleepTime * TOLERANCE_FACTOR;
        Assert.assertEquals(sleepTime, simpleTimer.getEndTime() - simpleTimer.getStartTime(), tolerance);
        Assert.assertEquals(sleepTime, simpleTimer.getElapsedTimeMillis(),    tolerance);
        Assert.assertEquals(sleepTime, simpleTimer.getDuration().getMillis(), tolerance);
        Assert.assertEquals(sleepTime, simpleTimer.getDuration().getMillis(), tolerance);
	}

}
