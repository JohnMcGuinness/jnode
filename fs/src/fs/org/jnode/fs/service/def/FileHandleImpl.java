/*
 * $Id$
 */
package org.jnode.fs.service.def;

import java.io.VMFileHandle;
import java.io.IOException;
import java.io.VMOpenMode;

import org.jnode.fs.FSFile;

/**
 * @author epr
 */
public class FileHandleImpl implements VMFileHandle {

	/** The open mode of this filehandle */
	private final VMOpenMode mode;
	/** The actual file on the filesystem */
	private final FSFile file;
	/** Is this a readonly connection? */
	private final boolean readOnly;
	/** The manager i'll use to close me */
	private final FileHandleManager fhm;
	/** Am i closed? */
	private boolean closed;
	/** Position within this file */
	private long fileOffset;
	
	/**
	 * Create a new instance
	 * @param file
	 * @param mode
	 * @param fhm
	 * @throws IOException
	 */
	public FileHandleImpl(FSFile file, VMOpenMode mode, FileHandleManager fhm) 
	throws IOException {
		this.mode = mode;
		this.file = file;
		this.readOnly = (mode == VMOpenMode.READ);
		this.fhm = fhm;
		this.closed = false;
	}

	/**
	 * Gets the length (in bytes) of this file
	 * @return long
	 */
	public synchronized long getLength() {
		if (closed) {
			return 0;
		}
		return file.getLength();
	}
	
	/**
	 * Sets the length of this file.
	 * @param length
	 * @throws IOException
	 */
	public synchronized void setLength(long length)
	throws IOException {
		if (closed) {
			throw new IOException("File closed");
		}
		if (readOnly) {
			throw new IOException("Cannot write");
		}
		file.setLength(length);
		if (length > fileOffset) {
			fileOffset = length;
		}
	}

	/**
	 * Gets the current position in the file
	 * @return long
	 */
	public long getPosition() {
		return fileOffset;
	}
	
	/**
	 * Sets the position in the file.
	 * @param position
	 * @throws IOException
	 */
	public void setPosition(long position)
	throws IOException {
		if (position < 0) {
			throw new IOException("Position < 0");
		} 
		if (position > getLength()) {
			throw new IOException("Position > file size");
		}
		this.fileOffset = position;
	}

	/**
	 * Read <code>len</code> bytes from the given position.
	 * The read data is read fom this file starting at offset <code>fileOffset</code>
	 * and stored in <code>dest</code> starting at offset <code>ofs</code>.
	 * @param dest
	 * @param off
	 * @param len
	 * @throws IOException
	 */	
	public synchronized void read(byte[] dest, int off, int len)
	throws IOException {
		if (closed) {
			throw new IOException("File closed");
		}
		file.read(fileOffset, dest, off, len);
		fileOffset += len;
	}
	
	/**
	 * Write <code>len</code> bytes to the given position. 
	 * The data is read from <code>src</code> starting at offset
	 * <code>ofs</code> and written to this file starting at offset <code>fileOffset</code>.
	 * @param src
	 * @param off
	 * @param len
	 * @throws IOException
	 */	
	public synchronized void write(byte[] src, int off, int len)
	throws IOException {
		if (closed) {
			throw new IOException("File closed");
		}
		if (readOnly) {
			throw new IOException("Cannot write");
		}
		file.write(fileOffset, src, off, len);
		fileOffset += len;
	}

	/**
	 * Close this file.
	 */
	public synchronized void close() {
		closed = true;
		fhm.close(this);
	}
	
	
	/**
	 * Has this handle been closed?
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * Duplicate this handle
	 * @throws IOException
	 */
	public VMFileHandle dup(VMOpenMode newMode)
	throws IOException {
		return fhm.dup(this, newMode);
	}
	
	/**
	 * Gets the file of this handle
	 */
	public FSFile getFile() {
		return file;
	}

	/**
	 * Gets the mode of this handle
	 */
	public VMOpenMode getMode() {
		return mode;
	}

	/**
	 * Is this handle readonly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

}
