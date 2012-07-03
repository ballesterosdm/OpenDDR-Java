/**
 * Copyright 2012 Fundación CTIC
 * This software is distributed under the terms of the GNU Lesser General Public License.
 *
 *
 * This file is part of OpenDDR Simple APIs.
 * OpenDDR Simple APIs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * OpenDDR Simple APIs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Simple APIs.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author José Quiroga Álvarez
 * @author Diego Martínez Ballesteros
 *
 */
package org.openddr.simpleapi.oddr.builder.vitamineddevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.openddr.simpleapi.oddr.model.device.Device;
import org.openddr.simpleapi.oddr.model.UserAgent;

public class VitaminedWinPhoneDeviceBuilder extends VitaminedOrderedTokenDeviceBuilder {

    private Map<String, Device> devices;

    public VitaminedWinPhoneDeviceBuilder() {
        super();
    }

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.containsWindowsPhone()) {
            return true;

        } else {
            return false;
        }
    }

    public Device build(UserAgent userAgent, int confidenceTreshold) {
        ArrayList<Device> foundDevices = new ArrayList<Device>();
        Iterator it = orderedRules.keySet().iterator();
        while (it.hasNext()) {
        	String tokenString = (String) it.next();
            Token tokenInstance = tokensCache.get(tokenString); //get the token by key
            Device d = elaborateWinPhoneDeviceWithToken(userAgent, tokenInstance);
            if (d != null) {
                if (d.getConfidence() > confidenceTreshold) {
                    return d;

                } else {
                    if (d.getConfidence() > 0) {
                        foundDevices.add(d);
                    }
                }
            }
        }
        if (foundDevices.size() > 0) {
            Collections.sort(foundDevices, Collections.reverseOrder());
            return foundDevices.get(0);
        }
        return null;
    }

    public void putDevice(String device, List<String> initProperties) {
        orderedRules.put(initProperties.get(0), device);
    }

    private Device elaborateWinPhoneDeviceWithToken(UserAgent userAgent, Token tokenInst) {
        if (userAgent.hasMozillaPattern() || userAgent.hasOperaPattern()) {
            int subtract = 0;
            Pattern loosePattern = tokenInst.getLooseMidPattern();

            if (!loosePattern.matcher(userAgent.getCompleteUserAgent()).matches()) {
                return null;
            }

            Pattern currentPattern = null;

            if (userAgent.hasOperaPattern()) {
                subtract += 10;
            }

            for (int i = 0; i <= 1; i++) {

                if (i == 1) currentPattern = tokenInst.getLooseTailPattern();
                else currentPattern = tokenInst.getTailPattern();
                if (userAgent.getPatternElementsInside() != null && currentPattern.matcher(userAgent.getPatternElementsInside()).matches()) {
                    String deviceId = (String) orderedRules.get(tokenInst.getId());

		    if (devices.containsKey(deviceId)) {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(100 - subtract);
                        return retDevice;
                    }
                }
                if (userAgent.getPatternElementsPre() != null && currentPattern.matcher(userAgent.getPatternElementsPre()).matches()) {
                    String deviceId = (String) orderedRules.get(tokenInst.getId());

		    if (devices.containsKey(deviceId)) {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(95 - subtract);
                        return retDevice;
                    }
                }

                if (i == 1) currentPattern = tokenInst.getLooseSemicolonPattern();
                else currentPattern = tokenInst.getSemicolonPattern();
                if (userAgent.getPatternElementsInside() != null && currentPattern.matcher(userAgent.getPatternElementsInside()).matches()) {
                    String deviceId = (String) orderedRules.get(tokenInst.getId());

		    if (devices.containsKey(deviceId)) {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(90 - subtract);
                        return retDevice;
                    }
                }

                if (i == 1) currentPattern = tokenInst.getLooseMidPattern();
                else currentPattern = tokenInst.getMidPattern();
                if (userAgent.getPatternElementsInside() != null && currentPattern.matcher(userAgent.getPatternElementsInside()).matches()) {
                    String deviceId = (String) orderedRules.get(tokenInst.getId());

		    if (devices.containsKey(deviceId)) {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(80 - subtract);
                        return retDevice;
                    }
                }
                if (userAgent.getPatternElementsPre() != null && currentPattern.matcher(userAgent.getPatternElementsPre()).matches()) {
                    String deviceId = (String) orderedRules.get(tokenInst.getId());

		    if (devices.containsKey(deviceId)) {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(80 - subtract);
                        return retDevice;
                    }
                }
                if (userAgent.getPatternElementsPost() != null && currentPattern.matcher(userAgent.getPatternElementsPost()).matches()) {
                    String deviceId = (String) orderedRules.get(tokenInst.getId());

		    if (devices.containsKey(deviceId)) {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(60 - subtract);
                        return retDevice;
                    }
                }
                subtract += 20;
            }
        }

        return null;
    }

    @Override
    protected void afterOderingCompleteInit(Map<String, Device> devices) {
        this.devices = devices;
        for (Object devIdObj : orderedRules.values()) {
            String devId = (String) devIdObj;
            if (!devices.containsKey(devId)) {
                throw new IllegalStateException("unable to find device with id: " + devId + "in devices");
            }
        }
    }
}
