/*
 * $Id$
 */
package org.jnode.net.ipv4.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

import org.apache.log4j.Logger;
import org.jnode.net.ipv4.IPv4Address;

/**
 * @author epr
 */
public class TCPSocketImpl extends SocketImpl {

	/** The protocol I'm using */
	private final TCPProtocol protocol;
	/** The control block */
	private TCPControlBlock controlBlock;
	/** The output stream */
	private TCPOutputStream os;
	/** The input stream */
	private TCPInputStream is;
	/** My logger */
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * Initialize a new instance
	 * 
	 * @param protocol
	 */
	public TCPSocketImpl(TCPProtocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * Accepts a connection on this socket.
	 * 
	 * @param s
	 *            The implementation object for the accepted connection.
	 * @see java.net.SocketImpl#accept(java.net.SocketImpl)
	 */
	protected void accept(SocketImpl s) throws IOException {
		log.debug("accept " + s);
		if (controlBlock == null) {
			throw new IOException("Not listening");
		}
		final TCPSocketImpl impl = (TCPSocketImpl)s;
		log.debug("accept: blocking");
		impl.controlBlock = controlBlock.appAccept();
		log.debug("accept: got one");
	}

	/**
	 * @see java.net.SocketImpl#available()
	 */
	protected final int available() throws IOException {
		return getInputStream().available();
	}

	/**
	 * @see java.net.SocketImpl#bind(java.net.InetAddress, int)
	 */
	protected void bind(InetAddress host, int port) throws IOException {
		if (controlBlock != null) {
			throw new IOException("Already bound");
		}
		controlBlock = protocol.bind(new IPv4Address(host), port);
	}

	/**
	 * @see java.net.SocketImpl#close()
	 */
	protected synchronized void close() throws IOException {
		if (is != null) {
			is.close();
		}
		if (os != null) {
			os.close();
		}
		if (controlBlock != null) {
			controlBlock.appClose();
			controlBlock = null;
		}
	}

	/**
	 * @see java.net.SocketImpl#connect(java.net.InetAddress, int)
	 */
	protected final void connect(InetAddress host, int port) throws IOException {
		connect(new InetSocketAddress(host, port), 0);
	}

	/**
	 * @see java.net.SocketImpl#connect(java.net.SocketAddress, int)
	 */
	protected void connect(SocketAddress address, int timeout) throws IOException {
		if (!(address instanceof InetSocketAddress)) {
			throw new IOException("InetSocketAddress expected");
		}
		final InetSocketAddress sa = (InetSocketAddress)address;
		if (controlBlock == null) {
			bind(InetAddress.getLocalHost(), 0);
		}
		controlBlock.appConnect(new IPv4Address(sa.getAddress()), sa.getPort());
	}

	/**
	 * @see java.net.SocketImpl#connect(java.lang.String, int)
	 */
	protected final void connect(String host, int port) throws IOException {
		connect(InetAddress.getByName(host), port);
	}

	/**
	 * @see java.net.SocketImpl#create(boolean)
	 */
	protected void create(boolean stream) throws IOException {
		// Do nothing yet
	}

	/**
	 * @see java.net.SocketImpl#getInputStream()
	 */
	protected InputStream getInputStream() throws IOException {
		if (controlBlock == null) {
			throw new IOException("Connect first");
		}
		if (is == null) {
			is = new TCPInputStream(controlBlock, this);
		}
		return is;
	}

	/**
	 * @see java.net.SocketOptions#getOption(int)
	 */
	public Object getOption(int option_id) throws SocketException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.net.SocketImpl#getOutputStream()
	 */
	protected OutputStream getOutputStream() throws IOException {
		if (controlBlock == null) {
			throw new IOException("Connect first");
		}
		if (os == null) {
			os = new TCPOutputStream(controlBlock, this);
		}
		return os;
	}

	/**
	 * Starts listening for connections on a socket. The backlog parameter is how many pending
	 * connections will queue up waiting to be serviced before being accept'ed. If the queue of
	 * pending requests exceeds this number, additional connections will be refused.
	 * 
	 * @param backlog
	 *            The length of the pending connection queue
	 * 
	 * @exception IOException
	 *                If an error occurs
	 * @see java.net.SocketImpl#listen(int)
	 */
	protected void listen(int backlog) throws IOException {
		if (controlBlock == null) {
			throw new IOException("Call bind first");
		}
		controlBlock.appListen();
	}

	/**
	 * @see java.net.SocketImpl#sendUrgentData(int)
	 */
	protected void sendUrgentData(int data) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.net.SocketOptions#setOption(int, java.lang.Object)
	 */
	public void setOption(int option_id, Object val) throws SocketException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.net.SocketImpl#shutdownInput()
	 */
	protected final void shutdownInput() throws IOException {
		getInputStream().close();
	}

	/**
	 * @see java.net.SocketImpl#shutdownOutput()
	 */
	protected final void shutdownOutput() throws IOException {
		getOutputStream().close();
	}

}
