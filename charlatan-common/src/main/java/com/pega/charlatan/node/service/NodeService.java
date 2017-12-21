package com.pega.charlatan.node.service;


import com.pega.charlatan.node.bean.CreateMode;
import com.pega.charlatan.node.bean.Node;
import com.pega.charlatan.node.bean.NodeState;
import com.pega.charlatan.utils.CharlatanException;
import com.pega.charlatan.watches.bean.Watcher;

import java.util.List;

/**
 * Created by natalia on 7/19/17.
 */
public interface NodeService {

	void close(long session);

	/**
	 * Create a node with the given path and data.
	 * <p>
	 * The createMode argument can also specify to create a sequential node. The
	 * actual path name of a sequential node will be the given path plus a
	 * suffix "i" where i is the current sequential number of the node. The sequence
	 * number is always fixed length of 10 digits, 0 padded. Once
	 * such a node is created, the sequential number will be incremented by one.
	 * <p>
	 * If a node with the same actual path already exists in the ZooKeeper, a
	 * KeeperException with error code KeeperException.NodeExists will be
	 * thrown.
	 * <p>
	 * An ephemeral node cannot have children. If the parent node of the given
	 * path is ephemeral, a KeeperException with error code
	 * KeeperException.NoChildrenForEphemerals will be thrown.
	 * <p>
	 * If the parent node does not exist in the ZooKeeper, a KeeperException
	 * with error code KeeperException.NoNode will be thrown.
	 * <p>
	 * This operation, if successful, will trigger all the watches left on the
	 * node of the given path by exists and getData API calls, and the watches
	 * left on the parent node by getChildren API calls.
	 * <p>
	 * If a node is created successfully, the ZooKeeper server will trigger the
	 * watches on the path left by exists calls, and the watches on the parent
	 * of the node by getChildren calls.
	 *
	 * @param path       node path
	 * @param data       node data
	 * @param createMode node mode
	 * @return the actual path of the created node
	 */
	String create(long session, String path, byte[] data, CreateMode createMode) throws CharlatanException;

	/**
	 * Delete the node with the given path. The call will succeed if such a node
	 * exists, and the given version matches the node's version (if the given
	 * version is -1, it matches any node's versions).
	 * <p>
	 * A KeeperException with error code KeeperException.NoNode will be thrown
	 * if the nodes does not exist.
	 * <p>
	 * A KeeperException with error code KeeperException.BadVersion will be
	 * thrown if the given version does not match the node's version.
	 * <p>
	 * A KeeperException with error code KeeperException.NotEmpty will be thrown
	 * if the node has children.
	 * <p>
	 * This operation, if successful, will trigger all the watches on the node
	 * of the given path left by exists API calls, and the watches on the parent
	 * node left by getChildren API calls.
	 *
	 * @param path    the path of the node to be deleted.
	 * @param version the expected node version.
	 * @throws CharlatanException If the server signals an error with a non-zero
	 *                         return code.
	 */
	void delete(String path, int version) throws CharlatanException;

	/**
	 * Return node including  its data and state.
	 * <p>
	 * If the watch is true and the call is successful (no exception is
	 * thrown), a watch will be left on the node with the given path. The watch
	 * will be triggered by a successful operation that corresponds Watcher.Type.
	 * <p>
	 * A CharlatanException with error code CharlatanException.NoNode will be thrown
	 * if no node with the given path exists.
	 *
	 * @param path  the given path
	 * @return node details
	 * @throws CharlatanException If the server signals an error with a non-zero error code
	 */
	Node getNode(String path, Watcher watcher, Watcher.Type watchType) throws CharlatanException;

	/**
	 * Set the data for the node of the given path if such a node exists and the
	 * given version matches the version of the node (if the given version is
	 * -1, it matches any node's versions). Return the stat of the node.
	 * <p>
	 * This operation, if successful, will trigger all the watches on the node
	 * of the given path left by getData calls.
	 * <p>
	 * A KeeperException with error code KeeperException.NoNode will be thrown
	 * if no node with the given path exists.
	 * <p>
	 * A KeeperException with error code KeeperException.BadVersion will be
	 * thrown if the given version does not match the node's version.
	 *
	 * @param path    the path of the node
	 * @param data    the data to set
	 * @param version the expected matching version
	 * @return the state of the node
	 * @throws CharlatanException If the server signals an error with a non-zero error code.
	 */
	NodeState setData(String path, byte[] data, int version) throws CharlatanException;

	/**
	 * Return the stat of the node of the given path. Return null if no such a
	 * node exists.
	 * <p>
	 * If the watcher is set and the call is successful (no exception is thrown),
	 * a watch will be left on the node with the given path. The watch will be
	 * triggered by a successful operation that creates/delete the node or sets
	 * the data on the node.
	 *
	 * @param path  the node path
	 * @param watcher whether need to watch this node
	 * @return the stat of the node of the given path; return null if no such a
	 * node exists.
	 */
	NodeState exists(String path, Watcher watcher);

	void removeEphemeralSessionNodes(long session);

	void registerWatch(Watcher watcher, List<String> dataWatches, List<String> childWatches, List<String> existWatches);
}
