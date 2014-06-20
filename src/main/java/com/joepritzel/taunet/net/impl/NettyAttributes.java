package com.joepritzel.taunet.net.impl;

import io.netty.util.AttributeKey;

/**
 * Contains all references to AttributeKeys to be used by Netty.
 * 
 * @author Joe Pritzel
 *
 */
public class NettyAttributes {
	/**
	 * The AttributeKey for Connection IDs.
	 */
	public static final AttributeKey<String> idKey = AttributeKey.valueOf("id");
}
