/**
 * <p>
 * <a href="https://www.smint.io">Smint.io</a> base library for synchronizing assets from our <em>Enterprise Buying
 * Platform</em> to downstream targets, like <a href="https://en.wikipedia.org/wiki/Digital_asset_management">Digital
 * Asset Management (DAM)</a> software.
 * </p>
 *
 * <p>
 * This base library makes integration of the Smint.io platform and synchronization of assets from Smint.io to any
 * target (one-way) a breeze. The basic infrastructure to run synchronization is provided by this library. What you need
 * to provide is special instances with some adaption to the specific synchronization target.
 * </p>
 *
 * <h2>What to implement to make it fly with a synchronization target</h2>
 * <p>
 * To make it work with a sync target, you need to implement the following interfaces:
 * </p>
 * <ul>
 * <li>{@link io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage}</li>
 * <li>{@link io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel}</li>
 * <li>{@link io.smint.clapi.consumer.integration.core.target.ISyncTarget}</li>
 * </ul>
 *
 * <h2>How to provide your implementation to the sync library: Factory or dependency injection</h2>
 * <p>
 * In order to provide your implementation to the synchronization library framework, you need to provide a factory,
 * implementing the interface {@link io.smint.clapi.consumer.integration.core.factory.ISyncTargetFactory}. There is a
 * default * implementation available as
 * {@link io.smint.clapi.consumer.integration.core.factory.impl.SyncTargetFactoryFromDI}. This class can be used even if
 * you do not have any dependency injection available. Nevertheless using any DI framework would be helpful. There is an
 * example how to use {@link io.smint.clapi.consumer.integration.core.factory.impl.SyncTargetFactoryFromDI} in the
 * documentation for that class.
 * </p>
 *
 * <h2>Initialize the synchronization process</h2>
 * <p>
 * Initialization is performed utilizing class {@link io.smint.clapi.consumer.integration.core.SmintIoSynchronization}.
 * </p>
 *
 * <pre>
 * ISmintIoSynchronization smintSync = new SmintIoSynchronization(
 *     new SyncFactoryFromDI(
 *         new MyAuthTokenStorage(),
 *         new MySettings(),
 *         () -&gt; new MySyncTargetImplementation()
 *     )
 * );
 * smintSync.startSchedule();
 * </pre>
 */
package io.smint.clapi.consumer.integration.core;
