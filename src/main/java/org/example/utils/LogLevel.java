package org.example.utils;

// enums work well with switch statements adn EnumMap
// This also gives a declaration order of values, we can use minimum-level filter if needed
public enum LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL;
}
