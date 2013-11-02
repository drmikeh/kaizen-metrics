package com.kaizencreek.metrics;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Project: kaizen-metrics
 * User: drmikeh
 * Date: 10/30/13
 *
 * A value class encapsulating a metric's scope and name.
 */
public class MetricName implements Comparable<MetricName> {

    private final String scope;

    private final String name;

    public MetricName(String scope, String name) {
        this.scope = scope;
        this.name = name;
    }

    public MetricName(Class<?> clazz, String name) {
        this(clazz.getName(), name);
    }

    public String getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        // return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        return scope + ':' + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetricName that = (MetricName) o;

        if (!scope.equals(that.scope)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scope.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(MetricName other) {
        int result = scope.compareTo(other.scope);
        if (result != 0) return result;
        return name.compareTo(other.name);
    }
}
