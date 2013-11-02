package com.kaizencreek.metrics

import org.apache.log4j.spi.LoggerFactory
import org.slf4j.Logger
import spock.lang.Specification

/**
 * Project: kaizen-metrics
 * User: drmikeh
 * Date: 11/2/13
 */
class CounterSpec extends Specification {

    def logger = org.slf4j.LoggerFactory.getLogger(CounterSpec.class);

    def "test a new Counter"() {
        given: "A new Counter"
        def counter = new Counter(new MetricName(CounterSpec.class, "testCounter"));

        expect:
        logger.info 'counter = ' + counter.toString()
        counter.count() == 0
        counter.getMetricName().scope == "com.kaizencreek.metrics.CounterSpec"
        counter.getMetricName().name == "testCounter"
    }

    def "test incrementing a Counter"() {
        given: "An incremented Counter"
        def counter = new Counter(new MetricName(CounterSpec.class, "testCounter"));
        counter.inc();

        expect: "The counter to have a count of 1"
        logger.info 'counter = ' + counter.toString()
        counter.count() == 1
    }

    def "test decrementing a Counter"() {
        given: "An incremented Counter"
        def counter = new Counter(new MetricName(CounterSpec.class, "testCounter"));
        counter.inc();
        counter.dec();

        expect: "The counter to have a count of 0"
        logger.info 'counter = ' + counter.toString()
        counter.count() == 0
    }

    def "test incrementing a Counter by a specified amount"() {
        given: "An incremented Counter"
        def counter = new Counter(new MetricName(CounterSpec.class, "testCounter"));
        counter.inc(7);

        expect: "The counter to have a count of 7"
        logger.info 'counter = ' + counter.toString()
        counter.count() == 7
    }
}
