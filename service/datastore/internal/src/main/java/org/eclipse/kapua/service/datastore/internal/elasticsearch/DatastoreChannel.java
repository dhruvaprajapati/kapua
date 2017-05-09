/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Models a topic for messages posted to the Kapua platform.<br>
 * Topic are expected to be in the form of "account/clientId/&lt;application_specific&gt;";
 * system topic starts with the $EDC account.
 *
 * @since 1.0.0
 */
public class DatastoreChannel {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(DatastoreChannel.class);

    public static final int MIN_PARTS = 3;
    /**
     * Default topic parts separator
     */
    public static final String TOPIC_SEPARATOR = "/";
    /**
     * Default multiple level wild card
     */
    public static final String MULTI_LEVEL_WCARD = "#";

    private static final String MULTI_LEVEL_SEPARATOR_WCARD = TOPIC_SEPARATOR + MULTI_LEVEL_WCARD;

    /**
     * Default single level wild card
     */
    public static final String SINGLE_LEVEL_WCARD = "+";

    public static final String ALERT_TOPIC = "ALERT";

    private KapuaId scopeId;
    private String clientId;
    private String channel;
    private String[] channelParts;

    private void init(KapuaId scopeId, String clientId, String channel) throws EsInvalidChannelException {

        // Must be not null
        if (scopeId == null) {
            throw new EsInvalidChannelException("Empty scopeId.");
        }
        // Must be not multilevel wild card
        if (MULTI_LEVEL_WCARD.equals(scopeId.toCompactId())) {
            throw new EsInvalidChannelException("Invalid scopeId: " + scopeId.toCompactId());
        }

        this.scopeId = scopeId;

        // Must be not null and not multilevel wild card
        if (clientId == null || MULTI_LEVEL_WCARD.equals(clientId)) {
            throw new EsInvalidChannelException("Invalid client id: " + clientId);
        }

        this.clientId = clientId;

        // Must be not null and not single level wild card
        if (channel == null) {
            throw new EsInvalidChannelException("Invalid channel: " + channel);
        }

        // Check if there is one single level wild card or if the multi level wild card is present more than once or not at the end of the topic
        if (channel.indexOf(SINGLE_LEVEL_WCARD) != -1) {
            throw new EsInvalidChannelException(String.format("Invalid channel [%s]. The channel cannot contain [%s] wildcard!", channel, SINGLE_LEVEL_WCARD));
        }

        int indexOfMultiLevelWildCard = channel.indexOf(MULTI_LEVEL_WCARD);
        if (indexOfMultiLevelWildCard != -1) {
            if (indexOfMultiLevelWildCard < channel.length() - 1) {
                throw new EsInvalidChannelException(String.format("Invalid channel [%s]. The channel [%s] wildcard is allowed only at the end of the channel!", channel, MULTI_LEVEL_WCARD));
            }
        }

        this.channel = channel;
        channelParts = this.channel.split(TOPIC_SEPARATOR);

        if (channelParts.length < 1) {
            // Special case: The topic is too small
            throw new EsInvalidChannelException(channel);
        }
    }

    /**
     * Construct a datastore channel given the account, client identifier and the list of the channel parts.
     *
     * @param scopeId
     * @param clientId
     * @param channelParts
     * @throws EsInvalidChannelException
     * @since 1.0.0
     */
    public DatastoreChannel(KapuaId scopeId, String clientId, List<String> channelParts) throws EsInvalidChannelException {
        this(scopeId, clientId, new Object() {

            @Override
            public String toString() {
                return getChannel(channelParts);
            }
        }.toString());
    }

    /**
     * Construct a datastore channel given the account, client identifier and the full channel string.
     *
     * @param scopeId
     * @param clientId
     * @param channel
     * @throws EsInvalidChannelException
     * @since 1.0.0
     */
    public DatastoreChannel(KapuaId scopeId, String clientId, String channel) throws EsInvalidChannelException {
        init(scopeId, clientId, channel);
    }

    /**
     * Construct a datastore channel given full channel string.
     *
     * @param fullName
     * @throws EsInvalidChannelException
     */
    public DatastoreChannel(String fullName) throws EsInvalidChannelException {

        // Must be not null and not multilevel wild card
        if (fullName == null) {
            throw new EsInvalidChannelException("Invalid channel: " + fullName);
        }

        String[] parts = fullName.split(Pattern.quote(TOPIC_SEPARATOR));
        if (parts.length < MIN_PARTS) {
            throw new EsInvalidChannelException(String.format("Invalid channel: less than %d parts found.", MIN_PARTS));
        }

        // init(parts[0], parts[1], fullName.substring(parts[0].length() + parts[1].length() + 2));
    }

