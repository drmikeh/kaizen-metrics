package com.kaizencreek.metrics;

/**
 * Project: kaizen-metrics
 * User: drmikeh
 * Date: 10/30/13
 */
public abstract class AbstractMetric implements Metric {

    protected final MetricName metricName;

    public AbstractMetric(MetricName metricName) {
        this.metricName = metricName;
    }

    @Override
    public MetricName getMetricName() {
        return metricName;
    }
}