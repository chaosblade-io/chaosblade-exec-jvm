package com.alibaba.chaosblade.exec.common.context;

/**
 * A handle associated with a {@link TimerTask} that is returned by a {@link Timer}.
 */
public interface Timeout {

    /**
     * Returns the {@link Timer} that created this handle.
     */
    Timer timer();

    /**
     * Returns the {@link TimerTask} which is associated with this handle.
     */
    TimerTask task();

    /**
     * Returns {@code true} if and only if the {@link TimerTask} associated with this handle has been expired.
     */
    boolean isExpired();

    /**
     * Returns {@code true} if and only if the {@link TimerTask} associated with this handle has been cancelled.
     */
    boolean isCancelled();

    /**
     * Attempts to cancel the {@link TimerTask} associated with this handle. If the task has been executed or cancelled
     * already, it will return with no side effect.
     *
     * @return True if the cancellation completed successfully, otherwise false
     */
    boolean cancel();
}
