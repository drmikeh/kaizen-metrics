package com.kaizencreek.metrics

import spock.lang.Specification

/**
 * Project: kaizen-metrics
 * User: drmikeh
 * Date: 8/20/13
 */
class SimpleTimerSpec extends Specification {

    private Random randomGenerator = new Random(System.currentTimeMillis());

    protected def TOLERANCE_FACTOR = 0.10      // 10%

    def "test unstartedTimer"() {
        given: "A SimpleTimer that has not been started"
        def simpleTimer = new SimpleTimer();

        expect:
        simpleTimer.getStartTime() == 0
        simpleTimer.getEndTime() == 0
        simpleTimer.getElapsedTimeMillis() == 0
        simpleTimer.getDuration().getMillis() == 0
        simpleTimer.getMeanLatencyInMillis(0) == 0
        simpleTimer.getThroughput(0) == 0
    }

    def "test startedTimer"() {
        given: "A SimpleTimer that has been started"
        def simpleTimer = new SimpleTimer().start()

        expect: "getStartTime to return non-zero positive start time"
        simpleTimer.getStartTime() > 0
    }

    def "test endedTimer"() {
        given: "A SimpleTimer that has ended after 200ms"
        def simpleTimer = new SimpleTimer().start()
        def sleepTime = 200L
        sleep(sleepTime);
        simpleTimer.end();

        expect: "An elapsed time of approximately 200ms"
        def tolerance = sleepTime * TOLERANCE_FACTOR
        approximatelyEquals(simpleTimer.getEndTime() - simpleTimer.getStartTime(), sleepTime, tolerance)
        approximatelyEquals(simpleTimer.getElapsedTimeMillis(), sleepTime, 10)
        approximatelyEquals(simpleTimer.getDuration().getMillis(), sleepTime, 10)
        println("elapsed time = " + simpleTimer.getDuration().getMillis())
    }

    def "test reset"() {
        given: "A SimpleTimer that has been reset"

        when:
        def simpleTimer = new SimpleTimer().start()
        sleep(123)
        simpleTimer.end()
        simpleTimer.reset()

        then:
        simpleTimer.getStartTime() == 0
        simpleTimer.getEndTime() == 0
        simpleTimer.elapsedTimeMillis == 0
        simpleTimer.getElapsedTimeMillis() == 0
        simpleTimer.getDuration().getMillis() == 0
        simpleTimer.getMeanLatencyInMillis(0) == 0
        simpleTimer.getThroughput(0) == 0
    }

    def "test throughput meanLatency"() {
        given: "A SimpleTimer that has been started"
        SimpleTimer simpleTimer = new SimpleTimer().start()

        when: "Some amount of work has been done"
        def numTasks = 10
        def meanLatency = 50   // 50ms
        def delta = 5          // 5ms
        doSomeWork(simpleTimer, numTasks, meanLatency - delta, meanLatency + delta)

        then: "We should be able to measure the throughput"
        println "Throughput = ${simpleTimer.getThroughput(numTasks)}"
        def expectedThroughput = 1000.0 / meanLatency       // throughput = 1 / meanLatency (converted to seconds)
        def tolerance = expectedThroughput * TOLERANCE_FACTOR
        approximatelyEquals(simpleTimer.getThroughput(numTasks), expectedThroughput, tolerance)
        approximatelyEquals(simpleTimer.getMeanLatencyInMillis(numTasks), meanLatency, delta)
    }

    def "test getETA"() {
        given: "A SimpleTimer that has been started"
        SimpleTimer simpleTimer = new SimpleTimer().start()

        when: "Some amount of work has been done"
        def numTasks = 50
        def totalTasks = 500
        def meanLatency = 20   // 20ms
        def delta = 5          // 5ms
        doSomeWork(simpleTimer, numTasks, meanLatency - delta, meanLatency + delta)

        then: "We should get a good estimate for the ETA"
        def timeRemaining = (totalTasks - numTasks) * meanLatency
        def tolerance = timeRemaining * TOLERANCE_FACTOR
        def eta = simpleTimer.getETA(numTasks, totalTasks)
        println "eta = ${eta}"
        approximatelyEquals(eta.getMillis(), timeRemaining, tolerance)

        def etaTime = simpleTimer.getETATime(numTasks, totalTasks)
        def currentMillis = System.currentTimeMillis()
        println "etaTime = ${etaTime}"
        approximatelyEquals(etaTime.getMillis(), currentMillis + timeRemaining, tolerance)
    }

    def approximatelyEquals(double actualValue, double estimatedValue, double tolerance) {
        def diff = Math.abs(estimatedValue - actualValue)
        diff <= tolerance
    }

    def doSomeWork(SimpleTimer timer, int numTasks, minTaskTime, maxTaskTime) {
        for (int n = 0; n < numTasks; n++) {
            // uniform random numbers between minTaskTime and maxTaskTime
            int randomWaitMillis = minTaskTime + randomGenerator.nextInt(maxTaskTime - minTaskTime)
            sleep(randomWaitMillis)
        }
    }
}