    /**
     * Get the account
     *
     * @return
     * @since 1.0.0
     */
    public String getAccount() {
        return null;
    }

    /**
     * Check if the channel admit any account (so if the channel starts with a specific wildcard).<br>
     * In the MQTT word this method return true if the topic starts with '+/'.
     *
     * @return
     * @since 1.0.0
     */
    public boolean isAnyAccount() {
        return channel.startsWith(SINGLE_LEVEL_WCARD);
    }

    /**
     * Get the client identifier
     *
     * @return
     * @since 1.0.0
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Check if the channel admit any client identifier (so if the channel has a specific wildcard in the second topic level).<br>
     * In the MQTT word this method return true if the topic starts with 'account/+/'.
     *
     * @param clientId
     * @return
     * @since 1.0.0
     */
    public static boolean isAnyClientId(String clientId) {
        return SINGLE_LEVEL_WCARD.equals(clientId);
    }

    /**
     * {@link DatastoreChannel#isAnyClientId(String clientId)}
     *
     * @return
     * @since 1.0.0
     */
    public boolean isAnyClientId() {
        return isAnyClientId(clientId);
    }

    /**
     * Check if the channel is an alert channel, so if it has 'ALERT' as third level topic (and no more topics level).<br>
     * In the MQTT word this method return true if the topic is like 'account/client/ALERT'.
     *
     * @param channel
     * @return
     * @since 1.0.0
     */
    public static boolean isAlertTopic(String channel) {
        return ALERT_TOPIC.equals(channel);
    }

    /**
     * {@link DatastoreChannel#isAlertTopic(String channel)}
     *
     * @return
     * @since 1.0.0
     */
    public boolean isAlertTopic() {
        return isAlertTopic(channel);
    }

    /**
     * Return true if the channel is single path channel and the last channel part is the {@link DatastoreChannel#MULTI_LEVEL_WCARD} char.
     *
     * @param channel
     * @return
     * @since 1.0.0
     */
    public static boolean isAnyChannel(String channel) {
        return channel != null && MULTI_LEVEL_WCARD.equals(channel);
    }

    /**
     * Return true if the channel is multi path channel and the last channel part is the {@link DatastoreChannel#MULTI_LEVEL_WCARD} char.<br>
     * <b>This method returns false if the channel is {@link DatastoreChannel#MULTI_LEVEL_WCARD}.</b>
     *
     * @param channel
     * @return
     * @since 1.0.0
     */
    public static boolean isWildcardChannel(String channel) {
        return channel != null && channel.endsWith(MULTI_LEVEL_SEPARATOR_WCARD);
    }

    /**
     * Get the channel formatted string given the topic parts
     *
     * @param parts
     * @return
     * @since 1.0.0
     */
    public static String getChannel(final List<String> parts) {
        return String.join(DatastoreChannel.TOPIC_SEPARATOR, parts);
    }

    /**
     * Get the channel
     *
     * @return
     * @since 1.0.0
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Get the last topic part (leaf)
     *
     * @return
     * @since 1.0.0
     */
    public String getLeafName() {
        return channelParts[channelParts.length - 1];
    }

    /**
     * Get the parent topic
     *
     * @return
     * @since 1.0.0
     */
    public String getParentTopic() {
        int iLastSlash = channel.lastIndexOf(TOPIC_SEPARATOR);
        return iLastSlash != -1 ? channel.substring(0, iLastSlash) : null;
    }

    /**
     * Get the grand parent topic
     *
     * @return
     * @since 1.0.0
     */
    public String getGrandParentTopic() {
        String parentTopic = getParentTopic();
        if (parentTopic != null) {
            int iLastSlash = parentTopic.lastIndexOf(TOPIC_SEPARATOR);
            return iLastSlash != -1 ? parentTopic.substring(0, iLastSlash) : null;
        } else {
            return null;
        }
    }

    /**
     * Get the channel parts
     *
     * @return
     * @since 1.0.0
     */
    public String[] getParts() {
        return channelParts;
    }

    /**
     * Get the full channel name
     *
     * @return
     * @since 1.0.0
     */
    public String getFullName() {
        return scopeId + TOPIC_SEPARATOR + clientId + TOPIC_SEPARATOR + channel;
    }

    @Override
    public String toString() {
        return channel;
    }

}
