package com.kaizencreek.metrics;

/**
 * Project: kaizen-metrics
 * User: drmikeh
 * Date: 10/30/13
 */
public interface Metric {

    MetricName getMetricName();

    void reset();
}