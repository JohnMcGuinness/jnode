/*
 * $Id$
 */
package org.jnode.fs.service.def;

import java.io.IOException;
import java.io.VMOpenMode;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.jnode.fs.FSFile;

/**
 * @author epr
 */
public class FileHandleManager {
	
	/** My logger */
	private final Logger log = Logger.getLogger(getClass());
	/** A map between File and FileData */
	public final HashMap openFiles = new HashMap();

	/**
	 * Create a filehandle for a given file entry.
	 * @param file
	 * @param mode
	 * @throws IOException
	 */
	public synchronized FileHandleImpl open(FSFile file, VMOpenMode mode) 
	throws IOException {
		FileData fd = (FileData)openFiles.get(file);
		if (fd == null) {
			fd = new FileData(file);
			final FileHandleImpl handle = fd.open(mode);
			openFiles.put(file, fd);
			return handle;
		} else {
			return fd.open(mode);
		}
	}
	
	/**
	 * Close a filehandle.
	 * @param handle
	 */
	public synchronized void close(FileHandleImpl handle) {
		final FSFile file = handle.getFile();
		final FileData fd = (FileData)openFiles.get(file);
		if (fd != null) {
			fd.close(handle);
			if (!fd.hasHandles()) {
				openFiles.remove(file);
			}
		} else {
			log.error("FileHandle tried to close an unknown file!!");
		}
	}
	
	/**
	 * Duplicate a filehandle.
	 * @param handle
	 */
	public synchronized FileHandleImpl dup(FileHandleImpl handle, VMOpenMode newMode) 
	throws IOException {
		final FSFile file = handle.getFile();
		final FileData fd = (FileData)openFiles.get(file);
		if (fd != null) {
			return fd.dup(handle, newMode);
		} else {
			throw new IOException("FileHandle tried to dup an unknown file!!");
		}
	}
	
	class FileData {
		
		/** My logger */
		private final Logger fdLog = Logger.getLogger(getClass());
		/** The actual file */
		private final FSFile file;
		/** Set of open filehandles */
		private final HashSet handles = new HashSet();
		/** Is any of the handles opened for write? */
		private boolean hasWriters;
		
		public FileData(FSFile file) {
			this.file = file;
		}
		
		/**
		 * Open an extra handle for this file.
		 * @param mode
		 * @throws IOException
		 */
		public FileHandleImpl open(VMOpenMode mode) 
		throws IOException {
			if (mode.canWrite()) {
				if (hasWriters) {
					throw new IOException("File is already open for writing");
				} else {
					hasWriters = true;
				}
			}
			final FileHandleImpl handle = new FileHandleImpl(file, mode, FileHandleManager.this);
			handles.add(handle);
			return handle;
		}
		
		/**
		 * Duplicate the given handle for this file.
		 * @param handle
		 */
		public FileHandleImpl dup(FileHandleImpl handle, VMOpenMode newMode) 
		throws IOException {
			if (handles.contains(handle)) {
				if (newMode.canWrite()) {
					if (hasWriters) {
						throw new IOException("File is already open for writing");
					} else {
						hasWriters = true;
					}
				}
				final FileHandleImpl newHandle;
				newHandle = new FileHandleImpl(file, newMode, FileHandleManager.this);
				handles.add(newHandle);
				return newHandle;				
			} else {
				throw new IOException("FileHandle is not known in FileData.close!!");
			}
		}
		
		/**
		 * Close the given handle for this file.
		 * @param handle
		 */
		public void close(FileHandleImpl handle) {
			if (handles.contains(handle)) {
				handles.remove(handle);
				if (handle.getMode().canWrite()) {
					hasWriters = false;
				}
			} else {
				fdLog.error("FileHandle is not known in FileData.close!!");
			}
		}
		
		/**
		 * Are there open handles for this file?
		 */
		public boolean hasHandles() {
			return !handles.isEmpty();
		}
	}
}
