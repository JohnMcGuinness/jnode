/*
 * $Id$
 */
package org.jnode.driver.video.nvidia;

import org.jnode.driver.Device;
import org.jnode.driver.DeviceException;
import org.jnode.driver.DriverException;
import org.jnode.driver.pci.PCIDevice;
import org.jnode.driver.video.AbstractFrameBufferDriver;
import org.jnode.driver.video.AlreadyOpenException;
import org.jnode.driver.video.FrameBufferConfiguration;
import org.jnode.driver.video.HardwareCursorAPI;
import org.jnode.driver.video.Surface;
import org.jnode.driver.video.UnknownConfigurationException;
import org.jnode.driver.video.ddc.DisplayDataChannelAPI;
import org.jnode.system.ResourceNotFreeException;

/**
 * @author Ewout Prangsma (epr@users.sourceforge.net)
 */
public class NVidiaDriver extends AbstractFrameBufferDriver implements NVidiaConstants {

	private FrameBufferConfiguration currentConfig;
	private NVidiaCore kernel;
	private final int architecture;
	private final String model;

	private static final FrameBufferConfiguration[] CONFIGS = new FrameBufferConfiguration[] { 
		NVidiaConfiguration.VESA_118, 
		NVidiaConfiguration.VESA_115 };

	/**
	 * Create a new instance
	 */
	public NVidiaDriver(int architecture, String model) {
		this.architecture = architecture;
		this.model = model;
	}

	/**
	 * @see org.jnode.driver.video.FrameBufferAPI#getConfigurations()
	 */
	public final FrameBufferConfiguration[] getConfigurations() {
		return CONFIGS;
	}

	/**
	 * @see org.jnode.driver.video.FrameBufferAPI#getCurrentConfiguration()
	 */
	public final FrameBufferConfiguration getCurrentConfiguration() {
		return currentConfig;
	}

	/**
	 * @see org.jnode.driver.video.FrameBufferAPI#open(org.jnode.driver.video.FrameBufferConfiguration)
	 */
	public synchronized Surface open(FrameBufferConfiguration config) throws UnknownConfigurationException, AlreadyOpenException, DeviceException {
		for (int i = 0; i < CONFIGS.length; i++) {
			if (config.equals(CONFIGS[i])) {
				kernel.open((NVidiaConfiguration) config);
				this.currentConfig = config;
				return kernel;
			}
		}
		throw new UnknownConfigurationException();
	}

	/**
	 * Notify of a close of the graphics object
	 * @param graphics
	 */
	final synchronized void close(NVidiaCore graphics) {
		this.currentConfig = null;
	}
	/**
	 * @see org.jnode.driver.Driver#startDevice()
	 */
	protected void startDevice() throws DriverException {
		try {
			kernel = new NVidiaCore(this, architecture, model, (PCIDevice) getDevice());
		} catch (ResourceNotFreeException ex) {
			throw new DriverException(ex);
		}
		super.startDevice();
		final Device dev = getDevice();
		dev.registerAPI(DisplayDataChannelAPI.class, kernel);
		dev.registerAPI(HardwareCursorAPI.class, kernel.getHardwareCursor());
	}

	/**
	 * @see org.jnode.driver.Driver#stopDevice()
	 */
	protected void stopDevice() throws DriverException {
		if (currentConfig != null) {
			kernel.close();
		}
		if (kernel != null) {
			kernel.release();
			kernel = null;
		}
		final Device dev = getDevice();
		dev.unregisterAPI(DisplayDataChannelAPI.class);
		dev.unregisterAPI(HardwareCursorAPI.class);
		super.stopDevice();
	}

}
