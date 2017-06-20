/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import java.math.BigInteger;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.DeviceUserCouplingMode;
import org.eclipse.kapua.service.device.registry.DeviceStatus;

/**
 * Data object used in Gherkin to input Device parameters.
 * The data setters intentionally use only cucumber-friendly data types and
 * generate the resulting Kapua types internally.
 */
public class CucDevice {

    Integer scopeId;
    KapuaId kScopeId;
    Integer groupId;
    KapuaId kGroupId;
    Integer connectionId;
    KapuaId kConnectionId;
    Integer reservedUserId;
    KapuaId kReservedUserId;
    String clientId;
    String displayName;
    String status;
    DeviceStatus kStatus;
    String modelId;
    String serialNumber;
    String imei;
    String imsi;
    String iccid;
    String biosVersion;
    String firmwareVersion;
    String osVersion;
    String jvmVersion;
    String osgiFrameworkVersion;
    String applicationFrameworkVersion;
    String applicationIdentifiers;
    String acceptEncoding;
    String credentialsMode;
    DeviceUserCouplingMode kCredentialsMode;

    public void parse() {
        if (scopeId != null) {
            kScopeId = new KapuaEid(BigInteger.valueOf(scopeId));
        }

        if (groupId != null) {
            kGroupId = new KapuaEid(BigInteger.valueOf(groupId));
        }

        if (connectionId != null) {
            kConnectionId = new KapuaEid(BigInteger.valueOf(connectionId));
        }

        if (reservedUserId != null) {
            kReservedUserId = new KapuaEid(BigInteger.valueOf(reservedUserId));
        }

        if (status != null) {
            switch (status.trim().toUpperCase()) {
            case "DISABLED":
                kStatus = DeviceStatus.DISABLED;
                break;
            case "ENABLED":
                kStatus = DeviceStatus.ENABLED;
                break;
            default:
                kStatus = null;
                break;
            }
        }

        if (credentialsMode != null) {
            switch (credentialsMode.trim().toUpperCase()) {
            case "INHERITED":
                kCredentialsMode = DeviceUserCouplingMode.INHERITED;
                break;
            case "LOOSE":
                kCredentialsMode = DeviceUserCouplingMode.LOOSE;
                break;
            case "STRICT":
                kCredentialsMode = DeviceUserCouplingMode.STRICT;
                break;
            default:
                kCredentialsMode = null;
                break;
            }
        }
    }

    public KapuaId getScopeId() {
        return kScopeId;
    }

    public void setScopeId(KapuaId scopeId) {
        kScopeId = scopeId;
    }

    public KapuaId getGroupId() {
        return kGroupId;
    }

    public void setGroupId(KapuaId groupId) {
        kGroupId = groupId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public KapuaId getConnectionId() {
        return kConnectionId;
    }

    public void setConnectionId(KapuaId connectionId) {
        kConnectionId = connectionId;
    }

    public KapuaId getReservedUserId() {
        return kReservedUserId;
    }

    public void setReservedUserId(KapuaId reservedUserId) {
        kReservedUserId = reservedUserId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public DeviceStatus getStatus() {
        return kStatus;
    }

    public void setStatus(DeviceStatus status) {
        this.kStatus = status;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getBiosVersion() {
        return biosVersion;
    }

    public void setBiosVersion(String biosVersion) {
        this.biosVersion = biosVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    public String getOsgiFrameworkVersion() {
        return osgiFrameworkVersion;
    }

    public void setOsgiFrameworkVersion(String osgiFrameworkVersion) {
        this.osgiFrameworkVersion = osgiFrameworkVersion;
    }

    public String getApplicationFrameworkVersion() {
        return applicationFrameworkVersion;
    }

    public void setApplicationFrameworkVersion(String applicationFrameworkVersion) {
        this.applicationFrameworkVersion = applicationFrameworkVersion;
    }

    public String getApplicationIdentifiers() {
        return applicationIdentifiers;
    }

    public void setApplicationIdentifiers(String applicationIdentifiers) {
        this.applicationIdentifiers = applicationIdentifiers;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public DeviceUserCouplingMode getDeviceUserCouplingBound() {
        return kCredentialsMode;
    }

    public void setDeviceUserCouplingBound(DeviceUserCouplingMode credentialsMode) {
        kCredentialsMode = credentialsMode;
    }
}
