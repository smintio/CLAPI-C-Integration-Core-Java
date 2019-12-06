// MIT License
//
// Copyright (c) 2019 Smint.io GmbH
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
// documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice (including the next paragraph) shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// SPDX-License-Identifier: MIT

package io.smint.clapi.consumer.integration.j2ee;

import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Singleton;

import io.smint.clapi.consumer.integration.core.services.IPlatformScheduler;
import io.smint.clapi.consumer.integration.core.services.impl.AbstractScheduler;


/**
 * Implements a scheduler based on {@link TimerService} to be used with Enterprise Java Beans.
 *
 * <p>
 * This scheduler relies on creation upon startup of the servlet/container. As soon as the instance is created by the
 * container, {@link #startup()} is called, which stores a reference to {@code this} bean to a static variable. Its
 * value can be retrieved with {@code #getSchedulerBean()} to acquire access to the scheduler as created by the J2EE
 * container.
 * </p>
 *
 * @see <a href="https://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html">
 *      https://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html</a>
 */
// see
// https://stackoverflow.com/questions/3468150/using-special-auto-start-servlet-to-initialize-on-startup-and-share-application/
@Startup
@Singleton
public class EjbScheduler extends AbstractScheduler<J2eeSchedulerJobData> implements IPlatformScheduler {


    private static final Logger LOG = Logger.getLogger(EjbScheduler.class.getName());

    private static EjbScheduler SCHEDULER_SINGLETON = null;

    @Resource
    private TimerService _timerService;


    /**
     * Returns the singleton scheduler bean as created by the J2EE container and stored with {@link #startup()}.
     *
     * @return the scheduler bean as saved after creation by the J2EE container or {@code null} if the bean has not been
     *         created yet.
     */
    public static EjbScheduler getSchedulerBean() {
        return SCHEDULER_SINGLETON;
    }


    /**
     * Stores a reference to {@code this}, which can be retrieved with {@link #getSchedulerBean()}.
     *
     * <p>
     * In case a second instance of this class is created, the reference to these additional instances are not stored.
     * Only a singleton is supported.
     * </p>
     */
    @PostConstruct
    public void startup() {
        if (SCHEDULER_SINGLETON == null) {
            SCHEDULER_SINGLETON = this;
        }
    }


    /**
     * Cancels all timers.
     *
     * <p>
     * With EJB timers are persistent and resurrect in case the application is stopped and started again. This would not
     * work with this class, as the actual code to execute with the timer is stored as {@code Runnable} inside a big
     * {@link java.util.Map}. This data will get lost in application unload from memory. Hence it will not be available
     * upon next start of the application. Therefore there is no use in persistent timers and so all timers are
     * cancelled prior to shutting down the application.
     * </p>
     */
    @PreDestroy
    public void shutdown() {
        final Collection<Timer> allTimers = this._timerService.getTimers();
        if (allTimers != null) {
            for (final Timer timer : allTimers) {
                timer.cancel();
            }
        }
    }


    @Timeout
    public void timerFired(final Timer timer) {

        if (timer != null && timer.getInfo() != null) {

            final J2eeSchedulerJobData jobData = this.getJob(timer.getInfo().toString());
            if (jobData != null && jobData.job != null) {

                // CHECKSTYLE OFF: IllegalCatch
                try {
                    jobData.job.run();
                } catch (final Exception ingore) {
                    LOG.log(Level.SEVERE, "Timed job failed to execute!", ingore);
                }
                // CHECKSTYLE ON: IllegalCatch

            } else {
                this.removeJob(jobData.jobKey);
                timer.cancel();
            }

        } else {
            timer.cancel();
        }
    }


    @Override
    public String scheduleAtFixedRate(final Runnable job, final long period) {
        LOG.entering(this.getClass().getName(), "scheduleAtFixedRate ", new Object[] { job, new Long(period) });

        Objects.requireNonNull(this._timerService, "No timer service has been injected by the J2EE container!");


        if (job == null || period < MINIMAL_PERIOD_MILLISECONDS) {
            LOG.finer("Ingoring invalid job and not scheduling it.");
            LOG.exiting(this.getClass().getName(), "scheduleAtFixedRate", null);
            return null;
        }


        LOG.finer("Scheduling a new job.");
        final J2eeSchedulerJobData jobInfo = new J2eeSchedulerJobData();
        jobInfo.job = job;

        jobInfo.jobKey = this.putJob(jobInfo);
        jobInfo.timer = this._timerService.createTimer(0, period, jobInfo.jobKey);

        LOG.exiting(this.getClass().getName(), "'scheduleAtFixedRate'", jobInfo.jobKey);
        return jobInfo.jobKey;
    }


    @Override
    public IPlatformScheduler stopSchedule(final String jobKey) {
        LOG.entering(this.getClass().getName(), "stopSchedule ", new Object[] { jobKey });

        if (jobKey != null && !jobKey.isEmpty()) {

            final J2eeSchedulerJobData scheduledJob = this.removeJob(jobKey);
            if (scheduledJob != null && scheduledJob.timer != null) {

                LOG.finer(() -> "Stopping job with key >>" + scheduledJob.jobKey + "<<.");
                scheduledJob.timer.cancel();

            } else {
                LOG.warning(() -> "No job with key >>" + jobKey + "<< has been found.");
            }

        } else {
            LOG.finer(() -> "Job key to find job to stop is invalid: " + jobKey);
        }

        LOG.exiting(this.getClass().getName(), "'stopSchedule'", this);
        return this;
    }


    @Override
    public IPlatformScheduler cancel() {
        LOG.entering(this.getClass().getName(), "cancel");

        // cancel all schedules
        for (final String key : this.getAllJobKey()) {
            final J2eeSchedulerJobData jobInfo = this.removeJob(key);
            if (jobInfo.timer != null) {
                jobInfo.timer.cancel();
            }
        }

        LOG.exiting(this.getClass().getName(), "cancel ", this);
        return this;
    }

}

