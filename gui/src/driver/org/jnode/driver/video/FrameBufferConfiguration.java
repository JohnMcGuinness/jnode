/*
 * $Id$
 */
package org.jnode.driver.video;

import java.awt.image.ColorModel;

import org.jnode.awt.image.JNodeBufferedImage;

/**
 * @author epr
 */
public abstract class FrameBufferConfiguration {

	private final int width;
	private final int height;
	private final ColorModel colorModel;
	
	public FrameBufferConfiguration(int width, int height, ColorModel colorModel) {
		this.colorModel = colorModel;
		this.height = height;
		this.width = width;
	}

	/**
	 * Gets the width of the screen in pixels
	 */
	public int getScreenWidth() {
		return width;
	}

	/**
	 * Gets the height of the screen in pixels
	 */
	public int getScreenHeight() {
		return height;
	}
	
	/**
	 * Gets the color model
	 */
	public ColorModel getColorModel() {
		return colorModel;
	}
	
	/**
	 * Returns a BufferedImage that supports the specified transparency 
	 * and has a data layout and color model compatible with this device. 
	 * This method has nothing to do with memory-mapping a device. 
	 * The returned BufferedImage has a layout and color model that 
	 * can be optimally blitted to this device. 
	 * @see java.awt.Transparency#BITMASK
	 * @see java.awt.Transparency#OPAQUE
	 * @see java.awt.Transparency#TRANSLUCENT
	 */
	public abstract JNodeBufferedImage createCompatibleImage(int w, int h, int transparency);	
}
